package chess.network.impl.packet;

import chess.network.GameStatus;
import chess.network.impl.exception.InvalidMessageException;

public class GameStatusPacket extends AbstractPacket implements PreGamePacket
{
	public static final int BYTE_LEN = 1;
	public static final int TYPE_SPEC = 3;
	
	public final GameStatus status;
	
	public GameStatusPacket(String str) throws InvalidMessageException
	{
		super(BYTE_LEN, str);
		
		status = GameStatus.byID((int)str.charAt(0));
		
		if(status == null)
			throw new InvalidMessageException(str, "Status is invalid");
	}
	public GameStatusPacket(GameStatus status)
	{
		this.status = status;
	}

	@Override
	public int getType()
	{
		return TYPE_SPEC;
	}

	@Override
	public String serialize()
	{
		return status.serialize();
	}

}
