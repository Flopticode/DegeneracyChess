package chess.figure.figures;

import chess.Board;
import chess.figure.AbstractFigure;
import chess.figure.FigureColor;
import chess.figure.FigureType;
import chess.figure.FigureTypes;

public class Knight extends AbstractFigure
{
	protected Knight(Board board, FigureType type, FigureColor color, short x, short y)
	{
		super(board, type, color, x, y);
	}
	public Knight(Board board, FigureColor color, short x, short y)
	{
		this(board, FigureTypes.KNIGHT, color, x, y);
	}

	@Override
	public Boolean isInMovementRange(short tarX, short tarY)
	{
		Boolean superCanJoink = super.isInMovementRange(tarX, tarY); 
		if(superCanJoink != null)
			return superCanJoink;
		
		return ((Math.abs(x-tarX) == 1 && Math.abs(y-tarY) == 2)|| (Math.abs(x-tarX) == 2 && Math.abs(y-tarY) == 1));
	}

	@Override
	public void tick()
	{
		
	}
	@Override
	public int getAttackStrength()
	{
		return 6;
	}
	@Override
	public int getDefenseStrength()
	{
		return 5;
	}

}
