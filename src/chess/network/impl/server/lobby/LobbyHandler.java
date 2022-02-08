package chess.network.impl.server.lobby;

import java.util.HashMap;
import java.util.Map;
import java.util.Random;

import chess.ChessGame;
import chess.figure.FigureColor;
import chess.network.GameStatus;
import chess.network.api.Server;
import chess.network.impl.packet.GameStatusPacket;
import chess.network.impl.packet.InGamePacket;
import chess.network.impl.packet.MovePacket;
import chess.network.impl.packet.Packet;
import chess.network.impl.packet.PreGamePacket;
import chess.network.impl.packet.StartGamePacket;
import chess.network.impl.server.NetworkAddress;

public class LobbyHandler
{
	public static final int PLAYER_COUNT = 2;
	
	private static Random rand = new Random();
	
	private NetworkAddress host;
	private String id;
	
	private Map<NetworkAddress, FigureColor> clientColors = new HashMap<>();
	private ChessGame game;
	private boolean gameStarted = false;
	
	private Server server;
	
	public LobbyHandler(Server server)
	{
		this.id = generateID();
		this.game = new ChessGame();
		this.server = server;
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
		
		if(p instanceof StartGamePacket startGamePacket)
			return handleStartGamePacket(addr, startGamePacket);
		if(p instanceof MovePacket movePacket)
			return handleMovePacket(addr, movePacket);
		
		return false; // A packet that is not intended as client-to-server packet was sent to the server
	}
	private boolean handleStartGamePacket(NetworkAddress addr, StartGamePacket pck)
	{
		if(!addr.equals(this.host))
			return false; // A client that is not the host of the lobby tried to start the game
		
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
		return getClientColor(addr) == game.getActiveColor() ? game.tryMove(pck.srcX, pck.srcY, pck.tarX, pck.tarY) : false;
	}
	private void broadcastPacket(Packet p)
	{
		server.sendToAll((char)p.getType() + p.serialize());
	}
	
	private static String generateID()
	{
		String str = "";
		
		for(int i = 0; i < 40; i++)
			str += rand.nextInt(10);
		
		return str;
	}
}
