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
		rightPanel = new RightPanel();
		drawManager = new DrawManager();
		frame.add(rightPanel,BorderLayout.EAST);
		frame.add(drawManager, BorderLayout.CENTER);
		frame.setVisible(true);
	}

}
