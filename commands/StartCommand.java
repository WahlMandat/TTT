package at.tobias.ttt.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import at.tobias.ttt.gamestates.LobbyState;
import at.tobias.ttt.main.Main;

public class StartCommand implements CommandExecutor{

	private static final int START_SECONDS = 10;
	
	private Main plugin;
	
	public StartCommand(Main plugin) {
		this.plugin = plugin;
	}
	
	@Override
	public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
		if(sender instanceof Player) {
			Player player  = (Player) sender;
			if(player.hasPermission("ttt.start")) {
				if(args.length == 0) {
					if(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState) {
						LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
						if(lobbyState.getCountdown().isRunning() && lobbyState.getCountdown().getSeconds() > START_SECONDS) {
							lobbyState.getCountdown().setSeconds(START_SECONDS);
							player.sendMessage(Main.PREFIX + "§aDer Spielstart wurde beschleunigt.");
						} else 
							player.sendMessage(Main.PREFIX + "§cDas Spiel ist bereits gestartet.");
					} else 
						player.sendMessage(Main.PREFIX + "§cDas Spiel ist bereits gestartet.");
				} else
					player.sendMessage(Main.PREFIX + "§cBitte benutze §6/start§c!");
			} else 
				player.sendMessage(Main.NO_PERMISSION);
		}
		return false;
	}

}
