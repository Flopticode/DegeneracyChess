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
	public static void printBytes(String str, PrintStream out)
	{
		for(int i = 0; i < str.length(); i++)
			out.print((int)str.charAt(i) + " ");
		out.print("\n");
	}
}
