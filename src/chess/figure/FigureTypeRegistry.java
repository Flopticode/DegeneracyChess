package chess.figure;

import java.util.HashMap;
import java.util.Map;

import com.google.gson.JsonElement;

public class FigureTypeRegistry
{
	private Map<String, FigureType> figureTypes = new HashMap<>();
	private Map<String, FigureFactory> figureFactories = new HashMap<>();
	private boolean initDone = false;
	
	public FigureTypeRegistry()
	{
		
	}
	
	public void registerFactory(String identifier, FigureFactory factory)
	{
		figureFactories.put(identifier, factory);
	}
	public FigureType register(JsonElement json)
	{
		FigureType newType = new FigureType(json, figureFactories);
		
		figureTypes.put(newType.getIdentifier(), newType);
		
		return newType;
	}
	
	public FigureType get(String identifier)
	{
		if(!initDone)
			throw new IllegalStateException("get() is not allowed before initialization is done.");
		
		FigureType type = figureTypes.get(identifier);
		
		if(type == null)
			throw new NullPointerException("There is no FigureType with identifier \"" + identifier + "\".");
		
		return type;
	}
	
	public void initDone()
	{
		this.initDone = true;
	}
}
