package com.o6no6.bedwars.custom;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;

public class ShopItem extends ItemStack {

    private String displayName;
    private List<String> lore;
    private boolean enchanted;

    public ShopItem(Material material) {
        this(material, null, null, 1,false);
    }

    public ShopItem(Material material, String title) {
        this(material, title, null, 1,false);
    }

    public ShopItem(Material material, int amount) {
        this(material, null, null, amount,false);
    }

    public ShopItem(Material material, String displayName, List<String> lore) {
        this(material, displayName, lore, 1,false);
    }

    public ShopItem(Material material, String title, int amount) {
        this(material, title, null, amount,false);
    }

    public ShopItem(Material material, String displayName, List<String> lore, boolean enchanted) {
        this(material, displayName, lore, 1,enchanted);
    }

    public ShopItem(Material material, String displayName, List<String> lore, int amount) {
        this(material, displayName, lore, amount,false);
    }

    public ShopItem(Material material, String displayName, List<String> lore, int amount, boolean enchanted) {
        super(material, amount);
        this.displayName = displayName;
        this.lore = lore;


        updateMeta();
    }

    public boolean isEnchanted() {
        return enchanted;
    }

    public void setEnchanted(boolean enchanted) {
        this.enchanted = enchanted;
        updateMeta();
    }

    private void updateMeta() {
        ItemMeta meta = getItemMeta();
        if (meta != null) {
            meta.setDisplayName(displayName);
            meta.setLore(lore);
            if (enchanted) {
                meta.addEnchant(Enchantment.DURABILITY, 1, true); // Applica un incantesimo fittizio per attivare l'animazione
            } else {
                meta.removeEnchant(Enchantment.DURABILITY); // Rimuovi l'incantesimo fittizio
            }
            setItemMeta(meta);
        }
    }
}