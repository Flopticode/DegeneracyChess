package chess.figure;

import chess.Board;

public interface StraightReach
{
	public default boolean canReachStraight(Board board, FigureColor activeColor, short figX, short figY, short tarX, short tarY, short maxLen,
			boolean pCL, boolean pCR, boolean pCT, boolean pCB, boolean allowJoinkFigure, boolean mustJoinkFigure)
	{
		short maxLineLen = (short)Math.max(board.getWidth(), board.getHeight());
		
		boolean cL = pCL, cR = pCR, cT = pCT, cB = pCB;
		
		if(figX != tarX && figY != tarY)
			return false;
		
		if(cT && figY <= tarY)
			cT = false;
		if(cB && figY >= tarY)
			cB = false;
		if(cL && figX <= tarX)
			cL = false;
		if(cR && figX >= tarX)
			cR = false;
		
		
		for(int i = 1; i <= Math.min(maxLen, maxLineLen-1); i++)
		{
			if(cR && board.getWidth() > figX+i && figY == tarY)
			{
				Figure figR = board.getFigureAt((short)(figX+i), figY);
				
				if(figX+i == tarX)
				{
					if(figR != null)
					{
						if(figR.getColor() != activeColor && allowJoinkFigure)
							return true;
						else
							cR = false;
					}
					else if(!mustJoinkFigure)
						return true;
				}
				else if(figR != null)
					return false;
			}
			if(cL && 0 <= figX-i && figY == tarY)
			{
				Figure figL = board.getFigureAt((short)(figX-i), figY);
				
				if(figX-i == tarX)
				{
					if(figL != null)
					{
						if(figL.getColor() != activeColor && allowJoinkFigure)
							return true;
						else
							cL = false;
					}
					else if(!mustJoinkFigure)
						return true;
				}
				else if(figL != null)
					return false;
			}
			if(cT && 0 <= figY-i && figX == tarX)
			{
				Figure figT = board.getFigureAt(figX, (short)(figY-i));
				
				if(figY-i == tarY)
				{
					if(figT != null)
					{
						if(figT.getColor() != activeColor && allowJoinkFigure)
							return true;
						else
							cT = false;
					}
					else if(!mustJoinkFigure)
						return true;
				}
				else if(figT != null)
					return false;
			}
			if(cB && board.getHeight() > figY+i && figX == tarX)
			{
				Figure figB = board.getFigureAt(figX, (short)(figY+i));
				
				if(figY+i == tarY)
				{
					if(figB != null)
					{
						if(figB.getColor() != activeColor && allowJoinkFigure)
							return true;
						else
							cB = false;
					}
					else if(!mustJoinkFigure)
						return true;
				}
				else if(figB != null)
					return false;
			}
			
			if(!cR && !cL && !cT && !cB)
				return false;
			
			if((cR && figX+i == tarX && figY == tarY)
			||(cL && figX-i == tarX && figY == tarY)
			||(cT && figX == tarX && figY-i == tarY)
			||(cB && figX == tarX && figY+i == tarY))
				return true;
			
		}
		
		return false;
	}
}
