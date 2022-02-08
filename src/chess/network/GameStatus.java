package chess.network;

public enum GameStatus implements Serializable
{
	WON(0),
	LOST(1),
	STARTED(2),
	REMIS(3),
	LOBBY(4);
	
	public final int id;
	
	private GameStatus(int id)
	{
		this.id = id;
	}
	
	@Override
	public String serialize()
	{
		return "" + (char)id;
	}
	
	public static GameStatus byID(int id)
	{
		for(GameStatus status : GameStatus.values())
			if(status.id == id)
				return status;
		return null;
	}
}
