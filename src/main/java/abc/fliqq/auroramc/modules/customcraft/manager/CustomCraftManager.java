package abc.fliqq.auroramc.modules.customcraft.manager;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Type;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.RecipeChoice;
import org.bukkit.inventory.ShapedRecipe;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import abc.fliqq.auroramc.core.services.MessageService;
import abc.fliqq.auroramc.core.util.LoggerUtil;
import abc.fliqq.auroramc.modules.customcraft.CustomCraft;
import abc.fliqq.auroramc.modules.customcraft.CustomCraftModule;

public class CustomCraftManager {

    private final List<CustomCraft> recipes = new ArrayList<>();
    private final Gson gson;
    private final File recipesFile;
    private final JavaPlugin plugin;
    private final List<ShapedRecipe> registeredRecipes = new ArrayList<>();

    public CustomCraftManager(CustomCraftModule module) {
        this.plugin = module.getPlugin();
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.recipesFile = new File(plugin.getDataFolder(), "modules/customcraft/custom_recipes.json");
        
        // S'assurer que le fichier existe et contient des données
        setupCustomRecipesFile();
        
        // Charger et enregistrer les recettes
        loadRecipes();
    }
    
    private void setupCustomRecipesFile() {
        if (!recipesFile.exists()) {
            try {
                // Assurez-vous que le dossier parent existe
                recipesFile.getParentFile().mkdirs();
                
                // Essayer de copier depuis les ressources
                InputStream inputStream = plugin.getResource("modules/customcraft/custom_recipes.json");
                if (inputStream != null) {
                    Files.copy(inputStream, recipesFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    LoggerUtil.info("Fichier custom_recipes.json créé avec succès.");
                } else {
                    // Si le fichier n'existe pas dans les ressources, créer un fichier vide avec un tableau JSON vide
                    Files.write(recipesFile.toPath(), "[]".getBytes());
                    LoggerUtil.warning("Ressource custom_recipes.json non trouvée, création d'un fichier vide.");
                }
            } catch (IOException e) {
                LoggerUtil.severe("Erreur lors de la création du fichier custom_recipes.json: " + e.getMessage());
                e.printStackTrace();
            }
        }
    }

    public void loadRecipes() {
        // Désinscrire les anciennes recettes
        unregisterRecipes();
    
        if (!recipesFile.exists()) {
            // Créer le fichier s'il n'existe pas     
            saveRecipes();
            return;
        }
    
        try (BufferedReader reader = new BufferedReader(new FileReader(recipesFile))) {
            Type listType = new TypeToken<List<CustomCraftData>>() {}.getType();
            List<CustomCraftData> dataList = gson.fromJson(reader, listType);
            recipes.clear();
            if (dataList != null) {
                for (CustomCraftData data : dataList) {
                    CustomCraft craft = convertToCustomCraft(data);
                    recipes.add(craft);
                    registerRecipe(craft);
                }
            }
            LoggerUtil.info("Chargement et enregistrement de " + recipes.size() + " recettes personnalisées.");
        } catch (Exception e) {
            LoggerUtil.severe("Erreur lors du chargement des recettes: " + e.getMessage());
            e.printStackTrace();
        }
    
        // Supprimer les recettes non reconnues
        Bukkit.recipeIterator().forEachRemaining(recipe -> {
            if (recipe instanceof ShapedRecipe) {
                NamespacedKey key = ((ShapedRecipe) recipe).getKey();
                if (key.getNamespace().equals(plugin.getName().toLowerCase()) && !isRecipeRegistered(key)) {
                    Bukkit.removeRecipe(key);
                    LoggerUtil.warning("Recette non reconnue supprimée : " + key.getKey());
                }
            }
        });
    }
    
    private boolean isRecipeRegistered(NamespacedKey key) {
        return registeredRecipes.stream().anyMatch(recipe -> recipe.getKey().equals(key));
    }
    private void registerRecipe(CustomCraft craft) {
        ItemStack result = craft.getResult();
        NamespacedKey key = new NamespacedKey(plugin, "custom_" + craft.getId());
    
        // Vérifier si une recette avec ce key existe déjà
        if (Bukkit.getRecipe(key) != null) {
            LoggerUtil.warning("Une recette avec le même NamespacedKey existe déjà : " + key.getKey());
            return; // Ne pas enregistrer de doublon
        }
    
        ShapedRecipe recipe = new ShapedRecipe(key, result);
    
        // Définir la forme
        recipe.shape(craft.getShape());
    
        // Définir les ingrédients
        for (Map.Entry<Character, ItemStack> entry : craft.getIngredients().entrySet()) {
            char ingredientChar = entry.getKey();
            Material ingredientMaterial = entry.getValue().getType();
            recipe.setIngredient(ingredientChar, new RecipeChoice.MaterialChoice(ingredientMaterial));
        }
    
        // Enregistrer la recette
        Bukkit.addRecipe(recipe);
        registeredRecipes.add(recipe);
    
        LoggerUtil.info("Recette enregistrée : " + key.getKey() + " pour " + result.getType().name());
    }

    private void unregisterRecipes() {
        for (ShapedRecipe recipe : registeredRecipes) {
            NamespacedKey key = recipe.getKey();
            if (Bukkit.getRecipe(key) != null) {
                Bukkit.removeRecipe(key);
                LoggerUtil.info("Recette supprimée : " + key.getKey());
            } else {
                LoggerUtil.warning("Impossible de trouver la recette à supprimer : " + key.getKey());
            }
        }
        registeredRecipes.clear();
    }

    public void saveRecipes() {
        List<CustomCraftData> dataList = new ArrayList<>();
        for (CustomCraft craft : recipes) {
            dataList.add(convertToCustomCraftData(craft));
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(recipesFile))) {
            gson.toJson(dataList, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public List<CustomCraft> getRecipes() {
        return new ArrayList<>(recipes);
    }

    public void addRecipe(CustomCraft craft) {
        recipes.add(craft);
        registerRecipe(craft);
        saveRecipes();
        LoggerUtil.info("Recette ajoutée : " + craft.getResult().getType().name());
    }

    public void removeRecipe(CustomCraft craft) {
        recipes.remove(craft);
        saveRecipes();
        // Recharger toutes les recettes pour mettre à jour les recettes enregistrées
        loadRecipes();
    }

    private CustomCraft convertToCustomCraft(CustomCraftData data) {
        Material resultMaterial = Material.getMaterial(data.result);
        if (resultMaterial == null) {
            LoggerUtil.warning("Matériau invalide pour le résultat: " + data.result);
            resultMaterial = Material.STONE; // Matériau par défaut
        }
    
        ItemStack result = new ItemStack(resultMaterial);
    
        // Appliquer les metaData si présentes
        if (data.displayName != null || data.lore != null || 
            (data.enchantments != null && !data.enchantments.isEmpty()) ||
            data.unbreakable || (data.hideFlags != null && !data.hideFlags.isEmpty())) {
            
            ItemMeta meta = result.getItemMeta();
            if (meta != null) {
                if (data.displayName != null) {
                    meta.setDisplayName(MessageService.colorize(data.displayName));
                }
    
                if (data.lore != null) {
                    List<String> coloredLore = new ArrayList<>();
                    for (String line : data.lore) {
                        coloredLore.add(MessageService.colorize(line));
                    }
                    meta.setLore(coloredLore);
                }
    
                if (data.enchantments != null) {
                    for (Map.Entry<String, Integer> entry : data.enchantments.entrySet()) {
                        Enchantment ench = Enchantment.getByName(entry.getKey());
                        if (ench != null) {
                            meta.addEnchant(ench, entry.getValue(), true);
                        } else {
                            LoggerUtil.warning("Enchantement invalide: " + entry.getKey());
                        }
                    }
                }
    
                // Définir l'indestructibilité
                meta.setUnbreakable(data.unbreakable);
    
                // Ajouter les flags pour masquer les informations
                if (data.hideFlags != null && !data.hideFlags.isEmpty()) {
                    for (String flagName : data.hideFlags) {
                        try {
                            ItemFlag flag = ItemFlag.valueOf(flagName);
                            meta.addItemFlags(flag);
                            LoggerUtil.info("Flag appliqué : " + flagName);
                        } catch (IllegalArgumentException e) {
                            LoggerUtil.warning("Flag d'item invalide: " + flagName);
                        }
                    }
                }
    
                result.setItemMeta(meta);
            }
            
        }


            

    
        Map<Character, ItemStack> ingredients = new HashMap<>();
        if (data.ingredients != null) {
            for (Map.Entry<String, String> entry : data.ingredients.entrySet()) {
                char key = entry.getKey().charAt(0);
                Material material = Material.getMaterial(entry.getValue());
                if (material != null) {
                    ingredients.put(key, new ItemStack(material));
                } else {
                    LoggerUtil.warning("Matériau invalide pour l'ingrédient " + key + ": " + entry.getValue());
                }
            }
        }
    
        // Créer l'objet CustomCraft
        CustomCraft customCraft = new CustomCraft(result, data.shape, ingredients, data.unbreakable);
    
        // Appliquer les nouveaux paramètres
        customCraft.setDisableNaturalCraft(data.disableNaturalCraft);
        customCraft.setDisableVillagerTrade(data.disableVillagerTrade);
    
        return customCraft;
    }
        
    
    private CustomCraftData convertToCustomCraftData(CustomCraft craft) {
        CustomCraftData data = new CustomCraftData();
        ItemStack result = craft.getResult();
        data.result = result.getType().name();
    
        // Extraire les metaData : displayName, lore, enchantments, unbreakable, hideFlags
        ItemMeta meta = result.getItemMeta();
        if (meta != null) {
            if (meta.hasDisplayName()) {
                data.displayName = meta.getDisplayName();
            }
    
            if (meta.hasLore()) {
                data.lore = meta.getLore();
            }
    
            if (!meta.getEnchants().isEmpty()) {
                data.enchantments = new HashMap<>();
                meta.getEnchants().forEach((ench, level) -> {
                    data.enchantments.put(ench.getName(), level);
                });
            }
    
            // Sauvegarder l'état d'indestructibilité
            data.unbreakable = meta.isUnbreakable();
    
            // Sauvegarder les flags d'item
            if (!meta.getItemFlags().isEmpty()) {
                data.hideFlags = new ArrayList<>();
                for (ItemFlag flag : meta.getItemFlags()) {
                    data.hideFlags.add(flag.name());
                    LoggerUtil.info("Flag sauvegardé : " + flag.name());
                }
            }
        }
    
        data.shape = craft.getShape();
        data.ingredients = new HashMap<>();
        for (Map.Entry<Character, ItemStack> entry : craft.getIngredients().entrySet()) {
            data.ingredients.put(String.valueOf(entry.getKey()), entry.getValue().getType().name());
        }
    
        // Ajouter les nouveaux paramètres
        data.disableNaturalCraft = craft.isDisableNaturalCraft();
        data.disableVillagerTrade = craft.isDisableVillagerTrade();
    
        return data;
    }

    private static class CustomCraftData {
        String result;
        String displayName;
        List<String> lore;
        Map<String, Integer> enchantments;
        boolean unbreakable = false;
        List<String> hideFlags;
        String[] shape;
        Map<String, String> ingredients;
        boolean disableNaturalCraft = false;
        boolean disableVillagerTrade = false;   
    }
}