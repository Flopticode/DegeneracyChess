package chess;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.LinkedList;
import java.util.List;

import chess.figure.AbstractInteractingFigure;
import chess.figure.Figure;
import chess.figure.FigureColor;
import chess.figure.FigureType;
import chess.figure.FigureTypes;
import chess.listeners.BoardChangedListener;
import chess.listeners.FigureMovedListener;
import chess.listeners.StatusEffectAddedListener;
import chess.listeners.StatusEffectRemovedListener;
import chess.statuseffect.board.StatusEffect;
import chess.statuseffect.board.StatusEffectType;

public class Board
{
	private static final int BOARD_WIDTH = 8;
	private static final int BOARD_HEIGHT = 8;
	
	private LinkedList<StatusEffect> statusEffects = new LinkedList<>();
	
	private Figure[][] board = new Figure[BOARD_WIDTH][BOARD_HEIGHT];
	
	private List<Figure> beatenFigures = new LinkedList<>();
	
	private final List<BoardChangedListener> changedListeners = new LinkedList<>();
	private final List<FigureMovedListener> movedListeners = new LinkedList<>();
	private final List<StatusEffectAddedListener> statusEffectAddedListeners = new LinkedList<>();
	private final List<StatusEffectRemovedListener> statusEffectRemovedListeners = new LinkedList<>();
	private final ChessGame game;
	
	public Board(ChessGame game, File boardFile) throws IOException
	{
		this.game = game;
		
		BufferedReader br = new BufferedReader(new FileReader(boardFile));
		short curRow = 0;
		short curCol = 0;
		
		String curID = "";
		
		while(br.ready())
		{
			
			char c = (char)br.read();
			
			if(c == '\n')
			{
				curRow++;
				curCol = 0;
				curID = "";
			}
			else if(c == ',')
			{
				try
				{
					FigureColor color = curID.substring(0, 2).equals("B_") ? FigureColor.BLACK : curID.substring(0, 2).equals("W_") ? FigureColor.WHITE : FigureColor.ASIAN;
					
					setFigure(curCol, curRow, color, FigureTypes.get(curID.substring(2)));
				}
				catch(NullPointerException npe)
				{
					br.close();
					throw new IllegalArgumentException("The given boardFile is invalid at position (" + curCol + "|" + curRow + ").");
				}
				
				curID = "";
				curCol++;
			}
			else
			{
				curID += c;
			}
		}
		br.close();
	}
	
	public void setFigure(short x, short y, FigureColor color, FigureType type)
	{
		board[x][y] = type.createNew(this, color, x, y);
		
		this.fireChangedListeners(this, x, y, board[x][y]);
	}
	public void destroyFigure(short x, short y)
	{
		if(board[x][y] != null)
		{
			this.beatenFigures.add(board[x][y]);
			board[x][y].onDeath();
			board[x][y] = null;
			
			this.fireChangedListeners(this, x, y, null);
		}
	}
	
	public boolean isInBounds(short x, short y)
	{
		short w = getWidth();
		short h = getHeight();
		
		return x >= 0 && x < w && y >= 0 && y < h;
	}
	
	public Figure getFigureAt(short x, short y)
	{
		return board[x][y];
	}
	public LinkedList<Figure> getFiguresInRadius(short cX, short cY, short radius)
	{
		LinkedList<Figure> figures = new LinkedList<>();
		
		for(short x = (short)(Math.max(0, cX-radius)); x < Math.min(getWidth(), cX+radius); x++)
			for(short y = (short)(Math.max(0, cY-radius)); y < Math.min(getHeight(), cY+radius); y++)
				if(Math.abs(cX-x)+Math.abs(cY-y) <= radius)
				{
					Figure fig = this.getFigureAt(x, y);
					if(fig != null)
						figures.add(fig);
				}
		
		return figures;
	}
	
