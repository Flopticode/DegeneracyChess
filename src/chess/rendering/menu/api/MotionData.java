package chess.rendering.menu.api;

public class MotionData
{
	public final int x;
	public final int y;
	
	public MotionData(int x, int y)
	{
		this.x = x;
		this.y = y;
	}
	
	public final int getX()
	{
		return x;
	}
	public final int getY()
	{
		return y;
	}
}
