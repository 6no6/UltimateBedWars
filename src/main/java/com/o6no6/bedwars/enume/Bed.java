package com.o6no6.bedwars.enume;

import org.bukkit.ChatColor;
import org.bukkit.Material;

public enum Bed {
    HITE_BED(Material.WHITE_BED, ChatColor.WHITE + "Letto Bianco Distrutto"),
    ORANGE_BED(Material.ORANGE_BED, ChatColor.GOLD + "Letto Arancione Distrutto"),
    YELLOW_BED(Material.YELLOW_BED, ChatColor.YELLOW + "Letto Giallo Distrutto"),
    CYAN_BED(Material.CYAN_BED, ChatColor.AQUA + "Letto Ciano Distrutto"),
    PURPLE_BED(Material.PURPLE_BED, ChatColor.DARK_PURPLE + "Letto Viola Distrutto"),
    BLUE_BED(Material.BLUE_BED, ChatColor.BLUE + "Letto Blu Distrutto"),
    GREEN_BED(Material.GREEN_BED, ChatColor.GREEN + "Letto Verde Distrutto"),
    RED_BED(Material.RED_BED,ChatColor.RED + "Letto Rosso Distrutto");


    private Material material;
    private String text;

    Bed(Material material, String text) {
        this.material = material;
        this.text = text;
    }

    public Material getMaterial() {
        return material;
    }

    public String getText(){
        return text;
    }
}