	public boolean interact(short x, short y, short tX, short tY)
	{
		Figure fig = this.getFigureAt(x, y);
		
		if(fig != null && fig instanceof AbstractInteractingFigure interactingFig)
		{
			Figure interactionFig = this.getFigureAt(tX, tY);
			
			if(!interactingFig.isInInteractionRange(x, y) || interactionFig == null)
				return false;
			
			return interactingFig.interact(interactionFig);
		}
		return false;
	}
	
	public boolean isValidMove(FigureColor activeColor, short figureX, short figureY, short targetX, short targetY)
	{
		if(figureX == targetX && figureY == targetY)
			return true;
		
		Boolean inMovementRange = board[figureX][figureY].isInMovementRange(targetX, targetY);
		if(inMovementRange != null)
			return inMovementRange;
		
		Figure tarFig = board[targetX][targetY];
		
		if(tarFig != null && !board[figureX][figureY].canJoinkFigureType(tarFig.getType()))
			return false;
			
		
		return true;
	}
	public boolean isFigureAt(short x, short y)
	{
		return this.getFigureAt(x, y) != null;
	}
	public boolean move(FigureColor activeColor, short figureX, short figureY, short targetX, short targetY)
	{
		FigureColor colorAtOrigin = this.getColorAt(figureX, figureY);
		
		if(colorAtOrigin != this.game.getActiveColor() && colorAtOrigin != FigureColor.ASIAN)
		{
			System.out.println(", but that idiot didn't realise that this is an illegal move... lmao?");
			return false;
		}
		
		if(!isValidMove(activeColor, figureX, figureY, targetX, targetY))
			return false;
		
		Figure tarFig = this.getFigureAt(targetX, targetY);
		Figure fig = this.getFigureAt(figureX, figureY);
		
		if(tarFig != null)
		{
			if(Util.randomInt(0, tarFig.getDefenseStrength()) > Util.randomInt(0, fig.getAttackStrength()))
			{
				this.destroyFigure(figureX, figureY);
				this.setFigure(figureX, figureY, tarFig.getColor(), tarFig.getType());
				this.getFigureAt(figureX, figureY).setData(tarFig);
				this.board[targetX][targetY] = null;
				
				fireFigureMovedListeners(targetX, targetY, figureX, figureY, board[figureX][figureY]);
			}
			else
			{
				this.destroyFigure(targetX, targetY);
				this.setFigure(targetX, targetY, fig.getColor(), fig.getType());
				this.getFigureAt(targetX, targetY).setData(fig);
				this.board[figureX][figureY] = null;
				
				fireFigureMovedListeners(figureX, figureY, targetX, targetY, board[targetX][targetY]);
			}
		}
		else
		{
			this.setFigure(targetX, targetY, fig.getColor(), fig.getType());
			this.getFigureAt(targetX, targetY).setData(fig);
			this.board[figureX][figureY] = null;
			
			fireFigureMovedListeners(figureX, figureY, targetX, targetY, board[targetX][targetY]);
		}
		
		return true;
	}
	public FigureColor getColorAt(short x, short y)
	{
		return board[x][y] == null ? null : board[x][y].getColor();
	}
	public short getWidth()
	{
		return (short)board.length;
	}
	public short getHeight()
	{
		return (short)board[0].length;
	}
	
	public Short getPawnBaseIndex(FigureColor color)
	{
		switch(color)
		{
		case WHITE:
			return (Short)(short)(getHeight()-2);
		case BLACK:
			return 1;
		case ASIAN:
			return null;
		default:
			throw new IllegalArgumentException("Asians have no base.");
		}
	}
	
	private void fireChangedListeners(Board board, short x, short y, Figure newFig)
	{
		for(BoardChangedListener curListener : changedListeners)
			curListener.notify(board, x, y, newFig);
	}
	public void addChangedListener(BoardChangedListener listener)
	{
		changedListeners.add(listener);
	}
	public void removeChangedListener(BoardChangedListener listener)
	{
		changedListeners.remove(listener);
	}
	
