package net.yan.minecraft.mythrilbans.util;

import com.google.common.collect.Maps;
import org.bukkit.ChatColor;

import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MessageBuilder {

    private HashMap<String, String> placeholders = Maps.newHashMap();

    private String message;

    public MessageBuilder(String message) {
        this(message, false);
    }

    public MessageBuilder(String message, boolean withColors) {
        this.message = message;
        if (withColors)
            this.message = ChatColor.translateAlternateColorCodes('&', message);
    }

    public MessageBuilder add(String key, String value) {
        this.placeholders.put(key, value);
        return this;
    }

    private String find(String key, String value) {
        String REGEX = "%" + key + ":(\\w*?)%";
        Pattern pattern = Pattern.compile(REGEX);
        Matcher matcher = pattern.matcher(value);
        while (matcher.find()) {
            return matcher.group(1).replace("_", "-");
        }
        return null;
    }

    public String build() {
        placeholders.put("\n", "\n");
        placeholders.forEach((key, value) -> message = message.replace(key, value));
        return message;
    }

}
