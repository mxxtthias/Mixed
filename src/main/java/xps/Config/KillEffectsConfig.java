package xps.Config;

import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.logging.Level;

public class KillEffectsConfig {

    private static FileConfiguration config = null;
    private static File configFile;
    private static String file;
    private static Plugin plugin;

    public KillEffectsConfig(Plugin plugin) {
        this(plugin, null);
    }

    public KillEffectsConfig(Plugin plugin, String fileName) {
        KillEffectsConfig.plugin = plugin;
        file = fileName;
        configFile = new File(plugin.getDataFolder(), file);
    }

    public static void reloadConfig() {
        config = YamlConfiguration.loadConfiguration(configFile);

        final InputStream defConfigStream = plugin.getResource(file);
        if (defConfigStream == null) {
            return;
        }

        config.setDefaults(YamlConfiguration.loadConfiguration(new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
    }

    public static FileConfiguration getCustomConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    public void saveConfig() {
        if (config == null) {
            return;
        }
        try {
            RanksConfig.getCustomConfig().save(configFile);
        } catch (IOException ex) {
            plugin.getLogger().log(Level.SEVERE, "Could not save config to " + configFile, ex);
        }
    }
}
