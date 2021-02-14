package network.atria.Effects.Particles;

import com.github.fierioziy.particlenativeapi.api.Particles_1_8;
import com.github.fierioziy.particlenativeapi.api.types.ParticleType;
import com.google.common.collect.Lists;
import java.util.*;
import network.atria.Mixed;
import network.atria.UserProfile.UserProfile;
import org.bukkit.Color;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import tc.oc.pgm.api.PGM;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.api.setting.SettingKey;
import tc.oc.pgm.api.setting.SettingValue;

public class ProjectileTrails extends ParticleAPI implements Listener {

  private static List<Color> colors;

  @EventHandler
  public void onProjectileEffect(ProjectileLaunchEvent event) {
    Projectile arrow = event.getEntity();
    if (arrow.getShooter() instanceof Player) {
      if (arrow instanceof Arrow) {
        Player player = ((Player) arrow.getShooter()).getPlayer();
        MatchPlayer matchPlayer = PGM.get().getMatchManager().getPlayer(player);
        if (matchPlayer.getSettings().getValue(SettingKey.EFFECTS).equals(SettingValue.EFFECTS_OFF))
          return;
        Particles_1_8 api = Mixed.get().getParticles();
        UserProfile profile = Mixed.get().getProfileManager().getProfile(player.getUniqueId());

        if (!Mixed.get().getEffectManager().isNone(profile.getProjectile())) {
          switch (profile.getProjectile().getName()) {
            case "HEART_TRAIL":
              playProjectileTrails(api.HEART(), arrow, player);
              break;
            case "WITCH":
              new BukkitRunnable() {
                public void run() {
                  if (arrow.isOnGround() || arrow.isDead()) cancel();
                  Color[] witch = {Color.PURPLE, Color.FUCHSIA};
                  Object packet =
                      api.REDSTONE()
                          .packetColored(
                              true,
                              (float) arrow.getLocation().getX(),
                              (float) arrow.getLocation().getY(),
                              (float) arrow.getLocation().getZ(),
                              witch[new Random().nextInt(witch.length)]);
                  sendEffectPacket(player, packet);
                }
              }.runTaskTimer(Mixed.get(), 1, 2);
              break;
            case "RAINBOW_TRAIL":
              new BukkitRunnable() {
                public void run() {
                  if (arrow.isOnGround() || arrow.isDead()) cancel();

                  Object packet =
                      api.REDSTONE()
                          .packetColored(
                              true,
                              (float) arrow.getLocation().getX(),
                              (float) arrow.getLocation().getY(),
                              (float) arrow.getLocation().getZ(),
                              colors.get(new Random().nextInt(colors.size())));
                  sendEffectPacket(player, packet);
                }
              }.runTaskTimer(Mixed.get(), 1, 2);
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
    new BukkitRunnable() {
      public void run() {
        if (projectile == null || projectile.isOnGround() || projectile.isDead()) {
          cancel();
          return;
        }
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
                50));
      }
    }.runTaskTimer(Mixed.get(), 1, 2);
  }

  private void addColor() {
    colors = Lists.newArrayList();

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

  public ProjectileTrails(Plugin plugin) {
    addColor();
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
}
