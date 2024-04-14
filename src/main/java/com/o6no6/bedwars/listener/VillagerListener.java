package com.o6no6.bedwars.listener;

import com.o6no6.bedwars.BedWars;
import com.o6no6.bedwars.manager.VillagerManager;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerInteractEntityEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class VillagerListener extends VillagerManager implements Listener {


    private final Map<UUID, Long> lastSoundPlayTime = new HashMap<>();
    private final long soundCooldown = 1000;

    public VillagerListener(BedWars bedWars) {
        super(bedWars);
    }


    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {

            if (event.getRightClicked().getType() == EntityType.VILLAGER) {
                        Player player = event.getPlayer();
                        openCustomInventory(player, event.getRightClicked().getName());
            }event.setCancelled(true);
    }
}
