package chess.rendering.menu.api;

import java.awt.Graphics;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import chess.rendering.GameWindow;
import chess.rendering.GameWindowElement;

public class Menu extends GameWindowElement
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
	public void onMouseClick(ClickData data)
	{
		if(this.openMenu != null)
			openMenu.onMouseClick(data);
		else
		{
			for(MenuElement e : elements)
				if(e instanceof Clickable c && e.isInBounds(data.x, data.y))
					c.onClick(data);
		}
	}

	@Override
	public void onMousePress(ClickData data)
	{
		if(this.openMenu != null)
			openMenu.onMousePress(data);
		else
		{
			for(MenuElement e : elements)
				if(e instanceof Clickable c && e.isInBounds(data.x, data.y))
					c.onPress(data);
		}
	}

	@Override
	public void onMouseRelease(ClickData data)
	{
		if(this.openMenu != null)
			openMenu.onMouseRelease(data);
		else
		{
			for(MenuElement e : elements)
				if(e instanceof Clickable c && e.isInBounds(data.x, data.y))
					c.onRelease(data);
		}
	}

	@Override
	public void onMouseMotion(MotionData data)
	{
		if(this.openMenu != null)
			openMenu.onMouseMotion(data);
		else
		{
			for(MenuElement e : elements)
				e.onMotion(data);
		}
	}
}
