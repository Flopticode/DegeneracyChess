package chess.rendering.menu.api;

public interface Clickable
{
	public void onClick(ClickData data);
	public void onPress(ClickData data);
	public void onRelease(ClickData data);
}
