package chess.network.impl.packet;

import chess.network.impl.exception.InvalidMessageException;

public abstract class AbstractPacket implements Packet
{
	public AbstractPacket(int len, String str) throws InvalidMessageException
	{
		if(str.length() > len)
			throw new InvalidMessageException(str, "String is too long");
		else if(str.length() < len)
			throw new InvalidMessageException(str, "String is too short");
	}
	public AbstractPacket()
	{
		
	}
}
