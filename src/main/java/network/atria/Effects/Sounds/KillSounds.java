package network.atria.Effects.Sounds;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Effects.Particles.Effect;
import network.atria.Util.KillEffectsConfig;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import tc.oc.pgm.api.match.MatchScope;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.api.player.event.MatchPlayerDeathEvent;
import tc.oc.pgm.events.ListenerScope;

@ListenerScope(MatchScope.RUNNING)
public class KillSounds extends SoundAPI implements Listener {

  private static Set<Effect> sounds;

  public static Set<Effect> getSounds() {
    return sounds;
  }

  private void addSounds() {
    FileConfiguration config = KillEffectsConfig.getCustomConfig();
    sounds = new HashSet<>();
    config
        .getConfigurationSection("KILL_SOUND")
        .getKeys(false)
        .forEach(
            sound ->
                sounds.add(
                    new Effect(
                        sound,
                        Component.text(sound, NamedTextColor.GREEN, TextDecoration.BOLD),
                        config.getInt("KILL_SOUND." + sound + ".points"),
                        config.getBoolean("KILL_SOUND." + sound + ".donor"))));
  }

  @EventHandler
  public void onMatchPlayerDeath(MatchPlayerDeathEvent e) {
    MatchPlayer killer = null;
    MatchPlayer victim = e.getVictim();

    if (e.getKiller() != null) {
      killer = e.getKiller().getParty().getPlayer(e.getKiller().getId());
      MatchPlayer finalKiller = killer;
      Optional<Effect> sound =
          sounds.stream()
              .filter(
                  name ->
                      name.getName()
                          .equalsIgnoreCase(
                              MySQLSetterGetter.getKillSound(finalKiller.getId().toString())))
              .findFirst();

      if (sound.isPresent()) {
        switch (sound.get().getName()) {
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
    addSounds();
    plugin.getServer().getPluginManager().registerEvents(this, plugin);
  }
}
