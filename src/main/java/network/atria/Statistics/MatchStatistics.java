package network.atria.Statistics;

import java.util.Map;
import java.util.UUID;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;
import network.atria.Mixed;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitTask;
import tc.oc.pgm.api.match.Match;

public class MatchStatistics {

  private StoreStatistics storeStatistics;
  private final Map<UUID, BukkitTask> playtimeTask;

  public MatchStatistics() {
    this.playtimeTask = new ConcurrentHashMap<>();
  }

  public void newMatch() {
    this.storeStatistics = new StoreStatistics();
  }

  public void endMatch() {
    clearPlaytime();
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

  public void countPlaytime(UUID uuid, Match match) {
    storeStatistics.getPlaytime().putIfAbsent(uuid, new AtomicInteger());
    BukkitTask task =
        Bukkit.getScheduler()
            .runTaskTimerAsynchronously(
                Mixed.get(),
                new Runnable() {
                  @Override
                  public void run() {
                    if (!match.isRunning()) return;
                    storeStatistics.getPlaytime().get(uuid).getAndIncrement();
                  }
                },
                0L,
                20L);
    BukkitTask old = playtimeTask.put(uuid, task);
    if (old != null) old.cancel();
  }

  public void removePlaytime(UUID uuid) {
    BukkitTask old = playtimeTask.remove(uuid);
    if (old != null) old.cancel();
  }

  private void clearPlaytime() {
    playtimeTask.forEach(((uuid, bukkitTask) -> bukkitTask.cancel()));
    playtimeTask.clear();
  }

  public StoreStatistics getStoreStatistics() {
    return storeStatistics;
  }
}
