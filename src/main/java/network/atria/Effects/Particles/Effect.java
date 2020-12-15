package network.atria.Effects.Particles;

import java.util.UUID;
import net.kyori.adventure.text.Component;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Util.TextFormat;
import tc.oc.pgm.api.player.MatchPlayer;

public class Effect {

  private final String Name;
  private final Component coloredName;
  private final int point;
  private final boolean donor;

  public Effect(String Name, Component coloredName, int point, boolean donor) {
    this.Name = Name;
    this.coloredName = coloredName;
    this.point = point;
    this.donor = donor;
  }

  public String getName() {
    return this.Name;
  }

  public String getColoredName() {
    return TextFormat.format(this.coloredName);
  }

  public Integer getPoint() {
    return this.point;
  }

  public boolean canUseDonor(MatchPlayer player) {
    return this.donor && player.getBukkit().hasPermission("pgm.group.donor");
  }

  public boolean hasRequirePoint(UUID uuid) {
    return MySQLSetterGetter.getPoints(uuid) >= this.point;
  }
}
