package network.atria.KillEffects;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Main;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Arrow;
import org.bukkit.entity.Player;
import org.bukkit.entity.Projectile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.ProjectileLaunchEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

public class ProjectileTrails implements Listener {

  @EventHandler
  public void onProjectileEffect(ProjectileLaunchEvent e) {
    final Projectile projectile = e.getEntity();
    if (projectile.getShooter() instanceof Player) {
      if (projectile instanceof Arrow) {
        final Player player = (Player) projectile.getShooter();
        final String trails =
            MySQLSetterGetter.getProjectileTrails(player.getUniqueId().toString());

        switch (trails) {
          case "HEART":
            playProjectileTrails(EnumParticle.HEART, projectile, player);
            break;
          case "WITCH":
            playProjectileTrails(EnumParticle.SPELL_WITCH, projectile, player);
            break;
          case "RAINBOW_TRAIL":
            playProjectileTrails(EnumParticle.SPELL_MOB, projectile, player);
            break;
          case "GREEN":
            playProjectileTrails(EnumParticle.VILLAGER_HAPPY, projectile, player);
            break;
          case "NOTE":
            playProjectileTrails(EnumParticle.NOTE, projectile, player);
            break;
        }
      }
    }
  }

  private void playProjectileTrails(EnumParticle particle, Projectile projectile, Player player) {
    BukkitTask task =
        new BukkitRunnable() {
          public void run() {

            if (projectile == null || projectile.isOnGround() || projectile.isDead()) {
              cancel();
            }

            final double x = projectile.getLocation().getX();
            final double y = projectile.getLocation().getY();
            final double z = projectile.getLocation().getZ();

            final PacketPlayOutWorldParticles packet =
                new PacketPlayOutWorldParticles(
                    particle, true, (float) x, (float) y, (float) z, 0F, // x offset
                    0F, // y offset
                    0F, // z offset
                    1F, // speed
                    15, // amount
                    0);

            ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
          }
        }.runTaskTimer(Main.getInstance(), 2, 2);
  }

  public ProjectileTrails(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
}
