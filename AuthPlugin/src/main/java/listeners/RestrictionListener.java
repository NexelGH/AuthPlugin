package com.example.authplugin.listeners;

import com.example.authplugin.auth.AuthManager;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.Cancellable;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.player.AsyncPlayerChatEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerInteractEntityEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerItemConsumeEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.Locale;
import java.util.Set;

import static com.example.authplugin.util.Messages.error;

/** Blokady dla niezalogowanych: ruch, czat, komendy, interakcje ze światem, ekwipunek oraz walka. */
public final class RestrictionListener implements Listener {

    private static final Set<String> ALLOWED_COMMANDS = Set.of("login", "register");

    private final JavaPlugin plugin;
    private final AuthManager auth;

    public RestrictionListener(JavaPlugin plugin, AuthManager auth) {
        this.plugin = plugin;
        this.auth = auth;
    }

    @EventHandler
    public void onMove(PlayerMoveEvent event) {
        if (!auth.isLocked(event.getPlayer().getUniqueId())) return;

        Location from = event.getFrom();
        Location to = event.getTo();
        if (to == null) return;

        // Pozycja zostaje przywrócona, obrót kamery jest dozwolony
        if (from.getX() != to.getX() || from.getY() != to.getY() || from.getZ() != to.getZ()) {
            Location fixed = from.clone();
            fixed.setYaw(to.getYaw());
            fixed.setPitch(to.getPitch());
            event.setTo(fixed);
        }
    }

    @EventHandler
    public void onChat(AsyncPlayerChatEvent event) {
        if (!auth.isLocked(event.getPlayer().getUniqueId())) return;

        event.setCancelled(true);
        Bukkit.getScheduler().runTask(plugin,
                () -> event.getPlayer().sendMessage(error("Nie możesz pisać na czacie przed zalogowaniem.")));
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent event) {
        if (!auth.isLocked(event.getPlayer().getUniqueId())) return;

        String name = event.getMessage().substring(1).split(" ", 2)[0].toLowerCase(Locale.ROOT);
        if (ALLOWED_COMMANDS.contains(name)) return;

        event.setCancelled(true);
        event.getPlayer().sendMessage(error("Przed zalogowaniem możesz użyć tylko /login oraz /register."));
    }

    // ---- interakcje ze światem i ekwipunkiem ----

    @EventHandler
    public void onInteract(PlayerInteractEvent event) {
        cancelIfLocked(event, event.getPlayer());
    }

    @EventHandler
    public void onInteractEntity(PlayerInteractEntityEvent event) {
        cancelIfLocked(event, event.getPlayer());
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent event) {
        cancelIfLocked(event, event.getPlayer());
    }

    @EventHandler
    public void onBlockPlace(BlockPlaceEvent event) {
        cancelIfLocked(event, event.getPlayer());
    }

    @EventHandler
    public void onItemConsume(PlayerItemConsumeEvent event) {
        cancelIfLocked(event, event.getPlayer());
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        cancelIfLocked(event, event.getPlayer());
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (event.getEntity() instanceof Player player) {
            cancelIfLocked(event, player);
        }
    }

    @EventHandler
    public void onInventoryOpen(InventoryOpenEvent event) {
        if (event.getPlayer() instanceof Player player) {
            cancelIfLocked(event, player);
        }
    }

    @EventHandler
    public void onInventoryClick(InventoryClickEvent event) {
        cancelIfLocked(event, event.getWhoClicked());
    }

    private void cancelIfLocked(Cancellable event, org.bukkit.entity.HumanEntity human) {
        if (auth.isLocked(human.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onEntityDamageByEntity(EntityDamageByEntityEvent event) {
        Player player = null;
        if (event.getDamager() instanceof Player p) {
            player = p;
        } else if (event.getDamager() instanceof Projectile projectile && projectile.getShooter() instanceof Player p) {
            player = p;
        }

        if (player != null && auth.isLocked(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}