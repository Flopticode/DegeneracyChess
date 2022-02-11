package chess.network.impl.server.lobby;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.*;

import chess.ChessGame;
import chess.Util;
import chess.figure.FigureColor;
import chess.network.GameStatus;
import chess.network.api.Server;
import chess.network.impl.exception.InvalidMessageException;
import chess.network.impl.packet.*;
import chess.network.impl.packet.packets.*;
import chess.network.NetworkAddress;

public class RemoteLobbyHandler extends LobbyHandler
{
	private NetworkAddress address;
	private Map<NetworkAddress, Integer> addressClientIDMap = new HashMap<>();
	private List<ClientMessageHandler> clientMessageHandlers = new LinkedList<>();

	public RemoteLobbyHandler(String ip, int port) throws IOException
	{
		this.address = new NetworkAddress(ip, port);

		init();
	}
	

	public void sendToAll(String pMessage)
	{
		for(ClientMessageHandler handler : clientMessageHandlers)
			handler.send(pMessage);
	}
	private ClientMessageHandler findClientMessageHandler(String ip, int port)
	{
		for(ClientMessageHandler handler : clientMessageHandlers)
			if(handler.getClientIP().equals(ip) && handler.getClientPort() == port)
				return handler;
		return null;
	}
	private void removeClientMessageHandler(ClientMessageHandler handler)
	{
		clientMessageHandlers.remove(handler);
	}

	public void processNewConnection(String pClientIP, int pClientPort)
	{

	}

	public void processMessage(String pClientIP, int pClientPort, String message)
	{
		Packet p;
		try
		{
			p = Packet.create(message);
			System.out.print("Received " + p.getClass().getSimpleName() + " by client " + pClientIP + ":" + pClientPort + ": " + Util.getBytesString(message.substring(1), System.err));
		}
		catch (InvalidMessageException e)
		{
			System.err.println("Received invalid packet.");
			e.printStackTrace();
			return;
		}

		if(p instanceof JoinLobbyPacket joinLobbyPacket)
		{
			int id = getNextClientID();
			boolean succ = join(id, joinLobbyPacket.preferredColor);

			if(succ == false)
			{
				System.err.println("Couldn't join player to this lobby.");
				return;
			}
			else
			{
				NetworkAddress addr = new NetworkAddress(pClientIP, pClientPort);

				addressClientIDMap.put(addr, id);

				System.out.println("Client " + pClientIP + ":" + pClientPort + " was bound to id " + id);
			}
		}
		else
		{
			Integer id = addressClientIDMap.get(new NetworkAddress(pClientIP, pClientPort));

			if(id == null)
			{
				System.err.println("Unregistered client " + pClientIP + ":" + pClientPort + " sent a packet");
				return;
			}

			super.handlePacket(id, p);
		}
	}

	public void processClosingConnection(String pClientIP, int pClientPort)
	{

	}

	protected void broadcastPacket(Packet p)
	{
		System.out.println("Broadcasted packet " + p.getClass().getSimpleName() + " with data " + p.serialize());

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

	private class ClientMessageHandler extends Thread
	{
		private ClientMessageHandler.ClientSocketWrapper socketWrapper;
		private boolean active;

		private class ClientSocketWrapper
		{
			private Socket clientSocket;
			private BufferedReader fromClient;
			private PrintWriter toClient;

			public ClientSocketWrapper(Socket pSocket)
			{
				try
				{
					clientSocket = pSocket;
					toClient = new PrintWriter(clientSocket.getOutputStream(), true);
					fromClient = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
				}
				catch (IOException e)
				{
					clientSocket = null;
					toClient = null;
					fromClient = null;
				}
			}

			public String receive()
			{
				if(fromClient != null)
					try
					{
						return fromClient.readLine();
					}
					catch (IOException e)
					{
					}
				return(null);
			}

			public void send(String pMessage)
			{
				if(toClient != null)
				{
					toClient.println(pMessage);
				}
			}

			public String getClientIP()
			{
				if(clientSocket != null)
					return(clientSocket.getInetAddress().getHostAddress());
				else
					return(null); //Gemaess Java-API Rueckgabe bei nicht-verbundenen Sockets
			}

			public int getClientPort()
			{
				if(clientSocket != null)
					return(clientSocket.getPort());
				else
					return(0); //Gemaess Java-API Rueckgabe bei nicht-verbundenen Sockets
			}

			public void close()
			{
				if(clientSocket != null)
					try
					{
						clientSocket.close();
					}
					catch (IOException e)
					{
						/*
						 * Falls eine Verbindung getrennt werden soll, deren Endpunkt
						 * nicht mehr existiert bzw. ihrerseits bereits beendet worden ist,
						 * geschieht nichts.
						 */
					}
			}
		}

		private ClientMessageHandler(Socket pClientSocket)
		{
			socketWrapper = new ClientMessageHandler.ClientSocketWrapper(pClientSocket);
			if(pClientSocket!=null)
			{
				start();
				active = true;
			}
			else
			{
				active = false;
			}
		}

		public void run()
		{
			String message = null;
			while (active)
			{
				message = socketWrapper.receive();
				if (message != null)
					processMessage(socketWrapper.getClientIP(), socketWrapper.getClientPort(), message);
				else
				{
					ClientMessageHandler aMessageHandler = findClientMessageHandler(socketWrapper.getClientIP(), socketWrapper.getClientPort());
					if (aMessageHandler != null)
					{
						aMessageHandler.close();
						removeClientMessageHandler(aMessageHandler);
						processClosingConnection(socketWrapper.getClientIP(), socketWrapper.getClientPort());
					}
				}
			}
		}

		public void send(String pMessage)
		{
			if(active)
				socketWrapper.send(pMessage);
		}

		public void close()
		{
			if(active)
			{
				active=false;
				socketWrapper.close();
			}
		}

		public String getClientIP()
		{
			return(socketWrapper.getClientIP());
		}

		public int getClientPort()
		{
			return(socketWrapper.getClientPort());
		}

	}

	private void init() throws IOException
	{
		final ServerSocket serverSocket = new ServerSocket(address.port);

		new Thread(()-> {
			while(true)
			{
				try
				{
					Socket socket = serverSocket.accept();

					processNewConnection(socket.getInetAddress().getHostAddress(), socket.getPort());

					clientMessageHandlers.add(new ClientMessageHandler(socket));
				} catch (IOException e)
				{
					System.err.println("ERROR: Socket initialization failed.");
					Thread.currentThread().interrupt();
				}
			}
		}).start();
	}
}
