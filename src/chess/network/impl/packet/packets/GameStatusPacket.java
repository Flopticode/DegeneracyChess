package chess.network.impl.packet.packets;

import chess.network.GameStatus;
import chess.network.impl.exception.InvalidMessageException;
import chess.network.impl.packet.AbstractPacket;
import chess.network.impl.packet.PacketDataScanner;
import chess.network.impl.packet.PacketWriter;
import chess.network.impl.packet.PreGamePacket;

public class GameStatusPacket extends AbstractPacket implements PreGamePacket
{
	public final GameStatus status;
	
	public GameStatusPacket(String str) throws InvalidMessageException
	{
		PacketDataScanner scanner = new PacketDataScanner(str);

		try
		{
			status = GameStatus.byID(scanner.scanInt(1));
			scanner.close();
		}
		catch(StringIndexOutOfBoundsException e)
		{
			throw new InvalidMessageException(str, "Invalid data for this packet.");
		}
		catch(InvalidMessageException e)
		{
			throw e;
		}

		if(status == null)
			throw new InvalidMessageException(str, "Status is invalid");
	}
	public GameStatusPacket(GameStatus status)
	{
		this.status = status;
	}

	@Override
	public String serialize()
	{
		return new PacketWriter(this).writeInt(status.id, 1).build();
	}

}
