public class Parser
{
	public static double evaluate(String expr)
	{
		return (1/(new Double(expr.substring(4, expr.length()-1))));
	}
}
