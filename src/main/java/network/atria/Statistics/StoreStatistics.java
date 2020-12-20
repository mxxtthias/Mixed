package network.atria.Statistics;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class StoreStatistics {

  private final Map<UUID, AtomicInteger> kills;
  private final Map<UUID, AtomicInteger> deaths;
  private final Map<UUID, AtomicInteger> wools;
  private final Map<UUID, AtomicInteger> monuments;
  private final Map<UUID, AtomicInteger> cores;
  private final Map<UUID, AtomicInteger> flags;
  private final Map<UUID, AtomicInteger> points;

  public StoreStatistics() {
    this.kills = new HashMap<>();
    this.deaths = new HashMap<>();
    this.wools = new HashMap<>();
    this.monuments = new HashMap<>();
    this.cores = new HashMap<>();
    this.flags = new HashMap<>();
    this.points = new HashMap<>();
  }

  public Map<UUID, AtomicInteger> getKills() {
    return kills;
  }

  public Map<UUID, AtomicInteger> getDeaths() {
    return deaths;
  }

  public Map<UUID, AtomicInteger> getWools() {
    return wools;
  }

  public Map<UUID, AtomicInteger> getMonuments() {
    return monuments;
  }

  public Map<UUID, AtomicInteger> getCores() {
    return cores;
  }

  public Map<UUID, AtomicInteger> getFlags() {
    return flags;
  }

  public Map<UUID, AtomicInteger> getPoints() {
    return points;
  }
}
