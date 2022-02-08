package chess.listeners;

import chess.statuseffect.board.StatusEffect;

public interface StatusEffectAddedListener
{
	public void notify(StatusEffect statusEffect);
}
