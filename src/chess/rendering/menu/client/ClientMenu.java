package chess.rendering.menu.client;

import java.util.LinkedList;
import java.util.List;

import chess.rendering.GameWindow;
import chess.rendering.menu.api.Button;
import chess.rendering.menu.api.ClickData;
import chess.rendering.menu.api.Menu;

public class ClientMenu extends Menu
{
	private List<StartClickedListener> startClickedListeners = new LinkedList<>();
	
	public ClientMenu(GameWindow gameWindow)
	{
		super(gameWindow);
		
		this.add(new Button(0, 0, 200, 150, "./images/ui/client/start.png").addOnRelease(this::onClick));
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
