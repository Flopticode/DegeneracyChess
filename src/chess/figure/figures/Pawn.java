package chess.figure.figures;

import chess.Board;
import chess.figure.AbstractFigure;
import chess.figure.DiagonalReach;
import chess.figure.FigureColor;
import chess.figure.FigureType;
import chess.figure.FigureTypes;
import chess.figure.StraightReach;

public class Pawn extends AbstractFigure implements StraightReach, DiagonalReach
{
	protected Pawn(Board board, FigureType type, FigureColor color, short x, short y)
	{
		super(board, type, color, x, y);
	}
	public Pawn(Board board, FigureColor color, short x, short y)
	{
		this(board, FigureTypes.PAWN, color, x, y);
	}

	@Override
	public Boolean isInMovementRange(short tarX, short tarY)
	{
		Boolean superCanJoink = super.isInMovementRange(tarX, tarY); 
		if(superCanJoink != null)
			return superCanJoink;
		
		FigureColor color = board.getGame().getActiveColor();
		
		if(color != FigureColor.ASIAN && y == board.getPawnBaseIndex(color) && this.canReachStraight(board, color, x, y, tarX, tarY, (short)2, false, false, color == FigureColor.WHITE, color == FigureColor.BLACK, false, false))
			return true;
		else if(this.canReachStraight(board, color, x, y, tarX, tarY, (short)1, false, false, color == FigureColor.WHITE, color == FigureColor.BLACK, false, false))
			return true;
		
		return this.canReachDiagonally(board, color, x, y, tarX, tarY, (short)1, color == FigureColor.WHITE, color == FigureColor.WHITE, color == FigureColor.BLACK, color == FigureColor.BLACK, true, true);
	}
	
	@Override
	public void tick()
	{
		
	}
	@Override
	public int getAttackStrength()
	{
		return 3;
	}
	@Override
	public int getDefenseStrength()
	{
		return 2;
	}
}
