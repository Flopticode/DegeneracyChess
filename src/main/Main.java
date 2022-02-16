package main;
import java.io.IOException;

import javax.swing.JFrame;

import chess.ChessClient;
import chess.figure.FigureColor;
import chess.network.NetworkAddress;
import chess.network.impl.server.lobby.LobbyManagementServer;
import chess.network.impl.server.lobby.RemoteLobbyHandler;
import chess.rendering.client.game.BoardRenderer;
import chess.rendering.menu.MainMenu;

public class Main
{
	public static final int FRAME_X = 400;
	public static final int FRAME_Y = 400;
	public static final int FRAME_TITLE_BAR_HEIGHT = 18-BoardRenderer.CELL_SIZE;
	
	public static final JFrame FRAME;
	
	static
	{ 
		FRAME = new JFrame("Degeneracy - The Game");
	}
	
	public static void main(String[] args)
	{
		FRAME.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		FRAME.setLayout(null);
		
		@SuppressWarnings("serial")
		MainMenu menu = new MainMenu(FRAME_X, FRAME_Y) {

			@Override
			public void startServer()
			{
				LobbyManagementServer lobbyServer = new LobbyManagementServer(new NetworkAddress[] {new NetworkAddress("localhost", 420)});
				FRAME.setContentPane(lobbyServer.getGUI());
				FRAME.repaint();
			}

			@Override
			public void startClient(NetworkAddress serverAddr, FigureColor preferredColor)
			{
				ChessClient client = new ChessClient(false);

				boolean succ = client.tryConnect(serverAddr, preferredColor);
				if(!succ)
				{
					System.err.println("ERROR: Couldn't establish connection to server!");// TODO show an error whatever i dont care
					return;
				}
				FRAME.setContentPane(client.getGUI());
				FRAME.repaint();
			}
			
			@Override
			public void startLocal()
			{
				System.out.println("Local");

				try
				{
					RemoteLobbyHandler handler = new RemoteLobbyHandler("localhost", 420);
					ChessClient client = new ChessClient(true), client2 = new ChessClient(true);
					FRAME.setContentPane(client.getGUI());
					FRAME.repaint(0);
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
		menu.setBounds(0, 0, 500, 500);
		FRAME.setContentPane(menu);
		FRAME.setBounds(0, 0, 500, 500);
		FRAME.setVisible(true);
	}
}
