package chess.rendering.menu;

import chess.figure.FigureColor;
import chess.network.NetworkAddress;
import chess.rendering.menu.api.Button;
import chess.rendering.menu.api.ClickData;
import chess.rendering.menu.api.Menu;

@SuppressWarnings("serial")
public abstract class MainMenu extends Menu
{
	public static final int WIDTH = 400;
	public static final int HEIGHT = 400;
	
	public MainMenu(int x, int y)
	{
		super(x, y, WIDTH, HEIGHT);
		
		this.add(new Button(0, 0, 200, 150, "./images/ui/mainmenu/localgame.png").addOnRelease(this::onLocalGameClicked));
		this.add(new Button(0, 150, 200, 150, "./images/ui/mainmenu/multiplayer.png").addOnRelease(this::onMultiplayerClicked));
		
		this.addMenu("ServerClientSelectionMenu", new ServerClientSelection(x, y, WIDTH, HEIGHT));
	}
	
	private void onLocalGameClicked(ClickData data)
	{
		this.startLocal();
	}
	private void onMultiplayerClicked(ClickData data)
	{
		this.openMenu("ServerClientSelectionMenu");
	}
	
	public abstract void startServer();
	public abstract void startClient(NetworkAddress serverAddr, FigureColor preferredColor);
	public abstract void startLocal();
}
