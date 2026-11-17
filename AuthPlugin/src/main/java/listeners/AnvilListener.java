package com.example.authplugin.listeners;

import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.PrepareAnvilEvent;
import org.bukkit.inventory.AnvilInventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.EnchantmentStorageMeta;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.Map;

public final class AnvilListener implements Listener {

    private static final int MAX_ENCHANT_LEVEL = 250;

    @EventHandler
    public void onPrepareAnvil(PrepareAnvilEvent event) {
        AnvilInventory inventory = event.getInventory();
        ItemStack first = inventory.getItem(0); // Lewy slot (przedmiot główny)
        ItemStack second = inventory.getItem(1); // Prawy slot (książka)

        if (first == null || second == null || first.getType() == Material.AIR || second.getType() == Material.AIR) {
            return;
        }

        // Obsługujemy łączenie z zaklętą książką
        if (second.getType() == Material.ENCHANTED_BOOK) {
            EnchantmentStorageMeta bookMeta = (EnchantmentStorageMeta) second.getItemMeta();
            if (bookMeta == null || bookMeta.getStoredEnchants().isEmpty()) {
                return;
            }

            ItemStack result = first.clone();
            ItemMeta resultMeta = result.getItemMeta();
            if (resultMeta == null) return;

            boolean modified = false;
            int totalCost = 0;

            for (Map.Entry<Enchantment, Integer> entry : bookMeta.getStoredEnchants().entrySet()) {
                Enchantment ench = entry.getKey();
                int rightLevel = entry.getValue();

                // Sprawdzamy czy przedmiot przyjmie ten enchant
                if (ench.canEnchantItem(result) || result.getType() == Material.ENCHANTED_BOOK) {
                    int leftLevel = 0;
                    if (result.getType() == Material.ENCHANTED_BOOK) {
                        EnchantmentStorageMeta resultBookMeta = (EnchantmentStorageMeta) resultMeta;
                        if (resultBookMeta.hasStoredEnchant(ench)) {
                            leftLevel = resultBookMeta.getStoredEnchantLevel(ench);
                        }
                    } else {
                        if (resultMeta.hasEnchant(ench)) {
                            leftLevel = resultMeta.getEnchantLevel(ench);
                        }
                    }

                    int maxVanilla = ench.getMaxLevel(); // Pobiera domyślny max z gry (np. 5 dla Wydajności)
                    int finalLevel;

                    // Jeśli którykolwiek poziom przekracza standardowy limit gry (to jest tajny item)
                    if (leftLevel > maxVanilla || rightLevel > maxVanilla) {
                        if (leftLevel == rightLevel) {
                            // Zgodnie z ustaleniami: np. 10 + 10 = zostaje 10
                            finalLevel = leftLevel;
                        } else {
                            // Bierzemy wyższy z poziomów
                            finalLevel = Math.max(leftLevel, rightLevel);
                        }
                    } else {
                        // Standardowe przedmioty z czystego Minecrafta (Vanilla)
                        if (leftLevel == rightLevel) {
                            // W czystym MC 5 + 5 daje 5 (bo blokuje maxVanilla), a np. 4 + 4 daje 5
                            finalLevel = Math.min(leftLevel + 1, maxVanilla);
                        } else {
                            finalLevel = Math.max(leftLevel, rightLevel);
                        }
                    }

                    // Twardy limit do 250 poziomu dla tajnych itemów
                    finalLevel = Math.min(finalLevel, MAX_ENCHANT_LEVEL);

                    // Aplikujemy wynik
                    if (result.getType() == Material.ENCHANTED_BOOK) {
                        ((EnchantmentStorageMeta) resultMeta).addStoredEnchant(ench, finalLevel, true);
                    } else {
                        resultMeta.addEnchant(ench, finalLevel, true);
                    }

                    modified = true;
                    totalCost += 5;
                }
            }

            if (modified) {
                result.setItemMeta(resultMeta);
                event.setResult(result);
                // Koszt w kowadle ograniczony do max 30 poziomów
                inventory.setRepairCost(Math.min(Math.max(totalCost, 1), 30));
            }
        }
    }
}