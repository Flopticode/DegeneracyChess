package chess.figure.figures;

import chess.Board;
import chess.figure.AbstractFigure;
import chess.figure.DiagonalReach;
import chess.figure.FigureColor;
import chess.figure.FigureTypes;
import chess.figure.StraightReach;

public class Queen extends AbstractFigure implements StraightReach, DiagonalReach
{

	public Queen(Board board, FigureColor color, short x, short y)
	{
		super(board, FigureTypes.QUEEN, color, x, y);
	}

	@Override
	public Boolean isInMovementRange(short tarX, short tarY)
	{
		Boolean superCanJoink = super.isInMovementRange(tarX, tarY); 
		if(superCanJoink != null)
			return superCanJoink;
		
		FigureColor color = board.getGame().getActiveColor();
		
		if(this.canReachStraight(board, color, x, y, tarX, tarY, Short.MAX_VALUE, true, true, true, true, true, false))
			return true;
		if(this.canReachDiagonally(board, color, x, y, tarX, tarY, Short.MAX_VALUE, true, true, true, true, true, false))
			return true;
		
		return false;
	}

	@Override
	public void tick()
	{
		
	}

}
