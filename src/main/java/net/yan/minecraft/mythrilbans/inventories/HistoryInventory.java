package net.yan.minecraft.mythrilbans.inventories;

import com.google.common.collect.Lists;
import fr.minuskube.inv.ClickableItem;
import fr.minuskube.inv.SmartInventory;
import fr.minuskube.inv.content.*;
import net.yan.minecraft.mythrilbans.MythrilBans;
import net.yan.minecraft.mythrilbans.controllers.HistoryController;
import net.yan.minecraft.mythrilbans.data.Ban;
import net.yan.minecraft.mythrilbans.data.History;
import net.yan.minecraft.mythrilbans.util.ItemBuilder;
import net.yan.minecraft.mythrilbans.util.TimeParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.entity.Player;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class HistoryInventory implements InventoryProvider {

    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
    public static void open(Player player, int page,UUID target) {
        SmartInventory.builder()
                .id(target.toString())
                .title(ChatColor.YELLOW + "History of player")
                .provider(new HistoryInventory())
                .size(5, 9)
                .build().open(player, page);
    }

    @Override
    public void init(Player player, InventoryContents contents) {
        this.items(player,contents);
    }

    @Override
    public void update(Player player, InventoryContents contents) {
        this.items(player,contents);
    }

    private void items(Player player, InventoryContents contents) {
        HistoryController historyController = MythrilBans.getInstance().getHistoryController();

        Pagination pagination = contents.pagination();
        UUID target = UUID.fromString(contents.inventory().getId());

        List<History> histories = historyController.getBans(target);

        ClickableItem[] items = new ClickableItem[histories.size()];

        for (int i = 0; i < items.length; i++) {
            History history = histories.get(i);

            List<String> lore = Lists.newArrayList(
                    ChatColor.RED + "",
                    ChatColor.GRAY +"> Reason: " + ChatColor.WHITE + "" + history.ban().reason(),
                    ChatColor.GRAY +"> Staff: " + ChatColor.WHITE + "" + Bukkit.getOfflinePlayer(history.ban().staff()).getName(),
                    ChatColor.GRAY +"> Duration: " + ChatColor.WHITE + "" + TimeParser.parseLong(history.ban().duration(),true),
                    ChatColor.GRAY +"> Type: " + ChatColor.GOLD + "" + history.type()

            );

            items[i] = ClickableItem.empty(new ItemBuilder(Material.PAPER)
                    .setName(ChatColor.GREEN + "" + dateFormatter.format(new Date(history.registeredAt())))
                    .setLore(lore)
                    .build());
        }


        pagination.setItemsPerPage(21);
        pagination.setItems(items);

        SlotIterator slotIterator = contents.newIterator(SlotIterator.Type.HORIZONTAL, 1, 1);

        slotIterator.blacklist(1, 8);
        slotIterator.blacklist(2, 8);
        slotIterator.blacklist(3, 8);

        slotIterator.blacklist(2, 0);
        slotIterator.blacklist(3, 0);

        pagination.addToIterator(slotIterator);

        contents.set(new SlotPos(2, 0), ClickableItem.of(new ItemBuilder(Material.RED_STAINED_GLASS_PANE).setName(ChatColor.RED + "<< Previous Page").build(), e-> {
            open(player, pagination.previous().getPage(),target);
        }));

        contents.set(new SlotPos(2, 8), ClickableItem.of(new ItemBuilder(Material.GREEN_STAINED_GLASS_PANE).setName(ChatColor.GREEN + "Next Page >>").build(), e-> {
            open(player, pagination.next().getPage(),target);
        }));

    }
}
