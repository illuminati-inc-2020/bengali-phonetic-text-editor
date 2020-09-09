package Bangla.gui;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import java.awt.Font;
import java.awt.FontFormatException;
import javax.swing.ImageIcon;

public class HelpWindow extends JFrame{
	private final static String resourcepath="/resources/";
	private Font font;
	/**
	 * Creates a Help Window
	 */
	public HelpWindow(MainWindow win){
		super("-:\u09B8\u09BE\u09B9\u09BE\u09AF\u09CD\u09AF:-");
		this.setIconImage(win.getIconImage());
		this.setLayout(new BorderLayout());
		
		// Writing the text
		JTextArea text=new JTextArea();
		text.setLineWrap(true);
		text.setFont(win.getComponentFont());
		text.setEditable(false);
		try{
			InputStreamReader in=new InputStreamReader(
					getClass().getResourceAsStream(resourcepath+"help.txt"),
					Charset.availableCharsets().get("UTF-8")
				);
			char data[]=new char[1000000];
			int length=in.read(data);
			text.setText(String.valueOf(data,0,length));
			in.close();
		}catch(IOException exc){
			JOptionPane.showMessageDialog(null,"Resource file read error","File Read Error",JOptionPane.ERROR_MESSAGE);
		}
		this.add(new JScrollPane(text,JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER),BorderLayout.CENTER);
		text.setCaretPosition(0);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(450,600);
		Dimension d=getToolkit().getScreenSize();
		this.setLocation((d.width-getWidth())/2,(d.height-getHeight())/2);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}
}