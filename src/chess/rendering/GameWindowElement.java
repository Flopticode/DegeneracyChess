package chess.rendering;

import java.awt.Graphics;

import chess.rendering.menu.api.ClickData;

public abstract class GameWindowElement
{
	private GameWindow window;
	
	public GameWindowElement(GameWindow window)
	{
		this.window = window;
	}
	
	public final void repaint()
	{
		window.repaint();
	}
	
	public abstract void paint(Graphics g);
	
	public void onMousePress(ClickData data) { }
	public void onMouseRelease(ClickData data) { }
	public void onMouseClick(ClickData data) { }
}