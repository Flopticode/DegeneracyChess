package chess.rendering.client;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import chess.rendering.GameWindow;

public class LocalClientGUI extends ClientGUI
{
	public static final Font FONT = new Font("Arial", 0, 40);

	public LocalClientGUI(GameWindow gameWindow)
	{
		super(gameWindow);
	}
	
	@Override
	public void paint(Graphics g)
	{
		if(activeView == ClientGUIView.MENU && clientMenu != null)
		{
			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.white);
			g.setFont(FONT);
			g.drawString("Loading...", getWidth()/2-100, getHeight()/3);
		}
		else if(activeView == ClientGUIView.GAME && chessGUI != null)
			chessGUI.paint(g);
		else
		{
			g.setColor(Color.red);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.white);
			g.setFont(FONT);
			g.drawString("Nothing to render. You fucked up.", getWidth()/2-300, getHeight()/3);
		}
	}
}
