package at.tobias.ttt.main;

import java.util.ArrayList;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import at.tobias.ttt.commands.InstantStartCommand;
import at.tobias.ttt.commands.SetupCommand;
import at.tobias.ttt.commands.StartCommand;
import at.tobias.ttt.gamestates.GameState;
import at.tobias.ttt.gamestates.GameStateManager;
import at.tobias.ttt.listeners.PlayerLobbyConnectionListener;
import at.tobias.ttt.listeners.VotingListener;
import at.tobias.ttt.voting.Map;
import at.tobias.ttt.voting.Voting;

public class Main extends JavaPlugin{

	public static final String PREFIX = "�7[�cTTT�7] �r",
							   NO_PERMISSION = PREFIX + "�cDazu hast du keine Rechte!";
	
	private GameStateManager gameStateManager;
	private ArrayList<Player> players;
	private ArrayList<Map> maps;
	private Voting voting;
	
	
	@Override
	public void onEnable() {
		gameStateManager = new GameStateManager(this);
		players = new ArrayList<>();
		
		gameStateManager.setGameState(GameState.LOBBY_STATE);
		
		
		init(Bukkit.getPluginManager());
		System.out.println("[TTT] Das Plugin wurde gestartet.");
	}
	
	private void init(PluginManager pluginManager) {
		initVoting();
		
		getCommand("setup").setExecutor(new SetupCommand(this));
		getCommand("start").setExecutor(new StartCommand(this));
		getCommand("instantstart").setExecutor(new InstantStartCommand(this));
		
		pluginManager.registerEvents(new PlayerLobbyConnectionListener(this), this);
		pluginManager.registerEvents(new VotingListener(this), this);
	}
	private void initVoting() {
		maps = new ArrayList<>();
		for(String current : getConfig().getConfigurationSection("Arenas").getKeys(false)) {
			Map map = new Map(this,current);
			if(map.playable()) 
				maps.add(map);
			else 
				Bukkit.getConsoleSender().sendMessage("�cDie Map �4" + map.getName() + "�c ist noch nicht fertig eingerichtet.");
		}
		if(maps.size() >= Voting.MAP_AMOUNT)
		voting = new Voting(this, maps);
		else {
			Bukkit.getConsoleSender().sendMessage("�cF�r das Voting m�ssen mindestens �4" + Voting.MAP_AMOUNT + "�c Maps eingerichtet sein.");
			voting = null;
			}
		}
	
	@Override
	public void onDisable() {
		System.out.println("[TTT] Das Plugin wurde beendet.");	
	}
	public ArrayList<Player> getPlayers() {
		return players;
	}
	public GameStateManager getGameStateManager() {
		return gameStateManager;
	}
	public Voting getVoting() {
		return voting;
	}
	public ArrayList<Map> getMaps() {
		return maps;
	}
}
