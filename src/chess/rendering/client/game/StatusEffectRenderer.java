package chess.rendering.client.game;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

import chess.statuseffect.board.StatusEffect;
import chess.statuseffect.board.StatusEffectType;

public class StatusEffectRenderer
{
	public static final int ICON_WIDTH = 20;
	public static final int ICON_HEIGHT = 20;
	public static final float FACTOR_ICON_HEIGHT_FONT_SIZE = 0.8f;
	public static final int ICON_TEXT_GAP = 6;
	
	private static final Image ICON_NOT_FOUND;
	
	private static final Map<StatusEffectType, Image> effectIconMap = new HashMap<>();
	
	static
	{
		BufferedImage iconNotFound = new BufferedImage(ICON_WIDTH, ICON_HEIGHT, BufferedImage.TYPE_INT_RGB);
		Graphics g = iconNotFound.getGraphics();
		
		g.setColor(Color.black);
		g.fillRect(ICON_WIDTH/2, 0, ICON_WIDTH/2, ICON_HEIGHT/2);
		g.fillRect(0, ICON_HEIGHT/2, ICON_WIDTH/2, ICON_HEIGHT/2);
		
		g.setColor(Color.pink);
		g.fillRect(0, 0, ICON_WIDTH/2, ICON_HEIGHT/2);
		g.fillRect(ICON_WIDTH/2, ICON_HEIGHT/2, ICON_WIDTH/2, ICON_HEIGHT/2);
		
		ICON_NOT_FOUND = iconNotFound;
	}
	
	private final StatusEffect statusEffect;
	private final Image icon;
	
	public StatusEffectRenderer(StatusEffect statusEffect)
	{
		this.statusEffect = statusEffect;
		
		StatusEffectType type = statusEffect.getType();
		
		this.icon = getIcon(type);
	}
	
	public void render(Graphics g, int x, int y)
	{
		int fontSize = (int)(ICON_HEIGHT * FACTOR_ICON_HEIGHT_FONT_SIZE);
		
		g.drawImage(icon, x, y, null);
		g.setColor(Color.white);
		g.setFont(new Font("Arial", 0, fontSize));
		g.drawString(statusEffect.toString(), x + ICON_WIDTH + ICON_TEXT_GAP, y+(ICON_HEIGHT - fontSize) / 2 + fontSize);
	}
	
	private static Image getIcon(StatusEffectType type)
	{
		return effectIconMap.computeIfAbsent(type, StatusEffectRenderer::createIcon);
	}
	private static Image createIcon(StatusEffectType type)
	{
		try
		{
			switch(type)
			{
			case LIGHT_UNPLUGGED:
				return ImageIO.read(new File("./images/effects/light_unplugged.png")).getScaledInstance(ICON_WIDTH, ICON_HEIGHT, Image.SCALE_SMOOTH);
			default:
				return ICON_NOT_FOUND;
			}
		}
		catch(IOException ioe)
		{
			return ICON_NOT_FOUND;
		}
	}
}
