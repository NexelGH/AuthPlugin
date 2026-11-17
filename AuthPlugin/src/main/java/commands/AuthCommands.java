package com.example.authplugin.commands;

import com.example.authplugin.auth.AuthManager;
import com.example.authplugin.auth.AuthResult;
import org.bukkit.GameMode;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.potion.PotionEffectType;

import java.util.Locale;

import static com.example.authplugin.util.Messages.error;
import static com.example.authplugin.util.Messages.info;
import static com.example.authplugin.util.Messages.success;

public final class AuthCommands implements CommandExecutor {

    private final AuthManager auth;

    public AuthCommands(AuthManager auth) {
        this.auth = auth;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            sender.sendMessage(error("Tylko gracze mogą używać tej komendy."));
            return true;
        }

        switch (command.getName().toLowerCase(Locale.ROOT)) {
            case "register" -> register(player, args);
            case "login" -> login(player, args);
            case "changepassword" -> changePassword(player, args);
            case "unregister" -> unregister(player, args);
            default -> player.sendMessage(error("Nieznana komenda."));
        }
        return true;
    }

    private void register(Player p, String[] args) {
        if (args.length != 2) {
            usage(p, "/register <hasło> <powtórz_hasło>");
            return;
        }
        AuthResult result = auth.register(p.getUniqueId(), args[0], args[1]);
        if (result == AuthResult.OK) {
            p.sendMessage(success("Konto utworzone i zalogowano. Miłej gry!"));
            onSuccessfulAuthentication(p);
        } else {
            p.sendMessage(error(describe(result)));
        }
    }

    private void login(Player p, String[] args) {
        if (args.length != 1) {
            usage(p, "/login <hasło>");
            return;
        }
        AuthResult result = auth.login(p.getUniqueId(), args[0]);
        if (result == AuthResult.OK) {
            p.sendMessage(success("Zalogowano pomyślnie."));
            onSuccessfulAuthentication(p);
        } else {
            p.sendMessage(error(describe(result)));
        }
    }

    private void changePassword(Player p, String[] args) {
        if (args.length != 2) {
            usage(p, "/changepassword <stare_hasło> <nowe_hasło>");
            return;
        }
        reply(p, auth.changePassword(p.getUniqueId(), args[0], args[1]), "Hasło zostało zmienione.");
    }

    private void unregister(Player p, String[] args) {
        if (args.length != 1) {
            usage(p, "/unregister <hasło>");
            return;
        }
        reply(p, auth.unregister(p.getUniqueId(), args[0]),
                "Konto zostało usunięte. Zarejestruj się ponownie, jeśli chcesz grać dalej.");
    }

    /**
     * Metoda wywoływana po udanym zalogowaniu lub rejestracji.
     * Usuwa efekt ślepoty i przywraca poprawne uprawnienia latania.
     */
    private void onSuccessfulAuthentication(Player player) {
        // Usuwamy wyłącznie efekt zaćmienia (Blindness)
        player.removePotionEffect(PotionEffectType.BLINDNESS);

        // Przywracamy standardowe uprawnienia latania (jeśli gracz nie jest na Creative/Spectator)
        if (player.getGameMode() != GameMode.CREATIVE && player.getGameMode() != GameMode.SPECTATOR) {
            player.setAllowFlight(false);
            player.setFlying(false);
        }
    }

    private void reply(Player p, AuthResult result, String successMessage) {
        if (result == AuthResult.OK) {
            p.sendMessage(success(successMessage));
        } else {
            p.sendMessage(error(describe(result)));
        }
    }

    private void usage(Player p, String usage) {
        p.sendMessage(info("Użycie: " + usage));
    }

    private static String describe(AuthResult result) {
        return switch (result) {
            case ALREADY_REGISTERED -> "Masz już konto. Zaloguj się: /login <hasło>";
            case NOT_REGISTERED -> "Nie masz konta. Zarejestruj się: /register <hasło> <powtórz_hasło>";
            case PASSWORDS_MISMATCH -> "Podane hasła nie są takie same.";
            case PASSWORD_TOO_SHORT -> "Hasło musi mieć co najmniej " + AuthManager.MIN_PASSWORD_LENGTH + " znaki.";
            case WRONG_PASSWORD -> "Nieprawidłowe hasło.";
            case ALREADY_LOGGED_IN -> "Jesteś już zalogowany.";
            case NOT_LOGGED_IN -> "Musisz być zalogowany, aby wykonać tę czynność.";
            case OK -> "";
        };
    }
}