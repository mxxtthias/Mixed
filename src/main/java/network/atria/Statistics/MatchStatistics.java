package network.atria.Statistics;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicInteger;

public class MatchStatistics {

  private StoreStatistics storeStatistics;

  public void newMatch() {
    this.storeStatistics = new StoreStatistics();
  }

  public void addKill(UUID uuid) {
    AtomicInteger kills =
        storeStatistics.getKills().get(uuid) == null
            ? new AtomicInteger()
            : storeStatistics.getKills().get(uuid);
    kills.incrementAndGet();
    storeStatistics.getKills().put(uuid, kills);
  }

  public void addDeath(UUID uuid) {
    AtomicInteger deaths =
        storeStatistics.getDeaths().get(uuid) == null
            ? new AtomicInteger()
            : storeStatistics.getDeaths().get(uuid);
    deaths.incrementAndGet();
    storeStatistics.getDeaths().put(uuid, deaths);
  }

  public void addWool(UUID uuid) {
    AtomicInteger wools =
        storeStatistics.getWools().get(uuid) == null
            ? new AtomicInteger()
            : storeStatistics.getWools().get(uuid);
    wools.incrementAndGet();
    storeStatistics.getWools().put(uuid, wools);
  }

  public void addMonument(UUID uuid) {
    AtomicInteger monuments =
        storeStatistics.getMonuments().get(uuid) == null
            ? new AtomicInteger()
            : storeStatistics.getMonuments().get(uuid);
    monuments.incrementAndGet();
    storeStatistics.getMonuments().put(uuid, monuments);
  }

  public void addCore(UUID uuid) {
    AtomicInteger cores =
        storeStatistics.getCores().get(uuid) == null
            ? new AtomicInteger()
            : storeStatistics.getCores().get(uuid);
    cores.incrementAndGet();
    storeStatistics.getCores().put(uuid, cores);
  }

  public void addFlag(UUID uuid) {
    AtomicInteger flags =
        storeStatistics.getFlags().get(uuid) == null
            ? new AtomicInteger()
            : storeStatistics.getFlags().get(uuid);
    flags.incrementAndGet();
    storeStatistics.getFlags().put(uuid, flags);
  }

  public void addPoint(UUID uuid, int point) {
    AtomicInteger points =
        storeStatistics.getPoints().get(uuid) == null
            ? new AtomicInteger()
            : storeStatistics.getPoints().get(uuid);
    points.addAndGet(point);
    storeStatistics.getPoints().put(uuid, points);
  }

  public StoreStatistics getStoreStatistics() {
    return storeStatistics;
  }
}
