package chess.network.impl.client;

import chess.network.GameStatus;

public interface UpdateGameStatusListener
{
    public void onNewGameStatus(GameStatus status);
}
