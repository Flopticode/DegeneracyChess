package chess;

import chess.figure.FigureColor;
import chess.network.ConnectionFailedException;
import chess.network.GameStatus;
import chess.network.NetworkAddress;
import chess.network.impl.client.ChessNetworkRemoteClient;
import chess.rendering.client.ClientGUI;
import chess.rendering.client.LocalClientGUI;
import main.Main;

public class ChessClient
{
    private ClientGUI gui;
    private ChessNetworkRemoteClient network;
    private GameStatus currentGameStatus = null;
    private ChessGame game;

    public ChessClient(boolean isLocalGame)
    {
    	if(isLocalGame)
    	{
    		gui = new LocalClientGUI(Main.GAME_WINDOW);
    	}
    	else
    	{
    		gui = new ClientGUI(Main.GAME_WINDOW);
        	gui.addStartGameClickedListener(this::tryStartGame);
    	}
    }

    public boolean tryConnect(NetworkAddress networkAddress, FigureColor prefColor)
    {
        return tryConnect(networkAddress, prefColor, true);
    }
    public boolean tryConnect(NetworkAddress networkAddress, FigureColor prefColor, boolean isConnectingToManagement)
    {
        try
        {
            network = new ChessNetworkRemoteClient(networkAddress, prefColor, isConnectingToManagement);
            network.addOnUpdateGameStatus(this::updateGameStatus);
            return true;
        }
        catch (ConnectionFailedException e)
        {
            return false;
        }
    }

    protected void updateGameStatus(GameStatus newStatus)
    {
        System.out.println("New game status " + newStatus.toString() + ".");
        if(newStatus != currentGameStatus)
        {
            if(newStatus == GameStatus.STARTED)
                initGame();

            gui.updateGameStatus(currentGameStatus, newStatus);

            currentGameStatus = newStatus;
        }
    }

    private void initGame()
    {
        this.game = new ChessGame();
        this.gui.initGameView(game);
    }

    public void tryStartGame()
    {
        this.network.sendStartGamePacket();
    }

    public ClientGUI getGUI()
    {
        return gui;
    }
}
