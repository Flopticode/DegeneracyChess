package chess;

import java.awt.Point;

public enum Direction
{
	TOPLEFT(1),
	TOP(2),
	TOPRIGHT(3),
	LEFT(4),
	RIGHT(5),
	BOTLEFT(6),
	BOT(7),
	BOTRIGHT(8);
	
	public int id;
	
	private Direction(int id)
	{
		this.id = id;
	}
	
	public static Direction get(int id)
	{
		for(Direction dir : Direction.values())
			if(dir.id == id)
				return dir;
		
		throw new IllegalArgumentException("Invalid direction id " + id + ".");
	}
	public Point move(short x, short y, short l)
	{
		switch(this)
		{
		case BOT:
			return new Point(x, y+l);
		case BOTLEFT:
			return new Point(x-l, y+l);
		case BOTRIGHT:
			return new Point(x+l, y+l);
		case LEFT:
			return new Point(x-l, y);
		case RIGHT:
			return new Point(x+l, y);
		case TOP:
			return new Point(x, y-l);
		case TOPLEFT:
			return new Point(x-l, y-l);
		case TOPRIGHT:
			return new Point(x+l, y-l);
		default:
			throw new IllegalStateException("tf? there's definitely a case missing.");
		}
		
	}
}
