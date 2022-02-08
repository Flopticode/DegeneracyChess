package chess.network.impl.packet;

import chess.network.InteractionType;
import chess.network.impl.exception.InvalidMessageException;

public class MovePacket extends AbstractPacket implements InGamePacket
{
	public static final int TYPE_SPEC = 1;
	public static final int BYTE_LEN = 9;
	
	public final short srcX;
	public final short srcY;
	public final short tarX;
	public final short tarY;
	public final InteractionType mode;
	
	public MovePacket(String str) throws InvalidMessageException
	{
		super(BYTE_LEN, str);
		
		this.srcX = (short)(str.charAt(0) | (str.charAt(1) << Byte.SIZE));
		this.srcY = (short)(str.charAt(2) | (str.charAt(3) << Byte.SIZE));
		this.tarX = (short)(str.charAt(4) | (str.charAt(5) << Byte.SIZE));
		this.tarY = (short)(str.charAt(6) | (str.charAt(7) << Byte.SIZE));
		this.mode = InteractionType.byID((int)str.charAt(8));
		
		if(mode == null)
			throw new InvalidMessageException(str, "Mode is invalid");
	}

	@Override
	public String serialize()
	{
		char[] arr = new char[BYTE_LEN];
		arr[0] = (char)(srcX & 0x00FF);
		arr[1] = (char)(srcX & 0xFF00);
		arr[2] = (char)(srcY & 0x00FF);
		arr[3] = (char)(srcY & 0xFF00);
		arr[4] = (char)(tarX & 0x00FF);
		arr[5] = (char)(tarX & 0xFF00);
		arr[6] = (char)(tarY & 0x00FF);
		arr[7] = (char)(tarY & 0xFF00);
		arr[8] = mode.serialize().charAt(0);
		
		return new String(arr);
	}
	@Override
	public int getType()
	{
		return TYPE_SPEC;
	}
}
