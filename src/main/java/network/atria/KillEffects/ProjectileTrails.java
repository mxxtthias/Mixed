package network.atria.KillEffects;

import com.github.fierioziy.particlenativeapi.api.Particles_1_8;
import com.github.fierioziy.particlenativeapi.api.types.ParticleType;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Mixed;
import network.atria.Util.EffectUtils;
import network.atria.Util.KillEffectsConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ProjectileTrails extends EffectUtils implements Listener {

  private static Set<Effect> projectiles;

  public static Set<Effect> getProjectiles() {
    return projectiles;
  }

  private void addProjectiles() {
    FileConfiguration config = KillEffectsConfig.getCustomConfig();
    projectiles = new HashSet<>();

    config
        .getConfigurationSection("PROJECTILE_TRAILS")
        .getKeys(false)
        .parallelStream()
        .forEach(
            projectile ->
                projectiles.add(
                    new Effect(
                        projectile,
                        Component.text(projectile, NamedTextColor.GREEN, TextDecoration.BOLD),
                        config.getInt("PROJECTILE_TRAILS." + projectile + ".points"),
                        config.getBoolean("PROJECTILE_TRAILS." + projectile + ".donor"))));
  }

  @EventHandler
  public void onProjectileEffect(ProjectileLaunchEvent event) {
    Projectile arrow = event.getEntity();
    if (arrow.getShooter() instanceof Player) {
      if (arrow instanceof Arrow) {
        Player player = ((Player) arrow.getShooter()).getPlayer();
        Particles_1_8 api = Mixed.get().getParticles();
        Optional<Effect> projectile =
            projectiles.stream()
                .filter(
                    name ->
                        name.getName()
                            .equalsIgnoreCase(
                                MySQLSetterGetter.getProjectileTrails(
                                    player.getUniqueId().toString())))
                .findFirst();

        if (projectile.isPresent()) {
          switch (projectile.get().getName()) {
            case "HEART":
              playProjectileTrails(api.HEART(), arrow, player);
              break;
            case "WITCH":
              playProjectileTrails(api.SPELL_WITCH(), arrow, player);
              break;
            case "RAINBOW_TRAIL":
              playProjectileTrails(api.SPELL_MOB(), arrow, player);
              break;
            case "GREEN":
              playProjectileTrails(api.VILLAGER_HAPPY(), arrow, player);
              break;
            case "NOTE":
              playProjectileTrails(api.NOTE(), arrow, player);
              break;
          }
        }
      }
    }
  }

  private void playProjectileTrails(ParticleType type, Projectile projectile, Player player) {
    BukkitTask task =
        new BukkitRunnable() {
          public void run() {
            if (projectile == null || projectile.isOnGround() || projectile.isDead()) cancel();
            sendEffectPacket(
                player,
                type.packet(
                    true,
                    (float) projectile.getLocation().getX(),
                    (float) projectile.getLocation().getY(),
                    (float) projectile.getLocation().getZ(),
                    0D,
                    0D,
                    0D,
                    1D,
                    15));
          }
        }.runTaskTimer(Mixed.get(), 2, 2);
  }

  public ProjectileTrails(Plugin plugin) {
    addProjectiles();
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
}
