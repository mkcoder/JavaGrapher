import java.awt.*;
import java.awt.event.*;
import java.awt.geom.*;
import java.util.ArrayList;
import javax.swing.*;

public class DrawManager extends JPanel implements MouseMotionListener, MouseListener
{
	public static final double SCALE_SEN = 0.01; // sensitivity of scaling
	
	private Point origin;         // coordinates of graph's origin
	private DimensionF scale;     // size of one unit
	private Dimension size;       // panel's size
	private boolean drawAxesFlag; // flag for drawing axes
	private boolean drawGridFlag; // flag for drawing grid
	private boolean drawNumFlag;  // flag for drawing numbers on axes
	private Point mouseLast;      // last cursor position
	private Color gridColor;      // color of the grid and axes
	private DimensionF tick;      // distance between axis labels
	private ArrayList<Function> functions;
	private String debugString;   // string used for debug purposes
	
	public DrawManager()
	{
		setBackground(new Color(200, 255, 200));		
		drawAxesFlag = true;
		drawGridFlag = true;
		drawNumFlag = true;
		addMouseListener(this);
		addMouseMotionListener(this);
		scale = new DimensionF(20, 20);
		gridColor = Color.BLACK;
		tick = new DimensionF(1,1);
		functions = new ArrayList<Function>();
		debugString = "";
		
		functions.add(new Function("sin(x)",Color.RED));
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
		
		if(drawNumFlag)
		{
			drawNumbers(g);
		}
		
		for(Function f : functions)
		{
			f.draw(g, origin, scale, size);
		}
		
		Brush.drawString(g, debugString, new Point(20,20), Color.RED, 15);
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
		
		if(tick.width*scale.width < 1)
			return;
		
		for(int i = origin.x % (int)(tick.width*scale.width); i < size.width; i += tick.width*scale.width) // vertical grid lines
		{
			Brush.drawLine(g, new Point(i,0), new Point(i,size.height), color, 1);
		}
		
		for(int i = origin.y % (int)(tick.height*scale.height); i < size.height; i += tick.height*scale.height) // horizontal grid lines
		{
			Brush.drawLine(g, new Point(0,i), new Point(size.width,i), color, 1);
		}
	}
	
	private void drawNumbers(Graphics g)
	{		
		if(tick.width*scale.width < 1)
			return;
		
		for(int i = origin.x % (int)(tick.width*scale.width); i < size.width; i += tick.width*scale.width) // numbers on x-axis
		{
			Brush.drawString(g, Math.round((tick.width*i - origin.x)/scale.width) + "", new Point(i,origin.y), gridColor, 10);
		}
		
		for(int i = origin.y % (int)(tick.height*scale.height); i < size.height; i += tick.height*scale.height) // numbers on y-axis
		{
			Brush.drawString(g, Math.round(-(tick.height*i - origin.y)/scale.height) + "", new Point(origin.x,i), gridColor, 10);
		}
	}
	
	public void addFunction(Function f)
	{
		functions.add(f);
	}
	
	public void removeFunction(int index)
	{
		functions.remove(index);
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
	
	public void showNumbers(boolean flag)
	{
		drawNumFlag = flag;
	}
	
	public void setScale(DimensionF scale)
	{
		this.scale = scale;
	}
	
	public void setScaleH(double horizontal)
	{
		scale.width = horizontal;
	}
	
	public void setScaleV(double vertical)
	{
		scale.height = vertical;
	}
	
	public void setTick(DimensionF tick)
	{
		this.tick = tick;
	}
	
	public void setTickH(double horizontal)
	{
		tick.width = horizontal;
	}
	
	public void setTickV(double vertical)
	{
		tick.height = vertical;
	}
	
	public void setGridColor(Color color)
	{
		gridColor = color;
	}
	
	// EVENT HANDLERS

	@Override
    public void mouseDragged(MouseEvent e)
	{		
		if(SwingUtilities.isRightMouseButton(e))
		{
			if(mouseLast.x - e.getX() < 0)
			{
				scale.width *= 1 - (-(mouseLast.x - e.getX())) * SCALE_SEN;
				scale.height *= 1 - (-(mouseLast.x - e.getX())) * SCALE_SEN;
			}
			else if(mouseLast.x - e.getX() > 0)
			{
				scale.width *= 1 + (mouseLast.x - e.getX()) * SCALE_SEN;
				scale.height *= 1 + (mouseLast.x - e.getX()) * SCALE_SEN;
			}
			
		}
		else if(SwingUtilities.isLeftMouseButton(e))
		{
			origin.x -= mouseLast.x - e.getX();
			origin.y -= mouseLast.y - e.getY();		
		}
		
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