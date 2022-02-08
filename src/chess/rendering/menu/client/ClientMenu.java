package chess.rendering.menu.client;

import chess.network.impl.client.ChessClient;
import chess.rendering.menu.api.Button;
import chess.rendering.menu.api.ClickData;
import chess.rendering.menu.api.Menu;

@SuppressWarnings("serial")
public class ClientMenu extends Menu
{
	private ChessClient client;

	public ClientMenu(ChessClient client, int x, int y, int width, int height)
	{
		super(x, y, width, height);
		
		this.client = client;
		
		this.add(new Button(0, 0, 200, 150, "./images/ui/client/start.png").addOnClick(this::onClick));
	}
	
	public void onClick(ClickData data)
	{
		client.sendStartGamePacket();
	}
}
