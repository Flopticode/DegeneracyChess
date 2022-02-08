package chess.listeners;

import chess.figure.Figure;

public interface FigureMovedListener
{
	public void onFigureMoved(short figX, short figY, short tarX, short tarY, Figure figure);
}
