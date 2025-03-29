package abc.fliqq.auroramc.core;

public interface PluginModule {
    
    public String getName();
    public void onInit();
    public void onStop();
    public void onReload();

}
