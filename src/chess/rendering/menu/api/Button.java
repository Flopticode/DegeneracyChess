package chess.rendering.menu.api;

import java.awt.Graphics;
import java.awt.Image;
import java.util.LinkedList;
import java.util.List;
import java.util.function.Consumer;

import chess.rendering.ImageProvider;

public class Button extends MenuElement implements Clickable
{
	private Image image;
	private List<Consumer<ClickData>> onClick = new LinkedList<>();
	private List<Consumer<ClickData>> onPress = new LinkedList<>();
	private List<Consumer<ClickData>> onRelease = new LinkedList<>();
	
	public Button(int x, int y, int width, int height, Image image)
	{
		super(x, y, width, height);
		
		this.image = image;
		
		if(width != image.getWidth(null) || height != image.getHeight(null))
			image = image.getScaledInstance(width, height, Image.SCALE_SMOOTH);
	}
	public Button(int x, int y, int width, int height, String path)
	{
		this(x, y, ImageProvider.get(path, width, height));
	}
	public Button(int x, int y, Image image)
	{
		this(x, y, image.getWidth(null), image.getHeight(null), image);
	}
	public Button(int x, int y, String path)
	{
		this(x, y, ImageProvider.get(path));
	}
	
	public void setImage(Image image)
	{
		this.image = image;
	}
	public Button addOnClick(Consumer<ClickData> c)
	{
		onClick.add(c);
		return this;
	}
	public Button addOnPress(Consumer<ClickData> c)
	{
		onPress.add(c);
		return this;
	}
	public Button addOnRelease(Consumer<ClickData> c)
	{
		onRelease.add(c);
		return this;
	}

	@Override
	public void render(Graphics g)
	{
		g.drawImage(image, x, y, null);
	}

	@Override
	public void onClick(ClickData data)
	{
		for(Consumer<ClickData> c : onClick)
			c.accept(data);
	}

	@Override
	public void onPress(ClickData data)
	{
		for(Consumer<ClickData> c : onPress)
			c.accept(data);
	}

	@Override
	public void onRelease(ClickData data)
	{
		for(Consumer<ClickData> c : onRelease)
			c.accept(data);
	}
	
	@Override
	public void updateActualWidth()
	{
		this.width = this.widthRequest;
	}
	@Override
	public void updateActualHeight()
	{
		this.height = this.heightRequest;
	}
	
	
}
