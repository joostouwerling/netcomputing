package org.netcomputing.servlets.client;
import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;

import javax.swing.JPanel;

public class ImagePanel extends JPanel{
	private static final long serialVersionUID = 1L;
	private Image image;
	
	public ImagePanel(){
	 setBackground(Color.white);
	}
	
	public void setImage(Image image){
		this.image=image;
		repaint();
	}
	
	public void paint(Graphics g){
		super.paint(g);
		int cx=(getWidth()-image.getWidth(null))/2;
		int cy=(getHeight()-image.getHeight(null))/2;
		g.drawImage(image,cx,cy,null);
	}
	
}
