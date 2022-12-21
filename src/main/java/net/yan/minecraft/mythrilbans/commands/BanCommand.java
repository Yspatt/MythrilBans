package net.yan.minecraft.mythrilbans.commands;

import net.kyori.adventure.text.Component;
import net.yan.minecraft.mythrilbans.MythrilBans;
import net.yan.minecraft.mythrilbans.data.Ban;
import net.yan.minecraft.mythrilbans.data.History;
import net.yan.minecraft.mythrilbans.data.enums.HistoryType;
import net.yan.minecraft.mythrilbans.util.TimeParser;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.util.UUID;

public class BanCommand implements CommandExecutor {

    private MythrilBans mythrilBans;

    public BanCommand(MythrilBans mythrilBans) {
        this.mythrilBans = mythrilBans;
    }
    @Override
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, @NotNull String[] arguments) {

        if (!sender.isOp()) {
            sender.sendMessage(mythrilBans.getMessageController().get("no-permission"));
            return true;
        }

        if (arguments.length == 0) {
            sender.sendMessage(mythrilBans.getMessageController().get("ban-use"));
            return true;
        }

        OfflinePlayer target = Bukkit.getOfflinePlayer(arguments[0]);

        if (target == null) {
            sender.sendMessage(mythrilBans.getMessageController().get("target-not-exists").replace("<target>",arguments[0]));
            return true;
        }

        UUID staffUUID = sender instanceof Player ? ((Player)sender).getUniqueId() : null;
        long time = arguments.length > 1 ? TimeParser.parseString(arguments[1]) : 0;
        String reason = arguments.length > 2 ? getArguments(arguments): "No Reason.";

        Ban ban = new Ban(
                UUID.randomUUID(),
                target.getUniqueId(),
                staffUUID,
                reason,
                System.currentTimeMillis(),
                time
        );

        this.mythrilBans.getBanController().create(ban);

        if (target instanceof Player) {
            Player onlinePlayer = (Player) target;
            onlinePlayer.kick(Component.text(mythrilBans.getMessageController().getBanModel(ban)));
        }

        mythrilBans.getHistoryController().create(
                new History(
                        UUID.randomUUID(),
                        ban,
                        HistoryType.BAN,
                        System.currentTimeMillis()
                )
        );

        sender.sendMessage(Component.text(mythrilBans.getMessageController().get("successfully-ban").replace("<target>",target.getName())));

        return false;
    }

    private String getArguments(String[] arguments) {
        StringBuilder sb = new StringBuilder();
        for(int i = 2; i < arguments.length; i++)
        {
            sb.append(arguments[i]).append(" ");
        }
        return sb.toString();
    }
}
