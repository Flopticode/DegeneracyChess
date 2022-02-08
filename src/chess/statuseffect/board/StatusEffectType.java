package chess.statuseffect.board;

public enum StatusEffectType
{
	LIGHT_UNPLUGGED;
	
	@Override
	public String toString()
	{
		switch(this)
		{
		case LIGHT_UNPLUGGED:
			return "Light unplugged";
		default:
			throw new IllegalStateException("Invalid type \"" + this + "\". Is a case missing?");
		}
	}
}
