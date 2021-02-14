package network.atria.UserProfile;

import java.util.Objects;
import java.util.UUID;
import network.atria.Effects.Particles.Effect;
import network.atria.Ranks.Rank;

public class UserProfile {

  private final String name;
  private final UUID uuid;

  private Rank rank;
  private Effect killeffect;
  private Effect projectile;
  private Effect killsound;

  private Integer kills;
  private Integer deaths;
  private Integer flags;
  private Integer cores;
  private Integer wools;
  private Integer monuments;
  private Integer playtime;
  private Integer points;
  private Integer wins;
  private Integer loses;

  private Integer weekly_kills;
  private Integer weekly_deaths;
  private Integer weekly_flags;
  private Integer weekly_cores;
  private Integer weekly_wools;
  private Integer weekly_monuments;
  private Integer weekly_playtime;
  private Integer weekly_wins;
  private Integer weekly_loses;

  public UserProfile(
      String name,
      UUID uuid,
      Rank rank,
      Effect killeffect,
      Effect projectile,
      Effect killsound,
      Integer kills,
      Integer deaths,
      Integer flags,
      Integer cores,
      Integer wools,
      Integer monuments,
      Integer playtime,
      Integer points,
      Integer wins,
      Integer loses,
      Integer weekly_kills,
      Integer weekly_deaths,
      Integer weekly_flags,
      Integer weekly_cores,
      Integer weekly_wools,
      Integer weekly_monuments,
      Integer weekly_playtime,
      Integer weekly_wins,
      Integer weekly_loses) {
    this.name = name;
    this.uuid = uuid;
    this.rank = rank;

    this.killeffect = killeffect;
    this.projectile = projectile;
    this.killsound = killsound;

    this.kills = kills;
    this.deaths = deaths;
    this.flags = flags;
    this.cores = cores;
    this.wools = wools;
    this.monuments = monuments;
    this.playtime = playtime;
    this.points = points;
    this.wins = wins;
    this.loses = loses;

    this.weekly_kills = weekly_kills;
    this.weekly_deaths = weekly_deaths;
    this.weekly_flags = weekly_flags;
    this.weekly_cores = weekly_cores;
    this.weekly_wools = weekly_wools;
    this.weekly_monuments = weekly_monuments;
    this.weekly_playtime = weekly_playtime;
    this.weekly_wins = weekly_wins;
    this.weekly_loses = weekly_loses;
  }

  public void setRank(Rank rank) {
    this.rank = rank;
  }

  public void setKilleffect(Effect killeffect) {
    this.killeffect = killeffect;
  }

  public void setProjectile(Effect projectile) {
    this.projectile = projectile;
  }

  public void setKillsound(Effect killsound) {
    this.killsound = killsound;
  }

  public void setKills(Integer kills) {
    this.kills = kills;
  }

  public void setDeaths(Integer deaths) {
    this.deaths = deaths;
  }

  public void setFlags(Integer flags) {
    this.flags = flags;
  }

  public void setCores(Integer cores) {
    this.cores = cores;
  }

  public void setWools(Integer wools) {
    this.wools = wools;
  }

  public void setMonuments(Integer monuments) {
    this.monuments = monuments;
  }

  public void setPlaytime(Integer playtime) {
    this.playtime = playtime;
  }

  public void setPoints(Integer points) {
    this.points = points;
  }

  public void setWins(Integer wins) {
    this.wins = wins;
  }

  public void setLoses(Integer loses) {
    this.loses = loses;
  }

  public void setWeekly_kills(Integer weekly_kills) {
    this.weekly_kills = weekly_kills;
  }

  public void setWeekly_deaths(Integer weekly_deaths) {
    this.weekly_deaths = weekly_deaths;
  }

  public void setWeekly_flags(Integer weekly_flags) {
    this.weekly_flags = weekly_flags;
  }

  public void setWeekly_cores(Integer weekly_cores) {
    this.weekly_cores = weekly_cores;
  }

  public void setWeekly_wools(Integer weekly_wools) {
    this.weekly_wools = weekly_wools;
  }

  public void setWeekly_monuments(Integer weekly_monuments) {
    this.weekly_monuments = weekly_monuments;
  }

  public void setWeekly_playtime(Integer weekly_playtime) {
    this.weekly_playtime = weekly_playtime;
  }

  public void setWeekly_wins(Integer weekly_wins) {
    this.weekly_wins = weekly_wins;
  }

  public void setWeekly_loses(Integer weekly_loses) {
    this.weekly_loses = weekly_loses;
  }

  public String getName() {
    return name;
  }

  public UUID getUUID() {
    return uuid;
  }

  public Rank getRank() {
    return rank;
  }

  public Effect getKilleffect() {
    return killeffect;
  }

  public Effect getProjectile() {
    return projectile;
  }

  public Effect getKillsound() {
    return killsound;
  }

  public Integer getKills() {
    return kills;
  }

  public Integer getDeaths() {
    return deaths;
  }

  public Integer getFlags() {
    return flags;
  }

  public Integer getCores() {
    return cores;
  }

  public Integer getWools() {
    return wools;
  }

  public Integer getMonuments() {
    return monuments;
  }

  public Integer getPlaytime() {
    return playtime;
  }

  public Integer getPoints() {
    return points;
  }

  public Integer getWins() {
    return wins;
  }

  public Integer getLoses() {
    return loses;
  }

  public Integer getWeekly_kills() {
    return weekly_kills;
  }

  public Integer getWeekly_deaths() {
    return weekly_deaths;
  }

  public Integer getWeekly_flags() {
    return weekly_flags;
  }

  public Integer getWeekly_cores() {
    return weekly_cores;
  }

  public Integer getWeekly_wools() {
    return weekly_wools;
  }

  public Integer getWeekly_monuments() {
    return weekly_monuments;
  }

  public Integer getWeekly_playtime() {
    return weekly_playtime;
  }

  public Integer getWeekly_wins() {
    return weekly_wins;
  }

  public Integer getWeekly_loses() {
    return weekly_loses;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    UserProfile profile = (UserProfile) o;
    return uuid.equals(profile.uuid);
  }

  @Override
  public int hashCode() {
    return Objects.hash(uuid);
  }

  @Override
  public String toString() {
    return "UserProfile{"
        + "name='"
        + name
        + '\''
        + ", uuid="
        + uuid
        + ", rank="
        + rank
        + ", killeffect="
        + killeffect
        + ", projectile="
        + projectile
        + ", killsound="
        + killsound
        + ", kills="
        + kills
        + ", deaths="
        + deaths
        + ", flags="
        + flags
        + ", cores="
        + cores
        + ", wools="
        + wools
        + ", monuments="
        + monuments
        + ", playtime="
        + playtime
        + ", points="
        + points
        + ", wins="
        + wins
        + ", loses="
        + loses
        + ", weekly_kills="
        + weekly_kills
        + ", weekly_deaths="
        + weekly_deaths
        + ", weekly_flags="
        + weekly_flags
        + ", weekly_cores="
        + weekly_cores
        + ", weekly_wools="
        + weekly_wools
        + ", weekly_monuments="
        + weekly_monuments
        + ", weekly_playtime="
        + weekly_playtime
        + ", weekly_wins="
        + weekly_wins
        + ", weekly_loses="
        + weekly_loses
        + '}';
  }
}
