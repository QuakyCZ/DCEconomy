package me.quaky.dceconomy;

import me.quaky.dceconomy.commands.GemsCommandManager;
import me.quaky.dceconomy.files.KeysFile;
import me.quaky.dceconomy.files.UsersFile;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public final class Main extends JavaPlugin {
    Plugin factions = Bukkit.getPluginManager().getPlugin("DCFactions");
    private static Main plugin;
    @Override
    public void onEnable() {
        // Plugin startup logic
        try{
            plugin = this;
            KeysFile.setUp();
            UsersFile.setUp();

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
}
