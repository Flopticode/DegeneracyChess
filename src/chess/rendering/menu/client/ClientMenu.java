package chess.rendering.menu.client;

import chess.rendering.menu.api.Button;
import chess.rendering.menu.api.ClickData;
import chess.rendering.menu.api.Menu;

import java.util.LinkedList;
import java.util.List;

@SuppressWarnings("serial")
public class ClientMenu extends Menu
{
	private List<StartClickedListener> startClickedListeners = new LinkedList<>();
	
	public ClientMenu(int x, int y, int width, int height)
	{
		super(x, y, width, height);
		
		this.add(new Button(0, 0, 200, 150, "./images/ui/client/start.png").addOnClick(this::onClick));
	}
	
	public void addStartClickedListener(StartClickedListener l)
	{
		startClickedListeners.add(l);
	}
	public boolean removeStartClickedListener(StartClickedListener l)
	{
		return startClickedListeners.remove(l);
	}
	private void fireStartClickedListeners()
	{
		for(StartClickedListener l : startClickedListeners)
			l.onStartClicked();
	}
	
	public void onClick(ClickData data)
	{
		fireStartClickedListeners();
	}
}
