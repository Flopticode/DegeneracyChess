package chess.rendering.client.game;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import chess.figure.FigureColor;
import chess.figure.FigureType;

public class FigureImageSet
{	
	public static FigureImageSet createNotFoundSet(FigureType type)
	{
		Map<FigureColor, Image> map = new HashMap<>();
		
		try
		{
			map.put(FigureColor.WHITE, ImageIO.read(new File("./images/not_found.png")));
			map.put(FigureColor.BLACK, ImageIO.read(new File("./images/not_found.png")));
			map.put(FigureColor.ASIAN, ImageIO.read(new File("./images/not_found.png")));
		}
		catch(IOException ioe)
		{
			new IOException("Couldn't read not_found.png").printStackTrace();
			ioe.printStackTrace();
			System.exit(1);
		}
		return new FigureImageSet(type, map);
	}
	
	private FigureType figureType;
	private Map<FigureColor, Image> colorImageMap = new HashMap<>();;
	
	public FigureImageSet(FigureType type, Map<FigureColor, Image> colorImageMap)
	{
		this.figureType = type;
		this.colorImageMap = colorImageMap;
	}
	
	public Image getImageForColor(FigureColor color)
	{
		return colorImageMap.get(color);
	}
	public FigureType getType()
	{
		return figureType;
	}
}
