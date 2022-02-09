package chess.network.impl.packet.packets;

import chess.figure.FigureColor;
import chess.network.impl.exception.InvalidMessageException;
import chess.network.impl.packet.AbstractPacket;
import chess.network.impl.packet.PacketDataScanner;
import chess.network.impl.packet.PacketWriter;

public class RequestLobbyPacket extends AbstractPacket
{
    public final FigureColor color;

    public RequestLobbyPacket(String str) throws InvalidMessageException
    {
        PacketDataScanner scanner = new PacketDataScanner(str);

        try
        {
            this.color = FigureColor.byID(scanner.scanInt(1));
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

        if(this.color == null)
            throw new InvalidMessageException(str, "Invalid FigureColor provided");
    }
    public RequestLobbyPacket(FigureColor color)
    {
        this.color = color;
    }

    @Override
    public String serialize()
    {
        return new PacketWriter(this).writeInt(color.getID(), 1).build();
    }
}
