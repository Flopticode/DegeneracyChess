package chess.figure;

import chess.Board;
import chess.Direction;
import chess.GameRule;
import chess.figure.figures.Maxwell;

import java.awt.Point;

public abstract class AbstractFigure implements Figure
{
	protected FigureType type;
	protected FigureColor color;
	protected Board board;
	protected short dryState = Maxwell.ROUNDS_TO_DRY;
	
	protected short x, y;
	
	public AbstractFigure(Board board, FigureType type, FigureColor color, short x, short y)
	{
		this.type = type;
		this.color = color;
		this.x = x;
		this.y = y;
		this.board = board;
	}
	
	@Override
	public FigureType getType()
	{
		return type;
	}
	@Override
	public FigureColor getColor()
	{
		return color;
	}
	@Override
	public void setColor(FigureColor color)
	{
		this.color = color;
	}
	@Override
	public String toString()
	{
		return color+" "+type;
	}
	@Override
	public Boolean isInMovementRange(short tarX, short tarY)
	{
		return GameRule.FIGURES_CAN_JOINK_THEMSELVES.isActive() && x == tarX && y == tarY ? true : null;
	}
	@Override
	public boolean canJoinkFigureType(FigureType type)
	{
		return true;
	}
	@Override
	public void selfJoink()
	{
		this.destroy();
	}
	@Override
	public void setData(Figure figure)
	{
		this.dryState = figure.getDryState();
	}
	@Override
	public boolean dry()
	{
		this.dryState--;
		
		if(dryState == 0)
		{
			this.destroy();
			return true;
		}
		return false;
	}
	@Override
	public short getDryState()
	{
		return this.dryState;
	}
	@Override
	public void onDeath()
	{
		
	}
	@Override
	public void destroy()
	{
		this.board.destroyFigure(x, y);
	}
	
	public void setPosition(Board board, short newX, short newY)
	{
		this.x = newX;
		this.y = newY;
	}
	public void move(Direction direction, short distance)
	{
		Point pt = direction.move(x, y, distance);
		
		this.board.move(board.getGame().getActiveColor(), x, y, (short)pt.x, (short)pt.y);
	}
	public short getX()
	{
		return x;
	}
	public short getY()
	{
		return y;
	}
	
	public abstract void tick();
	public abstract int getAttackStrength();
	public abstract int getDefenseStrength();
}
