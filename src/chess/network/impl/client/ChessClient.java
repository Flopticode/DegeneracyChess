package chess.network.impl.client;

import chess.Util;
import chess.figure.FigureColor;
import chess.network.ConnectionFailedException;
import chess.network.GameStatus;
import chess.network.api.Client;
import chess.network.impl.exception.InvalidMessageException;
import chess.network.impl.packet.GameStatusPacket;
import chess.network.impl.packet.JoinLobbyPacket;
import chess.network.impl.packet.Packet;
import chess.network.impl.packet.StartGamePacket;
import chess.network.impl.server.NetworkAddress;
import chess.rendering.client.lobby.ClientGUI;

public class ChessClient extends Client
{
	private ClientGUI gui;
	
	public ChessClient(NetworkAddress serverAddr, FigureColor preferredColor) throws ConnectionFailedException
	{
		super(serverAddr.ip, serverAddr.port);
		
		if(!this.isConnected())
			throw new ConnectionFailedException("Connection to server failed.");
		
		gui = new ClientGUI(this);
		
		sendPacket(new JoinLobbyPacket(preferredColor));
	}
	
	private void sendPacket(Packet pck)
	{
		this.send((char)pck.getType() + pck.serialize());
	}
	public void sendStartGamePacket()
	{
		sendPacket(new StartGamePacket());
	}

	/**
	 *
	 */
	@Override
	public void processMessage(String pMessage)
	{
		Util.printBytes(pMessage, System.err);
		
		Packet pck;
			
		try
		{
			pck = Packet.create((int)pMessage.charAt(0), pMessage.substring(1));
		}
		catch(InvalidMessageException ime)
		{
			return;
		}
		
		if(pck instanceof GameStatusPacket gameStatusPacket)
			handleGameStatusPacket(gameStatusPacket);
	}
	
	private void handleGameStatusPacket(GameStatusPacket pck)
	{
		if(pck.status == GameStatus.STARTED)
			gui.initGame();
	}
	
	public ClientGUI getGUI()
	{
		return gui;
	}
}
