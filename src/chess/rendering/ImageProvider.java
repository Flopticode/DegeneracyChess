package chess.rendering;

import java.awt.Image;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.imageio.ImageIO;

public class ImageProvider
{
	public static final Map<String, Image> images = new HashMap<>();
	
	static
	{
		ImageProvider.get("./images/not_found.png");
	}
	
	public static Image get(String str, int width, int height)
	{
		return ImageProvider.get(str).getScaledInstance(width, height, Image.SCALE_SMOOTH); 
	}
	public static Image get(String str)
	{
		return images.computeIfAbsent(str, (s)->{
			try
			{
				return ImageIO.read(new File(s));
			}
			catch(IOException ioe)
			{
				return ImageProvider.get("./images/not_found.png");
			}
		});
	}
}
