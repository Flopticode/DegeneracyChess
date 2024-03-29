package chess.rendering.client;

import java.awt.Color;
import java.awt.Graphics;

import javax.swing.JPanel;

import chess.ChessGame;
import chess.network.GameStatus;
import chess.rendering.client.game.ChessGUI;
import chess.rendering.menu.client.ClientMenu;
import chess.rendering.menu.client.StartClickedListener;
import main.Main;

@SuppressWarnings("serial")
public class ClientGUI extends JPanel
{
	public static enum ClientGUIView
	{
		MENU,
		GAME;
	}

	protected ClientGUIView activeView = ClientGUIView.MENU;
	protected ClientMenu clientMenu;
	protected ChessGUI chessGUI;

	public ClientGUI()
	{
		this.setBounds(0, 0, 500, 500);

		this.activeView = ClientGUIView.MENU;
		this.clientMenu = new ClientMenu(0, 0, 500, 500);
		this.add(clientMenu);
	}

	public void addStartGameClickedListener(StartClickedListener l)
	{
		clientMenu.addStartClickedListener(l);
	}
	public boolean removeStartGameClickedListener(StartClickedListener l)
	{
		return clientMenu.removeStartClickedListener(l);
	}

	private void switchToView(ClientGUIView view)
	{
		this.activeView = view;
		Main.FRAME.repaint();
	}

	public void initGameView(ChessGame game)
	{
		this.chessGUI = new ChessGUI(game);
	}

	public void updateGameStatus(GameStatus oldStatus, GameStatus newStatus)
	{
		if(newStatus == GameStatus.STARTED)
		{
			switchToView(ClientGUIView.GAME);
		}
	}
	
	@Override
	public void paint(Graphics g)
	{
		if(activeView == ClientGUIView.MENU && clientMenu != null)
			clientMenu.paint(g);
		else if(activeView == ClientGUIView.GAME && chessGUI != null)
			chessGUI.paint(g);
		else
		{
			g.setColor(Color.red);
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}
	}
}
