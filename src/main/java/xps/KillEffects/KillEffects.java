package xps.KillEffects;

import net.minecraft.server.v1_8_R3.EnumParticle;
import net.minecraft.server.v1_8_R3.PacketPlayOutWorldParticles;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.api.player.event.MatchPlayerDeathEvent;

import java.util.Random;

public class KillEffects implements Listener {

    private final getPlayerData playerData = new getPlayerData();

    @EventHandler
    public void BLOOD(MatchPlayerDeathEvent e) {

        MatchPlayer murder = null;
        MatchPlayer victim = e.getVictim();

        if (e.getKiller() != null) {
            murder = e.getKiller().getParty().getPlayer(e.getKiller().getId());
            if (playerData.isSelected(murder.getId().toString(), "BLOOD")) {
                if (playerData.hasRequirePoint(murder.getId().toString(), playerData.getRequirePoints("BLOOD")) || playerData.hasDonorRank(murder.getBukkit().getPlayer())) {
                    Location location = victim.getBukkit().getLocation();

                    createCustomEffect(
                            EnumParticle.BLOCK_CRACK,
                            location,
                            0.75F,
                            1.5F,
                            0.75F,
                            0.01F,
                            50,
                            getData(Material.REDSTONE_BLOCK, (short) 0),
                            murder.getBukkit().getPlayer());

                    createCustomEffect(
                            EnumParticle.BLOCK_CRACK,
                            location,
                            0.75F,
                            1F,
                            0.75F,
                            0.01F,
                            50,
                            getData(Material.REDSTONE_BLOCK, (short) 0),
                            murder.getBukkit().getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void HEART(MatchPlayerDeathEvent e) {

        MatchPlayer murder = null;
        MatchPlayer victim = e.getVictim();

        if (e.getKiller() != null) {
            murder = e.getKiller().getParty().getPlayer(e.getKiller().getId());
            if (playerData.isSelected(murder.getId().toString(), "HEART")) {
                if (playerData.hasDonorRank(murder.getBukkit().getPlayer())) {
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
            }
        }
    }

    @EventHandler
    public void SMOKE(MatchPlayerDeathEvent e) {

        MatchPlayer murder = null;
        MatchPlayer victim = e.getVictim();

        if (e.getKiller() != null) {
            murder = e.getKiller().getParty().getPlayer(e.getKiller().getId());
            if (playerData.isSelected(murder.getId().toString(), "SMOKE")) {
                if (playerData.hasDonorRank(murder.getBukkit().getPlayer())) {
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
                            murder.getBukkit().getPlayer()
                    );

                }
            }
        }
    }

    @EventHandler
    public void FLAME(MatchPlayerDeathEvent e) {

        MatchPlayer murder = null;
        MatchPlayer victim = e.getVictim();

        if (e.getKiller() != null) {
            murder = e.getKiller().getParty().getPlayer(e.getKiller().getId());
            if (playerData.isSelected(murder.getId().toString(), "FLAME")) {
                if (playerData.hasRequirePoint(murder.getId().toString(), playerData.getRequirePoints("FLAME")) || playerData.hasDonorRank(murder.getBukkit().getPlayer())) {
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
            }
        }
    }

    @EventHandler
    public void RAINBOW(MatchPlayerDeathEvent e) {

        MatchPlayer murder = null;
        MatchPlayer victim = e.getVictim();

        if (e.getKiller() != null) {
            murder = e.getKiller().getParty().getPlayer(e.getKiller().getId());
            if (playerData.isSelected(murder.getId().toString(), "RAINBOW")) {
                if (playerData.hasRequirePoint(murder.getId().toString(), playerData.getRequirePoints("RAINBOW")) || playerData.hasDonorRank(murder.getBukkit().getPlayer())) {
                    Location location = victim.getBukkit().getLocation();

                    double radius = 2;
                    double maxheight = 7;

                    for (double y = 0; y < maxheight; y += 0.05) {

                        double x = Math.sin(y * radius);
                        double z = Math.cos(y * radius);

                        createCustomEffectWithColor(
                                EnumParticle.REDSTONE,
                                ((float) (location.getX() + x)),
                                ((float) (location.getY() + y)),
                                ((float) (location.getZ() + z)),
                                0,
                                0,
                                0,
                                0.01F,
                                10,
                                murder.getBukkit().getPlayer()
                        );
                    }

                }
            }
        }
    }

    @EventHandler
    public void Donor(MatchPlayerDeathEvent e) {

        MatchPlayer murder = null;
        MatchPlayer victim = e.getVictim();

        if (e.getKiller() != null) {
            murder = e.getKiller().getParty().getPlayer(e.getKiller().getId());
            if (playerData.isSelected(murder.getId().toString(), "DONOR")) {
                if (murder.getBukkit().hasPermission("pgm.group.donor")) {

                    final double radius = 1.2d;
                    final Location Location = victim.getBukkit().getLocation().add(0.0d, 2.5d, 0.0d);
                    final int point = 30;

                    for (int i = 0; i < point; i++) {
                        double circle = 2 * Math.PI * i / point;
                        Location ring = Location.clone().add(radius * Math.sin(circle), 0.0d, radius * Math.cos(circle));

                        createCustomEffect(
                                EnumParticle.REDSTONE,
                                ring,
                                0.0f,
                                0.0f,
                                0.0f,
                                0.01f,
                                20,
                                null,
                                murder.getBukkit().getPlayer()
                        );
                    }

                    createCustomEffect(
                            EnumParticle.SPELL_MOB,
                            victim.getBukkit().getLocation(),
                            0.5f,
                            0.5f,
                            0.5f,
                            0.01f,
                            100,
                            null,
                            murder.getBukkit().getPlayer());
                }
            }
        }
    }

    @EventHandler
    public void WATER_SPHERE(MatchPlayerDeathEvent e) {
        MatchPlayer murder = null;
        MatchPlayer victim = e.getVictim();

        if (e.getKiller() != null) {
            murder = e.getKiller().getParty().getPlayer(e.getKiller().getId());
            if (playerData.isSelected(murder.getId().toString(), "WATER_SPHERE")) {
                if (murder.getBukkit().hasPermission("pgm.group.donor")) {

                    for (int i = 0; i < Math.PI; i += Math.PI / 10) {
                        double radius = Math.sin(i);
                        double y = Math.cos(i);

                        for (double a = 0; a < Math.PI * 2; a += Math.PI / 10) {
                            double x = Math.cos(a) * radius;
                            double z = Math.sin(a) * radius;

                            Location location = victim.getBukkit().getLocation();
                            location.add(x, y, z);

                            createCustomEffect(
                                    EnumParticle.DRIP_LAVA,
                                    location,
                                    0f,
                                    0f,
                                    0f,
                                    0.1f,
                                    50,
                                    null,
                                    murder.getBukkit().getPlayer());

                            location.subtract(x, y, z);
                        }
                    }
                }
            }
        }
    }

    private void createCustomEffectWithColor(EnumParticle particle, float x, float y, float z, float x_offset, float y_offset, float z_offset, float speed, int amount, Player player) {

        int[] rainbow = null;

        Random random = new Random();
        int r = random.nextInt(256) + 1;
        int g = random.nextInt(256) + 1;
        int b = random.nextInt(256) + 1;

        if (r > 0 && g > 0 && b > 0) {
            rainbow = new int[]{r, g, b};
        }

        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
                particle,
                true,
                x,
                y,
                z,
                x_offset,
                y_offset,
                z_offset,
                speed,
                amount,
                rainbow
        );

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    private void createCustomEffect(EnumParticle particle, Location location, float x_offset, float y_offset, float z_offset, float speed, int amount, int[] data, Player player) {
        PacketPlayOutWorldParticles packet = new PacketPlayOutWorldParticles(
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
                data
        );

        ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
    }

    private int[] getData(Material data, short id) {
        return new int[]{data.getId(), id};
    }

    public KillEffects(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
