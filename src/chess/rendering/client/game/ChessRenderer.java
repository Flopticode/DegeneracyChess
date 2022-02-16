package chess.rendering.client.game;

import java.awt.Graphics;

import chess.Board;

public class ChessRenderer
{
	public static final int FRAME_BORDER_WIDTH = 15;
	public static final int FRAME_BORDER_HEIGHT = 39;
	
	public BoardRenderer boardRenderer;
	public EffectTableRenderer effectTableRenderer;
	
	public ChessRenderer(Board board)
	{
		boardRenderer = new BoardRenderer(this, board);
		effectTableRenderer = new EffectTableRenderer(this, board);
	}
	
	
	public void render(Graphics g)
	{
		boardRenderer.render(g);
		effectTableRenderer.render(g);
	}
}
