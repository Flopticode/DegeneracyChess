package chess.rendering.client.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.util.LinkedList;

import chess.Board;
import chess.statuseffect.board.StatusEffect;

public class EffectTableRenderer
{
	public static final int STATUS_EFFECT_RENDER_X = 410;
	public static final int STATUS_EFFECT_RENDER_Y = 10;
	
	public static final int TABLE_WIDTH = 250;
	public static final int TABLE_HEIGHT = 380;
	
	public static final int STATUS_EFFECT_RENDER_Y_GAP = 30;
	
	public static final int HEADLINE_HEIGHT = 50;
	
	private Board board;
	
	public EffectTableRenderer(ChessRenderer chessRenderer, Board board)
	{
		this.board = board;
		
		chessRenderer.requestSize(STATUS_EFFECT_RENDER_X + TABLE_WIDTH + 10, STATUS_EFFECT_RENDER_Y + TABLE_HEIGHT);
	}
	
	public void render(Graphics g)
	{
		g.setColor(new Color(40, 40, 40));
		g.fillRect(STATUS_EFFECT_RENDER_X, STATUS_EFFECT_RENDER_Y, TABLE_WIDTH, TABLE_HEIGHT);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 0, 25));
		g.drawString("active effects", STATUS_EFFECT_RENDER_X + 46, STATUS_EFFECT_RENDER_Y + 25);
		
		LinkedList<StatusEffect> effects = board.getStatusEffects();
		
		int curY = STATUS_EFFECT_RENDER_Y + HEADLINE_HEIGHT;
		for(StatusEffect effect : effects)
		{
			effect.getRenderer().render(g, STATUS_EFFECT_RENDER_X + 10, curY);
			curY += STATUS_EFFECT_RENDER_Y_GAP;
		}
	}
}
