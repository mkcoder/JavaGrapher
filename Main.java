import java.awt.BorderLayout;

import javax.swing.JFrame;

public class Main
{
	public static void main(String[] argv)
	{
		JFrame frame;
		RightPanel rightPanel;
		DrawManager drawManager;		
		
		frame = new JFrame("Grapher");
		frame.setSize(1024, 768);		
		drawManager = new DrawManager();
		rightPanel = new RightPanel(drawManager);
		frame.add(rightPanel,BorderLayout.EAST);
		frame.add(drawManager, BorderLayout.CENTER);		
		frame.setVisible(true);
		drawManager.initialize();
	}

}
