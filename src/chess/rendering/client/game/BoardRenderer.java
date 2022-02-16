package chess.rendering.client.game;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;

import chess.Board;
import chess.figure.Figure;
import chess.statuseffect.board.StatusEffect;
import chess.statuseffect.board.StatusEffectType;
import main.Main;

public class BoardRenderer
{
	public static final int CELL_SIZE = 50;
	
	private Board board;
	private ChessRenderer chessRenderer;
	
	private boolean isLightUnplugged = false;
	
	public BoardRenderer(ChessRenderer chessRenderer, Board board)
	{
		this.board = board;
		this.chessRenderer = chessRenderer;
		
		board.addChangedListener(this::onBoardChanged);
		board.addStatusEffectAddedListener(this::onBoardStatusEffectAdded);
		board.addStatusEffectRemovedListener(this::onBoardStatusEffectRemoved);
	}
	
	private void onBoardChanged(Board board, short x, short y, Figure fig)
	{
		Main.FRAME.repaint();
	}
	private void onBoardStatusEffectAdded(StatusEffect statusEffect)
	{
		if(statusEffect.getType() == StatusEffectType.LIGHT_UNPLUGGED)
		{
			updateLightUnplugged();
			Main.FRAME.repaint();
		}
	}
	private void onBoardStatusEffectRemoved(StatusEffect statusEffect)
	{
		if(statusEffect.getType() == StatusEffectType.LIGHT_UNPLUGGED)
		{
			updateLightUnplugged();
			Main.FRAME.repaint();
		}
	}
	private void updateLightUnplugged()
	{
		this.isLightUnplugged = this.board.hasStatusEffect(StatusEffectType.LIGHT_UNPLUGGED);
	}
	
	public void render(Graphics g)
	{
		short w = board.getWidth();
		short h = board.getHeight();
		
		for(short x = 0; x < w; x++)
			for(short y = 0; y < h; y++)
				renderCell(g, x, y, board.getFigureAt(x, y));
	}
	
	private void renderCell(Graphics g, short x, short y, Figure figure)
	{
		if(this.isLightUnplugged)
			g.setColor((x+y) %2 == 0 ? new Color(60, 60, 60) : Color.gray);
		else
			g.setColor((x+y) %2 == 0 ? Color.white : Color.gray);
		
		g.fillRect(x*CELL_SIZE, y*CELL_SIZE, CELL_SIZE, CELL_SIZE);
		
		if(!isLightUnplugged && figure != null)
		{
			g.setColor(Color.RED);
			g.drawImage(BoardRenderer.getImage(figure), x*CELL_SIZE, y*CELL_SIZE, null);
		}
	}
	public Point pixelToBoardCoords(int pixelX, int pixelY)
	{
		return new Point(pixelX / CELL_SIZE, pixelY / CELL_SIZE);
	}
	
	private static Image getImage(Figure figure)
	{
		return figure.getType().getImages().getImageForColor(figure.getColor());
	}
}
