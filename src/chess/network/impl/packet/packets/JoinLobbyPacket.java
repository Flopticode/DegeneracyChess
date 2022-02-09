package chess.network.impl.packet.packets;

import chess.figure.FigureColor;
import chess.network.GameStatus;
import chess.network.impl.exception.InvalidMessageException;
import chess.network.impl.packet.*;

public class JoinLobbyPacket extends AbstractPacket implements PreGamePacket
{
	public final FigureColor preferredColor;
	
	public JoinLobbyPacket(String str) throws InvalidMessageException
	{
		PacketDataScanner scanner = new PacketDataScanner(str);

		try
		{
			preferredColor = FigureColor.byID(scanner.scanInt(1));
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

		if(preferredColor == null)
			throw new InvalidMessageException(str, "Color is invalid");

	}
	public JoinLobbyPacket(FigureColor preferredColor)
	{
		this.preferredColor = preferredColor;
	}
	
	@Override
	public String serialize()
	{
		return new PacketWriter(this).writeInt(preferredColor.getID(), 1).build();
	}
}
