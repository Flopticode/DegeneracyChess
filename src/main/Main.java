package main;
import java.io.IOException;

import chess.ChessClient;
import chess.figure.FigureColor;
import chess.network.NetworkAddress;
import chess.network.impl.server.lobby.LobbyHandler;
import chess.network.impl.server.lobby.LobbyManagementServer;
import chess.network.impl.server.lobby.RemoteLobbyHandler;
import chess.rendering.GameWindow;
import chess.rendering.menu.MainMenu;

public class Main
{
	public static final GameWindow GAME_WINDOW = new GameWindow();
	
	public static void main(String[] args)
	{
		MainMenu menu = new MainMenu(GAME_WINDOW) {

			@Override
			public void startServer()
			{
				try
				{
					LobbyHandler handler = new RemoteLobbyHandler("10.101.4.78", LobbyHandler.PORT);
				} catch (IOException e)
				{
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
			
			@Override
			public void startManagementServer()
			{
				LobbyManagementServer lobbyServer = new LobbyManagementServer(new NetworkAddress[] {new NetworkAddress("10.101.4.78", 420)});
				
				GAME_WINDOW.registerIfNotPresent("ManagementServerGUI", lobbyServer::getGUI);
				GAME_WINDOW.show("ManagementServerGUI");
				GAME_WINDOW.repaint();
			}

			@Override
			public void startClient(NetworkAddress serverAddr, FigureColor preferredColor)
			{				
				ChessClient client = new ChessClient(false);
				
				GAME_WINDOW.registerIfNotPresent("ClientGUI", client::getGUI);

				boolean succ = client.tryConnect(serverAddr, preferredColor, false);
				if(!succ)
				{
					System.err.println("ERROR: Couldn't establish connection to server!");// TODO show an error whatever i don't care
					return;
				}
				
				GAME_WINDOW.show("ClientGUI");
				GAME_WINDOW.repaint();
			}
			
			@Override
			public void startLocal()
			{
				System.out.println("Local");

				try
				{
					RemoteLobbyHandler handler = new RemoteLobbyHandler("localhost", 420);
					ChessClient client = new ChessClient(true), client2 = new ChessClient(true);
					//FRAME.setContentPane(client.getGUI());
					//FRAME.repaint(0);
					client.tryConnect(new NetworkAddress("localhost", 420), FigureColor.WHITE, false);
					try
					{
						Thread.sleep(1000);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}

					client2.tryConnect(new NetworkAddress("localhost", 420), FigureColor.BLACK, false);

					try
					{
						Thread.sleep(1000);
					}
					catch(InterruptedException e)
					{
						e.printStackTrace();
					}
					
					client.tryStartGame();
				}
				catch(IOException ioe)
				{
					System.err.println("Couldn't create LobbyHandler");
					ioe.printStackTrace();
				}
			}
		};
		
		GAME_WINDOW.register("MenuGUI", menu);
		GAME_WINDOW.repaint();
	}
}
