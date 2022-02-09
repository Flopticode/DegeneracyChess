package chess.network.impl.server.lobby;

import java.util.LinkedList;
import java.util.List;

import chess.Util;
import chess.network.api.Server;
import chess.network.impl.exception.InvalidMessageException;
import chess.network.impl.packet.ManagementPacket;
import chess.network.impl.packet.Packet;
import chess.network.impl.packet.packets.LobbyDataPacket;
import chess.network.impl.packet.packets.RequestLobbyPacket;
import chess.network.impl.server.NetworkAddress;
import chess.rendering.server.ServerGUI;

public class LobbyManagementServer extends Server
{
	public static final int PORT = 19375;
	
	private List<LobbyHandler> lobbyHandlers = new LinkedList<>();
	private LobbyHandler freeLobby = null;
	
	private ServerGUI gui;
	
	public LobbyManagementServer()
	{
		super(PORT);
		this.gui = new ServerGUI();
		
		System.out.println("Lobby management server is initialized and open at port " + PORT + ".");
	}

	@Override
	public void processNewConnection(String pClientIP, int pClientPort)
	{
		
	}

	@Override
	public void processMessage(final String pClientIP, final int pClientPort, final String pMessage)
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
		
		if(pck instanceof ManagementPacket)
		{
			if(pck instanceof RequestLobbyPacket requestLobbyPck)
			{
				if(freeLobby == null)
					freeLobby = createNewLobby();

				this.sendPacket(addr, new LobbyDataPacket(freeLobby.getIP(), freeLobby.getPort()));
			}
		}
		else
		{
			LobbyHandler handler = findClientLobby(addr);
			
			if(handler == null)
				return; // Client sent lobby-specific packet while not in a lobby
			
			handler.handlePacket(addr, pck);
		}
	}

	@Override
	public void processClosingConnection(String pClientIP, int pClientPort)
	{
		NetworkAddress addr = NetworkAddress.create(pClientIP, pClientPort);
		
		LobbyHandler handler = findClientLobby(addr);
		
		if(handler != null)
			handler.dejoin(addr);
	}
	
	private LobbyHandler findClientLobby(NetworkAddress addr)
	{
		for(LobbyHandler handler : lobbyHandlers)
			if(handler.isClient(addr))
				return handler;
		return null;
	}

	private static int lastport = 420;
	private LobbyHandler createNewLobby()
	{
		LobbyHandler handler = new LobbyHandler("localhost", ++lastport);
		lobbyHandlers.add(handler);
		return handler;
	}

	private void sendPacket(NetworkAddress addr, Packet pck)
	{
		this.send(addr.ip, addr.port, pck.serialize());
	}

	public ServerGUI getGUI()
	{
		return gui;
	}
}
