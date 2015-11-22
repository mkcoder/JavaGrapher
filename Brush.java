import java.awt.*;

public class Brush
{
	public static void drawString(Graphics g, String text, Point location, Color color, int size)
	{
		Font font;
		
		font = new Font("SansSerif", Font.PLAIN, size);
		g.setFont(font);
		g.setColor(color);
		
		g.drawString(text, location.x, location.y);
	}
	
	public static void drawLine(Graphics g, Point p1, Point p2, Color color, int thickness)
	{
		g.setColor(color);
		((Graphics2D)g).setStroke(new BasicStroke(thickness));
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}
}
