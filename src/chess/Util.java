package chess;

import java.io.PrintStream;
import java.util.Random;

public abstract class Util
{
	private static Random rand = new Random();
	
	public static boolean randomBoolean(float probability)
	{
		if(probability >= 1)
			return true;
		else if(probability <= 0)
			return false;
		else
			return Math.random() <= probability;
	}
	public static int randomInt(int minValue, int maxValue)
	{
		return rand.nextInt(maxValue+1)+minValue;
	}
	public static String getBytesString(String str, PrintStream out)
	{
		String s = "";

		for(int i = 0; i < str.length(); i++)
			s += (int)str.charAt(i) + " ";
		s += '\n';

		return s;
	}
}
