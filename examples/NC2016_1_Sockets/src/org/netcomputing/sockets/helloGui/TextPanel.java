package org.netcomputing.sockets.helloGui;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextPane;
import javax.swing.ScrollPaneConstants;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Style;
import javax.swing.text.StyleConstants;
import javax.swing.text.StyledDocument;

public class TextPanel extends JPanel {

	private static final long serialVersionUID = 1L;

	JPanel panel;

	JScrollPane scrollpane;

	JTextPane textPane;

	public static TextPanel instance = new TextPanel();

	private StyledDocument swing_styled_document;

	private Style style;


	private Color defaultcolor = Color.black;

	private Color highlightcolor = Color.blue;

	public TextPanel() {
		super();
		setLayout(new BorderLayout());
		textPane = new JTextPane();
		swing_styled_document = new DefaultStyledDocument();
		style = swing_styled_document.addStyle("StyleName", null);
		StyleConstants.setFontFamily(style, "Courier New");
		setTextColor(defaultcolor);
		setFontsize(14);
		textPane.setDocument(swing_styled_document);
		textPane.setEditable(false);
		scrollpane = new JScrollPane(textPane);
		scrollpane.setHorizontalScrollBarPolicy(ScrollPaneConstants.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		add(scrollpane);
	}

	
	public void add(String s) {
		try {
			swing_styled_document.insertString(swing_styled_document.getLength(), s, style);
			textPane.setCaretPosition(swing_styled_document.getLength() - 1);
		} catch (BadLocationException e) {
			System.err.println(e);
		}
	}

	public void addNextLine(String s) {
		try {
			add("\n");
			swing_styled_document.insertString(swing_styled_document.getLength(), s, style);
			textPane.setCaretPosition(swing_styled_document.getLength() - 1);
		} catch (BadLocationException e) {
			System.err.println(e);
		}
	}

	public StyledDocument getSwing_styled_document() {
		return swing_styled_document;
	}

	public void setSwing_styled_document(StyledDocument doc) {
		this.swing_styled_document = doc;
	}

	public int getFontsize() {
		return StyleConstants.getFontSize(style);
	}

	public void setFontsize(int fontsize) {
		StyleConstants.setFontSize(style, fontsize);
	}

	public void cls() {
		try {
			swing_styled_document.remove(0, swing_styled_document.getLength());
		} catch (BadLocationException e) {
			System.err.println(e);
		}
	}

	public void setEditable(boolean editable) {
		textPane.setEditable(editable);
	}

	public String getText() {
		return textPane.getText();
	}

	public void setText(String text) {
		textPane.setText(text);
	}

	public void setBackground(Color defaultbackground) {
		if (textPane != null) {
			textPane.setBackground(defaultbackground);
		}
	}

	public void setTextColor(Color color) {
		StyleConstants.setForeground(style, color);
		if (color != highlightcolor) {
			defaultcolor = color;
		}
	}

	public void setTextBackground(Color color) {
		StyleConstants.setBackground(style, color);
	}

	public void setBold(boolean bold) {
		StyleConstants.setBold(style, bold);
	}

}
