package network.atria.Effects.Sounds;

import network.atria.Mixed;
import network.atria.UserProfile.UserProfile;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import tc.oc.pgm.api.match.MatchScope;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.api.player.event.MatchPlayerDeathEvent;
import tc.oc.pgm.api.setting.SettingKey;
import tc.oc.pgm.api.setting.SettingValue;
import tc.oc.pgm.events.ListenerScope;

@ListenerScope(MatchScope.RUNNING)
public class KillSounds extends SoundAPI implements Listener {

  @EventHandler
  public void onMatchPlayerDeath(MatchPlayerDeathEvent e) {
    MatchPlayer killer;
    MatchPlayer victim = e.getVictim();

    if (e.getKiller() != null) {
      killer = e.getKiller().getParty().getPlayer(e.getKiller().getId());
      if (!killer.getSettings().getValue(SettingKey.SOUNDS).equals(SettingValue.SOUNDS_ALL)) return;

      UserProfile profile = Mixed.get().getProfileManager().getProfile(killer.getId());
      if (!Mixed.get().getEffectManager().isNone(profile.getKillsound())) {
        switch (profile.getKilleffect().getName()) {
          case "DEFAULT":
            if (!killer.getParty().equals(victim.getParty())) {
              playSound(killer, "random.levelup", 1.5f);
              playSound(killer, "mob.irongolem.hit", 4f / 3f);
            } else {
              playSound(victim, "mob.irongolem.hit", 1f);
            }
            break;
          case "HOWL":
            if (!killer.getParty().equals(victim.getParty())) {
              playSound(killer, "mob.wolf.howl", 1f);
            } else {
              playSound(victim, "mob.irongolem.death", 1f);
            }
            break;
          case "VILLAGER":
            if (!killer.getParty().equals(victim.getParty())) {
              playSound(killer, "mob.villager.death", 0.8f);
            } else {
              playSound(victim, "mob.irongolem.death", 1f);
            }
            break;
          case "BOMB":
            if (!killer.getParty().equals(victim.getParty())) {
              playSound(killer, "random.explode", 2f);
            } else {
              playSound(victim, "mob.irongolem.death", 1f);
            }
            break;
          case "BURP":
            if (!killer.getParty().equals(victim.getParty())) {
              playSound(killer, "random.burp", 0.1f);
            } else {
              playSound(victim, "mob.irongolem.death", 1f);
            }
            break;
        }
      }
    } else {
      playSound(victim, "mob.irongolem.death", 1f);
    }
  }

  public KillSounds(Plugin plugin) {
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
}
