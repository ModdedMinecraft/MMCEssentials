package net.moddedminecraft.mmcessentials.commands;

import java.util.ArrayList;

import net.moddedminecraft.mmcessentials.MMCEssentials;
import net.moddedminecraft.mmcessentials.utils.Helplist;
import net.moddedminecraft.mmcessentials.utils.Util;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.block.Block;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class MMCECommand implements CommandExecutor {
	
	private final MMCEssentials plugin;
	
	public MMCECommand(MMCEssentials instance) {
		plugin = instance;
	}

	@SuppressWarnings("deprecation")
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) { 
		
		Player player = null;
		if (sender instanceof Player)  {
			player = (Player) sender;
		}

		if (cmd.getName().equalsIgnoreCase("mmce")) {
			if (args.length == 1) {
				if(args[0].equalsIgnoreCase("help")) {
					showHelp(sender);
					return true;
				}
				else if(args[0].equalsIgnoreCase("reload")) {
					if (sender.hasPermission("mmcessentials.reload")) {
						plugin.ReloadCfg();
						Util.sendMessage(sender, "&2Reloaded the config.yml of " + plugin.getDescription().getName() + " v" + plugin.getDescription().getVersion());
						return true;
					} else {
						Util.sendMessage(sender, "&4You do not have permission to use this!");
						return true;
					}
				}
			}
		}
		else if (cmd.getName().equalsIgnoreCase("blockinfo")) {
			if (sender.hasPermission("mmcessentials.blockinfo")) {
				ItemStack inHand = player.getItemInHand();
				player.sendMessage("In Hand: " + String.format("%s(%d:%d)", inHand.getType().name(), inHand.getTypeId(), inHand.getData().getData()));

				Block inWorld = plugin.getTargetNonAirBlock(player, 300);
				player.sendMessage("In World: " + String.format("%s(%d:%d)", inWorld.getType().name(), inWorld.getTypeId(), inWorld.getData()));

				return true;
			} else {
				Util.sendMessage(sender, "&4You do not have permission to use this!");
			}
		}
		else if (cmd.getName().equalsIgnoreCase("whois")) {
			if (sender.hasPermission("mmcessentials.whois")) {
				if (args.length == 1) {
					{
						Player[] plist = plugin.getServer().getOnlinePlayers();
						for (int i = plist.length - 1; i > -1; i--)
						{
							String pname = ChatColor.stripColor(plist[i].getDisplayName().toLowerCase());		
							if (pname.contains(args[0].toLowerCase()))
							{
								Util.sendMessage(sender, "&c" + pname +  "'s real name is " + plist[i].getName());
								return true;
							}
						}
						Util.sendMessage(sender, "&4No players found, Please type the players nickname.");
						return true;
					}
				} else {
					Util.sendMessage(sender, "&cUsage: &f/whois [Nickname]");
					return true;
				}
			}
			else {
				Util.sendMessage(sender, "&4You do not have permission to use this!");
			}
		}
		else if (cmd.getName().equalsIgnoreCase("gm"))
		{
			if (sender instanceof Player) {
				if (player.hasPermission("mmcessentials.command.gamemode")) {
					if (player.getGameMode() == GameMode.CREATIVE)
					{
						player.setGameMode(GameMode.SURVIVAL);
						Util.sendMessage(sender, "&6Changed your gamemode to &4Survival&6!");
						return true;
					}
					if (player.getGameMode() == GameMode.SURVIVAL)
					{
						player.setGameMode(GameMode.CREATIVE);
						Util.sendMessage(sender, "&6Changed your gamemode to &4Creative&6!");
						return true;
					}
					if (player.getGameMode() == GameMode.ADVENTURE)
					{
						player.setGameMode(GameMode.CREATIVE);
						Util.sendMessage(sender, "&6Changed your gamemode to &4Creative&6!");
						return true;
					}
				} else {
					Util.sendMessage(sender, "&4You do not have permission to use this!");
				}
			} else {
				Util.sendMessage(sender, "The console is unable to use a gamemode!");
				return true;
			}
		}
		else if (cmd.getName().equalsIgnoreCase("setprefix")) {
			if (args.length >= 1) {
				if (args.length == 1) {
					if (sender instanceof Player) {
						String contents = args[0].replaceAll("(&([a-f0-9]))", "").toLowerCase();
						int length = args[0].replaceAll("(&([a-f0-9]))", "").length();
						if (player.hasPermission("mmcessentials.prefix.set")) {
							if (length <= 10  || player.hasPermission("mmcessentials.prefix.bypass")) {
								if (!player.hasPermission("mmcessentials.prefix.staff") && (contents.contains("staff") || contents.contains("admin") || contents.contains("mod") || contents.contains("owner") || contents.contains("tmod"))) {
									player.sendMessage(ChatColor.RED + "You cannot have a staff prefix.");
									return true;
								} else {
									String setprefix = "manuaddv " + player.getName() + " prefix " + "'" + args[0] + "'";
									Bukkit.dispatchCommand(Bukkit.getConsoleSender(), setprefix);
									Util.sendMessage(sender, "&f[&6MMCPrefix&f] &3Prefix Set to: &f" + args[0]);
									return true;
								}

							} else if (length > 10 && !player.hasPermission("mmcessentials.prefix.bypass")) {

								Util.sendMessage(sender, "&4You cannot have a prefix longer than 10 characters");
								return true;
							} 
						} else {
							Util.sendMessage(sender, "&4You do not have access to this command!");
							return true;
						}
					} else {
						Util.sendMessage(sender, "The console is unable to use a prefix!");
						return true;
					}
				} else if (args.length == 2) {
					if (sender instanceof Player) {
						if (player.hasPermission("mmcessentials.prefix.set.others")) {
							Player player2 = Bukkit.getPlayerExact(args[1]);
							if (player2 == null) {
								Util.sendMessage(sender, "&4The player specified isn't online!");
								return true;
							} else if (player2.hasPermission("mmcessentials.prefix.protected")) {
								Util.sendMessage(sender, "&4The prefix of the specified player cannot be changed.");
								return true;
							} else {
								String setprefix = "manuaddv " + player2.getName() + " prefix " + "'" + args[0] + "'";
								Bukkit.dispatchCommand(Bukkit.getConsoleSender(), setprefix);
								Util.sendMessage(sender, "&f[&6MMCPrefix&f] &3Prefix Set for &6" + player2.getName() + "&3!");
								Util.sendMessage(sender, "&f[&6MMCPrefix&f] &3Prefix Set to: &f" + args[0]);
								return true;
							}
						} else {
							Util.sendMessage(sender, "&4You do not have access to this command!");
							return true;
						} 
					} else {
						Player player2 = Bukkit.getPlayerExact(args[1]);
						if (player2 == null) {
							Util.sendMessage(sender, "&4The player specified isn't online!");
							return true;
						} else {
							String setprefix = "manuaddv " + player2.getName() + " prefix " + "'" + args[0] + "'";
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), setprefix);
							Util.sendMessage(sender, "[MMCPrefix] Prefix Set for " + player2.getName() + "!");
							Util.sendMessage(sender, "[MMCPrefix] Prefix Set to: " + args[0]);
							return true;
						}
					} 
				}
			} else {
				Util.sendMessage(sender, "&4Incorrect Usage! /setprefix [prefix]");
				return true;
			}
		}
		else if (cmd.getName().equalsIgnoreCase("delprefix")) {
			if (player.hasPermission("mmcessentials.prefix.del")) {
				if (args.length == 0) {
					if (sender instanceof Player) {
						String setprefix = "manudelv " + player.getName() + " prefix";
						Bukkit.dispatchCommand(Bukkit.getConsoleSender(), setprefix);
						Util.sendMessage(sender, "&f[&6MMCPrefix&f] &3Prefix Deleted!");
						return true;
					} else {
						Util.sendMessage(sender, "&4The console does not have a prefix to delete!");
						return true;
					}
				}
				else if (args.length == 1) {
					if (player.hasPermission("mmcessentials.prefix.del.others"))
					{
						Player player2 = Bukkit.getPlayerExact(args[0]);
						if (player2 == null) {
							Util.sendMessage(sender, "&4The player specified isn't online!");
							return true;
						}
						else {
							String setprefix = "manudelv " + player2.getName() + " prefix";
							Bukkit.dispatchCommand(Bukkit.getConsoleSender(), setprefix);
							Util.sendMessage(sender, "&f[&6MMCPrefix&f] &3Prefix Deleted for &6" + player2.getName() + "&3!");
							return true;
						}
					}
					else {
						Util.sendMessage(sender, "&4You do not have access to this command!");
						return true;
					}
				} else {
					Util.sendMessage(sender, "&4Incorrect Usage! /delprefix [username]");
					return true;
				}
			} else {
				Util.sendMessage(sender, "&4You do not have access to this command!");
				return true;
			}
		}
		Util.sendMessage(sender, "&cInvalid command usage! Type /mmce help");
		return true;
	}


	void showHelp(CommandSender sender) {
		Util.sendMessage(sender, "&f--- &3MMCEssentials &bHelp &f---");

		ArrayList<Helplist> helpList = new ArrayList<Helplist>();

		helpList.add(new Helplist("&3[] = required  () = optional"));
		helpList.add(new Helplist("&3/mmce &bhelp - &7shows this help"));
		if (sender.hasPermission("mmcessentials.reload")) {
			helpList.add(new Helplist("&3/mmce &breload - &7reload the plugin config.yml"));
		}
		if (sender.hasPermission("mmcessentials.blockinfo")) {
			helpList.add(new Helplist("&3/blockinfo - &7In-hand and In-world block info."));
		}
		if (sender.hasPermission("mmcessentials.whois")) {
			helpList.add(new Helplist("&3/whois - &7Get a players real username."));
		}
		if (sender.hasPermission("mmcessentials.prefix.set")) {
			helpList.add(new Helplist("&3/setprefix [prefix] - &7Set your prefix."));
		}
		if (sender.hasPermission("mmcessentials.prefix.del")) {
			helpList.add(new Helplist("&3/delprefix - &7Reset your prefix."));
		}
		if (sender.hasPermission("mmcessentials.prefix.set.others")) {
			helpList.add(new Helplist("&3/setprefix [prefix] [name]- &7set another players prefix."));
		}
		if (sender.hasPermission("mmcessentials.prefix.del.others")) {
			helpList.add(new Helplist("&3/delprefix [name] - &7delete another players prefix."));
		}

		for(int i = 0; i < helpList.size(); i++) {
			Util.sendMessage(sender, helpList.get(i).command);

		}
	}

}
