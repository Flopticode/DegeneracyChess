package chess;

import chess.network.GameStatus;

public interface UpdateGameStatusListener
{
    public void onNewGameStatus(GameStatus status);
}
