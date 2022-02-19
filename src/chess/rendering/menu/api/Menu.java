package chess.rendering.menu.api;

import java.awt.Graphics;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import chess.rendering.GameWindow;
import chess.rendering.GameWindowElement;
import main.Main;

public class Menu extends GameWindowElement implements MouseListener, MouseMotionListener
{
	private List<MenuElement> elements = new LinkedList<>();
	private Map<String, Menu> subMenus = new HashMap<>();
	private List<OpenListener> onOpenListeners = new LinkedList<>();
	private Menu openMenu = null;
	private Menu openedBy = null;
	
	public Menu(GameWindow window)
	{
		super(window);
		
	}
	
	public void add(MenuElement e)
	{
		elements.add(e);	
	}
	public void remove(MenuElement e)
	{
		elements.remove(e);
	}
	
	public void addOpenListener(OpenListener openListener)
	{
		this.onOpenListeners.add(openListener);
	}
	public void removeOpenListener(OpenListener openListener)
	{
		this.onOpenListeners.remove(openListener);
	}
	private void fireOpenListeners()
	{
		for(OpenListener l : onOpenListeners)
			l.onOpen();
	}
	
	protected Menu getParentMenu()
	{
		return this.openedBy;
	}
	public void addMenu(String name, Menu subMenu)
	{
		subMenus.put(name, subMenu);
	}
	public boolean removeMenu(String name)
	{
		return subMenus.remove(name) != null;
	}
	public void openMenu(String name)
	{
		this.openMenu = subMenus.get(name);
		
		if(openMenu == null)
			throw new NullPointerException("Could not open menu \"" + name + "\": Not found.");
		
		this.openMenu.openedBy = this;
		
		fireOpenListeners();
		
		this.repaint();
	}
	public void close()
	{
		if(this.openedBy == null)
			throw new NullPointerException("Could not close menu as it was not opened by another menu.");
		
		this.openedBy.openMenu = null;
		this.openedBy.repaint();
		this.openedBy = null;
	}
	
	@Override
	public void paint(Graphics g)
	{
		if(this.openMenu != null)
			openMenu.paint(g);
		else
		{
			for(MenuElement e : elements)
				e.render(g);
		}
	}

	@Override
	public void mouseClicked(MouseEvent evt)
	{
		System.out.println("Clicked");
		if(this.openMenu != null)
			openMenu.mouseClicked(evt);
		else
		{
			ClickData data = new ClickData(evt.getX(), evt.getY()+Main.FRAME_TITLE_BAR_HEIGHT, evt.getButton());
			for(MenuElement e : elements)
				if(e instanceof Clickable c && e.isInBounds(data.x, data.y))
					c.onClick(data);
		}
	}

	@Override
	public void mousePressed(MouseEvent evt)
	{
		System.out.println("Pressed");
		if(this.openMenu != null)
			openMenu.mousePressed(evt);
		else
		{
			ClickData data = new ClickData(evt.getX(), evt.getY()+Main.FRAME_TITLE_BAR_HEIGHT, evt.getButton());
			for(MenuElement e : elements)
				if(e instanceof Clickable c && e.isInBounds(data.x, data.y))
					c.onPress(data);
		}
	}

	@Override
	public void mouseReleased(MouseEvent evt)
	{
		System.out.println("Released");
		if(this.openMenu != null)
			openMenu.mouseReleased(evt);
		else
		{
			ClickData data = new ClickData(evt.getX(), evt.getY()+Main.FRAME_TITLE_BAR_HEIGHT, evt.getButton());
			for(MenuElement e : elements)
				if(e instanceof Clickable c && e.isInBounds(data.x, data.y))
					c.onRelease(data);
		}
	}

	@Override
	public void mouseMoved(MouseEvent evt)
	{
		if(this.openMenu != null)
			openMenu.mouseMoved(evt);
		else
		{
			MotionData data = new MotionData(evt.getX(), evt.getY()+Main.FRAME_TITLE_BAR_HEIGHT);
			for(MenuElement e : elements)
				e.onMotion(data);
		}
	}
	
	@Override
	public void mouseEntered(MouseEvent evt) { }

	@Override
	public void mouseExited(MouseEvent evt) { }

	@Override
	public void mouseDragged(MouseEvent evt)
	{
		this.mouseClicked(evt);
	}
}
