package com.o6no6.bedwars.manager;

import com.o6no6.bedwars.BedWars;
import com.o6no6.bedwars.enume.Bed;
import com.o6no6.bedwars.enume.ShopItemList;
import com.o6no6.bedwars.enume.TeamShop;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Manager implements Listener {

    private static final String WORLD = "world";
    private final BedWars bedWars;
    private final Map<UUID, Long> lastSoundPlayTime = new HashMap<>();
    private final long soundCooldown = 500;
    private int playerCount = 0;
    private BukkitTask timerTask;

    public Manager(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    // Metodo per configurare i villager all'avvio del gioco
    public void setupVillagers() {
        World world = Bukkit.getWorld(WORLD);
        if (world == null) {
            // Se il mondo "world" non esiste, non si fa nulla
            return;
        }

        for (TeamShop teamShop : TeamShop.values()) {
            double xOffset = calculateXOffset(); // Calcola un offset per la posizione X
            Location spawnLocation = new Location(world, 85 + xOffset, 72, 32);

            // Spawn del Villager
            Villager villager = (Villager) world.spawnEntity(spawnLocation, EntityType.VILLAGER);
            configureVillager(villager, teamShop.getShopName());

            // Spawn dell'ArmorStand
            Location standLocation = new Location(world, 85 + xOffset, 71.75, 32);
            ArmorStand stand = (ArmorStand) world.spawnEntity(standLocation, EntityType.ARMOR_STAND);
            configureStand(stand);
        }
    }

    // Calcola un offset dinamico per la posizione X dei villager
    private double calculateXOffset() {
        // In questa versione di esempio, l'offset X è fisso a 1
        return 1;
    }

    // Apre un'inventario personalizzato per un giocatore
    public void openCustomInventory(Player player, String shopname) {
        Inventory inventory = Bukkit.createInventory(null, 9 * 6, shopname);

        for (ShopItemList item : ShopItemList.values()) {
            inventory.setItem(item.getSlot(), item.getBuyItem());
        }
        player.openInventory(inventory);
    }

    // Configura le proprietà di un villager
    private void configureVillager(Villager villager, String shopName) {
        villager.setInvulnerable(true);
        villager.setCanPickupItems(false);
        villager.setSilent(true);
        villager.setCustomName(shopName);
        villager.setCustomNameVisible(true);
        villager.setAI(false);
    }

    // Configura le proprietà di un ArmorStand
    private void configureStand(ArmorStand stand) {
        stand.setInvisible(true);
        stand.setGravity(false);
        stand.setInvulnerable(true);
        stand.setCustomName(ChatColor.BOLD.toString() + "TASTO DESTRO");
        stand.setCustomNameVisible(true);
    }

    // Avvia un timer quando un giocatore entra nel mondo di gioco
    @EventHandler
    public void onPlayerJoinEvent(PlayerJoinEvent event) {
        setupWorld();
        Player player = event.getPlayer();
        World world = player.getWorld();

        if (world.getName().equals(WORLD)) {
            player.teleport(new Location(world, 0, 143, 0));
            playerCount++;

            if (playerCount == 1) {
                startTimer(player);
            }
        }
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

                bedWars.getServer().broadcastMessage(""+bed.getText());

            }
        }

    }

    // Avvia un timer per il respawn dei villager
    private void startTimer(Player player) {
        timerTask = new BukkitRunnable() {
            int timeLeft = 1;

            @Override
            public void run() {
                player.sendTitle("" + timeLeft, null, 20, 20, 20);
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

    // Teletrasporta i giocatori alla posizione predefinita
    private void teleportPlayers() {
        for (Player player : bedWars.getServer().getOnlinePlayers()) {
            if (player.getWorld().getName().equals(WORLD)) {
                Location destination = new Location(player.getWorld(), 102, 72, 30);
                player.teleport(destination);
            }
        }
    }

    // Configura il mondo di gioco
    private void setupWorld() {
        World world = Bukkit.getWorld(WORLD);
        if (world != null) {
            world.setClearWeatherDuration(Integer.MAX_VALUE);
            world.setTime(6000);
            world.setStorm(false);
            world.setThundering(false);
        } else {
            bedWars.getLogger().warning("Il mondo 'world' non esiste o non è caricato correttamente.");
        }
    }

    // Gestisce l'acquisto riuscito di un oggetto
    private void handleSuccessfulPurchase(Player player, UUID playerUUID, long currentTime, ShopItemList item) {
        if (!lastSoundPlayTime.containsKey(playerUUID) || currentTime - lastSoundPlayTime.get(playerUUID) >= soundCooldown) {
            player.playSound(player.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 1.0f, 1.0f);
            lastSoundPlayTime.put(playerUUID, currentTime);
        }

        HashMap<Integer, ItemStack> remainingItems = player.getInventory().addItem(item.getBuyItem());
        if (!remainingItems.isEmpty()) {
            World world = player.getWorld();
            world.dropItemNaturally(player.getLocation(), item.getBuyItem());
        }
    }

    // Gestisce l'acquisto fallito di un oggetto
    private void handleFailedPurchase(Player player, UUID playerUUID, long currentTime) {
        if (!lastSoundPlayTime.containsKey(playerUUID) || currentTime - lastSoundPlayTime.get(playerUUID) >= soundCooldown) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            lastSoundPlayTime.put(playerUUID, currentTime);
        }
    }
}