package chess.figure.figures;

import java.util.LinkedList;
import java.util.List;

import chess.Board;
import chess.figure.AbstractFigure;
import chess.figure.AbstractInteractingFigure;
import chess.figure.DiagonalReach;
import chess.figure.Figure;
import chess.figure.FigureColor;
import chess.figure.FigureType;
import chess.figure.FigureTypes;
import chess.figure.StraightReach;

public class Maxwell extends AbstractFigure implements DiagonalReach, StraightReach, AbstractInteractingFigure
{
	public static final short MAXWELL_DRY_RANGE = 3;
	public static final short ROUNDS_TO_DRY = 3;
	
	private Figure focussedFigure = null;
	
	public Maxwell(Board board, FigureColor color, short x, short y)
	{
		super(board, FigureTypes.MAXWELL, color, x, y);
		
		board.addFigureMovedListener(this::onFigureMoved);
	}

	@Override
	public Boolean isInMovementRange(short tarX, short tarY)
	{
		Boolean superCanJoink = super.isInMovementRange(tarX, tarY); 
		if(superCanJoink != null)
			return superCanJoink;
		
		FigureColor color = board.getGame().getActiveColor();
		
		return this.canReachDiagonally(board, color, x, y, tarX, tarY, (short)1, true, true, true, true, true, false)
				|| this.canReachStraight(board, color, x, y, tarX, tarY, (short)1, true, true, true, true, true, false);
	}
	
	public void onFigureMoved(short x, short y, short tarX, short tarY, Figure fig)
	{
		if(fig == this.focussedFigure && !this.isInDryRange(tarX, tarY))
			focussedFigure = null;
	}
	
	@Override
	public void tick()
	{
		List<Figure> figuresToDry;
		
		if(focussedFigure == null)
			figuresToDry = getFiguresInDryRadius();
		else
		{
			figuresToDry = List.<Figure>of(focussedFigure);
		}
		
		for(Figure fig : figuresToDry)
		{
			if(fig.getColor() == this.color || (fig != focussedFigure && fig.getType() != FigureTypes.PAWN))
				continue;
			
			boolean died = fig.dry();
			
			if(died)
				System.out.println("Maxwell dried the figure at (" + fig.getX() + "|" + fig.getY() + ")");
				
		}
	}
	
	private LinkedList<Figure> getFiguresInDryRadius()
	{
		return this.board.getFiguresInRadius(x, y, MAXWELL_DRY_RANGE);
	}

	public boolean isInDryRange(short x, short y)
	{
		return x >= this.x-MAXWELL_DRY_RANGE && x <= this.y + MAXWELL_DRY_RANGE
			&& y >= this.y-MAXWELL_DRY_RANGE && y <= this.y + MAXWELL_DRY_RANGE;
	}
	
	@Override
	public boolean isInInteractionRange(short tarX, short tarY)
	{
		return isInDryRange(x, y);
	}

	@Override
	public boolean interact(Figure fig)
	{
		if(isInDryRange(fig.getX(), fig.getY()) && fig.getType() != FigureTypes.MAXWELL)
		{
			focussedFigure = fig;
			System.out.println("Maxwell is now focussing figure at (" + fig.getX() + "|" + fig.getY() + ").");
			return true;
		}
		return false;
	}
	
	@Override
	public boolean canJoinkFigureType(FigureType type)
	{
		return type != FigureTypes.MAXWELL;
	}
	@Override
	public void onDeath()
	{
		super.onDeath();
		
		this.focussedFigure = null;
	}
	@Override
	public void selfJoink()
	{
		/*Direction direction;
		while(board.getFigureByDirection(direction = Direction.get(Util.randomInt(0, 7)), 1) != null) { }
		
		this.move(direction, 1);*/
	}

	@Override
	public int getAttackStrength()
	{
		return 11;
	}
	@Override
	public int getDefenseStrength()
	{
		return 9;
	}
}
