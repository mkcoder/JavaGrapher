import java.awt.*;

/**
 * Container for paint wrappers.
 * 
 * @assignment Final project CS342, Java Grapher
 * @author Maciej Szpakowski
 * @date Dec 03, 2015
 */
public class Brush
{
	/**
	 * Paints string using g object.
	 * 
	 * @param g Graphics object used to paint.
	 * @param text String to paint.
	 * @param location where to pain.
	 * @param color text color.
	 * @param size font size.
	 */
	public static void drawString(Graphics g, String text, Point location, Color color, int size)
	{
		drawString(g, text, location, color, size, Font.PLAIN);
	}
	
	/**
	 * Paints string using g object.
	 * 
	 * @param g Graphics object used to paint.
	 * @param text String to paint.
	 * @param location where to pain.
	 * @param color text color.
	 * @param size font size.
	 * @param style style of the font.
	 */
	public static void drawString(Graphics g, String text, Point location, Color color, int size, int style)
	{
		Font font;
		
		font = new Font("SansSerif", style, size);
		g.setFont(font);
		g.setColor(color);
		
		g.drawString(text, location.x, location.y);
	}
	
	/**
	 * Paints line using g object.
	 * 
	 * @param g Graphics object used to paint.
	 * @param p1 where lines start.
	 * @param p2 where line ends.
	 * @param color color of the line.
	 * @param thickness thickness of the line.
	 */
	public static void drawLine(Graphics g, Point p1, Point p2, Color color, int thickness)
	{
		g.setColor(color);
		((Graphics2D)g).setStroke(new BasicStroke(thickness));
		g.drawLine(p1.x, p1.y, p2.x, p2.y);
	}
	
	/**
	 * Paints a filled rect.
	 * 
	 * @param g Graphics object used to paint.
	 * @param location where to draw rect.
	 * @param size dimensions of the rect.
	 * @param color color of the rect.
	 */
	public static void fillRect(Graphics g, Point location, Dimension size, Color color)
	{
		g.setColor(color);
		g.fillRect(location.x, location.y, size.width, size.height);
	}
	
	/**
	 * Paints a filled circle.
	 * 
	 * @param g Graphics object used to paint.
	 * @param location where to draw circle.
	 * @param radius radius of the cricle.
	 * @param color color of the circle.
	 */
	public static void fillCircle(Graphics g, Point location, int radius, Color color)
	{
	    g.setColor(color);
        g.fillOval(location.x - radius, location.y - radius, 2*radius, 2*radius);
	}
}
