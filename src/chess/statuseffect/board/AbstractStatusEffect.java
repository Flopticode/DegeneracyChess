package chess.statuseffect.board;

import chess.rendering.client.game.StatusEffectRenderer;

public abstract class AbstractStatusEffect implements StatusEffect
{
	private final StatusEffectType type;
	private final StatusEffectRenderer renderer;
	
	public AbstractStatusEffect(StatusEffectType type)
	{
		this.type = type;
		renderer = new StatusEffectRenderer(this);
	}
	
	@Override
	public StatusEffectType getType()
	{
		return type;
	}
	@Override
	public StatusEffectRenderer getRenderer()
	{
		return renderer;
	}
}
