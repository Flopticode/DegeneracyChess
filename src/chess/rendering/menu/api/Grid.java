package chess.rendering.menu.api;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.Rectangle;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public abstract class Grid extends Container
{
	private int horizontalSize = 0;
	private int verticalSize = 0;
	private boolean verticalHomogenous = false;
	private boolean horizontalHomogenous = false;
	
	private List<Rectangle> childrenPack = new LinkedList<>();
	
	private Map<Point, Dimension> cellSizes = new HashMap<>();
	
	private MenuElement[][][] children;
	
	public Grid(int x, int y, int widthRequest, int heightRequest)
	{
		super(x, y, widthRequest, heightRequest);
	}
	
	@Override
	public void add(MenuElement e)
	{
		this.attach(e, 0, 0, 1, 1);
	}
	public void attach(MenuElement e, int x, int y, int w, int h)
	{
		super.add(e);
		childrenPack.add(new Rectangle(x, y, w, h));
		
		//this.updateCellWidth(x, y);
		//this.updateCellHeight(x, y);
		this.updateActualWidth();
		this.updateActualHeight();
	}
	/*
	private void updateCellWidth(int x, int y)
	{
		Point pt = new Point(x, y);
		int w = this.widthRequest / this.horizontalSize;
		
		for(MenuElement e : children.values())
		{
			int wE = e.getActualWidth();
			
			if(wE > w)
				w = wE;
		}
		this.cellSizes.put(pt, new Dimension(width, this.cellSizes.get(pt).height));
	}
	private void updateCellHeight(int x, int y)
	{
		Point pt = new Point(x, y);
		int h = this.heightRequest / this.verticalSize;
		
		for(MenuElement e : children.values())
		{
			int hE = e.getActualHeight();
			
			if(hE > h)
				h = hE;
		}
		this.cellSizes.put(pt, new Dimension(this.cellSizes.get(pt).width, height));
	}*/
	private Dimension getCellSize(int x, int y)
	{
		return this.cellSizes.get(new Point(x, y));
	}
	
	@Override
	public void updateActualWidth()
	{
		int w = 0;
		
		for(int x = 0; x < this.horizontalSize; x++)
		{
			int widestInRow = 0;
			
			for(int y = 0; y < this.verticalSize; y++)
			{
				Dimension size = getCellSize(x, y);
				
				if(size.width > widestInRow)
					widestInRow = size.width;
			}
			
			w+=widestInRow;
		}
		
		if(w < this.widthRequest)
			w = this.widthRequest;
		
		this.width = w;
	}
	@Override
	public void updateActualHeight()
	{
		int h = 0;
		
		for(int y = 0; y < this.verticalSize; y++)
		{
			int highestInRow = 0;
			
			for(int x = 0; x < this.horizontalSize; x++)
			{
				Dimension size = getCellSize(x, y);
				
				if(size.height > highestInRow)
					highestInRow = size.height;
			}
			
			h+=highestInRow;
		}
		
		if(h < this.heightRequest)
			h = this.heightRequest;
		
		this.height = h;
	}
	/*
	@Override
	public void render(Graphics g)
	{
		int curX = this.x;
		
		for(int x = 0; x < this.horizontalSize; x++)
			for(int y = 0; y < this.verticalSize; y++)
			{
				MenuElement e = children.get(new Point(x, y));
				
				if(e != null)
			}
	}*/
}
