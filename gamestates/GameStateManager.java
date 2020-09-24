package at.tobias.ttt.gamestates;

import at.tobias.ttt.main.Main;

public class GameStateManager {

	private final Main plugin;
	private final GameState[] gameStates;
	private GameState currentGameState;
	
	public GameStateManager(Main plugin) {
		this.plugin = plugin;
		gameStates = new GameState[3];
		
		gameStates[GameState.LOBBY_STATE] = new LobbyState(this);
		gameStates[GameState.INGAME_STATE] = new IngameState();
		gameStates[GameState.ENDING_STATE] = new EndingState();
	}
	
	public void setGameState(int gameStateID) {
		if(currentGameState != null) 
			currentGameState.stop();
		currentGameState = gameStates[gameStateID];
		currentGameState.start();
		
	}
	public void stopCurrenGameState() {
		if(currentGameState != null) {
			currentGameState.stop();
			currentGameState = null;
		}
	}
	public GameState getCurrentGameState() {
		return currentGameState;
	}
	public Main getPlugin() {
		return plugin;
	}
	
}