	private void fireStatusEffectAddedListeners(StatusEffect statusEffect)
	{
		for(StatusEffectAddedListener curListener : statusEffectAddedListeners)
			curListener.notify(statusEffect);
	}
	public void addStatusEffectAddedListener(StatusEffectAddedListener listener)
	{
		this.statusEffectAddedListeners.add(listener);
	}
	public void removeStatusEffectAddedListener(StatusEffectAddedListener listener)
	{
		this.statusEffectAddedListeners.remove(listener);
	}
	private void fireStatusEffectRemovedListeners(StatusEffect statusEffect)
	{
		for(StatusEffectRemovedListener curListener : statusEffectRemovedListeners)
			curListener.notify(statusEffect);
	}
	public void addStatusEffectRemovedListener(StatusEffectRemovedListener listener)
	{
		this.statusEffectRemovedListeners.add(listener);
	}
	public void removeStatusEffectRemovedListener(StatusEffectRemovedListener listener)
	{
		this.statusEffectRemovedListeners.remove(listener);
	}
	public void addStatusEffect(StatusEffect statusEffect)
	{
		statusEffects.add(statusEffect);
		fireStatusEffectAddedListeners(statusEffect);
	}
	
	private void fireFigureMovedListeners(short x, short y, short tX, short tY, Figure fig)
	{
		for(FigureMovedListener curListener : movedListeners)
			curListener.onFigureMoved(x, y, tX, tY, fig);
	}
	public void addFigureMovedListener(FigureMovedListener listener)
	{
		this.movedListeners.add(listener);
	}
	public void removeFigureMovedListener(FigureMovedListener listener)
	{
		this.movedListeners.remove(listener);
	}
	
	@SuppressWarnings("unchecked")
	public LinkedList<StatusEffect> getStatusEffects()
	{
		return (LinkedList<StatusEffect>)this.statusEffects.clone();
	}
	public void clearStatusEffects()
	{
		while(!statusEffects.isEmpty())
		{
			StatusEffect curEffect = statusEffects.get(0);
			
			statusEffects.remove(curEffect);
			this.fireStatusEffectRemovedListeners(curEffect);
		}
	}
	public boolean hasStatusEffect(StatusEffectType type)
	{
		for(StatusEffect statusEffect : statusEffects)
			if(statusEffect.getType() == type)
				return true;
		return false;
	}
	
	public ChessGame getGame()
	{
		return game;
	}
	
	public void tick()
	{
		/* Tick status effects */
		for(int i = 0; i < statusEffects.size(); i++)
		{
			StatusEffect effect = statusEffects.get(i);
			effect.tick();
			
			if(effect.hasEnded())
			{
				statusEffects.remove(effect);
				this.fireStatusEffectRemovedListeners(effect);
				i--;
			}
		}
		
		/* Tick figures */
		for(short x = 0; x < this.getWidth(); x++)
			for(short y = 0; y < this.getHeight(); y++)
			{
				Figure curFig = this.getFigureAt(x, y);
				
				if(curFig != null)
				{
					curFig.tick();
				}
			}
		
		/* Respawn figures */
		if(!beatenFigures.isEmpty() && Util.randomBoolean(ChessGame.FIGURE_RESPAWN_PROBABILITY))
		{
			Figure fig = beatenFigures.get(Util.randomInt(0, beatenFigures.size()-1));
			boolean asian = fig.getColor() == FigureColor.ASIAN ? true : Util.randomBoolean(ChessGame.FIGURE_ASIAN_AT_RESPAWN_PROBABILITY);
			
			if(asian)
				fig.setColor(FigureColor.ASIAN);
			
			short x = (short)Util.randomInt(0, getWidth()-1);
			short y = (short)Util.randomInt(0, getHeight()-1);
			
			while(this.isFigureAt(x, y))
			{
				x = (short)Util.randomInt(0, getWidth()-1);
				y = (short)Util.randomInt(0, getHeight()-1);
			}
			
			this.setFigure(x, y, fig.getColor(), fig.getType());
			
			beatenFigures.remove(fig);
		}
	}
}