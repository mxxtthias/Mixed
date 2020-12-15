package network.atria.Effects.Sounds;

import net.kyori.adventure.key.Key;
import net.kyori.adventure.sound.Sound;
import network.atria.Mixed;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.api.setting.SettingKey;
import tc.oc.pgm.api.setting.SettingValue;

public class SoundAPI {

  public void playSound(MatchPlayer player, String key, Float pitch) {
    if (player.getSettings().getValue(SettingKey.SOUNDS).equals(SettingValue.SOUNDS_ALL)) {
      Sound sound = Sound.sound(Key.key(key), Sound.Source.MASTER, 2f, pitch);
      Mixed.get().getAudience().player(player.getBukkit()).playSound(sound);
    }
  }
}
