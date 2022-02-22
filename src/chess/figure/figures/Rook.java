package chess.figure.figures;

import chess.Board;
import chess.figure.AbstractFigure;
import chess.figure.FigureColor;
import chess.figure.FigureTypes;
import chess.figure.StraightReach;

public class Rook extends AbstractFigure implements StraightReach
{

	public Rook(Board board, FigureColor color, short x, short y)
	{
		super(board, FigureTypes.ROOK, color, x, y);
	}

	@Override
	public Boolean isInMovementRange(short tarX, short tarY)
	{
		Boolean superCanJoink = super.isInMovementRange(tarX, tarY); 
		if(superCanJoink != null)
			return superCanJoink;
		
		FigureColor color = board.getGame().getActiveColor();
		
		return this.canReachStraight(board, color, x, y, tarX, tarY, Short.MAX_VALUE, true, true, true, true, true, false);
	}

	@Override
	public void tick()
	{
		
	}

	@Override
	public int getAttackStrength()
	{
		return 7;
	}
	@Override
	public int getDefenseStrength()
	{
		return 5;
	}

}
