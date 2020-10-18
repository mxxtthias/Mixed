package network.atria.Discord;

import net.dv8tion.jda.api.EmbedBuilder;
import net.dv8tion.jda.api.entities.TextChannel;
import network.atria.Main;
import org.bukkit.command.CommandSender;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.plugin.Plugin;
import tc.oc.pgm.api.player.MatchPlayer;
import tc.oc.pgm.community.command.ModerationCommand;
import tc.oc.pgm.community.events.PlayerPunishmentEvent;

public class Punish implements Listener {

    @EventHandler
    public void onBanned(PlayerPunishmentEvent event) {
        createPunishEmbed(event.getPlayer(), event.getType(), event.getSender(), event.getReason());
    }

    private void createPunishEmbed(MatchPlayer punish, ModerationCommand.PunishmentType type, CommandSender staff, String reason) {
        EmbedBuilder embed = new EmbedBuilder();

        String headURL = "http://cravatar.eu/helmavatar/";

        embed.setAuthor(staff.getName() + " " + type.toString() + " " + punish.getName(), null, headURL + staff.getName());
        embed.addField("Reason", reason, true);
        embed.setFooter(punish.getId().toString());
        embed.setThumbnail(headURL + punish.getName());

        TextChannel channel = Main.getInstance().getGuild().getTextChannelById("761650075759149088");
        channel.sendMessage(embed.build()).queue();

    }

    public Punish(Plugin plugin) {
        plugin.getServer().getPluginManager().registerEvents(this, plugin);
    }
}
