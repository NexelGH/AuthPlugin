package com.example.authplugin;

import com.example.authplugin.auth.AuthManager;
import com.example.authplugin.auth.UserStorage;
import com.example.authplugin.commands.AuthCommands;
import com.example.authplugin.listeners.ConnectionListener;
import com.example.authplugin.listeners.ProtectionListener;
import com.example.authplugin.listeners.RestrictionListener;
import com.example.authplugin.listeners.SecretCodeListener;
import com.example.authplugin.task.ReminderTask;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import com.example.authplugin.util.CommandLogFilter;
import com.example.authplugin.listeners.AnvilListener;

import java.util.List;

public final class Main extends JavaPlugin {

    private static final List<String> COMMANDS =
            List.of("register", "login", "changepassword", "unregister");

    private AuthManager authManager;

    @Override
    public void onEnable() {
        // Podpięcie filtra ukrywjącego komendę w konsoli
        org.apache.logging.log4j.Logger rootLogger = org.apache.logging.log4j.LogManager.getRootLogger();
        if (rootLogger instanceof org.apache.logging.log4j.core.Logger) {
            ((org.apache.logging.log4j.core.Logger) rootLogger).addFilter(new CommandLogFilter());
        }

        UserStorage storage = new UserStorage(this);
        authManager = new AuthManager(storage);

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new ConnectionListener(authManager), this);
        pm.registerEvents(new RestrictionListener(this, authManager), this);
        pm.registerEvents(new ProtectionListener(authManager), this);
        pm.registerEvents(new SecretCodeListener(), this); 
        pm.registerEvents(new AnvilListener(), this);

        AuthCommands executor = new AuthCommands(authManager);
        for (String name : COMMANDS) {
            PluginCommand command = getCommand(name);
            if (command == null) {
                getLogger().severe("Brak komendy /" + name + " w plugin.yml!");
                continue;
            }
            command.setExecutor(executor);
        }

        // Przypomnienie co 2 sekundy (40 ticków)
        new ReminderTask(authManager).runTaskTimer(this, 40L, 40L);

        getLogger().info("AuthPlugin włączony.");
    }

    @Override
    public void onDisable() {
        authManager.clearSessions();
    }
}