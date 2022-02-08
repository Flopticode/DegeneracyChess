package chess.statuseffect.board;

public class LightUnpluggedStatusEffect extends AbstractStatusEffect
{
	private int timeRemain;
	
	public LightUnpluggedStatusEffect(int duration)
	{
		super(StatusEffectType.LIGHT_UNPLUGGED);
		this.timeRemain = duration;
	}
	
	@Override
	public void tick()
	{
		this.timeRemain--;
	}
	@Override
	public boolean hasEnded()
	{
		return this.timeRemain <= 0;
	}
	@Override
	public String toString()
	{
		return "Light unplugged (" + timeRemain + ")";
	}
}
