package com.o6no6.bedwars.listener;

import com.o6no6.bedwars.enumerativ.ShopItemList;
import com.o6no6.bedwars.enumerativ.TeamShop;
import org.bukkit.ChatColor;
import org.bukkit.Sound;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryListener implements Listener {
    private final Map<UUID, Long> lastSoundPlayTime = new HashMap<>();
    private final long soundCooldown = 500;

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {

        for (TeamShop teamShop : TeamShop.values()) {
            if (ChatColor.translateAlternateColorCodes('&', event.getView().getTitle())
                    .equals(teamShop.getShopName()) && event.getCurrentItem() != null) {

                long currentTime = System.currentTimeMillis();

                Player player = (Player) event.getWhoClicked();
                UUID playerUUID = player.getUniqueId();

                for (ShopItemList item : ShopItemList.values()) {
                    if (event.getRawSlot() == item.getSlot()) {
                        HashMap<Integer, ItemStack> removedItems = player.getInventory().removeItem(item.getSellItem());

                        if (removedItems.isEmpty()) {
                            if (!lastSoundPlayTime.containsKey(playerUUID) || currentTime - lastSoundPlayTime.get(playerUUID) >= soundCooldown) {
                                player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
                                lastSoundPlayTime.put(playerUUID, currentTime);
                            }

                            HashMap<Integer, ItemStack> remainingItems = player.getInventory().addItem(item.getBuyItem());

                            if (!remainingItems.isEmpty()) {
                                World world = player.getWorld();
                                world.dropItemNaturally(player.getLocation(), item.getBuyItem());
                            }
                        } else {
                            if (!lastSoundPlayTime.containsKey(playerUUID) || currentTime - lastSoundPlayTime.get(playerUUID) >= soundCooldown) {
                                player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
                                lastSoundPlayTime.put(playerUUID, currentTime);
                            }
                        }
                    }
                }
                event.setCancelled(true);
            }
        }
    }
}
