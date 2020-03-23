package me.quaky.dceconomy.commands.subcommands.gems;

import me.quaky.dceconomy.files.KeysFile;
import me.quaky.dceconomy.files.UsersFile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.naming.AuthenticationException;

import static org.bukkit.Bukkit.getLogger;

public class Add extends SubCommand {
    @Override
    public String getName() {
        return "add";
    }

    @Override
    public String getDescription() {
        return "Add gems to player";
    }

    @Override
    public String getSyntax() {
        return "/gems add <player name> <amount> <key>";
    }

    @Override
    public void perform(CommandSender sender, String[] args) {

        // 1. This should be performed only from the console
        if (sender instanceof Player) {
            sender.sendMessage("You don't have permissions to do this.");
            return;
        }
        if (args.length != 4) {
            sender.sendMessage(getSyntax());
            return;
        }

        this.sender = sender;

        // 2. Some basic security

        authorize(args[3]);

        KeysFile.generateNewKey();

        // 3. add gems

        String playerName = args[1];
        int gemsToAdd;

        try {
            gemsToAdd = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("You gave me wrong format of number.");
            getLogger().info(e.getMessage());
            return;
        }

        if (UsersFile.get().get(playerName) != null) {
            int gems = UsersFile.get().getInt(playerName + ".gems");
            UsersFile.get().set(playerName + ".gems", gems + gemsToAdd);
            sender.sendMessage(gemsToAdd + " gems were added to " + playerName);
        } else {
            sender.sendMessage("This player doesn't exist.");
        }
    }

    /**
     * Tries to authorize the given key
     *
     * @param key key
     */
    private void authorize(String key) {
        try {
            KeysFile.authorize(key, true);
        } catch (AuthenticationException e) {
            sender.sendMessage(e.getExplanation());
            return;
        }
    }
}
