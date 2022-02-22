package chess.rendering.client.game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import chess.ChessGame;
import chess.rendering.GameWindow;

public class ChessGUI implements MouseListener
{
	public static final String FRAME_TITLE = "Degeneracy - The Game";
	
	private ChessRenderer renderer;
	private ChessGame game;
	
	private Point lastPressedPt;
	
	public ChessGUI(ChessGame game)
	{
		this.renderer = new ChessRenderer(game.getBoard());
		
		this.game = game;
	}
	
	public ChessRenderer getRenderer()
	{
		return renderer;
	}
	
	public void paint(Graphics g)
	{
		renderer.render(g);
	}
	
	public ChessGame getGame()
	{
		return game;
	}

	@Override
	public void mouseClicked(MouseEvent e)
	{
		
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		Point pt = e.getPoint();
		pt.translate(0, GameWindow.FRAME_TITLE_BAR_HEIGHT);
		
		lastPressedPt = pt;
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		Point pt = e.getPoint();
		pt.translate(0, GameWindow.FRAME_TITLE_BAR_HEIGHT);
		
		Point boardCoordsStart = renderer.boardRenderer.pixelToBoardCoords(this.lastPressedPt.x, this.lastPressedPt.y);
		Point boardCoordsEnd = renderer.boardRenderer.pixelToBoardCoords(pt.x, pt.y);
		
		switch(e.getButton())
		{
		case MouseEvent.BUTTON1:
			game.tryMove((short)boardCoordsStart.x, (short)boardCoordsStart.y, (short)boardCoordsEnd.x, (short)boardCoordsEnd.y);
			break;
		case MouseEvent.BUTTON3:
			game.tryInteract((short)boardCoordsStart.x, (short)boardCoordsStart.y, (short)boardCoordsEnd.x, (short)boardCoordsEnd.y);
			break;
		}
	}

	@Override
	public void mouseEntered(MouseEvent e) {}

	@Override
	public void mouseExited(MouseEvent e) {}
}
