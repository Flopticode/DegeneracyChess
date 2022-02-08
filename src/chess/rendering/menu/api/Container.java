package chess.rendering.menu.api;

import java.util.LinkedList;
import java.util.List;

public abstract class Container extends MenuElement
{
	protected List<MenuElement> children = new LinkedList<>();
	
	public Container(int x, int y, int widthRequest, int heightRequest)
	{
		super(x, y, widthRequest, heightRequest);
	}
	
	public void add(MenuElement e)
	{
		children.add(e);
		
		updateActualWidth();
		updateActualHeight();
	}
	public void remove(MenuElement e)
	{
		children.remove(e);
	}
	public boolean isChild(MenuElement e)
	{
		return children.contains(e);
	}
}
