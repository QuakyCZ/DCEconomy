package me.quaky.dceconomy.commands.subcommands.gems;

import me.quaky.dceconomy.Main;
import org.bukkit.command.CommandSender;

public abstract class SubCommand {
    protected Main plugin = Main.getMain();
    protected CommandSender sender;
    public abstract String getName();

    public abstract String getDescription();

    public abstract String getSyntax();

    public abstract void perform(CommandSender sender, String args[]);
}
