import java.awt.Color;
import java.awt.Dimension;

import javax.swing.JPanel;

public class RightPanel extends JPanel
{
	private DrawManager drawManager;
	
	public RightPanel(DrawManager drawManager)
	{		
		this.drawManager = drawManager;
		setPreferredSize(new Dimension(200,1000));
	}
}
