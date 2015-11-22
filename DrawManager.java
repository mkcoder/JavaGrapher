import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;

public class DrawManager extends JPanel implements MouseMotionListener, MouseListener
{
	private Point origin;         // coordinates of graph's origin
	private Dimension scale;      // size of one unit
	private Dimension size;       // panel's size
	private boolean drawAxesFlag; // flag for drawing axes
	private boolean drawGridFlag; // flag for drawing grid
	private Point mouseLast;      // last cursor position
	private Color gridColor;      // color of the grid and axes
	private ArrayList<Function> functions;
	
	public DrawManager()
	{
		setBackground(new Color(200, 255, 200));		
		drawAxesFlag = true;
		drawGridFlag = true;
		addMouseListener(this);
		addMouseMotionListener(this);
		scale = new Dimension(20, 20);
		gridColor = Color.BLACK;
		functions = new ArrayList<Function>();
	}
	
	public void initialize()
	{
		origin = new Point(getWidth()/2, getHeight()/2);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		size = new Dimension(getWidth(), getHeight());
		
		if(drawGridFlag)
		{
			drawGrid(g);
		}
		
		if(drawAxesFlag)
		{
			drawAxes(g);
		}
		
		for(Function f : functions)
		{
			f.draw(g, origin, scale);
		}
	}
	
	private void drawAxes(Graphics g)
	{
		// x-axis
		Brush.drawLine(g, new Point(0, origin.y), new Point(size.width, origin.y), gridColor, 2);
		Brush.drawString(g, "X", new Point(origin.x+4, 15), Color.BLACK, 15, Font.BOLD);
		
		// y-axis
		Brush.drawLine(g, new Point(origin.x, 0), new Point(origin.x, size.height), gridColor, 2);
		Brush.drawString(g, "Y", new Point(size.width-15, origin.y-4), Color.BLACK, 15, Font.BOLD);
	}
	
	private void drawGrid(Graphics g)
	{
		Color color;
		
		// blend grid with background
		color = new Color((getBackground().getRed() + gridColor.getRed())/2, 
				(getBackground().getGreen() + gridColor.getGreen())/2, (getBackground().getBlue() + gridColor.getBlue())/2);
		
		for(int i = origin.x % scale.width; i < size.width; i += scale.width) // vertical grid lines
		{
			Brush.drawLine(g, new Point(i,0), new Point(i,size.height), color, 1);
		}
		
		for(int i = origin.y % scale.height; i < size.height; i += scale.height) // horizontal grid lines
		{
			Brush.drawLine(g, new Point(0,i), new Point(size.width,i), color, 1);
		}
	}
	
	// SETTERS
	
	public void showAxes(boolean flag)
	{
		drawAxesFlag = flag;
	}
	
	public void showGrid(boolean flag)
	{
		drawGridFlag = flag;
	}
	
	public void setScale(Dimension scale)
	{
		this.scale = scale;
	}
	
	public void setScaleH(int width)
	{
		scale.width = width;
	}
	
	public void setScaleV(int heigth)
	{
		scale.height = heigth;
	}
	
	public void setGridColor(Color color)
	{
		gridColor = color;
	}
	
	// EVENT HANDLERS

	@Override
    public void mouseDragged(MouseEvent e)
	{
		// drag origin
		origin.x -= mouseLast.x - e.getX();
		origin.y -= mouseLast.y - e.getY();
		mouseLast = new Point(e.getX(),e.getY());
		getParent().repaint();
    }

	@Override
    public void mouseMoved(MouseEvent e)
	{	    
    }

	@Override
    public void mouseClicked(MouseEvent e)
	{
    }

	@Override
    public void mouseEntered(MouseEvent e)
	{
    }

	@Override
    public void mouseExited(MouseEvent e)
	{
    }

	@Override
    public void mousePressed(MouseEvent e)
	{
		mouseLast = new Point(e.getX(),e.getY());
    }

	@Override
    public void mouseReleased(MouseEvent e)
	{	    
    }
}
