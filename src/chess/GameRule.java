package chess;

public enum GameRule
{
	FIGURES_CAN_JOINK_THEMSELVES(true);
	
	private boolean isActive;
	
	private GameRule(boolean isActive)
	{
		this.isActive = isActive;
	}
	
	public boolean isActive()
	{
		return isActive;
	}
}
