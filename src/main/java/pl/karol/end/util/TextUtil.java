package pl.karol.end.util;

import org.bukkit.ChatColor;

public final class TextUtil {

    public static String color(String message) {
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
