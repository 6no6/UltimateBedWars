package com.o6no6.bedwars.listener;

import com.o6no6.bedwars.BedWars;
import com.o6no6.bedwars.enume.Bed;
import com.o6no6.bedwars.enume.ShopItemList;
import com.o6no6.bedwars.enume.TeamShop;
import com.o6no6.bedwars.manager.Manager;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Sound;
import org.bukkit.block.Block;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.UUID;

public class MainListener extends Manager implements Listener {

    public MainListener(BedWars bedWars) {
        super(bedWars);
    }

    // Gestisce l'interazione di un giocatore con un villager
    @EventHandler
    public void onPlayerInteractEntity(PlayerInteractEntityEvent event) {
        if (event.getRightClicked().getType() == EntityType.VILLAGER) {
            Player player = event.getPlayer();
            openCustomInventory(player, event.getRightClicked().getName());
            event.setCancelled(true);
        }
    }

    // Gestisce il click sugli oggetti nell'inventario personalizzato
    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        // Controlla se l'inventario aperto corrisponde a un negozio di una squadra
        for (TeamShop teamShop : TeamShop.values()) {
            if (ChatColor.translateAlternateColorCodes('&', event.getView().getTitle())
                    .equals(teamShop.getShopName()) && event.getCurrentItem() != null) {

                long currentTime = System.currentTimeMillis();
                Player player = (Player) event.getWhoClicked();
                UUID playerUUID = player.getUniqueId();

                // Itera sugli oggetti del negozio della squadra
                for (ShopItemList item : ShopItemList.values()) {
                    if (event.getRawSlot() == item.getSlot()) {
                        // Rimuove l'oggetto dall'inventario del giocatore
                        HashMap<Integer, ItemStack> removedItems = player.getInventory().removeItem(item.getSellItem());

                        if (removedItems.isEmpty()) {
                            // Suono e effetti quando l'acquisto viene effettuato con successo
                            handleSuccessfulPurchase(player, playerUUID, currentTime, item);
                        } else {
                            // Suono quando l'acquisto fallisce per mancanza di spazio nell'inventario
                            handleFailedPurchase(player, playerUUID, currentTime);
                        }
                    }
                }
                event.setCancelled(true);
            }
        }
    }

    @EventHandler
    public void onBreakBlock(BlockBreakEvent e){
        Block block = e.getBlock();


        for (Bed bed : Bed.values()) {
            if (block.getType() == bed.getMaterial()) {
                for (Player player : Bukkit.getOnlinePlayers()) {
                    player.playSound(player.getLocation(), Sound.ENTITY_ENDER_DRAGON_GROWL, 1.0f, 1.0f);
                }

                bed.setDestroyed(true);

                updateScoreboards();
                bedWars.getServer().broadcastMessage(bed.getText());

            }
        }

    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (player.equals(player) && Bed.RED_BED.isDestroyed()) { // Verifica se il giocatore morto è il giocatore spettatore
            // Imposta il giocatore spettatore in modalità spettatore
            player.setGameMode(GameMode.SPECTATOR);
            // Teletrasporta il giocatore spettatore alla posizione di spettatore

        }
    }
}
