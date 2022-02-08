package chess.network;

public enum InteractionType implements Serializable
{
	MOVE(0),
	INTERACT(1);
	
	private int id;
	
	private InteractionType(int id)
	{
		this.id = id;
	}
	
	@Override
	public String serialize()
	{
		return "" + (char)id;
	}
	
	public static InteractionType byID(int id)
	{
		for(InteractionType t : InteractionType.values())
			if(t.id == id)
				return t;
		throw new NullPointerException("Element with id " + id + " does not exist.");
	}
}
