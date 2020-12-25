package network.atria.Ranks;

import java.util.*;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import net.kyori.adventure.text.TextComponent;
import net.kyori.adventure.text.format.NamedTextColor;
import net.kyori.adventure.text.format.TextDecoration;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Mixed;
import network.atria.Util.RanksConfig;
import network.atria.Util.TextFormat;
import org.bukkit.Sound;
import org.bukkit.configuration.file.FileConfiguration;
import tc.oc.pgm.api.player.MatchPlayer;

public class RankManager {

  private static List<Rank> ranks;

  public void createRank() {
    FileConfiguration config = RanksConfig.getCustomConfig();
    ranks = new ArrayList<>();

    config
        .getConfigurationSection("Ranks")
        .getKeys(false)
        .forEach(
            section ->
                ranks.add(
                    new Rank(
                        section,
                        TextFormat.formatAmpersand(
                            RanksConfig.getCustomConfig()
                                .getString("Ranks." + section + ".Display-Name")),
                        RanksConfig.getCustomConfig().getInt("Ranks." + section + ".Points"))));
  }

  public Rank getNextRank(UUID uuid) {
    Rank NOW = getRank(MySQLSetterGetter.getRank(uuid));

    for (int i = ranks.size() - 1; i >= 0; i--) {
      if (NOW.equals(getRanks().get(i))) {
        try {
          return getRanks().get(i + 1);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
          return NOW;
        }
      }
    }
    return null;
  }

  public Integer getRequirePoint(UUID uuid) {
    Rank NEXT = ranks.contains(getNextRank(uuid)) ? getNextRank(uuid) : null;
    int NOW_POINTS = MySQLSetterGetter.getPoints(uuid);
    if (NEXT == null) return null;
    return NEXT.getPoint() - NOW_POINTS;
  }

  public boolean canRankUp(UUID uuid) {
    if (getNextRank(uuid) == null
        || getNextRank(uuid).equals(getRank(MySQLSetterGetter.getRank(uuid)))) return false;
    return getRequirePoint(uuid) <= 0;
  }

  public void RankUP(MatchPlayer player) {
    UUID uuid = player.getId();
    ChatPrefix chatPrefix = new ChatPrefix();
    Rank next = getNextRank(uuid);
    Audience audience = Mixed.get().getAudience().player(uuid);

    if (canRankUp(uuid)) {
      TextComponent.Builder BROADCAST_RANK_UP = Component.text();
      BROADCAST_RANK_UP.append(Component.text(player.getPrefixedName()));
      BROADCAST_RANK_UP.append(Component.text(" has rank up to ", NamedTextColor.RED));
      BROADCAST_RANK_UP.append(next.getColoredName());
      Mixed.get().getAudience().players().sendMessage(BROADCAST_RANK_UP.build());

      player
          .getBukkit()
          .playSound(player.getBukkit().getLocation(), Sound.LEVEL_UP, 10, (float) 0.3);

      audience.sendMessage(
          TextComponent.ofChildren(
              Component.text("〓〓〓〓〓〓", NamedTextColor.YELLOW, TextDecoration.BOLD),
              Component.text(" Rank UP! ", NamedTextColor.RED, TextDecoration.BOLD),
              Component.text("〓〓〓〓〓〓", NamedTextColor.YELLOW, TextDecoration.BOLD)));
      audience.sendMessage(
          TextComponent.ofChildren(
              getRank(MySQLSetterGetter.getRank(uuid)).getColoredName(),
              Component.text(" ⇒ ", NamedTextColor.GRAY, TextDecoration.BOLD),
              next.getColoredName()));
      audience.sendMessage(
          Component.text("〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓〓", NamedTextColor.YELLOW, TextDecoration.BOLD));

      chatPrefix.setPrefixPermission(uuid);
      MySQLSetterGetter.setRank(player.getId().toString(), next.getName());
    }
  }

  public Rank getRank(String Name) {
    return ranks.stream().filter(rank -> Name.equals(rank.getName())).findFirst().orElse(null);
  }

  public List<Rank> getRanks() {
    return Collections.unmodifiableList(ranks);
  }
}
