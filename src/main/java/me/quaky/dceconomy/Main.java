package me.quaky.dceconomy;

import me.quaky.dceconomy.commands.GemsCommandManager;
import me.quaky.dceconomy.files.KeysFile;
import org.bukkit.plugin.java.JavaPlugin;

import static org.bukkit.Bukkit.getPluginManager;

public final class Main extends JavaPlugin {

    private static Main plugin;
    private static me.quaky.dcfactions.Main core;
    @Override
    public void onEnable() {
        // Plugin startup logic
        try{
            plugin = this;
            core = (me.quaky.dcfactions.Main)getPluginManager().getPlugin("DCFactions");
            if(core==null){
                getLogger().info("Core plugin is not installed. Disabling...");
                getPluginManager().disablePlugin(this);
                return;
            }
            KeysFile.setUp();

            getCommand("gems").setExecutor(new GemsCommandManager());
            getLogger().info("plugin loaded.");
        }catch (Error e){
            getLogger().info(e.toString());
        }

    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    public static Main getMain(){
        return plugin;
    }

    public static me.quaky.dcfactions.Main getCore(){
        return core;
    }
}
