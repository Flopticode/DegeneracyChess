package chess.network.impl.packet.packets;

import chess.network.impl.exception.InvalidMessageException;
import chess.network.impl.packet.AbstractPacket;
import chess.network.impl.packet.PacketWriter;
import chess.network.impl.packet.PreGamePacket;

public class StartGamePacket extends AbstractPacket implements PreGamePacket
{
	public StartGamePacket(String str) throws InvalidMessageException
	{

	}
	public StartGamePacket()
	{
		
	}

	@Override
	public String serialize()
	{
		return new PacketWriter(this).build();
	}

}
