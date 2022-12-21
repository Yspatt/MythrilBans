package net.yan.minecraft.mythrilbans.listeners;

import net.kyori.adventure.text.Component;
import net.yan.minecraft.mythrilbans.MythrilBans;
import net.yan.minecraft.mythrilbans.data.History;
import net.yan.minecraft.mythrilbans.data.enums.HistoryType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerPreLoginEvent;

import java.util.UUID;

public class PlayerBanEvents implements Listener {

    private MythrilBans mythrilBans;
    public PlayerBanEvents(MythrilBans mythrilBans) {
        this.mythrilBans = mythrilBans;
    }

    @EventHandler
    private void playerJoinEvent(AsyncPlayerPreLoginEvent event) {
        mythrilBans.getBanController().find(event.getUniqueId()).ifPresent(ban -> {

            if ((System.currentTimeMillis() >= (ban.registeredAt() + ban.duration())) && ban.duration() > 0) {
                mythrilBans.getBanController().delete(ban);

                mythrilBans.getHistoryController().create(
                        new History(
                                UUID.randomUUID(),
                                ban,
                                HistoryType.UNBAN,
                                System.currentTimeMillis()
                        )
                );
            } else {

                event.setLoginResult(AsyncPlayerPreLoginEvent.Result.KICK_OTHER);
                event.kickMessage(Component.text(mythrilBans.getMessageController().getBanModel(ban)));

            }
        });
    }
}
