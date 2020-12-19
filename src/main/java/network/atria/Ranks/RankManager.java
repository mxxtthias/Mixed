package network.atria.Ranks;

import java.util.*;
import network.atria.Database.MySQLSetterGetter;
import network.atria.Util.RanksConfig;
import network.atria.Util.TextFormat;
import org.bukkit.configuration.file.FileConfiguration;

public class RankManager {

  private static List<Rank> ranks;

  public void createRank() {
    FileConfiguration config = RanksConfig.getCustomConfig();
    ranks = new ArrayList<>();

    config
        .getConfigurationSection("Ranks")
        .getKeys(false)
        .forEach(
            section -> {
              ranks.add(
                  new Rank(
                      section,
                      TextFormat.formatAmpersand(
                          RanksConfig.getCustomConfig()
                              .getString("Ranks." + section + ".Display-Name")),
                      RanksConfig.getCustomConfig().getInt("Ranks." + section + ".Points")));
              System.out.println("Added");
            });
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

  public boolean RankUP(UUID uuid) {
    if (getNextRank(uuid) == null
        || getNextRank(uuid).equals(getRank(MySQLSetterGetter.getRank(uuid)))) return false;
    return getRequirePoint(uuid) <= 0;
  }

  public Rank getRank(String Name) {
    return ranks.stream().filter(rank -> Name.equals(rank.getName())).findFirst().orElse(null);
  }

  public List<Rank> getRanks() {
    return Collections.unmodifiableList(ranks);
  }
}
