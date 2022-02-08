package chess.figure;

import chess.Board;

public interface DiagonalReach
{
	public default boolean canReachDiagonally(Board board, FigureColor activeColor, short figX, short figY, short tarX, short tarY, short maxLen,
			boolean pCTL, boolean pCTR, boolean pCBL, boolean pCBR, boolean canJoinkFigure, boolean mustJoinkFigure)
	{
		short maxDiagLineLen = (short)Math.min(board.getWidth(), board.getHeight());
		
		boolean cTL = pCTL, cTR = pCTR, cBL = pCBL, cBR = pCBR;
		
		for(int i = 1; i <= Math.min(maxLen, maxDiagLineLen); i++)
		{
			if(cBR && board.getWidth() > figX+i && board.getHeight() > figY+i)
			{
				Figure figBR = board.getFigureAt((short)(figX+i), (short)(figY+i));
				
				if(figBR != null && canJoinkFigure)
				{
					if(cBR && figX+i == tarX && figY+i == tarY && figBR.getColor() != activeColor)
						return true;
					else if(!mustJoinkFigure)
						cBR = false;
				}
				if(figBR != null)
					cBR = false;
			}
			if(cTR && board.getWidth() > figX+i && 0 <= figY-i)
			{
				Figure figTR = board.getFigureAt((short)(figX+i), (short)(figY-i));
				
				if(figTR != null && canJoinkFigure)
				{
					if(cTR && figX+i == tarX && figY-i == tarY && figTR.getColor() != activeColor)
						return true;
					else if(!mustJoinkFigure)
						cTR = false;
				}
				if(figTR != null)
					cTR = false;
			}
			if(cBL && 0 <= figX-i && board.getHeight() > figY+i)
			{
				Figure figBL = board.getFigureAt((short)(figX-i), (short)(figY+i));
				
				if(figBL != null && canJoinkFigure)
				{
					if(cBL && figX-i == tarX && figY+i == tarY && figBL.getColor() != activeColor)
						return true;
					else if(!mustJoinkFigure)
						cBL = false;
				}
				if(figBL != null)
					cBL = false;
			}
			if(cTL && 0 <= figX-i && 0 <= figY-i)
			{
				Figure figTL = board.getFigureAt((short)(figX-i), (short)(figY-i));
				
				if(figTL != null && canJoinkFigure)
				{
					if(cTL && figX-i == tarX && figY-i == tarY && figTL.getColor() != activeColor)
						return true;
					else if(!mustJoinkFigure)
						cTL = false;
				}
				if(figTL != null)
					cTL = false;
			}
			
			if(!cBR && !cTR && !cBL && !cTL)
				return false;
			
			if((cBR && figX+i == tarX && figY+i == tarY)
			||(cTR && figX+i == tarX && figY-i == tarY)
			||(cBL && figX-i == tarX && figY+i == tarY)
			||(cTL && figX-i == tarX && figY-i == tarY))
				return true;
			
		}
		
		return false;
	}
}
