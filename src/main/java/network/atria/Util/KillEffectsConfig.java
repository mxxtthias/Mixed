package network.atria.Util;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

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

    config.setDefaults(
        YamlConfiguration.loadConfiguration(
            new InputStreamReader(defConfigStream, StandardCharsets.UTF_8)));
  }

  public static FileConfiguration getCustomConfig() {
    if (config == null) {
      reloadConfig();
    }
    return config;
  }
}
