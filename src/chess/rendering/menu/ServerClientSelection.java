package chess.rendering.menu;

import chess.figure.FigureColor;
import chess.network.NetworkAddress;
import chess.network.impl.server.lobby.LobbyManagementServer;
import chess.rendering.menu.api.Button;
import chess.rendering.menu.api.ClickData;
import chess.rendering.menu.api.Menu;

@SuppressWarnings("serial")
public class ServerClientSelection extends Menu
{

	public ServerClientSelection(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		
		this.add(new Button(0, 0, 200, 150, "./images/ui/server_client_selection/server.png").addOnRelease(this::onServerClicked));
		this.add(new Button(0, 150, 200, 150, "./images/ui/server_client_selection/client.png").addOnRelease(this::onClientClicked));
	}
	
	private void onServerClicked(ClickData data)
	{
		System.out.println("Server");
		
		((MainMenu)this.getParentMenu()).startServer();
		
		this.close();
	}
	private void onClientClicked(ClickData data)
	{
		System.out.println("Client");
		
		((MainMenu)this.getParentMenu()).startClient(new NetworkAddress("localhost", LobbyManagementServer.PORT), FigureColor.WHITE);
		
		this.close();
	}
}
