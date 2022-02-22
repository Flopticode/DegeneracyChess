package chess.figure.figures;

import chess.Board;
import chess.figure.AbstractFigure;
import chess.figure.DiagonalReach;
import chess.figure.FigureColor;
import chess.figure.FigureType;
import chess.figure.FigureTypes;

public class Bishop extends AbstractFigure implements DiagonalReach
{
	protected Bishop(Board board, FigureType type, FigureColor color, short x, short y)
	{
		super(board, type, color, x, y);
	}
	public Bishop(Board board, FigureColor color, short x, short y)
	{
		this(board, FigureTypes.BISHOP, color, x, y);
	}

	@Override
	public Boolean isInMovementRange(short tarX, short tarY)
	{
		Boolean superCanJoink = super.isInMovementRange(tarX, tarY); 
		if(superCanJoink != null)
			return superCanJoink;
		
		FigureColor color = board.getGame().getActiveColor();
		
		return this.canReachDiagonally(board, color, x, y, tarX, tarY, Short.MAX_VALUE, true, true, true, true, true, false);
	}
	
	@Override
	public void tick()
	{
		
	}
	@Override
	public int getAttackStrength()
	{
		return 5;
	}
	@Override
	public int getDefenseStrength()
	{
		return 4;
	}
}
