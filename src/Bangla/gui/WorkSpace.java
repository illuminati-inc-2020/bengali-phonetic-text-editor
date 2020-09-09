/* Subject			:	Class WorkSpace for Bengali phonetic Software
 * Date				: 	24/08/2011
 */

package Bangla.gui;

import Bangla.language.*;
import java.util.Stack;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JTextArea;
import javax.swing.JScrollPane;
import javax.swing.JOptionPane;

import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.event.KeyEvent;
import java.awt.event.KeyAdapter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;

/**
 * contains a scrollable textarea with save,load, undo, redo operation
 */
public class WorkSpace extends JScrollPane{
	private static final String imagepath="/resources/images/";
	private final MainWindow win;
	private final JTextArea textarea;
	private String name;
	private File file;
	private final Stack<TextAreaAction> undostack;
	private final Stack<TextAreaAction> redostack;
	private String text_backup;
	private SpellChecker spellchecker;
	/**
	 * Creates a blank workspace named "New File" 
	 */
	public WorkSpace(MainWindow win,String name){
		super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.win=win;
		this.name=name;
		this.textarea=new JTextArea();
		this.setViewportView(this.textarea);
		this.file=null;
		this.undostack=new Stack<TextAreaAction>();
		this.redostack=new Stack<TextAreaAction>();
		this.text_backup=this.textarea.getText();
		new Runnable(){
			private Thread t;			
			public void run(){
				while(true){
					updateUndoStack();
					try{
						Thread.sleep(100);
					}catch(InterruptedException exc){}
				}
			}
			public void start(){
				t=new Thread(this,"WorkSpace "+getName());
				t.start();
			}
		}.start();
		this.textarea.setCaretPosition(0);
		this.textarea.grabFocus();
	}
	/**
	 * Creates a workspace loading a given file
	 * @param file : file stream to be loaded
	 */
	public WorkSpace(MainWindow win,File file){
		super(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS,JScrollPane.HORIZONTAL_SCROLLBAR_AS_NEEDED);
		this.win=win;
		this.name=file.getName();
		this.textarea=new JTextArea();
		this.setViewportView(this.textarea);
		this.file=file;
		this.undostack=new Stack<TextAreaAction>();
		this.redostack=new Stack<TextAreaAction>();
		try{
			InputStreamReader in=new InputStreamReader(
					new FileInputStream(file),
					Charset.availableCharsets().get("UTF-8")
				);
			char data[]=new char[(int)file.length()];
			int length=in.read(data);
			this.textarea.setText(String.valueOf(data,0,length));
			in.close();
		}catch(FileNotFoundException exc){
			JOptionPane.showMessageDialog(win,
                    "File \""+file.getPath()+"\" does not exist",
                    "Error",
                    JOptionPane.INFORMATION_MESSAGE);
		}
		catch(java.io.IOException exc){
			JOptionPane.showMessageDialog(win,
                    "File \""+file.getPath()+"\" read error",
                    "Error",
                    JOptionPane.ERROR_MESSAGE);
		}
		this.text_backup=this.textarea.getText();
		new Runnable(){
			private Thread t;			
			public void run(){
				while(true){
					updateUndoStack();
					try{
						Thread.sleep(100);
					}catch(InterruptedException exc){}
				}
			}
			public void start(){
				t=new Thread(this,"WorkSpace "+getName());
				t.start();
			}
		}.start();
		this.textarea.setCaretPosition(0);
		this.textarea.grabFocus();
	}
	/**
	 * Saves the workspace to a file
	 * @param file : If null the file is saved to currenty opened file
	 * @return true on success and false on failure
	 */
	public boolean save(File file){
		if(file==null)
			file=this.getFile();
		else{
			if(file.exists()){
				int result=JOptionPane.showConfirmDialog(
						null,
						"Do you really want to overwrite the existing file?",
						"File alrerady exists",
						JOptionPane.YES_NO_OPTION);
				if(result==JOptionPane.NO_OPTION)
					return false;
			}
			else{
				file=new File(file.getPath()+".txt");
			}
		}
		try{
			OutputStreamWriter out=new OutputStreamWriter(
					new FileOutputStream(file),
					Charset.availableCharsets().get("UTF-8")
				);
			out.write(this.textarea.getText());
			out.close();
		}catch(java.io.IOException exc){
			JOptionPane.showMessageDialog(win,
                                "File \""+file.getPath()+"\" not found",
                                "Error",
                                JOptionPane.ERROR_MESSAGE);
		}
		this.name=file.getName();
		this.file=file;
		this.undostack.clear();
		this.redostack.clear();
		return true;
	}
	/**
	 * Set workspace name
	 */
	public void setName(String name){
		this.name=name;
	}
	/**
	 * @return workspace name 
	 */
	public String getName(){
		return this.name;
	}
	/**
	 * @return textarea object 
	 */
	public JTextArea getTextArea(){
		return this.textarea;
	}
	/**
	 * @return false if the workspace is unsaved otherwise returns true
	 */
	public boolean isSaved(){
		return undostack.isEmpty();
	}
	/**
	 * @return the opened in the workspace and null if no file is opened
	 */ 
	public File getFile(){
		return this.file;
	}
	/**
	 * Updates the undo stack and flushes the redo stack on external action
	 */
	private synchronized void updateUndoStack(){
		String text=textarea.getText();
		if(!text_backup.equals(text)){
			int start=0;
			int end1=text.length()-1;
			int end2=text_backup.length()-1;
			while(start<end1 && start<end2 && text_backup.charAt(start)==text.charAt(start))
				start++;
			while(start<end1 && start<end2 && text_backup.charAt(end2)==text.charAt(end1)){
				end1--;
				end2--;
			}
			try{
				undostack.push(new TextAreaAction(
						start,
						(text_backup.length()==0)?"":text_backup.substring(start,++end2),
						(text.length()==0)?"":text.substring(start,++end1)
					));
			}catch(StringIndexOutOfBoundsException exc){}
			text_backup=text;
			redostack.clear();
		}
	}
	/**
	 * Undos one action
	 */
	public synchronized void undo(){
		if(!undostack.isEmpty()){
			TextAreaAction temp=undostack.pop();
			String txt=textarea.getText();
			try{
				txt=txt.substring(0,temp.getPosition())+
						temp.getInputString()+
						txt.substring(temp.getPosition()+temp.getOutputString().length());
			}catch(StringIndexOutOfBoundsException exc){}
			textarea.setText(txt);
			textarea.setCaretPosition(temp.getPosition()+temp.getInputString().length());
			redostack.push(temp);
			text_backup=textarea.getText();
		}
	}
	/**
	 * Redos one action
	 */
	public synchronized void redo(){
		if(!redostack.isEmpty()){
			TextAreaAction temp=redostack.pop();
			String txt=textarea.getText();
			try{
				txt=txt.substring(0,temp.getPosition())+
						temp.getOutputString()+
						txt.substring(temp.getPosition()+temp.getInputString().length());
			}catch(StringIndexOutOfBoundsException exc){}
			textarea.setText(txt);
			textarea.setCaretPosition(temp.getPosition()+temp.getOutputString().length());
			undostack.push(temp);
			text_backup=textarea.getText();
		}
	}
	/**
	 * @return whether undo stack is empty
	 */
	public boolean isUndoStackEmpty(){
		return this.undostack.isEmpty();
	}
	/**
	 * @return whether redo stack is empty
	 */
	public boolean isRedoStackEmpty(){
		return this.redostack.isEmpty();
	}
	/**
	 * @param wrap : indicates if lines should be wrapped
	 */
	public void setLineWrap(boolean wrap){
		this.textarea.setLineWrap(wrap);
	}
	/**
	 * @return if lines will be wrapped
	 */
	public boolean getLineWrap(){
		return this.textarea.getLineWrap();
	}
	/**
	 * @param spellhecker : Sets spellchecker for workspace
	 */
	public void setSpellChecker(SpellChecker spellhecker){
		this.spellchecker=spellhecker;
	}
	/**
	 * @return spellchecker
	 */
	public SpellChecker getSpellChecker(){
		return spellchecker;
	}
}

/**
 * A structure for storing record of an action that can be performed in a textarea
 */
class TextAreaAction{
	private int pos;				// Starting position of action
	private String ip;				// Deleted string 
	private String op;				// Inserted String
	/**
	 * Creates a textarea action
	 */
	public TextAreaAction(int pos,String input,String output){
		this.pos=pos;
		this.ip=input;
		this.op=output;
	}
	/**
	 * @return starting position of action
	 */
	public int getPosition(){
		return this.pos;
	}
	/**
	 * @return The deleted string 
	 */
	public String getInputString(){
		return this.ip;
	}
	/**
	 * @return The inserted string
	 */
	public String getOutputString(){
		return this.op;
	}
}
