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
		setBackground(new Color(200,255,200));
		origin = new Point(getWidth()/2, getHeight()/2);
		drawAxesFlag = true;
		drawGridFlag = true;
		addMouseListener(this);
		addMouseMotionListener(this);
		scale = new Dimension(10,10);
		gridColor = Color.BLACK;
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
		
	}
	
	private void drawGrid(Graphics g)
	{
		
	}

	@Override
    public void mouseDragged(MouseEvent e)
	{
		// drag origin
		origin.x -= mouseLast.x - e.getX();
		origin.y -= mouseLast.y - e.getY();
		mouseLast = new Point(e.getX(),e.getY());
		getParent().repaint();
    }
	
	public void showAxes(boolean flag)
	{
		drawAxesFlag = flag;
	}
	
	public void showGrid(boolean flag)
	{
		drawGridFlag = flag;
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
