package Bangla.gui;

import java.awt.Font;
import java.awt.FontFormatException;
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class CustomizedFileChooser extends JFileChooser{
	private final static String fontpath="/resources/font/";
	Font font;
	/**
	 * Creates a castomized File chooser
	 * @param currentDirectoryPath 
	 */
	public CustomizedFileChooser(String currentDirectoryPath){
		super(currentDirectoryPath);
		FileNameExtensionFilter filter=new FileNameExtensionFilter("Text Files (*.txt)","txt");
		setFileFilter(filter);
		Font font=null;
		try{
			font=Font.createFont(Font.TRUETYPE_FONT,getClass().getResourceAsStream(fontpath+"Bangla Akademi.ttf"));
			font=font.deriveFont(Font.PLAIN,18);
		}catch(IOException exc){}
		catch(FontFormatException exc){}
		catch(IllegalArgumentException exc){}
		setFont(font);
	}
}