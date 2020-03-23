package me.quaky.dceconomy.commands.subcommands.gems;

import me.quaky.dceconomy.files.KeysFile;
import me.quaky.dcfactions.files.UsersFile;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import javax.naming.AuthenticationException;

import static org.bukkit.Bukkit.getLogger;
import static org.bukkit.Bukkit.getPlayer;

public class Remove extends SubCommand {
    @Override
    public String getName() {
        return "remove";
    }

    @Override
    public String getDescription() {
        return "Remove player's gems";
    }

    @Override
    public String getSyntax() {
        return "/gems remove <player name> <amount> <key>";
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
        String playerName = args[1];
        /*if (UsersFile.get().get(playerName) == null) {
            sender.sendMessage("This player doesn't exist.");
            return;
        }*/
        // 2. Some basic security

        try {
            KeysFile.authorize(args[3], true);
        } catch (AuthenticationException e) {
            sender.sendMessage(e.getExplanation());
        }

        // 3. remove gems

        // Try to get player's gems
        int gemsToRemove;

        try {
            gemsToRemove = Integer.parseInt(args[2]);
        } catch (NumberFormatException e) {
            sender.sendMessage("You gave me wrong format of number.");
            getLogger().info(e.getMessage());
            return;
        }

        int gems = UsersFile.get().getInt(playerName + ".gems");
        if(gems-gemsToRemove<0){
            gemsToRemove=gems;
        }

        // Save users file
        UsersFile.get().set(playerName + ".gems", gems - gemsToRemove);
        UsersFile.save();

        // Send message to the sender that the command was performed.
        sender.sendMessage(playerName + " lost " + gemsToRemove + "gems.");
        getPlayer(playerName).sendMessage("You lost " + gemsToRemove + "gems.");

    }
}
