package chess.network.impl.server.lobby;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import chess.ChessGame;
import chess.Util;
import chess.figure.FigureColor;
import chess.network.GameStatus;
import chess.network.api.Server;
import chess.network.impl.exception.InvalidMessageException;
import chess.network.impl.packet.*;
import chess.network.impl.packet.packets.*;
import chess.network.impl.server.NetworkAddress;

public class LobbyHandler extends Server
{
	public static final int PLAYER_COUNT = 2;
	
	private static Random rand = new Random();
	
	private NetworkAddress host;
	private String id;
	
	private Map<NetworkAddress, FigureColor> clientColors = new HashMap<>();
	private ChessGame game;
	private boolean gameStarted = false;

	NetworkAddress address;

	public LobbyHandler(String ip, int port)
	{
		super(port);

		this.address = new NetworkAddress(ip, port);
		this.id = generateID();
		this.game = new ChessGame();
	}

	@Override
	public void processNewConnection(String pClientIP, int pClientPort)
	{

	}

	@Override
	public void processMessage(String pClientIP, int pClientPort, String pMessage)
	{
		Util.printBytes(pMessage, System.err);

		if(pMessage.isEmpty())
			return; // Client sent empty packet

		NetworkAddress addr = NetworkAddress.create(pClientIP, pClientPort);

		int type = (int)pMessage.charAt(0);

		if(!Packet.isType(type))
			return; // Client sent packet with invalid type specification

		Packet pck;

		try
		{
			pck = Packet.create(type, pMessage.substring(1));
		}
		catch(InvalidMessageException e)
		{
			return; // Client sent package with correct type, but with incorrect data
		}

		handlePacket(addr, pck);
	}

	@Override
	public void processClosingConnection(String pClientIP, int pClientPort)
	{

	}

	public boolean join(NetworkAddress addr, FigureColor color)
	{
		if(this.clientColors.size() == PLAYER_COUNT)
			return false;
		
		if(this.clientColors.size() == 0)
			this.host = addr;
		
		this.clientColors.put(addr, color);
		
		return true;
	}
	public boolean dejoin(NetworkAddress addr)
	{
		// Dejoin player
		return true;
	}
	private NetworkAddress getClient(NetworkAddress na)
	{
		for(NetworkAddress addr : clientColors.keySet())
		{
			if(addr.equals(na))
				return addr;
		}
		return null;
	}
	public boolean isClient(NetworkAddress addr)
	{
		return getClient(addr) != null;
	}
	private FigureColor getClientColor(NetworkAddress addr)
	{
		return this.clientColors.get(addr);
	}
	
	public boolean handlePacket(NetworkAddress addr, Packet p)
	{
		if(addr == null)
			return false;
		
		if(p instanceof InGamePacket && !gameStarted)
			return false;
		else if(p instanceof PreGamePacket && gameStarted)
			return false;

		if(p instanceof JoinLobbyPacket joinLobbyPacket)
			return handleJoinLobbyPacket(addr, joinLobbyPacket);
		if(p instanceof StartGamePacket startGamePacket)
			return handleStartGamePacket(addr, startGamePacket);
		if(p instanceof MovePacket movePacket)
			return handleMovePacket(addr, movePacket);

		System.err.println("Invalid packet type " + p.getClass().getName() + " received");
		return false;
	}
	private boolean handleJoinLobbyPacket(NetworkAddress addr, JoinLobbyPacket pck)
	{
		return join(addr, pck.preferredColor);
	}
	private boolean handleStartGamePacket(NetworkAddress addr, StartGamePacket pck)
	{
		if(!addr.equals(this.host))
		{
			System.err.println("A client that is not the host tried to start the game.");
			return false;
		}

		if(this.clientColors.size() == PLAYER_COUNT)
			this.gameStarted = true;
		
		this.broadcastPacket(new GameStatusPacket(getGameStatus()));

		return true;
	}
	private GameStatus getGameStatus()
	{
		return this.gameStarted ? GameStatus.STARTED : GameStatus.LOBBY;
	}
	private boolean handleMovePacket(NetworkAddress addr, MovePacket pck)
	{
		switch(pck.mode)
		{
			case MOVE:
				return getClientColor(addr) == game.getActiveColor() ? game.tryMove(pck.srcX, pck.srcY, pck.tarX, pck.tarY) : false;
			case INTERACT:
				return getClientColor(addr) == game.getActiveColor() ? game.tryInteract(pck.srcX, pck.srcY, pck.tarX, pck.tarY) : false;
			default:
				System.err.println("Invalid move packet received (invalid InteractionType provided)");
				return false; // Invalid interaction type
		}
	}
	private void broadcastPacket(Packet p)
	{
		sendToAll(p.serialize());
	}

	public int getPort()
	{
		return address.port;
	}
	public String getIP()
	{
		return address.ip;
	}

	private static String generateID()
	{
		String str = "";
		
		for(int i = 0; i < 40; i++)
			str += rand.nextInt(10);
		
		return str;
	}
}
