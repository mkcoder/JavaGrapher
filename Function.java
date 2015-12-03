import java.awt.*;

/**
 * Wrapper for painting a function and its particles.
 * 
 * @assignment Final project CS342, Java Grapher
 * @author Maciej Szpakowski
 * @date Dec 03, 2015
 */
public class Function
{
	public static final int THREAD_COUNT = 4; // how many threads will paint
                                              // should equal to number of cores for best performance
	
	private String expression;          // expression that represents this function
	private Color color;                // color of the graph
	private boolean visible;            // flag to show the function
	private boolean showParticlesFlag;  // flag to show function's particles
	private ParticleGenerator particles;// particle generator
	private Thread[] threads;	        // threads painting the function
	
	/**
	 * Constructor.
	 * 
	 * @param expr expression itself.
	 * @param color color of the function.
	 * @param d reference to DrawManager object.
	 */
	public Function(String expr, Color color, DrawManager d)
	{
		this.color = color;
		this.expression = expr;
		particles = new ParticleGenerator(expression, color, d);
		visible = true;
		showParticlesFlag = true;
		threads = new Thread[THREAD_COUNT];
	}
	
	/**
	 * Draws the function.
	 * 
	 * @param g Graphics object used to paint.
	 * @param d reference to DrawManager.
	 */
	public void draw(Graphics g, DrawManager d)
	{
		for(int i=0;i<THREAD_COUNT;i++) // start threads
		{
			final int param = i; // thread parameter
			
			threads[i] = new Thread()
			{				
				public void run() // thread entry point
				{
					Point p1;
					Point p2;
					double x;
					double y1;
					double y2;
					int begin;
					int end;
					
					if(!visible) // draw or not draw
					{
						return;
					}
					
					p1 = new Point();
					p2 = new Point();
					begin = param*d.getSize().width/THREAD_COUNT;
					end = (param+1)*d.getSize().width/THREAD_COUNT;
					
					for(int j = begin; j < end; j++) // segment of the panel to draw
					{			
						x = d.screenToGlobalX(j);
						y1 = Expression.evaluate(expression.replace("x", x + ""));
									
						p1.x = j;
						p1.y = d.globalToScreenY(-y1); // get the first point
						
						x = d.screenToGlobalX(j + 1);
						y2 = Expression.evaluate(expression.replace("x", x + ""));
						
						if(!Double.isFinite(y1) || !Double.isFinite(y2)) // ignore infinities
						{
							continue;
						}
						
						p2.x = j + 1;
						p2.y = d.globalToScreenY(-y2); // get the second point
						
						Brush.drawLine(g, p1, p2, color, 1); // connect the dots
					}
				}
			};
			threads[i].start();
		}
		
		for(Thread t : threads) // join threads
		{
			try
			{
	            t.join();
            }
			catch (InterruptedException e) 
			{
            }
		}
		
		if(showParticlesFlag) // draw particles
		{
			particles.draw(g, d, color);
		}
	}
	
	/**
	 * Shows or hides the graph.
	 * 
	 * @param flag visible or not.
	 */
	public void setVisible(boolean flag)
	{
		visible = flag;
	}
	
	/**
	 * Gets the visibility flag.
	 * 
	 * @return this.visible.
	 */
	public boolean getVisible()
	{
		return visible;
	}
	
	/**
	 * Sets the color of the graph.
	 * 
	 * @param color color to set.
	 */
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	/**
	 * Shows or hides particles.
	 * 
	 * @param flag particles visible or not.
	 */
	public void showParticles(boolean flag)
	{
		showParticlesFlag = flag;
	}
	
	/**
	 * Gets the expression.
	 * 
	 * @return this.expression.
	 */
	public String getExpression()
	{
		return expression;
	}
	
	/**
	 * Sets the expression.
	 * 
	 * @param expression expression to set.
	 */
	public void setExpression(String expression)
	{
		this.expression = expression;
		this.particles.setExpression(expression);
	}

	/**
	 * Gets the color of the graph.
	 * 
	 * @return this.color.
	 */
	public Color getColor()
	{
		return color;
	}
}