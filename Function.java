import java.awt.*;
import java.awt.geom.*;

import javax.swing.JOptionPane;

public class Function
{
	private String expression;
	private Color color;
	private boolean visible;
	
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
		double y1;
		double y2;
		
		p1 = new Point();
		p2 = new Point();
		
		for(int i = 0; i < d.getSize().width - 1; i++)
		{			
			x = d.screenToGlobalX(i);
			y1 = Parser.evaluate(expression.replace("x", x+""));
						
			p1.x = i;
			p1.y = d.globalToScreenY(-y1);                                /////////////////// WEIRD, WHY THIS HAS TO BE NEGATIVE !!!!
			
			x = d.screenToGlobalX(i + 1);
			y2 = Parser.evaluate(expression.replace("x", x+""));
			
			if(!Double.isFinite(y1) || !Double.isFinite(y2))
				continue;
			
			p2.x = i+1;
			p2.y = d.globalToScreenY(-y2);                                /////////////////// WEIRD, WHY THIS HAS TO BE NEGATIVE !!!!
			
			Brush.drawLine(g, p1, p2, color, 1);
		}
	}
	
	public void setVisible(boolean flag)
	{
		visible = flag;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
}
