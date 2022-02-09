package chess.rendering.client;

import java.awt.*;

import javax.swing.JPanel;

import chess.ChessGame;
import chess.network.GameStatus;
import chess.rendering.client.game.ChessGUI;
import chess.rendering.menu.client.ClientMenu;
import chess.rendering.menu.client.StartClickedListener;

@SuppressWarnings("serial")
public class ClientGUI extends JPanel
{
	public static enum ClientGUIView
	{
		MENU,
		GAME;
	}

	private ClientGUIView activeView = ClientGUIView.MENU;
	private ClientMenu clientMenu;
	private ChessGUI chessGUI;

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
	}

	public void initGameView(ChessGame game)
	{
		this.chessGUI = new ChessGUI(game);
	}

	public void updateGameStatus(GameStatus oldStatus, GameStatus newStatus)
	{
		if(oldStatus == GameStatus.LOBBY && newStatus == GameStatus.STARTED)
		{
			switchToView(ClientGUIView.GAME);
		}
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		if(activeView == ClientGUIView.MENU && clientMenu != null)
			clientMenu.paint(g);
		else if(activeView == ClientGUIView.GAME && chessGUI != null)
			chessGUI.paintComponent(g);
		else
		{
			g.setColor(Color.red);
			g.fillRect(0, 0, WIDTH, HEIGHT);
		}
	}
}
