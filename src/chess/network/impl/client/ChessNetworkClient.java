package chess.network.impl.client;

import chess.UpdateGameStatusListener;
import chess.Util;
import chess.figure.FigureColor;
import chess.network.ConnectionFailedException;
import chess.network.GameStatus;
import chess.network.api.Client;
import chess.network.impl.exception.InvalidMessageException;
import chess.network.impl.packet.packets.*;
import chess.network.impl.packet.Packet;
import chess.network.NetworkAddress;

import java.util.LinkedList;
import java.util.List;

public class ChessNetworkClient extends Client
{
	private List<UpdateGameStatusListener> updateGameStatusListeners = new LinkedList<>();
	private FigureColor preferredColor;

	public ChessNetworkClient(NetworkAddress serverAddr, FigureColor preferredColor, boolean isConnectingToManagement) throws ConnectionFailedException
	{
		super(serverAddr.ip, serverAddr.port);

		if(!this.isConnected())
			throw new ConnectionFailedException("Connection to server failed.");

		this.preferredColor = preferredColor;

		if(isConnectingToManagement)
			this.sendRequestLobbyPacket();
		else
			this.sendJoinLobbyPacket();
	}

	public void addOnUpdateGameStatus(UpdateGameStatusListener l)
	{
		updateGameStatusListeners.add(l);
	}
	public boolean removeOnUpdateGameStatus(UpdateGameStatusListener l)
	{
		return updateGameStatusListeners.remove(l);
	}
	private void fireOnUpdateGameStatus(GameStatus newStatus)
	{
		for(UpdateGameStatusListener l : updateGameStatusListeners)
			l.onNewGameStatus(newStatus);
	}
	
	private void sendPacket(Packet pck)
	{
		this.send(pck.serialize());
	}
	public void sendStartGamePacket()
	{
		sendPacket(new StartGamePacket());
	}
	public void sendJoinLobbyPacket()
	{
		sendPacket(new JoinLobbyPacket(preferredColor));
	}
	public void sendRequestLobbyPacket()
	{
		sendPacket(new RequestLobbyPacket(preferredColor));
	}

	@Override
	public void processMessage(String pMessage)
	{
		Packet pck;
			
		try
		{
			pck = Packet.create((int)pMessage.charAt(0), pMessage.substring(1));
		}
		catch(InvalidMessageException ime)
		{
			System.err.println("An invalid package was received from client.");
			return;
		}
		
		if(pck instanceof GameStatusPacket gameStatusPacket)
			handleGameStatusPacket(gameStatusPacket);
		if(pck instanceof LobbyDataPacket lobbyDataPacket)
			handleLobbyDataPacket(lobbyDataPacket);
	}
	
	private void handleGameStatusPacket(GameStatusPacket pck)
	{
		fireOnUpdateGameStatus(pck.status);
	}
	private void handleLobbyDataPacket(LobbyDataPacket lobbyDataPacket)
	{
		this.close();
		this.connect(lobbyDataPacket.ip, lobbyDataPacket.port);
		this.sendJoinLobbyPacket();
	}
}
