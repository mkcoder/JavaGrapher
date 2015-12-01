import java.awt.*;
import java.util.ArrayList;


public class ParticleGenerator
{
    private String expression;
    private ArrayList<Particle> particles;
    private DrawManager drawManager;
    
   /**
     * @param particles
     * @param positions
     */
    public ParticleGenerator(String expression, Color color, DrawManager d)
    {  
        this.expression = expression;
        this.particles = new ArrayList<Particle>();
        this.drawManager = d; 
    }
 

    public void setExpression(String expression)
    {
        this.expression = expression;
    }
    
    public void draw(Graphics g, DrawManager d, Color color)
    {
        update(color);
        render(g, color);
    }

    private void update(Color color)
    {
        if(particles.isEmpty())
        {
            Point p;
            double x;
            double y;
            
            x = drawManager.screenToGlobalX(0);
            y = Parser.evaluate(expression.replace("x", x+""));
            
            int randomLifeSpan = (int)(Math.random()*(500));

            p = new Point();
            
            p.x = 0;
            p.y = drawManager.globalToScreenY(-y); 
            
            particles.add(new Particle(p,3,randomLifeSpan,color));
            return;
        }
       
        for(int i = 0; i < particles.size(); i++)
        {
            double particleDensity;
            boolean remove;
            Particle p;
            Point p1;
            int j;
            double x;
            double y;
            
            p = particles.get(i);
            j = p.nextPosition;
            
            x = drawManager.screenToGlobalX(j);
            y = Parser.evaluate(expression.replace("x", x+""));
            
            p1 = new Point();
            p1.x = j;
            p1.y = drawManager.globalToScreenY(-y);                               

            remove = p.update(p1);
            particleDensity = Math.random();

            
            if(remove || (particles.size() < 500 && particleDensity < 0.01))
            {
                if(remove)
                {
                    particles.remove(i);
                }
                int randomLifeSpan = (int)(Math.random()*(500));
                Point p2;
                double x1;
                double y2;
                
                x1 = drawManager.screenToGlobalX(0);
                y2 = Parser.evaluate(expression.replace("x", x1+""));
                
                
                p2 = new Point();
                
                p2.x = 0;
                p2.y = drawManager.globalToScreenY(-y2); 
                
                particles.add(new Particle(p2,3,randomLifeSpan,color));
                
            }
           
        }
    }
    
    private void render(Graphics g)
    {
        for(Particle p : particles)
        {
            p.render(g);
        }

    }
    
    private void render(Graphics g, Color color)
    {
        for(Particle p : particles)
        {
            p.render(g,color);
        }

    }

private class Particle
   {
       private Point p;
       private int radius;
       private int nextPosition;
       private Color color;
       private int life;

       /**
     * @param x
     * @param y
     * @param radius
     * @param nextPosition
     * @param life
     * @param color
     */
    public Particle(Point p, int radius, int life, Color color)
    {
        this.p = p;
        this.radius = radius;
        this.life = life;
        this.nextPosition = 1;
        this.color = color;
    }
    
    public boolean update(Point p)
    {
        
//        int red = (color.getRed()) -1;
//        int blue = (color.getBlue()) -1;
//        int green = (color.getGreen()) -1;
        
        nextPosition++;
        this.p = p;
        life--;
        

        if(life <= 0)
        return true;
        
        return false;
    }
    
    public void render(Graphics g)
    {
        Brush.fillCircle(g, this.p, this.radius, this.color);
    }
    
    public void render(Graphics g, Color userColor)
    {
        Brush.fillCircle(g, this.p, this.radius, userColor);
    }
   }

}
