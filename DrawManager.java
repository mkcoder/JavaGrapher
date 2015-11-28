import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

public class DrawManager extends JPanel implements MouseMotionListener, MouseListener, ComponentListener
{
	public static final double SCALE_SEN = 0.01; // sensitivity of scaling
	
	private PointF origin;          //
	private Point screenOrigin;     // location of orgin in screen coordinates
	private DimensionF scale;       // size of one unit in pixels (pixels per unit)
	private DimensionF tick;        // grid density in global coordinates	
	private Dimension size;         // panel's size in pixels
	private Dimension lastSize;     // panel's size before resizing
	private boolean drawAxesFlag;   // flag for drawing axes
	private boolean drawGridFlag;   // flag for drawing grid
	private boolean drawNumFlag;    // flag for drawing numbers on axes
	private boolean drawCursorFlag; // flag for drawing cursor's global coordinates
	private Point mouseLast;        // last cursor position in screen coordinates
	private Color gridColor;        // color of the grid and axes
	private String debugString;     // string used for debug purposes
	
	private ArrayList<Function> functions; // all functions
	
	public DrawManager()
	{
		setBackground(new Color(200, 255, 200));
		drawAxesFlag = true;
		drawGridFlag = true;
		drawNumFlag = true;
		drawCursorFlag = true;
		addMouseListener(this);
		addMouseMotionListener(this);
		addComponentListener(this);
		scale = new DimensionF(40, 40);
		gridColor = Color.BLACK;
		tick = new DimensionF(0.5, 0.5);
		functions = new ArrayList<Function>();
		debugString = "";
		origin = new PointF(1, 1);
		screenOrigin = new Point();
		mouseLast = new Point();
		
		functions.add(new Function("sin(x)",Color.RED));
	}
	
