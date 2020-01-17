package at.tobias.ttt.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.inventory.ItemStack;

import at.tobias.ttt.countdowns.LobbyCountdown;
import at.tobias.ttt.gamestates.LobbyState;
import at.tobias.ttt.main.Main;
import at.tobias.ttt.util.ConfigLocationUtil;
import at.tobias.ttt.util.ItemBuilder;
import at.tobias.ttt.voting.Voting;

public class PlayerLobbyConnectionListener implements Listener {

	public static final String VOTING_ITEM_NAME = "§6§lVoting-Menu";
	
	private Main plugin;
	private ItemStack voteItem;
	
	public PlayerLobbyConnectionListener(Main plugin) {
		this.plugin = plugin;
		voteItem = new ItemBuilder(Material.NETHER_STAR).setDisplayName(PlayerLobbyConnectionListener.VOTING_ITEM_NAME).build();
	}
	
	@EventHandler
	public void handlePlayerJoin(PlayerJoinEvent event) {
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState)) return;
			Player player = event.getPlayer();
			plugin.getPlayers().add(player);
			event.setJoinMessage(Main.PREFIX + "§a" + player.getDisplayName() + " §7ist dem Spiel beigetreten.");
			
			player.getInventory().clear();
			player.getInventory().setItem(4, voteItem);
		
			ConfigLocationUtil locationUtil = new ConfigLocationUtil(plugin, "Lobby");
			if(locationUtil.loadLocation() != null) {
				player.teleport(locationUtil.loadLocation());
			} else 
				Bukkit.getConsoleSender().sendMessage("§cDie Lobby-Location wurde noch nicht gesetzt!");
			
			
			LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
			LobbyCountdown countdown = lobbyState.getCountdown();
			if(plugin.getPlayers().size() >= LobbyState.MIN_PLAYERS) {
				if(!countdown.isRunning()) {
					countdown.stopIdle();
					countdown.start();
				}
			}
		}
	@EventHandler
	public void handlePlayerQuit(PlayerQuitEvent event) {
		if(!(plugin.getGameStateManager().getCurrentGameState() instanceof LobbyState)) return;
		Player player = event.getPlayer();
		plugin.getPlayers().remove(player);
		event.setQuitMessage(Main.PREFIX + "§c" + player.getDisplayName() + " §7hat das Spiel verlassen.");
	

			LobbyState lobbyState = (LobbyState) plugin.getGameStateManager().getCurrentGameState();
			LobbyCountdown countdown = lobbyState.getCountdown();
			if(plugin.getPlayers().size() < LobbyState.MIN_PLAYERS) {
				if(countdown.isRunning()) {
					countdown.stop();
					countdown.startIdle();
				}
			}
			Voting voting = plugin.getVoting();
			if(voting.getPlayerVotes().containsKey(player.getName())) {
				voting.getVotingMaps()[voting.getPlayerVotes().get(player.getName())].removeVote();
				voting.getPlayerVotes().remove(player.getName());
				voting.initVotingInventory();
			}
	}
}
	

