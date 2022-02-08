package chess.figure;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import chess.Board;
import chess.rendering.client.game.BoardRenderer;
import chess.rendering.client.game.FigureImageSet;

public class FigureType
{
	private final String identifier;
	private final String nameStr;
	private final FigureFactory factory;
	private FigureImageSet images;
	
	public FigureType(JsonElement json, Map<String, FigureFactory> factories)
	{
		String whiteImg, blackImg, asianImg;
		
		if(!json.isJsonObject())
			throw new IllegalArgumentException("Invalid json.");
		
		JsonObject curObject = json.getAsJsonObject();
		
		JsonElement identifierJson = curObject.get("identifier");
		JsonElement nameJson = curObject.get("name");
		JsonElement imagesJson = curObject.get("images");
		
		if(identifierJson == null || !identifierJson.isJsonPrimitive() || nameJson == null || !nameJson.isJsonPrimitive() || imagesJson == null || !imagesJson.isJsonObject())
			throw new IllegalArgumentException("Invalid json.");
		
		this.identifier = identifierJson.getAsString();
		this.nameStr = nameJson.getAsString();
		this.factory = factories.get(this.identifier);
		
		if(factory == null)
			throw new NullPointerException("No FigureFactory was provided for FigureType \"" + nameStr + "\". Please register a FigureFactory first.");
		
		JsonObject imagesJsonObj = imagesJson.getAsJsonObject();
		
		JsonElement whiteElement = imagesJsonObj.get("white");
		JsonElement blackElement = imagesJsonObj.get("black");
		JsonElement asianElement = imagesJsonObj.get("asian");
		
		if(whiteElement == null || blackElement == null || asianElement == null || !whiteElement.isJsonPrimitive() || !blackElement.isJsonPrimitive() || !asianElement.isJsonPrimitive())
			throw new IllegalArgumentException("Invalid json.");
		
		whiteImg = whiteElement.getAsString();
		blackImg = blackElement.getAsString();
		asianImg = asianElement.getAsString();
		
		Map<FigureColor, Image> map = new HashMap<>();
		try
		{
			map.put(FigureColor.WHITE, ImageIO.read(new File(whiteImg)).getScaledInstance(BoardRenderer.CELL_SIZE, BoardRenderer.CELL_SIZE, Image.SCALE_SMOOTH));
			map.put(FigureColor.BLACK, ImageIO.read(new File(blackImg)).getScaledInstance(BoardRenderer.CELL_SIZE, BoardRenderer.CELL_SIZE, Image.SCALE_SMOOTH));
			map.put(FigureColor.ASIAN, ImageIO.read(new File(asianImg)).getScaledInstance(BoardRenderer.CELL_SIZE, BoardRenderer.CELL_SIZE, Image.SCALE_SMOOTH));
		}
		catch(IOException ioe)
		{
			new IOException("Couldn't find at least one image referenced in FigureType " + this.nameStr + ".").printStackTrace();
			ioe.printStackTrace();
			
			this.images = FigureImageSet.createNotFoundSet(this);
		}
		
		images = new FigureImageSet(this, map);
	}
	
	@Override
	public String toString()
	{
		return this.nameStr;
	}
	public String getName()
	{
		return this.nameStr;
	}
	public String getIdentifier()
	{
		return identifier;
	}
	public FigureImageSet getImages()
	{
		return images;
	}
	
	public Figure createNew(final Board board, final FigureColor color, final short x, final short y)
	{
		return factory.create(board, color, x, y);
	}
}
