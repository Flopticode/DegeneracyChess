package chess.network.impl.exception;

@SuppressWarnings("serial")
public class InvalidMessageException extends Exception
{
	public InvalidMessageException(String message, String reason)
	{
		super("The message \"" + message + "\" is invalid: " + reason);
	}
}
