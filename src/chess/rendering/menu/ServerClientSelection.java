package chess.rendering.menu;

import chess.figure.FigureColor;
import chess.network.NetworkAddress;
import chess.network.impl.server.lobby.LobbyManagementServer;
import chess.rendering.GameWindow;
import chess.rendering.menu.api.Button;
import chess.rendering.menu.api.ClickData;
import chess.rendering.menu.api.Menu;

public class ServerClientSelection extends Menu
{

	public ServerClientSelection(GameWindow window)
	{
		super(window);
		
		this.add(new Button(0, 0, 200, 150, "./images/ui/server_client_selection/server.png").addOnRelease(this::onServerClicked));
		this.add(new Button(0, 0, 200, 150, "./images/ui/server_client_selection/management_server.png").addOnRelease(this::onManagementServerClicked));
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
		
		((MainMenu)this.getParentMenu()).startClient(new NetworkAddress("10.101.4.78", LobbyManagementServer.PORT), FigureColor.BLACK);
		
		this.close();
	}
	private void onManagementServerClicked(ClickData data)
	{
		System.out.println("Management server");
		
		((MainMenu)this.getParentMenu()).startManagementServer();
		
		this.close();
	}
}
