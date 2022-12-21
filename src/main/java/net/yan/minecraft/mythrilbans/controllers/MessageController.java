package net.yan.minecraft.mythrilbans.controllers;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import net.yan.minecraft.mythrilbans.MythrilBans;
import net.yan.minecraft.mythrilbans.data.Ban;
import net.yan.minecraft.mythrilbans.util.ConfigurationHelper;
import net.yan.minecraft.mythrilbans.util.MessageBuilder;
import net.yan.minecraft.mythrilbans.util.TimeParser;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

public class MessageController {
    public Map<String,String> messages;
    public String banModel = "";
    public void build(MythrilBans instance) {
        messages = Maps.newHashMap();

        ConfigurationHelper config = instance.getConfiguration();

        Set<String> section = Objects.requireNonNull(config.getConfigurationSection("messages")).getKeys(false);
        for (String string : section) {
            messages.put(string,config.getString("messages." + string));
        }

        List<String> model = config.getStringList("ban.join-message");

        for (String s : model) {
            banModel += s + "\n";
        }
    }

    public String get(String key) {
        return ChatColor.translateAlternateColorCodes('&',messages.get(key));
    }

    public String getBanModel(Ban ban) {
        MessageBuilder builder = new MessageBuilder(banModel,true);
        builder.add("<reason>",ban.reason());
        builder.add("<duration>", TimeParser.parseLong(ban.duration(),false));
        builder.add("<staff>", Bukkit.getOfflinePlayer(ban.staff()).getName());

        return builder.build();
    }
}
