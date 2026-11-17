package com.example.authplugin.auth;

import java.util.Set;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;

/**
 * Logika uwierzytelniania. Sesje są tylko w pamięci (czyszczone przy wyjściu).
 * ConcurrentHashMap, bo listener czatu działa na wątku asynchronicznym.
 */
public final class AuthManager {

    public static final int MIN_PASSWORD_LENGTH = 4;

    private final UserStorage storage;
    private final Set<UUID> authenticated = ConcurrentHashMap.newKeySet();

    public AuthManager(UserStorage storage) {
        this.storage = storage;
    }

    // ---- stan ----

    public boolean isAuthenticated(UUID id) {
        return authenticated.contains(id);
    }

    public boolean isLocked(UUID id) {
        return !isAuthenticated(id);
    }

    public boolean isRegistered(UUID id) {
        return storage.contains(id);
    }

    public void logout(UUID id) {
        authenticated.remove(id);
    }

    public void clearSessions() {
        authenticated.clear();
    }

    // ---- operacje ----

    public AuthResult register(UUID id, String password, String repeat) {
        if (isRegistered(id)) return AuthResult.ALREADY_REGISTERED;
        if (!password.equals(repeat)) return AuthResult.PASSWORDS_MISMATCH;
        if (password.length() < MIN_PASSWORD_LENGTH) return AuthResult.PASSWORD_TOO_SHORT;

        storage.saveHash(id, PasswordHasher.hash(password));
        authenticated.add(id);
        return AuthResult.OK;
    }

    public AuthResult login(UUID id, String password) {
        if (!isRegistered(id)) return AuthResult.NOT_REGISTERED;
        if (isAuthenticated(id)) return AuthResult.ALREADY_LOGGED_IN;
        if (!PasswordHasher.verify(password, storage.getHash(id))) return AuthResult.WRONG_PASSWORD;

        authenticated.add(id);
        return AuthResult.OK;
    }

    public AuthResult changePassword(UUID id, String oldPassword, String newPassword) {
        if (isLocked(id)) return AuthResult.NOT_LOGGED_IN;
        if (!PasswordHasher.verify(oldPassword, storage.getHash(id))) return AuthResult.WRONG_PASSWORD;
        if (newPassword.length() < MIN_PASSWORD_LENGTH) return AuthResult.PASSWORD_TOO_SHORT;

        storage.saveHash(id, PasswordHasher.hash(newPassword));
        return AuthResult.OK;
    }

    public AuthResult unregister(UUID id, String password) {
        if (isLocked(id)) return AuthResult.NOT_LOGGED_IN;
        if (!PasswordHasher.verify(password, storage.getHash(id))) return AuthResult.WRONG_PASSWORD;

        storage.remove(id);
        authenticated.remove(id);
        return AuthResult.OK;
    }
}