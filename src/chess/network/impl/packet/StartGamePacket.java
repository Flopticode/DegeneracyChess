package chess.network.impl.packet;

import chess.network.impl.exception.InvalidMessageException;

public class StartGamePacket extends AbstractPacket implements PreGamePacket
{
	public static final int TYPE_SPEC = 2;
	public static final int BYTE_LEN = 0;
	
	public StartGamePacket(String str) throws InvalidMessageException
	{
		super(BYTE_LEN, str);
	}
	public StartGamePacket()
	{
		
	}
	
	@Override
	public int getType()
	{
		return TYPE_SPEC;
	}

	@Override
	public String serialize()
	{
		return "";
	}

}
