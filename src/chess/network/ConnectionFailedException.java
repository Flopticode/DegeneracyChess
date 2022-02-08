package chess.network;

@SuppressWarnings("serial")
public class ConnectionFailedException extends Exception
{
	public ConnectionFailedException(String text)
	{
		super(text);
	}
}