	public void initialize()
	{
		lastSize = getSize();
		scale = new DimensionF(40, 40);
	}
	
	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);
		size = getSize();
		screenOrigin.x = globalToScreenX(0);
		screenOrigin.y = globalToScreenY(0);
		
		if(drawGridFlag) // draw grid
		{
			drawGrid(g);
		}
		
		if(drawAxesFlag) //draw axes
		{
			drawAxes(g);
		}
		
		if(drawNumFlag) // draw numbers on axes
		{
			drawNumbers(g);
		}
		
		for(Function f : functions) // draw functions
		{
			f.draw(g, this);
		}
		
		if(drawCursorFlag) // draw cursor coordinates
		{
			drawCursorCoords(g);
		}

		Brush.drawString(g, debugString, new Point(20,20), Color.RED, 15);
	}
	
	private void drawAxes(Graphics g)
	{		
		int xySize;         // size of "X" and "Y" on axes
		int thickness;      // axes thickness
		
		xySize = 15;
		thickness = 2;
		
		// x-axis
		Brush.drawLine(g, new Point(0, screenOrigin.y), 
				new Point(size.width, screenOrigin.y), gridColor, thickness);
		Brush.drawString(g, "X", new Point(screenOrigin.x+4, 15), Color.BLACK, xySize, Font.BOLD);
		
		// y-axis
		Brush.drawLine(g, new Point(screenOrigin.x, 0), new Point(screenOrigin.x, size.height), gridColor, thickness);
		Brush.drawString(g, "Y", new Point(size.width-15, screenOrigin.y-4), Color.BLACK, xySize, Font.BOLD);
	}
	
	private void drawGrid(Graphics g)
	{
		Color color; // final color of the grid
		Point start; // start of each line
		Point end;   // end of each line
		
		start = new Point();
		end = new Point();
		
		// blend grid with background
		color = new Color((getBackground().getRed() + gridColor.getRed())/2, 
				          (getBackground().getGreen() + gridColor.getGreen())/2,
				          (getBackground().getBlue() + gridColor.getBlue())/2);
		
		if(tick.width*scale.width < 1 || tick.height*scale.height < 1) // prevent division by 0
		{
			return;
		}
		
		/*for(int i = screenOrigin.x % (int)(tick.width*scale.width); i < size.width; i += tick.width*scale.width) // vertical grid lines
		{
			start.x = i;
			start.y = 0;
			end.x = i;
			end.y = size.height;
			
			Brush.drawLine(g, start, end, color, 1);
		}
		
		for(int i = screenOrigin.y % (int)(tick.height*scale.height); i < size.height; i += tick.height*scale.height) // horizontal grid lines
		{
			start.x = 0;
			start.y = i;
			end.x = size.width;
			end.y = i;
			
			Brush.drawLine(g, start, end, color, 1);
		}*/
		
		for(int i = 0; i<50; i++) // vertical grid lines
		{
			start.x = globalToScreenX(i*tick.width);
			start.y = 0;
			end.x = globalToScreenX(i*tick.width);
			end.y = size.height;
			
			Brush.drawLine(g, start, end, color, 1);
		}
	}
	
	private void drawNumbers(Graphics g)
	{
		Point location; // helper point
		int fontSize;   // font size
		String coord;   // coordinate as string
		
		fontSize = 11;
		location = new Point();
		
		if(tick.width*scale.width < 1 || tick.height*scale.height < 1) // prevent division by 0
		{
			return;
		}
		
		for(int i = screenOrigin.x % (int)(tick.width*scale.width); i < size.width; i += tick.width*scale.width) // numbers on x-axis
		{
			location.x = i + 3;
			location.y = screenOrigin.y + 11;
			coord = Math.round(screenToGlobalX(i)) + "";
			
			Brush.drawString(g, coord, location, gridColor, fontSize);
		}
		
		for(int i = screenOrigin.y % (int)(tick.height*scale.height); i < size.height; i += tick.height*scale.height) // numbers on y-axis
		{			
			location.x = screenOrigin.x - 6 - g.getFontMetrics(g.getFont()).stringWidth(Math.round(screenToGlobalY(i))+"");
			location.y = i - 2;
			coord = Math.round(screenToGlobalY(i)) + "";
			
			if(Math.round(screenToGlobalY(i)) != 0) // don't draw 0 again
			{
				Brush.drawString(g, coord, location, gridColor, fontSize);
			}
		}
	}
	
	private void drawCursorCoords(Graphics g)
	{
		Point location; // location of the box
		Dimension dim;  // dimensions of the box
		Color color;    // color of the box
		String s;       // helper string
		int fontSize;   // font size
		
		location = new Point(10,10);
		dim = new Dimension(size.width/11, size.width/18);
		color = Color.WHITE;
		fontSize = size.width/60;
		
		Brush.fillRect(g, location, dim, color);
		
		location.y += size.width/58; // move down
		location.x += 3;
		Brush.drawString(g, "Cusor", location, Color.BLACK, fontSize);
		
		location.y += size.width/58; // move down
		s = String.format("X:%.3f", screenToGlobalX(mouseLast.x));
		Brush.drawString(g, s, location, Color.BLACK, fontSize);
		
		location.y += size.width/58; // move down
		s = String.format("Y:%.3f", screenToGlobalY(mouseLast.y));
		Brush.drawString(g, s, location, Color.BLACK, fontSize);
	}
	
	public void addFunction(Function f)
	{
		functions.add(f);
	}
	
	public void removeFunction(int index)
	{
		functions.remove(index);
	}
	
	public Function getFunction(int index)
	{
		return functions.get(index);
	}
	
	public double screenToGlobalX(int screenX)
	{
		return (-screenOrigin.x + screenX) / scale.width;
	}
	
	public double screenToGlobalY(int screenY)
	{		
		return -(-screenOrigin.y + screenY) / scale.height;
	}
	
	public int globalToScreenX(double globalX)
	{
		return (int)(origin.x*size.width + globalX*scale.width);
	}
	
	public int globalToScreenY(double globalY)
	{
		return (int)(origin.y*size.height + globalY*scale.height);
	}
	
	// GETTERS
	
	public DimensionF getScale()
	{
		return scale;
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
	
	public void showCursorCoords(boolean flag)
	{
		drawCursorFlag = flag;
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
		if(SwingUtilities.isRightMouseButton(e)) // right click scales
		{
			if(mouseLast.x - e.getX() < 0) // scale down if moved left
			{
				scale.width *= 1 - (-(mouseLast.x - e.getX())) * SCALE_SEN;
				scale.height *= 1 - (-(mouseLast.x - e.getX())) * SCALE_SEN;
			}
			else if(mouseLast.x - e.getX() > 0) // scale up if moved right
			{
				scale.width *= 1 + (mouseLast.x - e.getX()) * SCALE_SEN;
				scale.height *= 1 + (mouseLast.x - e.getX()) * SCALE_SEN;
			}
			
		}
		else if(SwingUtilities.isLeftMouseButton(e)) // left click pans
		{
			origin.x -= 1.0/size.width * (double)(mouseLast.x - e.getX());
			origin.y -= 1.0/size.height * (double)(mouseLast.y - e.getY());
		}
		
		mouseLast.setLocation(e.getX(),e.getY());
		repaint();
    }

	@Override
    public void mouseMoved(MouseEvent e)
	{
		mouseLast.setLocation(e.getX(),e.getY());
		repaint();
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

	@Override
    public void componentHidden(ComponentEvent e)
	{
    }

	@Override
    public void componentMoved(ComponentEvent e)
	{   
    }

	@Override
    public void componentResized(ComponentEvent e)
    {		
		scale.width *= (double)getWidth()/lastSize.width;
		scale.height *= (double)getHeight()/lastSize.height;		
		lastSize = getSize();
    }

	@Override
    public void componentShown(ComponentEvent e)
	{   
    }
}