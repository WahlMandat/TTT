package at.tobias.ttt.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.tobias.ttt.gamestates.LobbyState;
import at.tobias.ttt.main.Main;
import at.tobias.ttt.util.ConfigLocationUtil;
import at.tobias.ttt.voting.Map;

public class SetupCommand implements CommandExecutor{

	private Main plugin;
	
	public SetupCommand(Main plugin) {
		this.plugin = plugin;
	}

	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player = (Player) sender;
			if(player.hasPermission("ttt.setup")) {
				if(args.length == 0) {
					player.sendMessage(Main.PREFIX + "§cBitte nutze §6/setup <LOBBY>§c!");
				} else {
					if(args[0].equalsIgnoreCase("lobby")) {
						if(args.length == 1) {
							new ConfigLocationUtil(plugin, player.getLocation(), "Lobby").saveLocation();
							player.sendMessage(Main.PREFIX + "§aDie Lobby wurde neu gesetzt. ");
						} else 
							player.sendMessage(Main.PREFIX + "§cBitte benutze §6/setup lobby§c!");
					} else if(args[0].equalsIgnoreCase("create")) {
						if(args.length == 3) {
							Map map = new Map(plugin, args[1]);
							if(!map.exists()) {
								map.create(args[2]);
								player.sendMessage(Main.PREFIX + "§aDie Map §6" + map.getName() + " §awurde erstellt!");
							} else
								player.sendMessage(Main.PREFIX + "§cDiese Map existiert bereits.");
						} else 
							player.sendMessage(Main.PREFIX + "§cBitte benutze §6/setup create <NAME> <BUILDER>§c!");
				    } else if(args[0].equalsIgnoreCase("set")) {
				    	if(args.length == 3) {
				    		Map map = new Map(plugin, args[1]);
				    		if(map.exists()) {
				    			try {
				    			int spawnNumber = Integer.parseInt(args[2]);
				    			if(spawnNumber > 0 && spawnNumber <= LobbyState.MAX_PLAYERS) {
				    				map.setSpawnLocation(spawnNumber, player.getLocation());
				    				player.sendMessage(Main.PREFIX + "§aDu hast die Spawn-Location §6" + spawnNumber + "§a für die Map §6" + map.getName() + "§a gesetzt.");
				    			} else
				    				player.sendMessage(Main.PREFIX + "§cBitte gib eine Zahl §6zwischen 1 und " + LobbyState.MAX_PLAYERS + " §can.");
				    			}catch (NumberFormatException e) {
									if(args[2].equalsIgnoreCase("spectator")) {
										map.setSpectatorLocation(player.getLocation());
										player.sendMessage(Main.PREFIX + "§aDu hast die §6Spectator-Location §afür die Map §6" + map.getName() + "§a gesetzt.");
										} else {
											player.sendMessage(Main.PREFIX +"§cBitte benutze /setup set <NAME> <1-" + LobbyState.MAX_PLAYERS + " // SPECTATOR>");
										}
									}
				    		} else 
				    			player.sendMessage(Main.PREFIX + "§cDiese Map existiert noch nicht!");
				    	} else
				    		player.sendMessage(Main.PREFIX +"§cBitte benutze /setup set <NAME> <1-" + LobbyState.MAX_PLAYERS + " // SPECTATOR>");
					} else 
						player.sendMessage(Main.PREFIX + "§cBitte benutze §6/setup <LOBBY/SET>§c!");
				}
			} else
				player.sendMessage(Main.NO_PERMISSION);
		}
		return false;
	}
	
}
