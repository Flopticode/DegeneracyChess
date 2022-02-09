package chess.network.impl.packet.packets;

import chess.network.InteractionType;
import chess.network.impl.exception.InvalidMessageException;
import chess.network.impl.packet.AbstractPacket;
import chess.network.impl.packet.InGamePacket;
import chess.network.impl.packet.PacketDataScanner;
import chess.network.impl.packet.PacketWriter;

public class MovePacket extends AbstractPacket implements InGamePacket
{
	public final short srcX;
	public final short srcY;
	public final short tarX;
	public final short tarY;
	public final InteractionType mode;
	
	public MovePacket(String str) throws InvalidMessageException
	{
		PacketDataScanner scanner = new PacketDataScanner(str);

		try
		{
			this.srcX = (short)scanner.scanInt(2);
			this.srcY = (short)scanner.scanInt(2);
			this.tarX = (short)scanner.scanInt(2);
			this.tarY = (short)scanner.scanInt(2);
			this.mode = InteractionType.byID((int)scanner.scanInt(1));
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

		if(mode == null)
			throw new InvalidMessageException(str, "Mode is invalid");
	}

	@Override
	public String serialize()
	{
		return new PacketWriter(this)
				.writeInt(srcX, 2)
				.writeInt(srcY, 2)
				.writeInt(tarX, 2)
				.writeInt(tarY, 2)
				.writeInt(mode.id, 1).build();
	}
}
