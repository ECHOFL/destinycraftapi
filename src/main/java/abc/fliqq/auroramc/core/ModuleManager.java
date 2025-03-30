package abc.fliqq.auroramc.core;

import java.util.ArrayList;
import java.util.List;

import lombok.Getter;

public class ModuleManager {
    
    @Getter
    private final List<PluginModule> modules = new ArrayList<>();

    public void registerModule(PluginModule module) {
        modules.add(module);
    }
    public void initModules() {
        for (PluginModule module : modules) {
            module.onInit();
        }
    }

    public void stopModules() {
        for (PluginModule module : modules) {
            module.onStop();
        }
    }
    public void reloadModules() {
        for (PluginModule module : modules) {
            module.onReload();
        }
    }
    
    public PluginModule getModule(String name) {
        for (PluginModule module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return module;
            }
        }
        return null;
    }

    public boolean isModuleEnabled(String name) {
        for (PluginModule module : modules) {
            if (module.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }
}
