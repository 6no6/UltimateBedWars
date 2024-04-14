package com.o6no6.bedwars.manager;

import com.o6no6.bedwars.BedWars;
import com.o6no6.bedwars.enumerativ.ShopItemList;
import com.o6no6.bedwars.enumerativ.TeamShop;
import com.o6no6.bedwars.listener.VillagerListener;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.ArmorStand;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.entity.Villager;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.LeatherArmorMeta;

import static org.bukkit.entity.Villager.Profession.ARMORER;

public class VillagerManager {
    private static final String WORLD = "world";

    public BedWars bedWars;

    public VillagerManager(BedWars bedWars) {
        this.bedWars = bedWars;
    }

    public void setupVillagers() {
        int i = 1;
        for (TeamShop teamShop : TeamShop.values())  {

            Villager villager = (Villager) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), 85+i, 72, 32), EntityType.VILLAGER);
            ArmorStand stand = (ArmorStand) Bukkit.getWorld("world").spawnEntity(new Location(Bukkit.getWorld("world"), 85+i, 71.75, 32), EntityType.ARMOR_STAND);

            stand.setInvisible(true);
            stand.setGravity(false);
            stand.setInvulnerable(true);
            stand.setCustomName("TASTO DESTRO");
            stand.setCustomNameVisible(true);

            villager.setInvulnerable(true);
            villager.setCanPickupItems(false);
            villager.setSilent(true);
            villager.setCustomName(teamShop.getShopName());
            villager.setCustomNameVisible(true);
            villager.setAI(false);

            ItemStack pettorina = new ItemStack(Material.LEATHER_CHESTPLATE);
            LeatherArmorMeta chestplateMeta = (LeatherArmorMeta) pettorina.getItemMeta();
            chestplateMeta.setColor(teamShop.getColor());

            pettorina.setItemMeta(chestplateMeta);

            villager.getEquipment().setChestplate(pettorina);



            i++;
            Bukkit.getPluginManager().registerEvents(new VillagerListener(bedWars), bedWars);
        }


    }


    public void openCustomInventory(Player player, String shopname) {

        Inventory inventory = Bukkit.createInventory(null, 9 * 6, shopname);

        for (int i = 0; i < ShopItemList.values().length; i++) {
            ShopItemList item = ShopItemList.values()[i];
                inventory.setItem(item.getSlot(), item.getBuyItem());

        }

        player.openInventory(inventory);
    }
}
