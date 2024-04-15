package com.o6no6.bedwars.manager;

import com.o6no6.bedwars.BedWars;
import com.o6no6.bedwars.enume.Bed;
import com.o6no6.bedwars.enume.DiamondTier;
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
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;
import org.bukkit.scoreboard.DisplaySlot;
import org.bukkit.scoreboard.Objective;
import org.bukkit.scoreboard.Scoreboard;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class Manager implements Listener {

    public static final String WORLD = "world";
    public final BedWars bedWars;
    public final Map<UUID, Long> lastSoundPlayTime = new HashMap<>();
    public final long soundCooldown = 500;
    public int playerCount = 0;
    public BukkitTask timerTask;

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


        String[] colors = {"white", "orange", "yellow", "cyan", "purple", "blue", "green", "red"};

        for (TeamShop teamShop : TeamShop.values()) {
            Location spawnLocation = new Location(world, bedWars.getConfig().getDouble("positionvillager."+colors[teamShop.ordinal()]+".x"), 72,
                    bedWars.getConfig().getDouble("positionvillager."+teamShop.getColor()+".z"));

            // Spawn del Villager
            Villager villager = (Villager) world.spawnEntity(spawnLocation, EntityType.VILLAGER);
            configureVillager(villager, teamShop.getShopName());

            // Spawn dell'ArmorStand
            Location standLocation = new Location(world, bedWars.getConfig().getDouble("positionvillager."+colors[teamShop.ordinal()]+".x"), 71.75,
                    bedWars.getConfig().getDouble("positionvillager."+colors[teamShop.ordinal()]+".z"));
            ArmorStand stand = (ArmorStand) world.spawnEntity(standLocation, EntityType.ARMOR_STAND);
            configureStand(stand);
        }
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

        setupScoreboard(player);

        if (world.getName().equals(WORLD)) {
            player.teleport(new Location(world, 0, 143, 0));
            playerCount++;

            if (playerCount == 1) {
                startTimer(player);
            }
        }
    }

    private void setupScoreboard(Player player) {
        Scoreboard scoreboard = Bukkit.getScoreboardManager().getNewScoreboard();
        Objective objective = scoreboard.registerNewObjective("ScoreboardPrincipale", "BedWars", ChatColor.BOLD + "BEDWARS");
        objective.setDisplaySlot(DisplaySlot.SIDEBAR);

        SimpleDateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        String date = dateFormat.format(new Date());

        setScore(objective, date + ChatColor.GRAY + " #1/15" + ChatColor.RESET, 15);
        setScore(objective, " ", 14);
        setScore(objective, "Diamante II" + ChatColor.AQUA + " 4:30" + ChatColor.RESET, 13);
        setScore(objective, " ", 12);

        updateBedStatus(objective);

        setScore(objective, "", 3);
        setScore(objective, ChatColor.YELLOW + "mc.heroforge.it", 2);

        player.setScoreboard(scoreboard);
    }

    private void updateScoreboards() {
        for (Player player : Bukkit.getOnlinePlayers()) {
            updateScoreboard(player);
        }

    }
    private void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("ScoreboardPrincipale");
        if (objective != null) {
            objective.unregister(); // Rimuovi l'obiettivo corrente per ricrearlo con le nuove informazioni
        }

        setupScoreboard(player); // Ricrea la scoreboard aggiornata per il giocatore
    }

    private void setScore(Objective objective, String entry, int score) {
        objective.getScore(entry).setScore(score);
    }

    private void updateBedStatus(Objective objective) {
        for (Bed bed: Bed.values()){
            setScore(objective, bed.getText() + ChatColor.RESET + (!bed.isDestroyed() ? (ChatColor.GREEN + " ✔") : (ChatColor.RED + " ✖")), 11 - bed.ordinal());

        }
    }

    private void updateTierDiamond(Objective objective) {



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
                bedWars.getServer().broadcastMessage(""+bed.getText());

            }
        }

    }
    private void updateScoreboard(Player player) {
        Scoreboard scoreboard = player.getScoreboard();
        Objective objective = scoreboard.getObjective("ScoreboardPrincipale");
        if (objective != null) {
            objective.unregister(); // Rimuovi l'obiettivo corrente per ricrearlo con le nuove informazioni
        }

        setupScoreboard(player); // Ricrea la scoreboard aggiornata per il giocatore
    }

    private void setScore(Objective objective, String entry, int score) {
        objective.getScore(entry).setScore(score);
    }

    private void updateBedStatus(Objective objective) {
        for (Bed bed: Bed.values()){
            setScore(objective, bed.getText() + ChatColor.RESET + (!bed.isDestroyed() ? (ChatColor.GREEN + " ✔") : (ChatColor.RED + " ✖")), 11 - bed.ordinal());

        }
    }

    private void updateTierDiamond(Objective objective) {



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
    protected void handleSuccessfulPurchase(Player player, UUID playerUUID, long currentTime, ShopItemList item) {
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
    protected void handleFailedPurchase(Player player, UUID playerUUID, long currentTime) {
        if (!lastSoundPlayTime.containsKey(playerUUID) || currentTime - lastSoundPlayTime.get(playerUUID) >= soundCooldown) {
            player.playSound(player.getLocation(), Sound.UI_BUTTON_CLICK, 1.0f, 1.0f);
            lastSoundPlayTime.put(playerUUID, currentTime);
        }
    }

<<<<<<< HEAD

=======
    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();

        if (player.equals(player) && Bed.RED_BED.isDestroyed()) { // Verifica se il giocatore morto è il giocatore spettatore
            // Imposta il giocatore spettatore in modalità spettatore
            player.setGameMode(GameMode.SPECTATOR);
            // Teletrasporta il giocatore spettatore alla posizione di spettatore

        }
    }
>>>>>>> 9938c353b3d709fa783b3d84b26242714c8cb2ea
}
