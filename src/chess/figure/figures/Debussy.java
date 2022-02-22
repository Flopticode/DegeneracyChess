package chess.figure.figures;

import chess.Board;
import chess.figure.AbstractInteractingFigure;
import chess.figure.DiagonalReach;
import chess.figure.Figure;
import chess.figure.FigureColor;
import chess.figure.FigureTypes;

public class Debussy extends Bishop implements DiagonalReach, AbstractInteractingFigure
{
	public Debussy(Board board, FigureColor color, short x, short y)
	{
		super(board, FigureTypes.DEBUSSY, color, x, y);
	}
	
	@Override
	public boolean interact(Figure figure)
	{
		if(figure.getColor() == FigureColor.ASIAN)
		{
			this.board.setFigure(figure.getX(), figure.getY(), this.color, figure.getType());
			return true;
		}
		return false;
	}

	@Override
	public boolean isInInteractionRange(short tarX, short tarY)
	{
		return isInMovementRange(tarX, tarY);
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
