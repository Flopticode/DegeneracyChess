package chess.listeners;

import chess.Board;
import chess.figure.Figure;

public interface BoardChangedListener
{
	public void notify(Board board, short x, short y, Figure newType);
}
