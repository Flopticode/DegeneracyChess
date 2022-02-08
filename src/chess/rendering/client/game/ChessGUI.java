package chess.rendering.client.game;

import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;

import javax.swing.JPanel;

import chess.ChessGame;

@SuppressWarnings("serial")
public class ChessGUI extends JPanel implements MouseListener
{
	public static final String FRAME_TITLE = "Degeneracy - The Game";
	
	private ChessRenderer renderer;
	private ChessGame game;
	
	private Point lastPressedPt;
	
	public ChessGUI(ChessGame game)
	{
		this.setBounds(0, 0, 500, 500);		
		this.addMouseListener(this);
		
		this.renderer = new ChessRenderer(game.getBoard());
		
		repaint(0, 0, 0, 500, 500);
		
		this.game = game;
	}
	
	public ChessRenderer getRenderer()
	{
		return renderer;
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		renderer.boardRenderer.render(g);
		renderer.effectTableRenderer.render(g);
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
		lastPressedPt = e.getPoint();
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		Point boardCoordsStart = renderer.boardRenderer.pixelToBoardCoords(this.lastPressedPt.x, this.lastPressedPt.y);
		Point boardCoordsEnd = renderer.boardRenderer.pixelToBoardCoords(e.getPoint().x, e.getPoint().y);
		
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
