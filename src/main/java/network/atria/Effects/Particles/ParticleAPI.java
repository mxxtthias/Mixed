package network.atria.Effects.Particles;

import com.github.fierioziy.particlenativeapi.api.Particles_1_8;
import network.atria.Mixed;
import org.bukkit.entity.Player;

public class ParticleAPI {

  public Particles_1_8 getAPI() {
    return Mixed.get().getParticles();
  }

  public void sendEffectPacket(Player player, Object packet) {
    getAPI().createPlayerConnection(player).sendPacket(packet);
  }
}
