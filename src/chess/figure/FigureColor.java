package chess.figure;

import chess.network.Serializable;

public enum FigureColor implements Serializable
{
	WHITE("W", 0),
	BLACK("B", 1),
	ASIAN("A", 2);
	
	
	private String nameStr;
	private int id;
	
	private FigureColor(String nameStr, int id)
	{
		this.nameStr = nameStr;
		this.id = id;
	}
	
	public FigureColor getOpponent()
	{
		return this == WHITE ? BLACK : this == BLACK ? WHITE : null;
	}
	public int getID()
	{
		return id;
	}
	
	@Override
	public String toString()
	{
		return this.nameStr;
	}
	
	public static FigureColor byID(int id)
	{
		for(FigureColor c : FigureColor.values())
			if(c.id == id)
				return c;
		return null;
	}

	@Override
	public String serialize()
	{
		return "" + (char)this.id;
	}
}
