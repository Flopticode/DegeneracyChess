package chess.figure.figures;

import chess.Board;
import chess.Util;
import chess.figure.FigureColor;
import chess.figure.FigureTypes;
import chess.statuseffect.board.LightUnpluggedStatusEffect;
import chess.statuseffect.board.StatusEffectType;

public class SystemIntegrator extends Knight
{
	public static final float SYSTEM_INTEGRATOR_UNPLUGS_LIGHT = 0.01f;
	public static final int ROUNDS_LIGHT_UNPLUGGED = 2;
	
	public SystemIntegrator(Board board, FigureColor color, short x, short y)
	{
		super(board, FigureTypes.SYSTEM_INTEGRATOR, color, x, y);
	}

	@Override
	public void tick()
	{
		if(!board.hasStatusEffect(StatusEffectType.LIGHT_UNPLUGGED) && Util.randomBoolean(SYSTEM_INTEGRATOR_UNPLUGS_LIGHT))
		{
			this.board.addStatusEffect(new LightUnpluggedStatusEffect(ROUNDS_LIGHT_UNPLUGGED));
		}
	}
	@Override
	public int getAttackStrength()
	{
		return 9;
	}
	@Override
	public int getDefenseStrength()
	{
		return 7;
	}
}
