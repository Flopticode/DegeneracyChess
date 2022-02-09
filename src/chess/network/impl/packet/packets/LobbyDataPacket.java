package chess.network.impl.packet.packets;

import chess.figure.FigureColor;
import chess.network.impl.exception.InvalidMessageException;
import chess.network.impl.packet.AbstractPacket;
import chess.network.impl.packet.PacketDataScanner;
import chess.network.impl.packet.PacketWriter;

public class LobbyDataPacket extends AbstractPacket
{
    public final String ip;
    public final int port;

    public LobbyDataPacket(String str) throws InvalidMessageException {
        PacketDataScanner scanner = new PacketDataScanner(str);

        try
        {
            ip = scanner.scanString();
            port = scanner.scanInt(4);
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
    }
    public LobbyDataPacket(String ip, int port)
    {
        this.ip = ip;
        this.port = port;
    }

    @Override
    public String serialize()
    {
        return new PacketWriter(this).writeString(ip).writeInt(port, 4).build();
    }
}
