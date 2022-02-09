package chess.network.impl.packet;

import chess.network.impl.exception.InvalidMessageException;

public class PacketDataScanner
{
    private final String data;
    private int index = 0;

    public PacketDataScanner(String data)
    {
        this.data = data;
    }

    public int scanInt(int bytes) throws StringIndexOutOfBoundsException
    {
        if(bytes > 4)
            throw new IllegalArgumentException("Integers are 32-bit data types (" + bytes*8 + " is obviously too much)");
        else if(bytes < 0)
            throw new IllegalArgumentException("I will not store my int reverse (negative byte size is absolute garbage...)");

        int integer = 0;
        int mask = 0xFF;

        for(int i = 0; i < bytes; i++)
        {
            integer |= (data.charAt(index+i) & mask);
            mask |= mask << 8;
        }

        index += bytes;

        return integer;
    }
    public String scanString() throws StringIndexOutOfBoundsException
    {
        int len = scanInt(2);

        String str = "";

        for(int i = 0; i < len; i++)
            str += data.charAt(index+i);

        index += len;

        return str;
    }
    public void close() throws InvalidMessageException
    {
        if(this.index != data.length())
            throw new InvalidMessageException(data, "Invalid size");
    }
}
