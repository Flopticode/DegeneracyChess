package chess.network.impl.server.lobby;

import chess.ChessGame;
import chess.Util;
import chess.figure.FigureColor;
import chess.network.GameStatus;
import chess.network.NetworkAddress;
import chess.network.impl.exception.InvalidMessageException;
import chess.network.impl.packet.Packet;
import chess.network.impl.packet.packets.GameStatusPacket;
import chess.network.impl.packet.packets.JoinLobbyPacket;
import chess.network.impl.packet.packets.MovePacket;
import chess.network.impl.packet.packets.StartGamePacket;

import java.util.HashMap;
import java.util.Map;

public abstract class LobbyHandler
{
    public static final int MAX_PLAYER_COUNT = 2;

    private Map<Integer, FigureColor> clientIDColorMap = new HashMap<>();
    private Integer hostID;
    private GameStatus gameStatus = GameStatus.LOBBY;

    private ChessGame game;

    protected boolean join(int clientID, FigureColor color)
    {
        if(clientIDColorMap.size() == MAX_PLAYER_COUNT)
            return false;
        else if(clientIDColorMap.get(clientID) != null)
            return false;

        clientIDColorMap.put(clientID, color);

        if(clientIDColorMap.size() == 1)
            this.hostID = clientID;

        return true;
    }
    protected boolean dejoin(int clientID)
    {
        return clientIDColorMap.remove(clientID) != null;
    }

    private int lastClientID = 0;
    protected int getNextClientID()
    {
        return ++lastClientID;
    }
    protected boolean handlePacket(int clientID, Packet p)
    {
        if(p instanceof StartGamePacket startGamePacket)
            return handleStartGamePacket(clientID, startGamePacket);
        else if(p instanceof MovePacket movePacket)
            return handleMovePacket(clientID, movePacket);

        System.err.println("Packet type " + p.getClass().getName() + " is not allowed.");
        return false;
    }

    private boolean isHost(int clientID)
    {
        return hostID == clientID;
    }
    private FigureColor getColor(int clientID)
    {
        return clientIDColorMap.get(clientID);
    }

    private boolean handleStartGamePacket(int clientID, StartGamePacket p)
    {
        if(isHost(clientID) && this.clientIDColorMap.size() == MAX_PLAYER_COUNT)
        {
            System.out.println("Info: Game started");

            gameStatus = GameStatus.STARTED;

            game = new ChessGame();

            broadcastPacket(new GameStatusPacket(gameStatus));

            return true;
        }
        else
        {
            System.err.println("ERROR: Starting game failed.");
            return false;
        }
    }
    private boolean handleMovePacket(int clientID, MovePacket pck)
    {
        FigureColor activeColor = game.getActiveColor();
        FigureColor clientColor = getColor(clientID);

        switch(pck.mode)
        {
            case MOVE:
                return clientColor == activeColor ? game.tryMove(pck.srcX, pck.srcY, pck.tarX, pck.tarY) : false;
            case INTERACT:
                return clientColor == activeColor ? game.tryInteract(pck.srcX, pck.srcY, pck.tarX, pck.tarY) : false;
            default:
                System.err.println("Invalid move packet received (invalid InteractionType provided)");
                return false; // Invalid interaction type
        }
    }

    protected abstract void broadcastPacket(Packet p);
}
