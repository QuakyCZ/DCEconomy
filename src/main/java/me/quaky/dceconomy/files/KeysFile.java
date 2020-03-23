package me.quaky.dceconomy.files;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;

import javax.naming.AuthenticationException;
import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.Random;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getServer;

public class KeysFile {
    private static File keys;
    private static FileConfiguration keysYaml;

    public static void setUp() {

        keys = new File(getServer().getPluginManager().getPlugin("DCFactions").getDataFolder(), "keys.yml");
        if (!keys.exists()) {
            getServer().getPluginManager().getPlugin("DCFactions").saveResource("keys.yml",false);
        }
        keysYaml = YamlConfiguration.loadConfiguration(keys);
        generateNewKey();
    }

    public static FileConfiguration get() {
        return keysYaml;
    }

    public static void save() {
        try {
            keysYaml.save(keys);
        } catch (IOException e) {
            getLogger().info("Couldn't save the file.");
        }
    }

    public static void reload() {
        keysYaml = YamlConfiguration.loadConfiguration(keys);
    }

    /**
     * If the key is in the file and if it's unused, it will use it.
     * @param key the checked key
     * @param generateNewKey generate new key?
     * @throws AuthenticationException if the key doesn't exist or the key has been already used.
     */
    public static void authorize(String key, boolean generateNewKey) throws AuthenticationException {
        byte[] encoded = Base64.getEncoder().encode(key.getBytes());
        if(get().get(encoded.toString())==null){
            throw new AuthenticationException("Key doesn't exist.");
        }
        if(KeysFile.get().getBoolean(encoded.toString()+".used")==true){
            throw new AuthenticationException("This key has been already used.");
        }
        KeysFile.get().set(encoded.toString()+".used",true);
        save();
        reload();
        if(generateNewKey)
            generateNewKey();

    }

    /**
     * Generates new key and saves it to the file.
     */
    public static void generateNewKey(){
        int leftLimit = 48; // numeral '0'
        int rightLimit = 122; // letter 'z'
        int targetStringLength = 10;
        Random random = new Random();

        String generatedString = random.ints(leftLimit, rightLimit + 1)
                .filter(i -> (i <= 57 || i >= 65) && (i <= 90 || i >= 97))
                .limit(targetStringLength)
                .collect(StringBuilder::new, StringBuilder::appendCodePoint, StringBuilder::append)
                .toString();
        String encoded = new String(Base64.getEncoder().encode(generatedString.getBytes()));

        //getLogger().info("String: " + generatedString + " encoded: " + encoded);

        if(get().get(encoded)!=null){
            generateNewKey();
        }else{
            get().addDefault(encoded,null);
            get().set(encoded+".used",false);
            save();
            reload();
        }
    }

}
