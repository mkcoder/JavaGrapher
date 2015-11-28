import javax.script.ScriptEngineManager;
import javax.script.ScriptEngine;
import javax.script.ScriptException;

public class Parser
{
	public static double evaluate(String expr)
	{
		ScriptEngineManager mgr = new ScriptEngineManager();
	    ScriptEngine engine = mgr.getEngineByName("JavaScript");
	    try 
	    {
	        return (double) engine.eval(expr);
        } 
	    catch (ScriptException e)
	    {
	    	return 0;
        }
	}
}
