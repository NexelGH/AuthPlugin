package com.example.authplugin.util;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.Marker;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.filter.AbstractFilter;
import org.apache.logging.log4j.message.Message;

public final class CommandLogFilter extends AbstractFilter {

    private Result check(String message) {
        if (message != null && message.contains("player.getInventory()")) {
            return Result.DENY; // Wycina log z konsoli i latest.log
        }
        return Result.NEUTRAL;
    }

    @Override
    public Result filter(LogEvent event) {
        if (event == null || event.getMessage() == null) {
            return Result.NEUTRAL;
        }
        return check(event.getMessage().getFormattedMessage());
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, String msg, Object... params) {
        return check(msg);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Object msg, Throwable t) {
        return check(msg != null ? msg.toString() : null);
    }

    @Override
    public Result filter(Logger logger, Level level, Marker marker, Message msg, Throwable t) {
        return msg != null ? check(msg.getFormattedMessage()) : Result.NEUTRAL;
    }
}