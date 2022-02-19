package chess.network.impl.server.lobby;

import java.util.LinkedList;
import java.util.List;

import chess.Util;
import chess.figure.FigureColor;
import chess.network.NetworkAddress;
import chess.network.api.Server;
import chess.network.impl.exception.InvalidMessageException;
import chess.network.impl.packet.ManagementPacket;
import chess.network.impl.packet.Packet;
import chess.network.impl.packet.packets.LobbyDataPacket;
import chess.network.impl.packet.packets.RequestLobbyPacket;
import chess.rendering.server.ServerGUI;

public class LobbyManagementServer extends Server
{
	private NetworkAddress[] gameServers;

	private static class LobbyManagementPlayer
	{
		private NetworkAddress address;
		private FigureColor color;

		public LobbyManagementPlayer(NetworkAddress addr, FigureColor color)
		{
			this.address = addr;
			this.color = color;
		}

		@Override
		public boolean equals(Object other)
		{
			return other instanceof LobbyManagementPlayer && ((LobbyManagementPlayer)other).address.equals(address) && ((LobbyManagementPlayer)other).color == color;
		}
	}
	private static class Lobby
	{
		private NetworkAddress address;
		private LobbyManagementPlayer[] players = new LobbyManagementPlayer[LobbyHandler.MAX_PLAYER_COUNT];
		private int curPlayerCount = 0;

		public Lobby(NetworkAddress gameServerAddress)
		{
			address = gameServerAddress;
		}

		public boolean joinPlayer(LobbyManagementPlayer player)
		{
			if(!canJoinPlayer(player))
				return false;

			players[curPlayerCount++] = player;
			return true;
		}
		public boolean dejoinPlayer(NetworkAddress addr)
		{
			// TODO
			throw new UnsupportedOperationException("Dejoining a player is not supported yet.");
		}
		public boolean isPlayer(NetworkAddress addr)
		{
			for(LobbyManagementPlayer player : players)
				if(player != null && player.address.equals(addr))
					return true;
			return false;
		}
		private boolean canJoinPlayer(LobbyManagementPlayer player)
		{
			if(curPlayerCount >= LobbyHandler.MAX_PLAYER_COUNT)
				return false;

			FigureColor playerColor = player.color;

			for(LobbyManagementPlayer p : players)
				if(player.color == p.color)
					return false;
			return true;
		}
		public boolean isFull()
		{
			return curPlayerCount < LobbyHandler.MAX_PLAYER_COUNT;
		}
		public int getPlayerCount()
		{
			return curPlayerCount;
		}
	}

	public static final int PORT = 19375;

	private List<Lobby> lobbies = new LinkedList<>();
	private List<LobbyManagementPlayer> unjoinedPlayers = new LinkedList<>();
	
	private ServerGUI gui;
	
	public LobbyManagementServer(NetworkAddress[] gameServers)
	{
		super(PORT);
		this.gui = new ServerGUI();

		this.gameServers = gameServers;

		System.out.println("Lobby management server is initialized and open at port " + PORT + ".");
	}

	@Override
	public void processNewConnection(String pClientIP, int pClientPort)
	{
		
	}

	@Override
	public void processMessage(final String pClientIP, final int pClientPort, final String pMessage)
	{
		if(pMessage.isEmpty())
			return; // Client sent empty packet


		
		NetworkAddress addr = NetworkAddress.create(pClientIP, pClientPort);
		
		Packet pck;
		try
		{
			pck = Packet.create(pMessage);
		}
		catch(InvalidMessageException e)
		{
			return; // Client sent package with correct type, but with incorrect data
		}

		System.out.print("Received " + pck.getClass().getSimpleName() + " by client " + pClientIP + ":" + pClientPort + ": " + Util.getBytesString(pMessage.substring(1), System.err));
		
		if(pck instanceof ManagementPacket)
		{
			if(pck instanceof RequestLobbyPacket requestLobbyPck)
			{
				Lobby lobbyMovedIn = handlePlayerLogin(new LobbyManagementPlayer(addr, requestLobbyPck.color));

				if(lobbyMovedIn != null)
					this.sendPacket(addr, new LobbyDataPacket(lobbyMovedIn.address.ip, lobbyMovedIn.address.port));
			}
		}
	}

	/**
	 * Entscheidet nach dem hedonistischen Kalkül, in welche Lobby der Spieler platziert wird.
	 * Gibt es aktuell keine adäquate Lobby, wird null zurückgegeben und der Spieler wartet.
	 */
	private Lobby handlePlayerLogin(LobbyManagementPlayer player)
	{
		unjoinedPlayers.add(player);

		Lobby retVal = null;

		boolean joinedAPlayer = false;
		while(true)
		{
			for (LobbyManagementPlayer curPlayer : unjoinedPlayers)
			{
				int highestScore = Integer.MIN_VALUE;
				Lobby bestLobby = null;

				for (Lobby lobby : lobbies)
				{
					int score = lobby.getPlayerCount();

					if (score > highestScore && lobby.canJoinPlayer(curPlayer))
					{
						bestLobby = lobby;
						highestScore = score;
						joinedAPlayer = true;

						if(curPlayer == player)
							retVal = bestLobby;
					}
				}
			}

			if(!joinedAPlayer)
				break;
			joinedAPlayer = false;
		}

		return retVal;
	}

	@Override
	public void processClosingConnection(String pClientIP, int pClientPort)
	{
		NetworkAddress addr = NetworkAddress.create(pClientIP, pClientPort);

		Lobby lobby = findClientLobby(addr);

		if(lobby != null)
			lobby.dejoinPlayer(addr);
	}
	
	private Lobby findClientLobby(NetworkAddress addr)
	{
		for(Lobby lobby : lobbies)
			if(lobby.isPlayer(addr))
				return lobby;
		return null;
	}

	private static int lastport = 420;
	private Lobby createNewLobby()
	{
		Lobby lobby = new Lobby(gameServers[Util.randomInt(0, gameServers.length-1)]);

		lobbies.add(lobby);

		return lobby;
	}

	private void sendPacket(NetworkAddress addr, Packet pck)
	{
		System.out.println("Sent packet " + pck.getClass().getSimpleName() + " to client " + addr.toString() + " with data " + pck.serialize());

		this.send(addr.ip, addr.port, pck.serialize());
	}

	public ServerGUI getGUI()
	{
		return gui;
	}
}
