package com.o6no6.bedwars.enume;
import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum TeamShop {

    TEAM_1(ChatColor.RED.toString() + ChatColor.BOLD + "Shop Item", Color.RED),
    TEAM_2(ChatColor.BLUE.toString() + ChatColor.BOLD + "Shop Item", Color.BLUE),

    TEAM_3(ChatColor.GREEN.toString() + ChatColor.BOLD + "Shop Item", Color.GREEN),
    TEAM_4(ChatColor.YELLOW.toString() + ChatColor.BOLD + "Shop Item", Color.YELLOW),

    TEAM_5(ChatColor.AQUA.toString() + ChatColor.BOLD + "Shop Item", Color.AQUA),
    TEAM_6(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "Shop Item", Color.PURPLE),

    TEAM_7(ChatColor.GOLD.toString() + ChatColor.BOLD + "Shop Item", Color.ORANGE),
    TEAM_8(ChatColor.WHITE.toString() + ChatColor.BOLD + "Shop Item", Color.WHITE);

    private final String shopName;
    private final Color color;

    TeamShop(String shopName, Color color) {
        this.shopName = shopName;
        this.color = color;
    }

    public String getShopName() {
        return shopName;
    }

    public Color getColor() {
        return color;
    }
}

