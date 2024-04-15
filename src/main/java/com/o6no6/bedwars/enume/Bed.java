package com.o6no6.bedwars.enume;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum Bed {
    WHITE_BED(Material.WHITE_BED, ChatColor.WHITE + "Bianco", false),
    ORANGE_BED(Material.ORANGE_BED, ChatColor.GOLD + "Arancione", false),
    YELLOW_BED(Material.YELLOW_BED, ChatColor.YELLOW + "Giallo", false),
    CYAN_BED(Material.CYAN_BED, ChatColor.AQUA + "Ciano", false),
    PURPLE_BED(Material.PURPLE_BED, ChatColor.DARK_PURPLE + "Viola", false),
    BLUE_BED(Material.BLUE_BED, ChatColor.BLUE + "Blu", false),
    GREEN_BED(Material.GREEN_BED, ChatColor.GREEN + "Verde", false),
    RED_BED(Material.RED_BED, ChatColor.RED + "Rosso", false);

    private Material material;
    private String text;
    private boolean destroyed;

    Bed(Material material, String text, boolean destroyed) {
        this.material = material;
        this.text = text;
        this.destroyed = destroyed;
    }

    public Material getMaterial() {
        return material;
    }

    public String getText() {
        return text;
    }

    public boolean isDestroyed() {
        return destroyed;
    }

    public void setDestroyed(boolean destroyed) {
        this.destroyed = destroyed;
    }
}
