package com.o6no6.bedwars;

import com.o6no6.bedwars.listener.InventoryListener;
import com.o6no6.bedwars.listener.PlayerListener;
import com.o6no6.bedwars.listener.VillagerListener;
import org.bukkit.Bukkit;
import org.bukkit.World;
import org.bukkit.plugin.java.JavaPlugin;
import com.o6no6.bedwars.manager.VillagerManager;

public final class BedWars extends JavaPlugin {

private static final String WORLD = "world";

    @Override
    public void onEnable() {
        setupWorld();
        registerListeners();

    }

    private void setupWorld() {
        World world = Bukkit.getWorld(WORLD);
        if (world != null) {
            world.setClearWeatherDuration(Integer.MAX_VALUE);
            world.setTime(6000);
            world.setStorm(false);
            world.setThundering(false);
        } else {
            getLogger().warning("Il mondo 'world' non esiste o non è caricato correttamente.");
        }
    }



    private void registerListeners() {
        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);
        Bukkit.getPluginManager().registerEvents(new PlayerListener(this), this);

    }
}
