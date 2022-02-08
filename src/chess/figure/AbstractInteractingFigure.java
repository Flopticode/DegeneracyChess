package chess.figure;

public interface AbstractInteractingFigure
{
	public abstract boolean isInInteractionRange(short tarX, short tarY);
	public abstract boolean interact(Figure fig);
}
