package com.example.authplugin.listeners;

import com.example.authplugin.auth.AuthManager;
import com.example.authplugin.task.ReminderTask;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;

public final class ConnectionListener implements Listener {

    private final AuthManager auth;

    public ConnectionListener(AuthManager auth) {
        this.auth = auth;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        auth.logout(player.getUniqueId());

        // Zapobiega wyrzuceniu za latanie / spadek podczas oczekiwania na logowanie
        player.setAllowFlight(true);
        player.setFlying(false);

        // Nakłada efekt zaćmienia (Blindness) na czas logowania (nieskończony czas, bez cząsteczek)
        player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, Integer.MAX_VALUE, 0, false, false));

        player.sendMessage(ReminderTask.reminderText(auth.isRegistered(player.getUniqueId())));
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent event) {
        auth.logout(event.getPlayer().getUniqueId());
    }
}