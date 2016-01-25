package net.moddedminecraft.mmcessentials.listeners;

import net.moddedminecraft.mmcessentials.MMCEssentials;
import net.moddedminecraft.mmcessentials.utils.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerChangedWorldEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.scheduler.BukkitRunnable;
import org.bukkit.scheduler.BukkitTask;

import com.earth2me.essentials.Essentials;
import com.earth2me.essentials.User;

public class PlayerListener implements Listener {

	private static MMCEssentials plugin;
	
	public PlayerListener(MMCEssentials instance) {
		plugin = instance;
	}

	@SuppressWarnings({ "deprecation", "unused", "static-access" })
	@EventHandler
	public void onPlayerJoin(final PlayerJoinEvent event) {

		plugin.playersOnline = Bukkit.getServer().getOnlinePlayers().length;

		BukkitTask task;
		
		if (plugin.voteStarted) {
			task = new BukkitRunnable()
			{
				public void run()
				{
					Player player = event.getPlayer();
					plugin.displayVotes();
					Util.sendMessage(player, "&f[&6Restart-Vote&f] &3There is a vote to restart the server.");
					Util.sendMessage(player, ChatColor.GOLD + "Type " + ChatColor.GREEN + "/reboot yes" + ChatColor.GOLD + " if you agree");
					Util.sendMessage(player, ChatColor.GOLD + "Type " + ChatColor.RED + "/reboot no" + ChatColor.GOLD + " if you do not agree");
					Util.sendMessage(player, ChatColor.GOLD + "If there are more yes votes than no, The server will be restarted! (minimum of 5)");
				}
			}.runTaskLater(plugin, 50L);
		}

	}

	@SuppressWarnings({ "deprecation", "static-access" })
	@EventHandler
	public void onPlayerQuit(PlayerQuitEvent event) {
		plugin.playersOnline = Bukkit.getServer().getOnlinePlayers().length;
	}
	
	@EventHandler(priority = EventPriority.HIGHEST)
	public void onPlayerChangedWorldFlyReset(final PlayerChangedWorldEvent event) {
		Player p = event.getPlayer();
		Essentials ess = (Essentials) Bukkit.getPluginManager().getPlugin("Essentials");
		final User user = ess.getUser(event.getPlayer());
		if (p.getAllowFlight() == true){
			user.getBase().setAllowFlight(true);
		}
	}
}
