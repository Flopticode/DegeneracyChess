package chess.rendering.client.game;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;

import javax.swing.JPanel;

import chess.Board;

@SuppressWarnings("serial")
public class ChessRenderer extends JPanel
{
	public static final int FRAME_BORDER_WIDTH = 15;
	public static final int FRAME_BORDER_HEIGHT = 39;
	
	public BoardRenderer boardRenderer;
	public EffectTableRenderer effectTableRenderer;
	
	public ChessRenderer(Board board)
	{
		boardRenderer = new BoardRenderer(this, board);
		effectTableRenderer = new EffectTableRenderer(this, board);
		
		Dimension minSize = this.getMinimumSize();
		this.setBounds(0, 0, FRAME_BORDER_WIDTH+minSize.width, FRAME_BORDER_HEIGHT+minSize.height);
	}
	
	public void requestSize(int w, int h)
	{
		Dimension minSize = this.getMinimumSize();
		this.setMinimumSize(new Dimension(Math.max(minSize.width, w), Math.max(minSize.height, h)));
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		g.setColor(Color.black);
		g.fillRect(0, 0, getWidth(), getHeight());
		
		boardRenderer.render(g);
		effectTableRenderer.render(g);
	}
}
