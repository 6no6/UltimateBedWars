package com.o6no6.bedwars.enumerativ;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public enum ShopItemList {

    WHITE_WOOL(1, Material.WHITE_WOOL, 16, Material.COPPER_INGOT, 4),
    WOODEN_SWORD(2, Material.WOODEN_SWORD, 1, Material.COPPER_INGOT, 6),
    WOODEN_PICKAXE(3, Material.WOODEN_PICKAXE, 1, Material.COPPER_INGOT, 4),
    WOODEN_AXE(4, Material.WOODEN_AXE, 1, Material.COPPER_INGOT, 3),
    HEALING_POTION(5, Material.POTION, 1, Material.IRON_INGOT, 6),
    GOLDEN_APPLE(6, Material.GOLDEN_APPLE, 1, Material.GOLD_INGOT, 9),
    STONE(10, Material.STONE, 16, Material.IRON_INGOT, 4),
    STONE_SWORD(11, Material.STONE_SWORD, 1, Material.IRON_INGOT, 4),
    STONE_PICKAXE(12, Material.STONE_PICKAXE, 1, Material.IRON_INGOT, 6),
    IRON_AXE(13, Material.IRON_AXE, 1, Material.GOLD_INGOT, 4),

    TNT(15, Material.TNT, 1, Material.GOLD_INGOT, 2),
    OAK_PLANKS(19, Material.OAK_PLANKS, 16, Material.GOLD_INGOT, 3),
    IRON_SWORD(20, Material.IRON_SWORD, 1, Material.GOLD_INGOT, 4),
    IRON_PICKAXE(21, Material.IRON_PICKAXE, 1, Material.GOLD_INGOT, 6),
    NETHERITE_AXE(22, Material.NETHERITE_AXE, 1, Material.EMERALD, 2),

    FIRE_CHARGE(24, Material.FIRE_CHARGE, 1, Material.GOLD_INGOT, 2),
    END_STONE(28, Material.END_STONE, 8, Material.DIAMOND, 2),
    DIAMOND_SWORD(29, Material.DIAMOND_SWORD, 1, Material.DIAMOND, 2),
    DIAMOND_PICKAXE(30, Material.DIAMOND_PICKAXE, 1, Material.DIAMOND, 3),

    BOW(31, Material.BOW, 8, Material.EMERALD, 1),

    ENDER_PEARL(33, Material.ENDER_PEARL, 2, Material.DIAMOND, 3),

    OBSIDIAN(37, Material.OBSIDIAN, 4, Material.DIAMOND, 1),
    NETHERITE_SWORD(38, Material.NETHERITE_SWORD, 1, Material.EMERALD, 4),
    NETHERITE_PICKAXE(39, Material.NETHERITE_PICKAXE, 1, Material.EMERALD, 6),
    ARROW(40, Material.ARROW, 6, Material.GOLD_INGOT, 12),
    TRIDENT(42, Material.TRIDENT, 1, Material.EMERALD, 2);

    private final int slot;
    private final ItemStack buyItem;
    private final ItemStack sellItem;

    ShopItemList(int slot, Material buyMaterial, int buyAmount, Material sellMaterial, int sellAmount) {
        this.slot = slot;
        this.buyItem = new ItemStack(buyMaterial, buyAmount);
        this.sellItem = new ItemStack(sellMaterial, sellAmount);
    }

    public int getSlot() {
        return slot;
    }

    public ItemStack getBuyItem() {
        return buyItem.clone();
    }

    public ItemStack getSellItem() {
        return (sellItem != null) ? sellItem.clone() : null;
    }
}
