package com.o6no6.bedwars.listener;

import com.o6no6.bedwars.BedWars;
import org.bukkit.*;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import com.o6no6.bedwars.manager.VillagerManager;
public class PlayerListener  extends VillagerManager implements Listener {

    private int playerCount = 0;
    private BukkitTask timerTask;

    public PlayerListener(BedWars bedWars) {
        super(bedWars);
    }

    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (world.getName().equals("world")) {
            player.teleport(new Location(world, 0, 143, 0));
            playerCount++;

            if (playerCount == 1) {
                startTimer(player);
            }
        }
    }

    private void startTimer(Player player) {
        timerTask = new BukkitRunnable() {
            int timeLeft = 1;

            @Override
            public void run() {
                player.sendTitle(""+timeLeft , null, 20, 20, 20);
                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);

                if (--timeLeft < 0) {
                    setupVillagers();
                    teleportPlayers();
                    playerCount = 0;
                    cancel();
                }
            }
        }.runTaskTimer(bedWars, 0, 20);
    }

    private void teleportPlayers() {
        for (Player player : bedWars.getServer().getOnlinePlayers()) {
            if (player.getWorld().getName().equals("world")) {
                Location destination = new Location(player.getWorld(), 102, 72, 30);
                player.teleport(destination);
            }
        }
    }
}
