package chess.figure;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import chess.figure.figures.Bishop;
import chess.figure.figures.Debussy;
import chess.figure.figures.Knight;
import chess.figure.figures.Maxwell;
import chess.figure.figures.Pawn;
import chess.figure.figures.Queen;
import chess.figure.figures.Rook;
import chess.figure.figures.SystemIntegrator;

public class FigureTypes
{
	public static final FigureTypeRegistry figureTypes = new FigureTypeRegistry();
	
	static
	{
		registerFactories();
		
		String jsonString = "";
		
		try
		{
			File file = new File("./data/figure_types.json");
			
			BufferedReader reader = new BufferedReader(new FileReader(file));
			
			while(reader.ready())
				jsonString += (char)reader.read();
			
			reader.close();
		}
		catch(IOException ioe)
		{
			new IOException("Error while reading figure_types.json file.").printStackTrace();
			ioe.printStackTrace();
			System.exit(1);
		}
		
		JsonElement json = new JsonParser().parse(jsonString);
		
		if(json == null || !json.isJsonObject())
			throw new IllegalArgumentException("Invalid json.");
		
		JsonObject root = json.getAsJsonObject();
		
		JsonElement figures = root.get("figures");
		
		if(figures == null || !figures.isJsonArray())
			throw new IllegalArgumentException("Invalid json.");
		
		JsonArray figuresArr = figures.getAsJsonArray();
		
		for(int i = 0; i < figuresArr.size(); i++)
		{
			JsonElement curElement = figuresArr.get(i);
			
			figureTypes.register(curElement);
		}
		
		figureTypes.initDone();
	}
	
	public static final FigureType ROOK = figureTypes.get("ROOK");
	public static final FigureType PAWN = figureTypes.get("PAWN");
	public static final FigureType BISHOP = figureTypes.get("BISHOP");
	public static final FigureType KNIGHT = figureTypes.get("KNIGHT");
	public static final FigureType MAXWELL = figureTypes.get("MAXWELL");
	public static final FigureType DEBUSSY = figureTypes.get("DEBUSSY");
	public static final FigureType SYSTEM_INTEGRATOR = figureTypes.get("SYSTEM_INTEGRATOR");
	public static final FigureType QUEEN = figureTypes.get("QUEEN");
	
	private static void registerFactories()
	{
		figureTypes.registerFactory("ROOK", Rook::new);
		figureTypes.registerFactory("PAWN", Pawn::new);
		figureTypes.registerFactory("BISHOP", Bishop::new);
		figureTypes.registerFactory("KNIGHT", Knight::new);
		figureTypes.registerFactory("MAXWELL", Maxwell::new);
		figureTypes.registerFactory("DEBUSSY", Debussy::new);
		figureTypes.registerFactory("SYSTEM_INTEGRATOR", SystemIntegrator::new);
		figureTypes.registerFactory("QUEEN", Queen::new);
	}
	
	public static FigureType get(String id)
	{
		FigureType type = figureTypes.get(id);
		
		if(type == null)
			throw new NullPointerException("Invalid FigureType id \"" + id + "\".");
		
		return type;
	}
}
