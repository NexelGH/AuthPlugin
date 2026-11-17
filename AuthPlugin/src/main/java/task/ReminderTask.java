package com.example.authplugin.task;

import com.example.authplugin.auth.AuthManager;
import net.kyori.adventure.text.Component;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import static com.example.authplugin.util.Messages.info;

/**
 * Co 2 s: pasek akcji (actionbar) dla niezalogowanych.
 * Co 20 s: dodatkowo wiadomość na czacie.
 */
public final class ReminderTask extends BukkitRunnable {

    private static final int CHAT_EVERY_N_RUNS = 10;

    private final AuthManager auth;
    private int runs;

    public ReminderTask(AuthManager auth) {
        this.auth = auth;
    }

    @Override
    public void run() {
        runs++;
        boolean sendChat = runs % CHAT_EVERY_N_RUNS == 0;

        for (Player player : Bukkit.getOnlinePlayers()) {
            if (!auth.isLocked(player.getUniqueId())) continue;

            Component text = reminderText(auth.isRegistered(player.getUniqueId()));
            player.sendActionBar(text);
            if (sendChat) {
                player.sendMessage(text);
            }
        }
    }

    public static Component reminderText(boolean registered) {
        return info(registered
                ? "Zaloguj się komendą /login <hasło>"
                : "Zarejestruj się komendą /register <hasło> <powtórz_hasło>");
    }
}