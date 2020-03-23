package me.quaky.dceconomy.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.logging.Level;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class UsersFile {
    private static File users;
    private static FileConfiguration usersYaml;
    private static boolean isSetUp = false;

    public static void setUp() {

        users = new File(getServer().getPluginManager().getPlugin("DCFactions").getDataFolder(), "users.yml");
        if (!users.exists()) {
            getLogger().log(Level.WARNING, "The file wasn't found.");
            throw new Error("File not found.");
        }
        usersYaml = YamlConfiguration.loadConfiguration(users);
        isSetUp = true;
    }

    public static FileConfiguration get() {
        if (isSetUp) {
            return usersYaml;
        }
        try {
            setUp();
            return get();
        } catch (Error e) {
            throw e;
        }
    }

    public static void save() {

        if (!isSetUp) {
            try {
                setUp();
            } catch (Error e) {
                throw e;
            }
        }

        try {
            usersYaml.save(users);
        } catch (IOException e) {
            getLogger().info("Couldn't save users.yml file.");
            getLogger().info(e.getLocalizedMessage());
        }
    }

    public static void reload() {
        usersYaml = YamlConfiguration.loadConfiguration(users);
    }

}
