package xps.KillEffects;

import org.bukkit.entity.Player;
import xps.Config.KillEffectsConfig;
import xps.Database.MySQLSetterGetter;

public class getPlayerData {

    public boolean hasRequirePoint(String uuid, int require) {
        return MySQLSetterGetter.getPoints(uuid) >= require;
    }

    public boolean hasDonorRank(Player player) {
        return player.hasPermission("pgm.group.donor");
    }

    public Integer getRequirePoints(String effect) {
        return KillEffectsConfig.getCustomConfig().getInt(effect + ".points");
    }
}
