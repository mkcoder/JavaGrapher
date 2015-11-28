import java.awt.*;
import java.awt.geom.*;

import javax.swing.JOptionPane;

public class Function
{
	private String expression;
	private Color color;
	private boolean visible;
	private ParticleGenerator particles;
	
	public Function(String expr, Color color)
	{
		Color compl;
		
		compl = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());		
		this.color = color;
		this.expression = expr;
		particles = new ParticleGenerator(expression, compl);
		visible = true;
	}
	
	public void draw(Graphics g, DrawManager d)
	{
		Point p1;
		Point p2;
		double x;
		double y1;
		double y2;
		
		if(!visible) // draw on not draw
		{
			return;
		}
		
		p1 = new Point();
		p2 = new Point();
		
		for(int i = 0; i < d.getSize().width - 1; i++)
		{			
			x = d.screenToGlobalX(i);
			y1 = Parser.evaluate(expression.replace("x", x + ""));
						
			p1.x = i;
			p1.y = d.globalToScreenY(-y1);                                /////////////////// WEIRD, WHY THIS HAS TO BE NEGATIVE !!!!
			
			x = d.screenToGlobalX(i + 1);
			y2 = Parser.evaluate(expression.replace("x", x + ""));
			
			if(!Double.isFinite(y1) || !Double.isFinite(y2)) // ignore infinities
			{
				continue;
			}
			
			p2.x = i+1;
			p2.y = d.globalToScreenY(-y2);                                /////////////////// WEIRD, WHY THIS HAS TO BE NEGATIVE !!!!
			
			Brush.drawLine(g, p1, p2, color, 1);
		}
		
		//particles.draw(g, d);
	}
	
	public void setVisible(boolean flag)
	{
		visible = flag;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public String getExpression()
	{
		return expression;
	}
	
	public void setExpression(String expression)
	{
		this.expression = expression;
	}

	public Color getColor() {
		return color;
	}
}
