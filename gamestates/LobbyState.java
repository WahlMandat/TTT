package at.tobias.ttt.gamestates;

import org.bukkit.Bukkit;

import at.tobias.ttt.countdowns.LobbyCountdown;

public class LobbyState extends GameState{

	public static final int MIN_PLAYERS = 1,
							MAX_PLAYERS = 12;
	
	private LobbyCountdown countdown;
	
	public LobbyState(GameStateManager gameStateManager) {
		countdown = new LobbyCountdown(gameStateManager);
	}
	
	@Override
	public void start() {
		countdown.startIdle();
	}

	@Override
	public void stop() {
		Bukkit.broadcastMessage("Wir wären im Ingamestate");
	}
	public LobbyCountdown getCountdown() {
		return countdown;
	}
}
