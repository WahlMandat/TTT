package at.tobias.ttt.countdowns;

import java.util.ArrayList;
import java.util.Collections;

import org.apache.logging.log4j.core.config.plugins.Plugin;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.craftbukkit.v1_8_R3.entity.CraftPlayer;
import org.bukkit.entity.Player;

import at.tobias.ttt.gamestates.GameState;
import at.tobias.ttt.gamestates.GameStateManager;
import at.tobias.ttt.gamestates.LobbyState;
import at.tobias.ttt.main.Main;
import at.tobias.ttt.voting.Map;
import at.tobias.ttt.voting.Voting;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle;
import net.minecraft.server.v1_8_R3.IChatBaseComponent.ChatSerializer;
import net.minecraft.server.v1_8_R3.PacketPlayOutTitle.EnumTitleAction;


public class LobbyCountdown extends Countdown{

	private static final int COUNTDOWN_TIME = 60,
							 IDLE_TIME = 15;
	
	private GameStateManager gameStateManager;
	
	private int seconds;
	private boolean isRunning;
	private int idleID;
	private boolean isIdling;
	
	public LobbyCountdown(GameStateManager gameStateManager) {
		this.gameStateManager = gameStateManager;
		seconds = COUNTDOWN_TIME;
	}
	
	@Override
	public void start() {
		isRunning = true;
		taskID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				switch (seconds) {
				case 60: case 45: case 30: case 20:  case 5: case 4: case 3: case 2:
					Bukkit.broadcastMessage(Main.PREFIX + "§7Das Spiel startet in §a" + seconds + " Sekunden§7.");
					
					for(Player player : gameStateManager.getPlugin().getPlayers()) {
						player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1F, 1F);
					}
					break;
				case 10:
					Bukkit.broadcastMessage(Main.PREFIX + "§7Das Spiel startet in §a" + seconds + " Sekunden§7.");
					
					for(Player player : gameStateManager.getPlugin().getPlayers()) {
						player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1F, 1F);
						player.sendTitle("§a10", " ");
					}
					break;
				
				case 1:
					
					Voting vote = gameStateManager.getPlugin().getVoting();
					Bukkit.broadcastMessage(Main.PREFIX + "§7Das Spiel startet in §a1 Sekunde§7.");
					for(Player player : gameStateManager.getPlugin().getPlayers()) {
						player.playSound(player.getLocation(), Sound.ORB_PICKUP, 1F, 1F);	
						player.sendTitle("§fTTT" ,"§6" + vote.getWinnerMap().getName());
					}
					break;
				case 0:
					gameStateManager.setGameState(GameState.INGAME_STATE);
					
					if(seconds == 0) {
						Voting voting = gameStateManager.getPlugin().getVoting();
						Map winningMap;
						if(voting != null) 
							winningMap = voting.getWinnerMap();
						else {
							ArrayList<Map> maps = gameStateManager.getPlugin().getMaps();
							Collections.shuffle(maps);
							winningMap = maps.get(0);
						}
						Bukkit.broadcastMessage(Main.PREFIX +"§7Map: §6"+ winningMap.getName());
					}
					for(Player player : gameStateManager.getPlugin().getPlayers()) {
						player.playSound(player.getLocation(), Sound.ENDERDRAGON_GROWL, 1F, 1F);
					}
					
					break;
				default:
					break;
				}
				seconds--;
			}
		}, 0, 20);
	}

	@Override
	public void stop() {
		if(isRunning) {
			Bukkit.getScheduler().cancelTask(taskID);
			isRunning = false;
			seconds = COUNTDOWN_TIME;
		}
	}
	
	public void startIdle() {
		isIdling = true;
		idleID = Bukkit.getScheduler().scheduleSyncRepeatingTask(gameStateManager.getPlugin(), new Runnable() {
			
			@Override
			public void run() {
				Bukkit.broadcastMessage(Main.PREFIX + "§7Bis zum Spielstart fehlen noch §6" +
										(LobbyState.MIN_PLAYERS - gameStateManager.getPlugin().getPlayers().size() + " Spieler§7."));

			}
		}, 0, 20 * IDLE_TIME);
	}
	public void stopIdle() {
		if(isIdling) {
			Bukkit.getScheduler().cancelTask(idleID);
			isIdling = false;
		}
	}
	public int getSeconds() {
		return seconds;
	}
	public void setSeconds(int seconds) {
		this.seconds = seconds;
	}
	
	public boolean isRunning() {
		return isRunning;
	}
}
