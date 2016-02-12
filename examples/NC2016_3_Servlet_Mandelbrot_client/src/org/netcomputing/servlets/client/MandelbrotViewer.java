package org.netcomputing.servlets.client;

import java.awt.BorderLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.net.URL;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

/**
 * Test client image server
 * 
 * Wico Mulder 20 - 08 -2003
 */

public class MandelbrotViewer extends JFrame implements ActionListener {

	private static final long serialVersionUID = 1L;
	private static String URLSTRING = "http://localhost:8080/NC2016_3_Servlet_Mandelbrot/MandelbrotServlet";
	private ImagePanel imagepanel;
	private double minx, miny, maxx, maxy;
	int width, height;

	JButton left, right, up, down, zoomin, zoomout;
	JButton updatewidthheight;
	JTextField widthfield, heightfield;
	JLabel infolabel;

	public MandelbrotViewer() {
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		left = new JButton("L");
		right = new JButton("R");
		up = new JButton("U");
		down = new JButton("D");
		zoomin = new JButton("in");
		zoomout = new JButton("out");

		left.addActionListener(this);
		right.addActionListener(this);
		up.addActionListener(this);
		down.addActionListener(this);
		zoomin.addActionListener(this);
		zoomout.addActionListener(this);
		infolabel = new JLabel();

		JPanel buttonpanel = new JPanel(new BorderLayout());
		JPanel zoombuttonpanel = new JPanel(new GridLayout(2, 1));
		zoombuttonpanel.add(zoomin);
		zoombuttonpanel.add(zoomout);
		buttonpanel.add(zoombuttonpanel, BorderLayout.CENTER);
		buttonpanel.add(left, BorderLayout.WEST);
		buttonpanel.add(right, BorderLayout.EAST);
		buttonpanel.add(up, BorderLayout.NORTH);
		buttonpanel.add(down, BorderLayout.SOUTH);

		widthfield = new JTextField("320");
		heightfield = new JTextField("240");
		updatewidthheight = new JButton("update width & height");
		updatewidthheight.addActionListener(this);
		JPanel widthheight = new JPanel(new GridLayout(2, 1));
		widthheight.add(widthfield);
		widthheight.add(heightfield);
		JPanel sizepanel = new JPanel(new GridLayout(1, 2));
		sizepanel.add(widthheight);
		sizepanel.add(updatewidthheight);
		JPanel buttonandsizepanel = new JPanel(new GridLayout(2, 1));
		buttonandsizepanel.add(buttonpanel);
		buttonandsizepanel.add(sizepanel);
		imagepanel = new ImagePanel();
		JPanel mainpanel = new JPanel(new BorderLayout());
		mainpanel.add(infolabel, BorderLayout.NORTH);
		mainpanel.add(imagepanel, BorderLayout.CENTER);
		mainpanel.add(buttonandsizepanel, BorderLayout.SOUTH);
		minx = -2;
		miny = -1;
		maxx = 0.5;
		maxy = 1;
		width = Integer.parseInt(widthfield.getText());
		height = Integer.parseInt(heightfield.getText());
		refresh();
		getContentPane().add(mainpanel);
		setSize(width + 20, height + 300);
		setVisible(true);
	}

	public void refresh() {
		String sminx = Double.toString(minx);
		String sminy = Double.toString(miny);
		String smaxx = Double.toString(maxx);
		String smaxy = Double.toString(maxy);
		String swidth = Integer.toString(width);
		String sheight = Integer.toString(height);
		String params = "?minx=" + sminx + "&miny=" + sminy + "&maxx=" + smaxx
				+ "&maxy=" + smaxy + "&width=" + swidth + "&height=" + sheight;
		try {
			URL u = new URL(URLSTRING + params);
			ImageIcon icon = new ImageIcon(u);
			imagepanel.setImage(icon.getImage());
		} catch (IOException e) {
			System.err.println(e);
		}
		infolabel.setText("(x,y)=(" + minx + "," + miny + "):(" + maxx + ","
				+ maxy + ")" + params);
	}

	public void actionPerformed(ActionEvent e) {
		JButton b = (JButton) e.getSource();
		if (b == zoomin) {
			zoom(0.1);
		}
		if (b == zoomout) {
			zoom(-0.1);
		}
		if (b == left) {
			delta(-0.1, 0);
		}
		if (b == right) {
			delta(0.1, 0);
		}
		if (b == up) {
			delta(0, -0.1);
		}
		if (b == down) {
			delta(0, 0.1);
		}
		if (b == updatewidthheight) {
			width = Integer.parseInt(widthfield.getText());
			height = Integer.parseInt(heightfield.getText());
		}
		refresh();
	}

	public void delta(double factorx, double factory) {
		minx += factorx * (maxx - minx);
		maxx += factorx * (maxx - minx);
		miny += factory * (maxy - miny);
		maxy += factory * (maxy - miny);
	}

	public void zoom(double factor) {
		double centerx = (maxx + minx) / 2.0;
		double centery = (maxy + miny) / 2.0;
		minx = centerx - ((centerx - minx) * (1.0 - factor));
		maxx = centerx + ((maxx - centerx) * (1.0 - factor));
		miny = centery - ((centery - miny) * (1.0 - factor));
		maxy = centery + ((maxy - centery) * (1.0 - factor));
	}

	public static void main(String[] args) {
		new MandelbrotViewer();
	}

}
