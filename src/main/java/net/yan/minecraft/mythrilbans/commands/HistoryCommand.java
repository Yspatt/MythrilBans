package net.yan.minecraft.mythrilbans.commands;

import net.yan.minecraft.mythrilbans.MythrilBans;
import net.yan.minecraft.mythrilbans.inventories.HistoryInventory;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

public class HistoryCommand implements CommandExecutor {

    private MythrilBans mythrilBans;

    public HistoryCommand(MythrilBans mythrilBans) {
        this.mythrilBans = mythrilBans;
    }

    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!(sender instanceof Player)) {
            return true;
        }

        if (!sender.isOp()) {
            sender.sendMessage(mythrilBans.getMessageController().get("no-permission"));
            return true;
        }

        if (arguments.length == 0) {
            sender.sendMessage(mythrilBans.getMessageController().get("history-use"));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(arguments[0]);

        if (target == null) {
            sender.sendMessage(mythrilBans.getMessageController().get("target-not-exists").replace("<target>",arguments[0]));
            return true;
        }

        Player player = (Player) sender;

        HistoryInventory.open(player,0,target.getUniqueId());
        return false;
    }
}
