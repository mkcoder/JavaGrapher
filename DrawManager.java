import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;

import javax.swing.*;

/**
 * Panel for drawing graphs. It wraps entire drawing logic.
 * 
 * @assignment Final project CS342, Java Grapher
 * @author Maciej Szpakowski
 * @date Dec 03, 2015
 */
public class DrawManager extends JPanel implements MouseMotionListener, 
                                        MouseListener, ComponentListener, ActionListener
{
	public static final double SCALE_SEN = 0.01;     // sensitivity of scaling
	public static final double MIN_SCALE = 0.00001;  // min scale
	public static final double MAX_SCALE = 1000000.0;// max scale
	public static final double MIN_TICK = 0.0001;    // min tick
	public static final double MAX_TICK = 10000000;  // max tick
	
	private PointF origin;          // origin coordinates relative to panel
	private Point screenOrigin;     // location of orgin in screen coordinates
	private DimensionF scale;       // size of one unit in pixels (pixels per unit)
	private DimensionF tick;        // grid density in global coordinates	
	private Dimension size;         // panel's size in pixels
	private Dimension lastSize;     // panel's size before resizing
	private boolean drawAxesFlag;   // flag for drawing axes
	private boolean drawGridFlag;   // flag for drawing grid
	private boolean drawNumFlag;    // flag for drawing numbers on axes
	private boolean drawCursorFlag; // flag for drawing cursor's global coordinates
	private boolean hintDrag;       // flag for drawing dragging hint
	private boolean hintZoom;       // flag for drawing zooming hint
	private Point mouseLast;        // last cursor position in screen coordinates
	private Color gridColor;        // color of the grid and axes
	private String debugString;     // string used for debug purposes
	private Timer timer;            // timer that invokes draw	
	private ArrayList<Function> functions; // all functions
	
	/**
	 * Default constructor.
	 */
	public DrawManager()
	{
		Color initColor;      // initial bg color
		DimensionF initScale; // initial scale
		DimensionF initTick;  // initial tick
		PointF initOrigin;    // initial origin position
		int timerPeriod;      // how often trigger paint
		
		initColor = new Color(200, 255, 200);
		initScale = new DimensionF(40, 40);
		initTick = new DimensionF(1, 1);
		initOrigin = new PointF(0.5, 0.5);
		timerPeriod = 20;
		
		setBackground(initColor);
		hintDrag = true;
		hintZoom = true;
		drawAxesFlag = true;
		drawGridFlag = true;
		drawNumFlag = true;
		drawCursorFlag = true;		
		scale = initScale;
		gridColor = Color.BLACK;
		tick = initTick;
		functions = new ArrayList<Function>();
		debugString = "";
		origin = initOrigin;
		screenOrigin = new Point();
		mouseLast = new Point();
		timer = new Timer(timerPeriod, this);
		timer.start(); 
		addMouseListener(this);
		addMouseMotionListener(this);
		addComponentListener(this);
	}
	
	/**
	 * Post constructor. It has to be called below the constructor.
	 * lastsize has to be updated before anyhing happens but after panel has been constructed.
	 */
	public void initialize()
	{
		lastSize = getSize();
		scale = new DimensionF(40, 40);
	}
	
	/**
	 * Main paint method for the panel.
	 */
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
		
		if(hintDrag) // draw hint for dragging
		{
			g.drawString("Left click to move origin", 20, size.height - 40);
		}
		
		if(hintZoom) // draw hint for zooming
		{
			g.drawString("Right click to zoom int/out", 20, size.height - 20);
		}

		//Brush.drawString(g, debugString, new Point(20,20), Color.RED, 15);
	}
	
	/**
	 * Draws axes.
	 * 
	 * @param g Graphics object used to draw.
	 */
	private void drawAxes(Graphics g)
	{		
		int xySize;         // size of "X" and "Y" on axes
		int thickness;      // axes thickness
		
		xySize = 15;
		thickness = 2;
		
		// x-axis
		Brush.drawLine(g, new Point(0, screenOrigin.y), 
				new Point(size.width, screenOrigin.y), gridColor, thickness);
		Brush.drawString(g, "X", new Point(screenOrigin.x+4, 15), gridColor, xySize, Font.BOLD);
		
		// y-axis
		Brush.drawLine(g, new Point(screenOrigin.x, 0), 
				       new Point(screenOrigin.x, size.height), gridColor, thickness);
		Brush.drawString(g, "Y", new Point(size.width-15, screenOrigin.y-4), 
				         gridColor, xySize, Font.BOLD);
	}
	
	/**
	 * Draws grid.
	 * 
	 * @param g Graphics object used to draw.
	 */
	private void drawGrid(Graphics g)
	{
		Color color; // final color of the grid
		Point start; // start of each line
		Point end;   // end of each line
		double i;    // iterator number
		
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
		
		i = screenToGlobalX(0) - screenToGlobalX(0) % tick.width; // find the first line on the screen
		
		while(i < screenToGlobalX(size.width)) // vertical grid lines
		{
			start.x = globalToScreenX(i);
			start.y = 0;
			end.x = globalToScreenX(i);
			end.y = size.height;
			
			Brush.drawLine(g, start, end, color, 1);
			i += tick.width;
		}
		
		i = screenToGlobalY(size.height) - 
			screenToGlobalY(size.height) % tick.height; // find the first line on the screen
		
		while(i < screenToGlobalY(0)) // horizontal grid lines
		{
			start.x = 0;
			start.y = globalToScreenY(-i);
			end.x = size.width;
			end.y = globalToScreenY(-i);
			
			Brush.drawLine(g, start, end, color, 1);
			i += tick.height;
		}
	}
	
	/**
	 * Draws numbers on axes.
	 * 
	 * @param g Graphics object used to draw.
	 */
	private void drawNumbers(Graphics g)
	{
		Point location; // helper point
		int fontSize;   // font size
		String coord;   // coordinate as string
		double i;       // iterator number
		
		fontSize = 11;
		location = new Point();
		
		if(tick.width*scale.width < 1 || tick.height*scale.height < 1) // prevent division by 0
		{
			return;
		}
		
		i = screenToGlobalX(0) - screenToGlobalX(0) % tick.width;
		
		while(i < screenToGlobalX(size.width)) // numbers on x
		{
			if(i == Math.floor(i))
			{
				coord = (int)i + "";
			}
			else
			{
				coord = i + "";
			}
			
			location.x = globalToScreenX(i) + 3;
			location.y = screenOrigin.y + 11;
			
			Brush.drawString(g, coord, location, gridColor, fontSize);
			i += tick.width;
		}
		
		i = screenToGlobalY(size.height) - screenToGlobalY(size.height) % tick.height;
		
		while(i < screenToGlobalY(0)) // numbers on y
		{			
			if(i == Math.floor(i))
			{
				coord = (int)i + "";
			}
			else
			{
				coord = i + "";
			}
			
			location.x = screenOrigin.x - 6 - g.getFontMetrics(g.getFont()).stringWidth(coord);
			location.y = globalToScreenY(-i) - 2;
			
			if(i != 0) // don't draw 0 again
			{
				Brush.drawString(g, coord, location, gridColor, fontSize);
			}
			i += tick.height;
		}
	}
	
	/**
	 * Draws box with cursor coordinates.
	 * 
	 * @param g Graphics object used to draw.
	 */
	private void drawCursorCoords(Graphics g)
	{
		Point location; // location of the box
		Dimension dim;  // dimensions of the box
		Color color;    // color of the box
		String s1;       // helper string 1
		String s2;       // helper string 2
		int fontSize;   // font size
		int lineHeight; // offset between lines
		
		location = new Point(10,10);
		dim = new Dimension(size.width/11, size.width/18);
		color = Color.WHITE;
		fontSize = size.width/60;
		lineHeight = size.width/58;
		s1 = String.format("X:%.3f", screenToGlobalX(mouseLast.x));
		s2 = String.format("Y:%.3f", screenToGlobalY(mouseLast.y));
		dim.width = dim.width < fontSize * s1.length()/2 ? fontSize * s1.length()/2 : dim.width;
		dim.width = dim.width < fontSize * s2.length()/2 ? fontSize * s2.length()/2 : dim.width;
		
		Brush.fillRect(g, location, dim, color);
		
		location.y += lineHeight; // move down
		location.x += 3;
		Brush.drawString(g, "Cusor", location, Color.BLACK, fontSize);
		
		location.y += lineHeight; // move down		
		Brush.drawString(g, s1, location, Color.BLACK, fontSize);
		
		location.y += lineHeight; // move down		
		Brush.drawString(g, s2, location, Color.BLACK, fontSize);
	}
	
	/**
	 * Adds a new function to functions list.
	 * 
	 * @param f newly created function.
	 */
	public void addFunction(Function f)
	{
		functions.add(f);
	}

	/**
	 * Removes a function from functions list.
	 * 
	 * @param index index of the function in the list to remove.
	 */
	public void removeFunction(int index)
	{
		functions.remove(index);
	}
	
	/**
	 * Converts panels's coordinates to graphs coordinates.
	 * 
	 * @param screenX x coordinate on the panel.
	 * @return graph's x cooridnate where screenX is.
	 */
	public double screenToGlobalX(int screenX)
	{
		return (-screenOrigin.x + screenX) / scale.width;
	}
	
	/**
	 * Converts panels's coordinates to graphs coordinates.
	 * 
	 * @param screenY y coordinate on the panel.
	 * @return graph's y cooridnate where screenY is.
	 */
	public double screenToGlobalY(int screenY)
	{		
		return -(-screenOrigin.y + screenY) / scale.height;
	}
	
	/**
	 * Convert's graph's coordinates to panel coordinates.
	 * 
	 * @param globalX x coordinate on the graph.
	 * @return panel x coordinate where globalX is.
	 */
	public int globalToScreenX(double globalX)
	{
		return (int)(origin.x*size.width + globalX*scale.width);
	}
	
	/**
	 * Convert's graph's coordinates to panel coordinates.
	 * 
	 * @param globalY y coordinate on the graph.
	 * @return panel y coordinate where globalY is.
	 */
	public int globalToScreenY(double globalY)
	{
		return (int)(origin.y*size.height + globalY*scale.height);
	}
	
	/**
	 * Utility function that clamps a number.
	 * 
	 * @param val what number to clamp.
	 * @param min what's the min value it can have.
	 * @param max what's the max value it can have.
	 * @return val clamped between min and max.
	 */
	public double clamp(double val, double min, double max)
	{
		if(val < min)
			return min;
		else if(val > max)
			return max;
		else
			return val;
	}
	
	// GETTERS
	/**
	 * Returns a function from functions list.
	 * 
	 * @param index index of the function in functions list to return.
	 * @return this.functions[index].
	 */
	public Function getFunction(int index)
	{
		return functions.get(index);
	}
	
	/**
	 * Gets scale of the graph.
	 * 
	 * @return this.scale.
	 */
	public DimensionF getScale()
	{
		return scale;
	}
	
	/**
	 * Get tick of the graph.
	 * 
	 * @return this.tick.
	 */
	public DimensionF getTick()
	{
		return tick;
	}
	
	// SETTERS
	/**
	 * Changes visibility of the axes.
	 * 
	 * @param flag show ir hide.
	 */
	public void showAxes(boolean flag)
	{
		drawAxesFlag = flag;
	}
	
	/**
	 * Changes visibility of the grid/
	 * 
	 * @param flag show or hide.
	 */
	public void showGrid(boolean flag)
	{
		drawGridFlag = flag;
	}
	
	/**
	 * Changes visibility of the numbers on the axes.
	 * 
	 * @param flag show or hide.
	 */
	public void showNumbers(boolean flag)
	{
		drawNumFlag = flag;
	}
	
	/**
	 * Changes visibility of the box with cursor coordinates.
	 * 
	 * @param flag show o hide.
	 */
	public void showCursorCoords(boolean flag)
	{
		drawCursorFlag = flag;
	}
	
	/**
	 * Sets scale on the graph.
	 * 
	 * @param scale scale to set.
	 */
	public void setScale(DimensionF scale)
	{
		this.scale = scale;
		this.scale.height = clamp(scale.height, MIN_SCALE, MAX_SCALE);
		this.scale.width = clamp(scale.width, MIN_SCALE, MAX_SCALE);
	}
	
	/**
	 * Sets horizontal scale of the graph.
	 * 
	 * @param horizontal horizontal scale of the graph.
	 */
	public void setScaleH(double horizontal)
	{
		scale.width = horizontal;
		this.scale.width = clamp(scale.width, MIN_SCALE, MAX_SCALE);
	}
	
	/**
	 * Sets vertical scale of the graph.
	 * 
	 * @param vertical vertical scale of the graph.
	 */
	public void setScaleV(double vertical)
	{
		scale.height = vertical;
		this.scale.height = clamp(scale.height, MIN_SCALE, MAX_SCALE);
	}
	
	/**
	 * Sets tick of the grid (how far apart are grid lines from one another).
	 * 
	 * @param tick tick of the grid.
	 */
	public void setTick(DimensionF tick)
	{
		this.tick = tick;
		this.tick.height = clamp(tick.height, MIN_TICK, MAX_TICK);
		this.tick.width = clamp(tick.width, MIN_TICK, MAX_TICK);
	}
	
	/**
	 * Sets horizontal tick of the grid (how far apart are grid lines from one another).
	 * 
	 * @param tick horizontal tick of the grid.
	 */
	public void setTickH(double horizontal)
	{
		tick.width = horizontal;
		this.tick.width = clamp(tick.width, MIN_TICK, MAX_TICK);
	}
	
	/**
	 * Sets vertical tick of the grid (how far apart are grid lines from one another).
	 * 
	 * @param tick vertical tick of the grid.
	 */
	public void setTickV(double vertical)
	{
		tick.height = vertical;
		this.tick.height = clamp(tick.height, MIN_TICK, MAX_TICK);
	}
	
	/**
	 * Sets color of the grid.
	 * 
	 * @param color color of the grid.
	 */
	public void setGridColor(Color color)
	{
		gridColor = color;
	}
	
	// EVENT HANDLERS

	/**
	 * Handles zooming and dragging of the graph.
	 */
    @Override
    public void mouseDragged(MouseEvent e)
	{
    	DimensionF helperDim; // auxiliary dimensions
    	
		if(SwingUtilities.isRightMouseButton(e)) // right click scales
		{
			helperDim = scale;
			
			if(mouseLast.x - e.getX() < 0) // scale down if moved left
			{				
				helperDim.width *= 1 - (-(mouseLast.x - e.getX())) * SCALE_SEN;
				helperDim.height *= 1 - (-(mouseLast.x - e.getX())) * SCALE_SEN;
				setScale(helperDim);
			}
			else if(mouseLast.x - e.getX() > 0) // scale up if moved right
			{
				helperDim.width *= 1 + (mouseLast.x - e.getX()) * SCALE_SEN;
				helperDim.height *= 1 + (mouseLast.x - e.getX()) * SCALE_SEN;
				setScale(helperDim);
			}
			
			hintZoom = false;
		}
		else if(SwingUtilities.isLeftMouseButton(e)) // left click drags
		{
			origin.x -= 1.0/size.width * (double)(mouseLast.x - e.getX());
			origin.y -= 1.0/size.height * (double)(mouseLast.y - e.getY());
			
			hintDrag = false;
		}
		
		mouseLast.setLocation(e.getX(),e.getY());
    }

    /**
     * Get the location of the cursor so it can be displayed properly.
     */
	@Override
    public void mouseMoved(MouseEvent e)
	{
		mouseLast.setLocation(e.getX(),e.getY());
    }

	/**
	 * Doesn't do anything. Interface requirement.
	 */
	@Override
    public void mouseClicked(MouseEvent e)
	{
    }

	/**
	 * Doesn't do anything. Interface requirement.
	 */
	@Override
    public void mouseEntered(MouseEvent e)
	{
    }

	/**
	 * Doesn't do anything. Interface requirement.
	 */
	@Override
    public void mouseExited(MouseEvent e)
	{
    }

	/**
	 * Get the location of the cursor so it can be displayed properly.
	 */
	@Override
    public void mousePressed(MouseEvent e)
	{
		mouseLast = new Point(e.getX(),e.getY());
    }

	/**
	 * Doesn't do anything. Interface requirement.
	 */
	@Override
    public void mouseReleased(MouseEvent e)
	{	    
    }

	/**
	 * Doesn't do anything. Interface requirement.
	 */
	@Override
    public void componentHidden(ComponentEvent e)
	{
    }

	/**
	 * Doesn't do anything. Interface requirement.
	 */
	@Override
    public void componentMoved(ComponentEvent e)
	{   
    }
	
	/**
	 * Handles rescaling when size of the window changes.
	 */
	@Override
    public void componentResized(ComponentEvent e)
    {
		try
		{
			scale.width *= (double)getWidth()/lastSize.width;
			scale.height *= (double)getHeight()/lastSize.height;
			lastSize = getSize();
		}
		catch(NullPointerException ex)
		{
			//dont do anything, thats weird java feature
		}
    }

	/**
	 * Doesn't do anything. Interface requirement.
	 */
	@Override
    public void componentShown(ComponentEvent e)
	{
    }

	/**
	 * Handles timer interrupts so draw function can be called.
	 */
	@Override
    public void actionPerformed(ActionEvent e)
	{
		try
		{
			if(e.getSource() == timer) // timer
			{
				getParent().repaint();
			}
		}
		catch(NullPointerException ex)
		{
			// dont do anything, weird JAVA feature
		}
    }
}