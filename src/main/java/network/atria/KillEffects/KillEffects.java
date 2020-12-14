package network.atria.KillEffects;

import static java.lang.Math.*;

import com.github.fierioziy.particlenativeapi.api.Particles_1_8;
import java.util.*;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Mixed;
import network.atria.Util.EffectUtils;
import network.atria.Util.KillEffectsConfig;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.api.player.event.MatchPlayerDeathEvent;

public class KillEffects extends EffectUtils implements Listener {

  private static Set<Effect> effects;
  private List<Color> colors;

  public static Set<Effect> getEffects() {
    return effects;
  }

  private void addEffects() {
    FileConfiguration config = KillEffectsConfig.getCustomConfig();
    effects = new HashSet<>();

    config
        .getConfigurationSection("KILL_EFFECT")
        .getKeys(false)
        .forEach(
            effect ->
                effects.add(
                    new Effect(
                        effect,
                        Component.text(effect, NamedTextColor.GREEN, TextDecoration.BOLD),
                        config.getInt("KILL_EFFECT." + effect + ".points"),
                        config.getBoolean("KILL_EFFECT." + effect + ".donor"))));
  }

  @EventHandler
  public void onPlayerDeath(MatchPlayerDeathEvent e) {

    Particles_1_8 api = Mixed.get().getParticles();
    MatchPlayer murder = null;
    MatchPlayer victim = e.getVictim();

    if (e.getKiller() != null) {
      murder = e.getKiller().getParty().getPlayer(e.getKiller().getId());
      Location location = victim.getBukkit().getLocation();
      if (murder != null) {
        MatchPlayer finalMurder1 = murder;
        Optional<Effect> effect =
            effects.stream()
                .filter(
                    name ->
                        name.getName()
                            .equalsIgnoreCase(
                                MySQLSetterGetter.getKillEffect(finalMurder1.getId().toString())))
                .findFirst();

        if (effect.isPresent()) {
          switch (effect.get().getName()) {
            case "BLOOD":
              sendEffectPacket(
                  murder.getBukkit(),
                  api.BLOCK_CRACK()
                      .of(Material.REDSTONE_BLOCK)
                      .packet(true, location, 0.5D, 1.5D, 0.5D, 0.1D, 100));
              break;

            case "HEART":
              sendEffectPacket(
                  murder.getBukkit(),
                  api.HEART().packet(true, location, 0.75D, 0.85D, 0.75D, 0.01D, 50));
              break;

            case "SMOKE":
              sendEffectPacket(
                  murder.getBukkit(),
                  api.SMOKE_LARGE().packet(true, location, 0.75D, 1.2D, 0.75D, 0.1D, 70));
              break;

            case "FLAME":
              sendEffectPacket(
                  murder.getBukkit(),
                  api.FLAME().packet(true, location, 0.75D, 1.2D, 0.75D, 0.1D, 100));
              break;
            case "RAINBOW":
              double maxheight = 7;

              for (double y = 0; y < maxheight; y += 0.05) {
                double radius = 2;
                double x = Math.sin(y * radius);
                double z = cos(y * radius);

                Object packet =
                    api.REDSTONE()
                        .packetColored(
                            true,
                            (float) (location.getX() + x),
                            (float) (location.getY() + y),
                            (float) (location.getZ() + z),
                            colors.get(new Random().nextInt(colors.size())));

                api.createPlayerConnection(murder.getBukkit()).sendPacket(packet);
              }
              break;

            case "DONOR":
              double radius = 1.2d;
              Location Location = victim.getBukkit().getLocation().add(0.0D, 2.5D, 0.0D);
              int point = 30;

              sendEffectPacket(
                  murder.getBukkit(),
                  api.REDSTONE()
                      .packetColored(
                          true, location, colors.get(new Random().nextInt(colors.size()))));

              for (int i = 0; i < point; i++) {
                double circle = 2 * Math.PI * i / point;
                Location ring =
                    Location.clone().add(radius * Math.sin(circle), 0.0d, radius * cos(circle));
                Object packet =
                    api.REDSTONE()
                        .packetColored(
                            true,
                            ring.getX(),
                            ring.getY(),
                            ring.getZ(),
                            colors.get(new Random().nextInt(colors.size())));

                api.createPlayerConnection(murder.getBukkit()).sendPacket(packet);
              }
              break;
            case "SPHERE":
              MatchPlayer finalMurder = murder;
              new BukkitRunnable() {
                double phi = 0;

                public void run() {
                  phi += Math.PI / 10;

                  for (double theta = 0; theta <= 2 * Math.PI; theta += Math.PI / 40) {
                    double r = 2;
                    double x = r * cos(theta) * sin(phi);
                    double y = r * cos(phi) + 1.5;
                    double z = r * sin(theta) * sin(phi);

                    Location location = victim.getBukkit().getLocation();
                    location.add(x, y, z);

                    sendEffectPacket(
                        finalMurder.getBukkit(),
                        api.CRIT_MAGIC().packet(true, location, 0D, 0D, 0D, 0.1D, 60));
                    location.subtract(x, y, z);
                  }
                  if (phi > 2 * Math.PI) {
                    this.cancel();
                  }
                }
              }.runTaskTimer(Mixed.get(), 0, 1);
              break;

            case "MAGIC":
              Color[] magic = {Color.PURPLE, Color.FUCHSIA};
              sendEffectPacket(
                  murder.getBukkit(),
                  api.REDSTONE()
                      .packetColored(true, location, magic[new Random().nextInt(magic.length)]));
              sendEffectPacket(
                  murder.getBukkit(),
                  api.SPELL_MOB_AMBIENT().packet(false, location, 0D, 0D, 0D, 0.1D, 100));
              break;
          }
        }
      }
    }
  }

  private void addColor() {
    colors = new ArrayList<>();

    colors.add(Color.BLUE);
    colors.add(Color.LIME);
    colors.add(Color.ORANGE);
    colors.add(Color.PURPLE);
    colors.add(Color.AQUA);
    colors.add(Color.YELLOW);
    colors.add(Color.GREEN);
    colors.add(Color.RED);
    colors.add(Color.OLIVE);
    colors.add(Color.MAROON);
    colors.add(Color.TEAL);
    colors.add(Color.NAVY);
  }

  public KillEffects(Plugin plugin) {
    addEffects();
    addColor();
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
}
