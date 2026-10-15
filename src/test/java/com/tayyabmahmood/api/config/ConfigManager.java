package com.tayyabmahmood.api.config;

import java.io.IOException;
import java.io.InputStream;
import java.io.UncheckedIOException;
import java.util.Properties;

// Lookup order: -Dkey, then ENV_VAR, then config.properties
public final class ConfigManager {

    private static final Properties FILE_PROPS = load();

    private ConfigManager() {
    }

    private static Properties load() {
        Properties props = new Properties();
        try (InputStream in = ConfigManager.class.getClassLoader().getResourceAsStream("config.properties")) {
            if (in != null) {
                props.load(in);
            }
        } catch (IOException e) {
            throw new UncheckedIOException("Could not read config.properties", e);
        }
        return props;
    }

    public static String get(String key) {
        String sys = System.getProperty(key);
        if (sys != null && !sys.isBlank()) {
            return sys;
        }
        String env = System.getenv(key.replace('.', '_').toUpperCase());
        if (env != null && !env.isBlank()) {
            return env;
        }
        String value = FILE_PROPS.getProperty(key);
        if (value == null) {
            throw new IllegalStateException("Missing configuration value for '" + key + "'");
        }
        return value;
    }

    public static int getInt(String key) {
        return Integer.parseInt(get(key));
    }

    public static String baseUrl() {
        return get("base.url");
    }

    public static String username() {
        return get("auth.username");
    }

    public static String password() {
        return get("auth.password");
    }
}
