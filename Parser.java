public class Parser
{
	public static double evaluate(String expr)
	{
		return Math.sin(new Double(expr.substring(4, expr.length()-1)));
	}
}
