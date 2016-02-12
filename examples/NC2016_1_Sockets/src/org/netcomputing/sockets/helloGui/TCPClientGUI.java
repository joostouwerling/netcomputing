package org.netcomputing.sockets.helloGui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.net.UnknownHostException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.Border;

/**
 * Basic TCP socket example, client side
 */
public class TCPClientGUI extends JFrame implements ActionListener {
	private static String SERVER_ADDRESS = "localhost";
	private static int SERVER_PORT = 7375;
	private JButton send;
	private JTextField inputfield;
	private JLabel responsefield;

	public TCPClientGUI() {
		super("client");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		send = new JButton("send");
		send.addActionListener(this);
		this.getContentPane().add(send, BorderLayout.SOUTH);
		JPanel middle = new JPanel(new GridLayout(2, 1));
		Border paneEdge = BorderFactory.createEmptyBorder(10, 10, 10, 10);
		middle.setBorder(paneEdge);
		middle.setBackground(new Color(255,255,128));
		inputfield = new JTextField();
		responsefield = new JLabel("...");
		middle.add(inputfield);
		middle.add(responsefield);
		this.getContentPane().add(middle, BorderLayout.CENTER);
		setSize(300, 200);
		setVisible(true);
	}

	@Override
	public void actionPerformed(ActionEvent arg0) {
		Socket s = null;
		DataOutputStream out;
		DataInputStream in;
		try {
			s = new Socket(SERVER_ADDRESS, SERVER_PORT);
			in = new DataInputStream(s.getInputStream());
			out = new DataOutputStream(s.getOutputStream());
			out.writeUTF(inputfield.getText());
			String data = in.readUTF();
			responsefield.setText(data);
		} catch (UnknownHostException e) {
			System.err.println(e);
		} catch (IOException e) {
			System.err.println(e);
		} finally {
			if (s != null)
				try {
					s.close();
				} catch (IOException e) {
					System.err.println(e);
				}
		}
	}

	public static void main(String[] args) {
		new TCPClientGUI();
	}

}
