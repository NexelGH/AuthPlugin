package com.example.authplugin.auth;

public enum AuthResult {
    OK,
    ALREADY_REGISTERED,
    NOT_REGISTERED,
    PASSWORDS_MISMATCH,
    PASSWORD_TOO_SHORT,
    WRONG_PASSWORD,
    ALREADY_LOGGED_IN,
    NOT_LOGGED_IN
}