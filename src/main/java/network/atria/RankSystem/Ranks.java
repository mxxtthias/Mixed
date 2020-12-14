package network.atria.RankSystem;

import java.util.*;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Util.RanksConfig;
import network.atria.Util.TextFormat;

public class Ranks {

  private static List<Rank> ranks;

  public static void createRank() {
    ranks = new ArrayList<>();

    RanksConfig.getCustomConfig()
        .getConfigurationSection("Ranks")
        .getKeys(false)
        .forEach(
            rank ->
                ranks.add(
                    new Rank(
                        rank,
                        TextFormat.formatAmpersand(
                            RanksConfig.getCustomConfig()
                                .getString("Ranks." + rank + ".Display-Name")),
                        RanksConfig.getCustomConfig().getInt("Ranks." + rank + ".Point"))));
  }

  public Rank getNextRank(UUID uuid) {
    Rank now = getRank(MySQLSetterGetter.getRank(uuid));

    for (int i = ranks.size() - 1; i >= 0; i--) {
      if (now.equals(ranks.get(i))) {
        try {
          return ranks.get(i + 1);
        } catch (IndexOutOfBoundsException | NullPointerException e) {
          return now;
        }
      }
    }
    return null;
  }

  public Integer getRequirePoint(UUID uuid) {
    Rank next = ranks.contains(getNextRank(uuid)) ? getNextRank(uuid) : null;
    int now = MySQLSetterGetter.getPoints(uuid);
    if (next == null) return null;
    return next.getPoint() - now;
  }

  public boolean RankUP(UUID uuid) {
    return getRequirePoint(uuid) <= 0;
  }

  public Rank getRank(String name) {
    Optional<Rank> rank =
        ranks.stream().filter(get -> get.getName().equalsIgnoreCase(name)).findFirst();
    return rank.orElse(null);
  }
}
