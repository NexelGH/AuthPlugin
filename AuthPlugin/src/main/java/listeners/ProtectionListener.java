package com.example.authplugin.listeners;

import com.example.authplugin.auth.AuthManager;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityAirChangeEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.EntityTargetLivingEntityEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;

/** Pełna nieśmiertelność, brak utraty tlenu i głodu oraz brak celowania przez moby dla niezalogowanych. */
public final class ProtectionListener implements Listener {

    private final AuthManager auth;

    public ProtectionListener(AuthManager auth) {
        this.auth = auth;
    }

    /** Obejmuje wszystkie typy obrażeń (upadek, moby, gracze, ogień, lawa, tonięcie itd.). */
    @EventHandler
    public void onDamage(EntityDamageEvent event) {
        if (event.getEntity() instanceof Player player && auth.isLocked(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onAirChange(EntityAirChangeEvent event) {
        if (event.getEntity() instanceof Player player && auth.isLocked(player.getUniqueId())) {
            // Trzymamy tlen na maksimum, dzięki temu nie ma utonięcia
            event.setAmount(player.getMaximumAir());
        }
    }

    @EventHandler
    public void onFoodChange(FoodLevelChangeEvent event) {
        if (event.getEntity() instanceof Player player && auth.isLocked(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }

    @EventHandler
    public void onMobTarget(EntityTargetLivingEntityEvent event) {
        if (event.getTarget() instanceof Player player && auth.isLocked(player.getUniqueId())) {
            event.setCancelled(true);
        }
    }
}