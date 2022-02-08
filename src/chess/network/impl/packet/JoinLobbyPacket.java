package chess.network.impl.packet;

import chess.figure.FigureColor;
import chess.network.impl.exception.InvalidMessageException;

public class JoinLobbyPacket extends AbstractPacket implements ManagementPacket
{
	public static final int TYPE_SPEC = 0;
	public static final int BYTE_LEN = 1;
	
	public final FigureColor preferredColor;
	
	public JoinLobbyPacket(String str) throws InvalidMessageException
	{
		super(BYTE_LEN, str);
		
		this.preferredColor = FigureColor.byID((int)str.charAt(0));
	}
	public JoinLobbyPacket(FigureColor preferredColor)
	{
		this.preferredColor = preferredColor;
	}
	
	@Override
	public String serialize()
	{
		return this.preferredColor.serialize();
	}
	@Override
	public int getType()
	{
		return TYPE_SPEC;
	}
}
