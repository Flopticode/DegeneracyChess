package chess.network.impl.packet;

import chess.network.impl.packet.packets.*;

import java.util.HashMap;
import java.util.Map;

public abstract class PacketRegistry
{
    private static final Map<Class<? extends AbstractPacket>, PacketClassInfo> map = new HashMap<>();
    private static final Map<Integer, Class<? extends AbstractPacket>> idClassMap = new HashMap<>();

    private static int id = 0;
    public static void registerPacketType(Class<? extends AbstractPacket> clazz)
    {
        map.put(clazz, new PacketClassInfo(++id));
        idClassMap.put(id, clazz);
    }
    public static int getPacketTypeID(Class<? extends AbstractPacket> clazz)
    {
        return getInfo(clazz).id;
    }
    public static Class<? extends AbstractPacket> getClass(int id)
    {
        return idClassMap.get(id);
    }

    private static PacketClassInfo getInfo(Class<? extends AbstractPacket> clazz)
    {
        PacketClassInfo info = map.get(clazz);
        if(info == null)
            throw new NullPointerException("No such packet class was registered.");

        return info;
    }

    private record PacketClassInfo(int id)
    {
        public int getID()
        {
            return id;
        }
    }

    static
    {
        registerPacketType(GameStatusPacket.class);
        registerPacketType(JoinLobbyPacket.class);
        registerPacketType(LobbyDataPacket.class);
        registerPacketType(MovePacket.class);
        registerPacketType(RequestLobbyPacket.class);
        registerPacketType(StartGamePacket.class);
    }
}
