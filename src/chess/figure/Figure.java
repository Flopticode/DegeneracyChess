package chess.figure;

import chess.Direction;

public interface Figure
{
	public FigureType getType();
	public FigureColor getColor();
	public void setColor(FigureColor color);
	public Boolean isInMovementRange(short tarX, short tarY);
	public void tick();
	public short getX();
	public short getY();
	public boolean canJoinkFigureType(FigureType type);
	public void selfJoink();
	public void destroy();
	public void setData(Figure figure);
	public void move(Direction direction, short distance);
	public void onDeath();
	public short getDryState();
	public boolean dry();
	public int getAttackStrength();
	public int getDefenseStrength();
}
