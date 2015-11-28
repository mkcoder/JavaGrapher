import java.awt.*;
import java.util.ArrayList;


public class ParticleGenerator
{
    private String expression;
    private ArrayList<Particle> particles;
    private ArrayList<Point> positions;
    private Color particleColor;
    
   /**
     * @param particles
     * @param positions
     */
    public ParticleGenerator(String expression, Color color)
    {  
        this.particleColor = color;
        this.expression = expression;
        this.particles = new ArrayList<Particle>();
        this.positions = new ArrayList<Point>();
     
    }
    
    public void intPositions(DrawManager d)
    {
        double x;
        double y1;
        
        for(int i = 0; i < d.getSize().width - 1; i++)
        {
            Point p;
            
            p = new Point();
            
            x = d.screenToGlobalX(i);
            y1 = Parser.evaluate(expression.replace("x", x+""));
            
            p.x  = i;
            p.y = d.globalToScreenY(-y1);  
            
            positions.add(i,p);
        }
    }
    
    public void intParticles(DrawManager d)
    {
        for(int i = 0; i < 500; i++)
        {
            int randomPosition = (int)(Math.random()*(d.getSize().width - 1));
            int randomLifeSpan = (int)(Math.random()*(d.getSize().width - 1));
            particles.add(new Particle(positions.get(randomPosition),3,randomLifeSpan,this.particleColor,randomPosition ));
        }
    }
    public void draw(Graphics g, DrawManager d)
    {
        
        intPositions(d);
        intParticles(d);  
       int x = 100;
        while(x > 0)
        {
            
        
        update();
        render(g);

                    new Thread()
                    {
                        @Override
                        public void run()
                        {
                            try
                            {
                             //System.out.println("here");
                             Thread.sleep(1000/60);
                             //System.out.println("after here");
        
                            }
                            catch (InterruptedException e)
                            {
                                e.printStackTrace();
                            }
                        }
                   }.start();
           System.out.println(x);
        x--;
        }
    }

    private void update()
    {
        for(int i = 0; i < particles.size() - 1; i++)
        {
            boolean remove;
            Particle p;
            Point p1;
            int j;
            
            p = particles.get(i);
            
            j = p.nextPosition;
            
            remove  = j == positions.size()-1 ? true: false;
            p1 = positions.get(j);
            
            if(p.update(p1) || remove)
            {
                particles.remove(i);
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
     * @param dx
     * @param dy
     * @param radius
     * @param life
     * @param color
     */
    public Particle(Point p, int radius, int life, Color color, int position)
    {
        this.p = p;
        this.radius = radius;
        this.life = life;
        this.nextPosition = position;
        this.color = color;
    }
    
    public boolean update(Point p)
    {
        
        int red = (color.getRed()) -1;
        int blue = (color.getBlue()) -1;
        int green = (color.getGreen()) -1;
        this.p = p;
        life--;
        
        red = red < 0 ? 0 : red;
        blue = blue < 0 ? 0: blue; 
        green = green < 0 ? 0: green;
       
       color = new Color(red,green,blue);
        if(life <= 0)
        return true;
        
        return false;
    }
    
    public void render(Graphics g)
    {
        Brush.fillCircle(g, this.p, this.radius, this.color);
    }
   }

}
