import java.awt.*;
import java.awt.geom.*;

public class Function
{
	private String expression;
	private Color color;
	
	public Function(String expr, Color color)
	{
		this.color = color;
		this.expression = expr;
	}
	
	public void draw(Graphics g, DrawManager d)
	{
		Point p1;
		Point p2;
		double x;
		double y;
		
		p1 = new Point();
		p2 = new Point();
		
		for(int i = 0; i < d.getSize().width - 1; i++)
		{
			x = d.screenToGlobalX(i);
			y = Parser.evaluate(expression.replace("x", x+""));
			p1.x = i;
			p1.y = d.globalToScreenY(y);
			
			x = d.screenToGlobalX(i + 1);
			y = Parser.evaluate(expression.replace("x", x+""));
			p2.x = i+1;
			p2.y = d.globalToScreenY(y);
			
			Brush.drawLine(g, p1, p2, color, 1);
		}
	}
}
