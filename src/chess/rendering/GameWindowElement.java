package chess.rendering;

import java.awt.Graphics;

import chess.rendering.menu.api.ClickData;
import chess.rendering.menu.api.MotionData;

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
	
	public final int getWidth()
	{
		return window.getWidth();
	}
	public final int getHeight()
	{
		return window.getHeight();
	}
	
	public abstract void paint(Graphics g);
	
	public void onMousePress(ClickData data) { }
	public void onMouseRelease(ClickData data) { }
	public void onMouseClick(ClickData data) { }
	public void onMouseMotion(MotionData data) { }
}
