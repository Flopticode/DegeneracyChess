package chess.network.impl.packet;

import chess.network.Serializable;
import chess.network.impl.exception.InvalidMessageException;
import chess.network.impl.packet.packets.GameStatusPacket;
import chess.network.impl.packet.packets.JoinLobbyPacket;
import chess.network.impl.packet.packets.MovePacket;
import chess.network.impl.packet.packets.StartGamePacket;

import java.lang.reflect.InvocationTargetException;

public interface Packet extends Serializable
{
	public static boolean isType(int type)
	{
		return PacketRegistry.getClass(type) != null;
	}
	public static Packet create(int type, String data) throws InvalidMessageException
	{
		Class<? extends AbstractPacket> clazz = PacketRegistry.getClass(type);

		if(clazz == null)
			throw new IllegalArgumentException("A packet type " + type + " does not exist.");

		try
		{
			return clazz.getConstructor(String.class).newInstance(data);
		}
		catch (InstantiationException | IllegalAccessException | NoSuchMethodException e)
		{
			new RuntimeException("Some idiot implemented the " + clazz.getName() + " class wrong. Tip: You should strongly consider adding correct constructors in the future...").printStackTrace();
			e.printStackTrace();
			System.out.println("\n\n");
		}
		catch(InvocationTargetException e)
		{
			new RuntimeException("Package constructor threw an Exception.").printStackTrace();
			e.getCause().printStackTrace();
			System.out.println("\n\n");
		}
		return null;
	}
}
