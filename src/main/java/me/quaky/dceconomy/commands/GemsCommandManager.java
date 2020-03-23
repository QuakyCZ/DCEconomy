package me.quaky.dceconomy.commands;

import me.quaky.dceconomy.commands.subcommands.gems.Add;
import me.quaky.dceconomy.commands.subcommands.gems.Remove;
import me.quaky.dceconomy.commands.subcommands.gems.SubCommand;
import me.quaky.dceconomy.files.UsersFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;


public class GemsCommandManager implements CommandExecutor {
    private Map<String, SubCommand> subCommands;

    public GemsCommandManager() {
        System.out.println("create GemsCommandManager command manager");
        loadSubCommands();
    }

    private void loadSubCommands() {
        subCommands = new HashMap<>();
        subCommands.put("add", new Add());
        subCommands.put("remove", new Remove());

        for (SubCommand command : subCommands.values()) {
            System.out.println("GemsCommandManager command manager loaded " + command.getName());
        }


    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

        if (args.length > 0) {
            if (subCommands.containsKey(args[0])) {
                subCommands.get(args[0]).perform(sender, args);
            } else if (UsersFile.get().get(args[0]) != null) {
                if ((sender instanceof Player && args[0].equals(sender.getName())) || sender.isOp())
                    sender.sendMessage(UsersFile.get().get(args[0] + ".gems").toString());
                else
                    sender.sendMessage("You don't have permissions to see " + args[0] + "'s gems.");
            } else {
                sender.sendMessage("You gave me an invalid sub-command.");
            }
        } else if (args.length == 0 && sender instanceof Player) {
            if (UsersFile.get().get(sender.getName()) != null) {
                sender.sendMessage("Your gems: " + UsersFile.get().getString(sender.getName() + ".gems"));
            }
        } else {
            sender.sendMessage("Please give me a sub-command.");
        }

        return true;
    }
}
