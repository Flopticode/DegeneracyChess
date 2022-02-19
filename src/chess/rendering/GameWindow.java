package chess.rendering;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import chess.rendering.menu.api.ClickData;
import main.Main;

@SuppressWarnings("serial")
public class GameWindow extends JFrame implements MouseListener
{
	public static final int FRAME_X = 400;
	public static final int FRAME_Y = 400;
	public static final int FRAME_WIDTH = 450;
	public static final int FRAME_HEIGHT = 400;
	
	public static final Font DEFAULT_FONT = new Font("Arial", 0, 40);
	public static final String WINDOW_TITLE = "Degeneracy - The Game";

	public Map<String, GameWindowElement> nameElementMap = new HashMap<>();
	public GameWindowElement activeElement = null;
	
	public GameWindow()
	{
		super(WINDOW_TITLE);
		
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		addMouseListener(this);
		setLayout(null);
		setBounds(FRAME_X, FRAME_Y, FRAME_WIDTH, FRAME_HEIGHT);
	}

	private static ClickData createClickData(MouseEvent e)
	{
		return new ClickData(e.getX(), e.getY()+Main.FRAME_TITLE_BAR_HEIGHT, e.getButton());
	}
	
	public void register(String name, GameWindowElement element)
	{
		nameElementMap.put(name, element);
		
		if(activeElement == null)
			activeElement = element;
	}
	public void show(String name)
	{
		activeElement = nameElementMap.get(name);
	}
	
	@Override
	public void paint(Graphics g)
	{
		if(activeElement == null)
		{
			g.setColor(Color.black);
			g.fillRect(0, 0, getWidth(), getHeight());
			g.setColor(Color.white);
			g.setFont(DEFAULT_FONT);
			g.drawString("Nothing to render...", getWidth() / 2 - 150, getHeight()/3);
		}
		else
		{
			g.setColor(Color.white);
			g.fillRect(0, 0, getWidth(), getHeight());
			activeElement.paint(g);
		}
	}
	
	@Override
	public void mouseClicked(MouseEvent e)
	{
		activeElement.onMouseClick(createClickData(e));
	}

	@Override
	public void mousePressed(MouseEvent e)
	{
		activeElement.onMousePress(createClickData(e));
	}

	@Override
	public void mouseReleased(MouseEvent e)
	{
		activeElement.onMouseRelease(createClickData(e));
	}

	@Override
	public void mouseEntered(MouseEvent e) { }

	@Override
	public void mouseExited(MouseEvent e) { }

}