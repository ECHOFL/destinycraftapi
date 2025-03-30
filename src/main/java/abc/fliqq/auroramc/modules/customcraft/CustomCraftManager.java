package abc.fliqq.auroramc.modules.customcraft;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import com.google.common.reflect.TypeToken;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import abc.fliqq.auroramc.core.services.MessageService;
import abc.fliqq.auroramc.core.util.LoggerUtil;
import lombok.Getter;

public class CustomCraftManager {

    private final List<CustomCraft> recipes = new ArrayList<>();
    private final Gson gson;
    private final File recipesFile;

    public CustomCraftManager(CustomCraftModule module) {
        this.gson = new GsonBuilder().setPrettyPrinting().create();
        this.recipesFile = new File(module.getPlugin().getDataFolder(), "modules/customcraft/custom_recipes.json");
        loadRecipes();
    }

    public void loadRecipes(){
        if(!recipesFile.exists()){
            // Créer le fichier s'il n'existe pas     
            saveRecipes();
            return;
        }

        try (BufferedReader reader = new BufferedReader(new FileReader(recipesFile))) {
            Type listType = new TypeToken<List<CustomCraftData>>() {}.getType();
            List<CustomCraftData> dataList = gson.fromJson(reader, listType);
            recipes.clear();
            if(dataList != null){
                for(CustomCraftData data : dataList){
                    recipes.add(convertToCustomCraft(data));
                }
            }
            LoggerUtil.info("Chargement de " + recipes.size() + " recettes personnalisées.");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void saveRecipes(){
        List<CustomCraftData> dataList = new ArrayList<>();
        for (CustomCraft craft : recipes){
            dataList.add(convertToCustomCraftData(craft));
        }
        try(BufferedWriter writer = new BufferedWriter(new FileWriter(recipesFile))){
            gson.toJson(dataList, writer);
        } catch (IOException e){
            e.printStackTrace();
        }
    }

    public List<CustomCraft> getRecipes(){
        return new ArrayList<>(recipes);
    }

    public void addRecipe(CustomCraft craft){
        recipes.add(craft);
        saveRecipes();
    }

    public void removeRecipe(CustomCraft craft){
        recipes.remove(craft);
        saveRecipes();
    }

    private CustomCraft convertToCustomCraft(CustomCraftData data){
        ItemStack result = new ItemStack(Material.getMaterial(data.result));
        // Appliquer les metaData si présentes
        if(data.displayName != null || data.lore != null || (data.enchantments != null && !data.enchantments.isEmpty())){
            ItemMeta meta = result.getItemMeta();
            if(data.displayName != null){
                // Coloriser le nom via MessageService
                meta.setDisplayName(MessageService.colorize(data.displayName));
            }
            if(data.lore != null){
                List<String> coloredLore = new ArrayList<>();
                for(String line : data.lore){
                    coloredLore.add(MessageService.colorize(line));
                }
                meta.setLore(coloredLore);
            }
            if(data.enchantments != null){
                for(Map.Entry<String, Integer> entry : data.enchantments.entrySet()){
                    Enchantment ench = Enchantment.getByName(entry.getKey());
                    if(ench != null){
                        meta.addEnchant(ench, entry.getValue(), true);
                    }
                }
            }
            result.setItemMeta(meta);
        }
        
        Map<Character, ItemStack> ingredients = new HashMap<>();
        if(data.ingredients != null){
            data.ingredients.forEach((key, materialName) -> {
                Material material = Material.getMaterial(materialName);
                if(material != null) ingredients.put(key.charAt(0), new ItemStack(material));
            });
        }
        return new CustomCraft(result, data.shape, ingredients);
    }
    
    private CustomCraftData convertToCustomCraftData(CustomCraft craft){
        CustomCraftData data = new CustomCraftData();
        ItemStack result = craft.getResult();
        data.result = result.getType().name();
        
        // Extraire les metaData : displayName, lore, enchantments
        ItemMeta meta = result.getItemMeta();
        if(meta != null){
            if(meta.hasDisplayName()){
                data.displayName = meta.getDisplayName();
            }
            if(meta.hasLore()){
                data.lore = meta.getLore();
            }
            if(!meta.getEnchants().isEmpty()){
                data.enchantments = new HashMap<>();
                meta.getEnchants().forEach((ench, level) -> {
                    data.enchantments.put(ench.getName(), level);
                });
            }
        }
        
        data.shape = craft.getShape();
        data.ingredients = new HashMap<>();
        for (Map.Entry<Character, ItemStack> entry : craft.getIngredients().entrySet()){
            data.ingredients.put(String.valueOf(entry.getKey()), entry.getValue().getType().name());
        }
        return data;
    }

    private static class CustomCraftData {
        String result;
        String displayName; // Nom personnalisé de l'item résultant (avec codes de couleur en notation &)
        List<String> lore;  // Lore de l'item résultant (avec codes de couleur)
        Map<String, Integer> enchantments; // Map d'enchantement (nom -> niveau)
        String[] shape;
        Map<String, String> ingredients;
    }
}