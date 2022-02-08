package chess.figure;

import chess.Board;

public interface FigureFactory
{
	public Figure create(Board board, FigureColor color, short x, short y);
}
