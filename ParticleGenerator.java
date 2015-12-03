import java.awt.*;
import java.util.ArrayList;


public class ParticleGenerator
{
    private String expression;                 //String representing the math function
    private ArrayList<Particle> particles;    //List of all the particles 
    private DrawManager drawManager;          //The drawManager associated with the GUI
    
 
    public ParticleGenerator(String expression, Color color, DrawManager d)
    //PRE: expression should be valid mathematical expression
    //     color should have a valid rgb values. 
    //     d should be an intialized drawManager object.
    //POST: a ParticleGenerator object is created that will draw particles onto
    //      expression passed in.
    {  
        this.expression = expression;
        this.particles = new ArrayList<Particle>();
        this.drawManager = d; 
    }
 

    public void setExpression(String expression)
    //PRE: expression is a string that is intialized
    //POST: FCTVAL = this.expression is updated to the passed in value.
    {
        this.expression = expression;
    }
    
    public void draw(Graphics g, DrawManager d, Color color)
    //PRE: g and d must be initialized. color should have a valid rgb values.
    //
    //POST: Up to 500 particles are drawn onto the function.
    //
    {
        update(color);
        render(g, color);
    }

    private void update(Color color)
    //PRE: color is a valid
    {
        if(particles.isEmpty()) //Initially our list is empty
                                //thus we have to add one seed particle
        {
            this.addParticle(color);
            return;
        }
       
        for(int i = 0; i < particles.size(); i++)
        {
            double particleDensity;     // Determines how many particles will be on the screen
            boolean remove;             //Has the particle reached the end of its life
            Particle p;                 //Each particle 
            Point p1;
            int nextPosition;          //The next x position
            double x;
            double y;
            
            p = particles.get(i);            
            nextPosition = p.nextPosition;
            
            x = drawManager.screenToGlobalX(nextPosition);     //map origin to global coordinate 
            y = Expression.evaluate(expression.replace("x", x+""));//Evaluate the expression at 
                                                                   // this new x position
            
            p1 = new Point();
            p1.x = nextPosition;
            p1.y = drawManager.globalToScreenY(-y);                               

            remove = p.update(p1);         
            particleDensity = Math.random();

            
            if(remove || (particles.size() < 500 && particleDensity < 0.01))//If we need to generate a new
                                                                           // particle
            {
                if(remove)//if remove is true, then we remove the particle from our list
                {
                    particles.remove(i);
                }
                this.addParticle(color);
            }
           
        }
    }
    
    private void addParticle(Color color)
    //POST: FCTVAL = a particle is added to the fuction and to the list
    {
        Point p;
        double x;
        double y;
        int randomLifeSpan;
        
        x = drawManager.screenToGlobalX(0);  //map origin to global coordinate          
        y = Expression.evaluate(expression.replace("x", x+"")); //Evaluate the expression at 0
        
        randomLifeSpan = (int)(Math.random()*(500));           //Generate a random life span

        p = new Point();
        
        p.x = 0;
        p.y = drawManager.globalToScreenY(-y); //Set the y-value 
                                               //to the our expression evaluated to
        
        particles.add(new Particle(p,3,randomLifeSpan,color)); //Create a particle at our evaluated point
    }
    
    private void render(Graphics g, Color color)
    //POST: FCTVAL = every particle in the list is rendered on the GUI
    //      at their point.     
    {
        for(Particle p : particles)
        {
            p.render(g,color);
        }

    }

private class Particle
   {
       private Point p;         //The location of the particle
       private int radius;      //Radius of the particle
       private int nextPosition;//The next location of the particle
       private Color color;     //The color of the particle
       private int life;        //The life span of the particle


    public Particle(Point p, int radius, int life, Color color)
    //PRE: p is an intialized point, radius > 0, life  > 0 
    //     and color has valid rgb values
    //POST: a particle is initalized with the given attributes
    {
        this.p = p;
        this.radius = radius;
        this.life = life;
        this.nextPosition = 1;
        this.color = color;
    }
    
    public boolean update(Point p)
    //PRE: p is a point within the limits of the GUI width and height
    //POST: FCTVAL = a boolean that indicates whether the particle is 
    //     still alive.
    {        
        nextPosition++;    
        this.p = p;
        life--;
        
        if(life <= 0) //if live is less that or equal to zero
                      //then this particle has died.
        return true;
        
        return false;
    }
    
    public void render(Graphics g, Color userColor)
    //PRE: g is initialed, userColor has valid rgb values
    //POST: the particle is rendered on the GUI with the given
    //      user color.
    {
        Brush.fillCircle(g, this.p, this.radius, userColor);
    }
   }

}
