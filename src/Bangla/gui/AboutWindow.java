package Bangla.gui;

import Bangla.gui.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
/**
 * Window that views the information about software
 */
public class AboutWindow extends JFrame{
	private static String resourcepath="/resources/";
	private static String imagepath="/resources/images/";
	private JButton btn_ok;
	/**
	 * Creates a window that views the information about software
	 * @param win : Parent window
	 */
	public AboutWindow(MainWindow win){
		super("-:About \u0986mar-\u0987 \u09AC\u09BE\u0982\u09B2\u09BE:-");
		this.setIconImage(win.getIconImage());
		this.setLayout(new BorderLayout());
		
		JLabel logo=new JLabel(new ImageIcon(getClass().getResource(imagepath+"logo.png")));
		this.add(logo,BorderLayout.NORTH);
		
		JTextArea text=new JTextArea();
		text.setEditable(false);
		try{
			InputStreamReader in=new InputStreamReader(
					getClass().getResourceAsStream(resourcepath+"about.txt"),
					Charset.availableCharsets().get("UTF-8")
				);
			char data[]=new char[1000];
			int length=in.read(data);
			text.setText(String.valueOf(data,0,length));
			in.close();
		}catch(IOException exc){
			JOptionPane.showMessageDialog(null,"Resource file read error","File Read Error",JOptionPane.ERROR_MESSAGE);				
		}
		text.setCaretPosition(0);
		this.add(text,BorderLayout.CENTER);
		
		this.btn_ok=new JButton("OK");
		this.btn_ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dispose();
			}
		});
		JPanel panel=new JPanel();
		panel.setLayout(new FlowLayout());
		panel.add(btn_ok);
		this.add(panel,BorderLayout.SOUTH);
		
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setSize(400,400);
		Dimension d=getToolkit().getScreenSize();
		this.setLocation((d.width-getWidth())/2,(d.height-getHeight())/2);
		this.setAlwaysOnTop(true);
		this.setVisible(true);
	}
}