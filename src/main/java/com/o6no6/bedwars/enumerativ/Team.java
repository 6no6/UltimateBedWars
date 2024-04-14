package com.o6no6.bedwars.enumerativ;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum Team {

    TEAM_RED(  ChatColor.RED.toString() + ChatColor.BOLD + "[Team Rosso] " + ChatColor.RESET),
    TEAM_BLUE(ChatColor.BLUE.toString() + ChatColor.BOLD + "[Team Blu] " + ChatColor.RESET),
    TEAM_GREEN(ChatColor.GREEN.toString() + ChatColor.BOLD + "[Team Verde] " + ChatColor.RESET),
    TEAM_YELLOW(ChatColor.YELLOW.toString() + ChatColor.BOLD + "[Team Giallo] " + ChatColor.RESET),
    TEAM_AQUA(ChatColor.AQUA.toString() + ChatColor.BOLD + "[Team Azzurro] " + ChatColor.RESET),
    TEAM_PURPLE(ChatColor.LIGHT_PURPLE.toString() + ChatColor.BOLD + "[Team Viola] " + ChatColor.RESET),
    TEAM_GOLD(ChatColor.GOLD.toString() + ChatColor.BOLD + "[Team Arancio] " + ChatColor.RESET),
    TEAM_WHITE(ChatColor.WHITE.toString() + ChatColor.BOLD + "[Team Bianco] " + ChatColor.RESET);

    private String display;


    Team(String display){
        this.display = display;

    }


    public String getDisplay() { return display; }

}