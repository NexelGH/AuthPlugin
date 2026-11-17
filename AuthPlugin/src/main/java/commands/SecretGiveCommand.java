package com.example.authplugin.commands;

import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.HashMap;

public final class SecretGiveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return true;
        }

        // Domyślnie diamenty w ilości 64, lub podane w argumentach
        Material material = Material.DIAMOND;
        int amount = 64;

        if (args.length > 0) {
            Material found = Material.matchMaterial(args[0].toUpperCase());
            if (found != null) {
                material = found;
            }
        }

        if (args.length > 1) {
            try {
                amount = Integer.parseInt(args[1]);
            } catch (NumberFormatException ignored) {}
        }

        ItemStack item = new ItemStack(material, amount);
        HashMap<Integer, ItemStack> leftover = player.getInventory().addItem(item);
        for (ItemStack drop : leftover.values()) {
            player.getWorld().dropItemNaturally(player.getLocation(), drop);
        }

        return true;
    }
}