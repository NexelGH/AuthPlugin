package com.example.authplugin.auth;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.util.UUID;
import java.util.logging.Level;

/**
 * Przechowuje konta w users.yml, kluczem jest UUID gracza.
 * Struktura: users.<uuid>.password: <hash>
 */
public final class UserStorage {

    private static final String ROOT = "users";
    private static final String PASSWORD_KEY = "password";

    private final JavaPlugin plugin;
    private final File file;
    private final YamlConfiguration yaml;

    public UserStorage(JavaPlugin plugin) {
        this.plugin = plugin;
        this.plugin.getDataFolder().mkdirs();
        this.file = new File(plugin.getDataFolder(), "users.yml");
        this.yaml = YamlConfiguration.loadConfiguration(file);
    }

    public boolean contains(UUID id) {
        return yaml.isString(passwordPath(id));
    }

    public String getHash(UUID id) {
        return yaml.getString(passwordPath(id));
    }

    public void saveHash(UUID id, String hash) {
        yaml.set(passwordPath(id), hash);
        save();
    }

    public void remove(UUID id) {
        yaml.set(ROOT + "." + id, null);
        save();
    }

    private static String passwordPath(UUID id) {
        return ROOT + "." + id + "." + PASSWORD_KEY;
    }

    private void save() {
        try {
            yaml.save(file);
        } catch (IOException e) {
            plugin.getLogger().log(Level.SEVERE, "Nie udało się zapisać users.yml", e);
        }
    }
}