package chess.rendering.server;

import chess.rendering.GameWindow;
import chess.rendering.menu.api.Menu;

public class ServerGUI extends Menu
{
	public static final int FRAME_X = 200;
	public static final int FRAME_Y = 200;
	public static final int FRAME_WIDTH = 400;
	public static final int FRAME_HEIGHT = 400;
	
	public ServerGUI(GameWindow window)
	{
		super(window);
	}
}
