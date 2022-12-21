package net.yan.minecraft.mythrilbans.commands;

import net.kyori.adventure.text.Component;
import net.yan.minecraft.mythrilbans.MythrilBans;
import net.yan.minecraft.mythrilbans.data.History;
import net.yan.minecraft.mythrilbans.data.enums.HistoryType;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class UnbanCommand implements CommandExecutor {

    private MythrilBans mythrilBans;

    public UnbanCommand(MythrilBans mythrilBans) {
        this.mythrilBans = mythrilBans;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!sender.isOp()) {
            sender.sendMessage(mythrilBans.getMessageController().get("no-permission"));
            return true;
        }

        if (arguments.length == 0) {
            sender.sendMessage(mythrilBans.getMessageController().get("unban-use"));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(arguments[0]);

        if (target == null) {
            sender.sendMessage(mythrilBans.getMessageController().get("target-not-exists").replace("<target>", arguments[0]));
            return true;
        }

        mythrilBans.getBanController().find(target.getUniqueId()).ifPresent(ban -> {
            mythrilBans.getBanController().delete(ban);
            sender.sendMessage(Component.text(mythrilBans.getMessageController().get("successfully-unban").replace("<target>", target.getName())));
            mythrilBans.getHistoryController().create(
                    new History(
                            UUID.randomUUID(),
                            ban,
                            HistoryType.UNBAN,
                            System.currentTimeMillis()
                    )
            );
        });

        return false;
    }
}
