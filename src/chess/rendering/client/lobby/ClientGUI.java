package chess.rendering.client.lobby;

import java.awt.Graphics;

import javax.swing.JPanel;

import chess.ChessGame;
import chess.network.impl.client.ChessClient;
import chess.rendering.menu.api.Menu;
import chess.rendering.menu.client.ClientMenu;

@SuppressWarnings("serial")
public class ClientGUI extends JPanel
{
	private ChessGame game;
	private Menu clientMenu;
	private ChessClient client;
	
	
	public ClientGUI(ChessClient client)
	{
		this.setBounds(0, 0, 500, 500);
		
		this.clientMenu = new ClientMenu(client, 0, 0, 500, 500);
		this.add(clientMenu);
	}
	
	public void initGame()
	{
		this.game = new ChessGame();
		this.add("gui", game.getGUI());
		this.remove(clientMenu);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		if(game != null)
			game.getGUI().paintComponent(g);
		else
			clientMenu.paint(g);
	}
}
