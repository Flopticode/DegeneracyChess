package chess.rendering.menu.api;

import java.awt.Graphics;

public abstract class MenuElement
{
	protected int x;
	protected int y;
	protected int widthRequest;
	protected int heightRequest;
	
	protected int width;
	protected int height;
	
	public MenuElement(int x, int y, int widthRequest, int heightRequest)
	{
		this.x = x;
		this.y = y;
		this.widthRequest = widthRequest;
		this.heightRequest = heightRequest;
		
		this.width = widthRequest;
		this.height = heightRequest;
	}
	
	public final int getX()
	{
		return x;
	}
	public final int getY()
	{
		return y;
	}
	public final int getWidthRequest()
	{
		return widthRequest;
	}
	public final int getHeightRequest()
	{
		return heightRequest;
	}
	
	public final boolean isInBounds(int x, int y)
	{
		return x >= this.x && x <= this.x+widthRequest && y >= this.y && y <= this.y+heightRequest;
	}
	
	public final int getActualWidth()
	{
		return widthRequest;
	}
	public final int getActualHeight()
	{
		return heightRequest;
	}
	
	public abstract void updateActualWidth();
	public abstract void updateActualHeight();
	
	public abstract void render(Graphics g);
	public void onMotion(MotionData data) { }
}
