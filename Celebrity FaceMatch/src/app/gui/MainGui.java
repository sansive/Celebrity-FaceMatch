package app.gui;

import app.agents.PerceptionAgent;

import java.awt.GraphicsEnvironment;
import java.awt.GraphicsDevice;
import java.awt.GraphicsConfiguration;
import java.awt.Rectangle;

public class MainGui extends Thread {
	
	String title;
	PerceptionAgent agent;
	JFrameApp frame;

	public MainGui(String title, PerceptionAgent agent) {
		this.title = title;
		this.agent = agent;
		this.frame = new JFrameApp(title, agent);
	}
	
	public void run() {
        GraphicsEnvironment ge = GraphicsEnvironment.getLocalGraphicsEnvironment();
        GraphicsDevice gd=ge.getDefaultScreenDevice();
        GraphicsConfiguration gc = gd.getDefaultConfiguration();
        Rectangle rectangle=gc.getBounds();;
        
        frame.setLocation((int)rectangle.getWidth()/7,(int)rectangle.getHeight()/7);
        frame.setBounds(100, 100, 755, 526);
        
        frame.setTitle(title);
        frame.setVisible(true);
        frame.setResizable(false);
	}
	
	public void showResults(String name1, String name2, String name3) {
		frame.ShowResults(name1, name2, name3);
	}

}