package chess.network.impl.packet;

import chess.network.Serializable;
import chess.network.impl.exception.InvalidMessageException;

public interface Packet extends Serializable
{
	public static boolean isType(int type)
	{
		return type == JoinLobbyPacket.TYPE_SPEC
				|| type == MovePacket.TYPE_SPEC
				|| type == GameStatusPacket.TYPE_SPEC
				|| type == StartGamePacket.TYPE_SPEC;
	}
	public static Packet create(int type, String data) throws InvalidMessageException
	{
		switch(type)
		{
		case JoinLobbyPacket.TYPE_SPEC:
			return new JoinLobbyPacket(data);
		case MovePacket.TYPE_SPEC:
			return new MovePacket(data);
		case GameStatusPacket.TYPE_SPEC:
			return new GameStatusPacket(data);
		case StartGamePacket.TYPE_SPEC:
			return new StartGamePacket();
		default:
			throw new IllegalArgumentException("Invalid type specification " + type);
		}
	}
	
	public int getType();
}
