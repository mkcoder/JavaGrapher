import java.awt.*;

public class Function
{
	public static final int THREAD_COUNT = 2;
	
	private String expression;
	private Color color;
	private boolean visible;
	private boolean showParticlesFlag;
	private ParticleGenerator particles;
	private Thread[] threads;	
	
	public Function(String expr, Color color, DrawManager d)
	{
		Color compl;
		
		compl = new Color(255 - color.getRed(), 255 - color.getGreen(), 255 - color.getBlue());		
		this.color = color;
		this.expression = expr;
		particles = new ParticleGenerator(expression, compl, d);
		visible = true;
		showParticlesFlag = true;
		threads = new Thread[THREAD_COUNT];
	}
	
	public void draw(Graphics g, DrawManager d)
	{
		for(int i=0;i<THREAD_COUNT;i++)
		{
			threads[i] = new Thread()
			{				
				public void run()
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
				}
			};
			threads[i].start();
		}
		for(Thread t : threads)
		{
			try
			{
	            t.join();
            }
			catch (InterruptedException e) 
			{
            }
		}
		
		if(showParticlesFlag)
		{
			particles.draw(g, d, color);
		}
	}
	
	public void setVisible(boolean flag)
	{
		visible = flag;
	}
	
	public boolean getVisible()
	{
		return visible;
	}
	
	public void setColor(Color color)
	{
		this.color = color;
	}
	
	public void showParticles(boolean flag)
	{
		showParticlesFlag = flag;
	}
	
	public String getExpression()
	{
		return expression;
	}
	
	public void setExpression(String expression)
	{
		this.expression = expression;
		this.particles.setExpression(expression);
	}

	public Color getColor()
	{
		return color;
	}
}