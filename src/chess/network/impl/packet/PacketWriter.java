package chess.network.impl.packet;

public class PacketWriter
{
    private String str = "";

    public PacketWriter(AbstractPacket p)
    {
        writeInt(PacketRegistry.getPacketTypeID(p.getClass()), 1);
    }

    public PacketWriter writeInt(int i, int bytes)
    {
        for(int z = bytes-1; z >= 0; z--)
            str += (char)getByte(i, z);

        return this;
    }
    public PacketWriter writeString(String str)
    {
        int len = str.length();

        this.str += (char)getByte(len, 1) + (char)getByte(len, 0);
        this.str += str;

        return this;
    }

    private int getByte(int i, int index)
    {
        int mask = 0xFF;

        for(int z = 0; z < index; z++)
            mask <<= 8;

        return (i & mask) >> (index*8);
    }

    public String build()
    {
        return str;
    }
}
