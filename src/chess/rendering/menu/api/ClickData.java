package chess.rendering.menu.api;

public class ClickData
{
	public final int x;
	public final int y;
	public final int button;
	
	public ClickData(int x, int y, int button)
	{
		this.x = x;
		this.y = y;
		this.button = button;
	}
}
