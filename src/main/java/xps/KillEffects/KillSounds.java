package xps.KillEffects;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import org.bukkit.util.Vector;
import tc.oc.pgm.api.match.MatchScope;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.api.player.event.MatchPlayerDeathEvent;
import tc.oc.pgm.api.setting.SettingKey;
import tc.oc.pgm.api.setting.SettingValue;
import tc.oc.pgm.events.ListenerScope;
import tc.oc.pgm.util.chat.Sound;
import xps.Database.MySQLSetterGetter;

@ListenerScope(MatchScope.RUNNING)
public class KillSounds implements Listener {

    private final getPlayerData playerData = new getPlayerData();

    private void playSound(MatchPlayer player, Sound sound) {
        if (player.getSettings().getValue(SettingKey.SOUNDS).equals(SettingValue.SOUNDS_ALL)) {
            player.playSound(sound);
        }
    }

    @EventHandler
    public void onMatchPlayerDeath(MatchPlayerDeathEvent e) {
        MatchPlayer killer = null;
        MatchPlayer victim = e.getVictim();;

        if (e.getKiller() != null) {
            killer = e.getKiller().getParty().getPlayer(e.getKiller().getId());
            if (!killer.getParty().equals(victim.getParty())) {

                String getSound = MySQLSetterGetter.getKillSound(killer.getId().toString());
                Vector death = victim.getBukkit().getLocation().toVector();

                switch (getSound) {
                    case "DEFAULT":
                        playSound(killer, new Sound("random.levelup", 1f, 1.5f));
                        playSound(killer, new Sound("mob.irongolem.hit", 1, 4f / 3f, death));

                        if(killer.getParty() == victim.getParty()) {
                            playSound(killer ,new Sound("mob.irongolem.hit", death));
                        }
                        break;
                    case "HOWL":
                        if(playerData.hasRequirePoint(killer.getId().toString(), playerData.getRequirePoints("HOWL"))) {
                            playSound(killer, new Sound("mob.wolf.howl", 1f, 1f));
                        }
                        break;
                    case "VILLAGER":
                        if(playerData.hasRequirePoint(killer.getId().toString(), playerData.getRequirePoints("VILLAGER"))) {
                            playSound(killer, new Sound("mob.villager.death", 1f, 0.8f));
                        }
                        break;
                    case "BOMB":
                        if(playerData.hasRequirePoint(killer.getId().toString(), playerData.getRequirePoints("BOMB"))) {
                            playSound(killer, new Sound("random.explode", 1f, 2f));
                        }
                        break;
                    case "BURP":
                        if(playerData.hasRequirePoint(killer.getId().toString(), playerData.getRequirePoints("BURP"))) {
                            playSound(killer, new Sound("random.burp", 1f, 0.1f));
                        }
                        break;
                }
            }
        }
    }

    public KillSounds(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
