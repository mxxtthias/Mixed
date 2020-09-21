package network.atria.KillEffects;

import static java.lang.Math.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Main;
import network.atria.Util.getPlayerData;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.api.player.event.MatchPlayerDeathEvent;

public class KillEffects implements Listener {

  @EventHandler
  public void onPlayerDeath(MatchPlayerDeathEvent e) {

    MatchPlayer murder = null;
    MatchPlayer victim = e.getVictim();

    if (e.getKiller() != null) {
      murder = e.getKiller().getParty().getPlayer(e.getKiller().getId());
      String getEffect = MySQLSetterGetter.getKillEffect(murder.getId().toString());

      switch (getEffect) {
        case "BLOOD":
          if (getPlayerData.hasRequirePoint(
                  murder.getId().toString(), getPlayerData.getRequirePoints("BLOOD"))
              || getPlayerData.hasDonorRank(murder.getBukkit().getPlayer())) {
            Location location = victim.getBukkit().getLocation();

            createCustomEffect(
                EnumParticle.BLOCK_CRACK,
                location,
                0.75F,
                1.5F,
                0.75F,
                0.01F,
                50,
                getData(),
                murder.getBukkit().getPlayer());

            createCustomEffect(
                EnumParticle.BLOCK_CRACK,
                location,
                0.75F,
                1F,
                0.75F,
                0.01F,
                50,
                getData(),
                murder.getBukkit().getPlayer());
          }
          break;

        case "HEART":
          if (getPlayerData.hasDonorRank(murder.getBukkit().getPlayer())) {
            Location location = victim.getBukkit().getLocation();

            createCustomEffect(
                EnumParticle.HEART,
                location,
                0.75F,
                0.75F,
                0.75F,
                0.01F,
                80,
                null,
                murder.getBukkit().getPlayer());
          }
          break;

        case "SMOKE":
          if (getPlayerData.hasDonorRank(murder.getBukkit().getPlayer())) {
            Location location = victim.getBukkit().getLocation();

            createCustomEffect(
                EnumParticle.SMOKE_LARGE,
                location,
                0.75F,
                1.2F,
                0.75F,
                0.1F,
                70,
                null,
                murder.getBukkit().getPlayer());
          }
          break;

        case "FLAME":
          if (getPlayerData.hasRequirePoint(
                  murder.getId().toString(), getPlayerData.getRequirePoints("FLAME"))
              || getPlayerData.hasDonorRank(murder.getBukkit().getPlayer())) {
            Location location = victim.getBukkit().getLocation();

            createCustomEffect(
                EnumParticle.FLAME,
                location,
                0.75F,
                1.2F,
                0.75F,
                0.01F,
                100,
                null,
                murder.getBukkit().getPlayer());
          }
          break;

        case "RAINBOW":
          if (getPlayerData.hasRequirePoint(
                  murder.getId().toString(), getPlayerData.getRequirePoints("RAINBOW"))
              || getPlayerData.hasDonorRank(murder.getBukkit().getPlayer())) {
            Location location = victim.getBukkit().getLocation();

            double radius = 2;
            double maxheight = 7;

            for (double y = 0; y < maxheight; y += 0.05) {

              double x = Math.sin(y * radius);
              double z = cos(y * radius);

              createCustomEffectRainbow(
                  (float) (location.getX() + x),
                  (float) (location.getY() + y),
                  (float) (location.getZ() + z),
                  0F,
                  0F,
                  0F,
                  0.01F,
                  50,
                  murder.getBukkit().getPlayer());
            }
          }
          break;

        case "DONOR":
          if (murder.getBukkit().hasPermission("pgm.group.donor")) {

            final double radius = 1.2d;
            final Location Location = victim.getBukkit().getLocation().add(0.0d, 2.5d, 0.0d);
            final int point = 30;

            for (int i = 0; i < point; i++) {
              double circle = 2 * Math.PI * i / point;
              Location ring =
                  Location.clone().add(radius * Math.sin(circle), 0.0d, radius * cos(circle));

              createCustomEffectRainbow(
                  (float) ring.getX(),
                  (float) ring.getY(),
                  (float) ring.getZ(),
                  0F,
                  0F,
                  0F,
                  0.01F,
                  50,
                  murder.getBukkit().getPlayer());
            }

            createCustomEffect(
                EnumParticle.SPELL_MOB,
                victim.getBukkit().getLocation(),
                0.5f,
                0.5f,
                0.5f,
                0.001f,
                80,
                null,
                murder.getBukkit().getPlayer());
          }
          break;

        case "SPHERE":
          if (murder.getBukkit().hasPermission("pgm.group.donor")) {

            final MatchPlayer killer = murder;

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

                  createCustomEffect(
                      EnumParticle.CRIT_MAGIC,
                      location,
                      0f,
                      0f,
                      0f,
                      0.1f,
                      1,
                      null,
                      killer.getBukkit().getPlayer());

                  location.subtract(x, y, z);
                }
                if (phi > 2 * Math.PI) {
                  this.cancel();
                }
              }
            }.runTaskTimer(Main.getInstance(), 0, 1);
          }
          break;

        case "MAGIC":
          if (murder.getBukkit().hasPermission("pgm.group.donor")) {

            Location location = victim.getBukkit().getLocation();
            createCustomEffect(
                EnumParticle.SPELL_WITCH,
                location,
                0.5f,
                1f,
                0.5f,
                0.1f,
                100,
                null,
                murder.getBukkit().getPlayer());
          }
          break;
      }
    }
  }

  private void createCustomEffectRainbow(
      float x,
      float y,
      float z,
      float x_offset,
      float y_offset,
      float z_offset,
      float speed,
      int amount,
      Player player) {

    PacketPlayOutWorldParticles packet =
        new PacketPlayOutWorldParticles(
            EnumParticle.REDSTONE,
            true,
            x,
            y,
            z,
            x_offset,
            y_offset,
            z_offset,
            speed,
            amount,
            setRainbow());

    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
  }

  public static int setRainbow() {
    List<Integer> rainbow = new ArrayList<>();

    rainbow.add(Color.AQUA.asRGB());
    rainbow.add(Color.BLUE.asRGB());
    rainbow.add(Color.LIME.asRGB());
    rainbow.add(Color.PURPLE.asRGB());
    rainbow.add(Color.RED.asRGB());
    rainbow.add(Color.WHITE.asRGB());
    rainbow.add(Color.YELLOW.asRGB());

    int index = (new Random()).nextInt(rainbow.size());

    return rainbow.get(index);
  }

  private void createCustomEffect(
      EnumParticle particle,
      Location location,
      float x_offset,
      float y_offset,
      float z_offset,
      float speed,
      int amount,
      int[] data,
      Player player) {
    PacketPlayOutWorldParticles packet =
        new PacketPlayOutWorldParticles(
            particle,
            true,
            (float) location.getX(),
            (float) location.getY(),
            (float) location.getZ(),
            x_offset,
            y_offset,
            z_offset,
            speed,
            amount,
            data);
    ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
  }

  private int[] getData() {
    return new int[] {Material.REDSTONE_BLOCK.getId(), (short) 0};
  }

  public KillEffects(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
}
