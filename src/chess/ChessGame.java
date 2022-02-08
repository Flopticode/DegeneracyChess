package chess;

import java.io.File;
import java.io.IOException;

import chess.figure.FigureColor;
import chess.rendering.client.game.ChessGUI;
import chess.sound.SoundManager;

public class ChessGame
{
	public static final float FIGURE_RESPAWN_PROBABILITY = 0.25f;
	public static final float FIGURE_ASIAN_AT_RESPAWN_PROBABILITY = 0.5f;
	public static final float FIGHT_BACK_PROBABILITY = 0.5f;
	
	private Board board;
	private FigureColor activeColor;
	private ChessGUI gui;
	private SoundManager soundManager;
	
	public ChessGame()
	{
		this.soundManager = new SoundManager();
		this.soundManager.play();
		this.activeColor = FigureColor.WHITE;
		
		try
		{
			this.board = new Board(this, new File("./data/standard.board"));
		}
		catch(IOException ioe)
		{
			new IOException("Couldn't read board file.").printStackTrace();
			ioe.printStackTrace();
			System.exit(1);
		}
		
		this.gui = new ChessGUI(this);
	}
	
	public boolean tryInteract(short x, short y, short tX, short tY)
	{
		if(board.isInBounds(x, y) && board.isInBounds(tX, tY) && interact(x, y, tX, tY))
		{
			this.endTurn();
			return true;
		}
		return false;
	}
	private boolean interact(short x, short y, short tX, short tY)
	{
		return this.board.interact(x, y, tX, tY);
	}
	
	public boolean tryMove(short figX, short figY, short tarX, short tarY)
	{
		if(board.isInBounds(figX, figY) && board.isInBounds(tarX, tarY) && move(figX, figY, tarX, tarY))
		{
			this.endTurn();
			return true;
		}
		return false;
	}
	private boolean move(short x, short y, short tX, short tY)
	{
		System.out.print("Player " + activeColor + " moving: (" + x + "|" + y + ") to (" + tX + "|" + tY + ")");
		
		boolean moveSucceed = this.board.move(activeColor, x, y, tX, tY);
		
		if(!moveSucceed)
			System.out.println(", but that idiot didn't realise that this is an illegal move... lmao?");
		else
			System.out.println(".");
		
		return moveSucceed;
			
	}
	public void endTurn()
	{
		this.board.tick();
		activeColor = activeColor.getOpponent();
	}
	public Board getBoard()
	{
		return this.board;
	}
	public ChessGUI getGUI()
	{
		return this.gui;
	}
	public FigureColor getActiveColor()
	{
		return activeColor;
	}
}
