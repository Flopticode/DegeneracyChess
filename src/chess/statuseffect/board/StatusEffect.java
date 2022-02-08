package chess.statuseffect.board;

import chess.rendering.client.game.StatusEffectRenderer;

public interface StatusEffect
{
	public void tick();
	public boolean hasEnded();
	public StatusEffectType getType();
	public StatusEffectRenderer getRenderer();
}
