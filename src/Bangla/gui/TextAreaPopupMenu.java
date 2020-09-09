package Bangla.gui;

import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontFormatException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.io.File;
import java.io.IOException;
import java.awt.GridLayout;
import java.awt.FlowLayout;
import java.awt.BorderLayout;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import Bangla.util.*;

/**
 * TextArea Popup Menu
 */
public class TextAreaPopupMenu extends JPopupMenu{
	private final MainWindow win;
	private JMenuItem menu_prediction[],menu_addtodictionary,menu_cut,menu_copy,menu_paste;
	private Font font;
	/**
	 * Creates a popup menu for the textarea of window
	 * @param win : The main window
	 */
	TextAreaPopupMenu(final MainWindow win){
		this.win=win;
		// Add to Dictionary
		menu_addtodictionary=new JMenuItem("Add to Dictionary");
		this.add(menu_addtodictionary);
		menu_addtodictionary.addActionListener(new ActionAddToDictionary(win));
		// Separator
		this.addSeparator();
		// Cut
		menu_cut=new JMenuItem("Cut");
		this.add(menu_cut);
		menu_cut.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        win.getSelectedWorkSpace().getTextArea().cut();
                        try{
                            win.getSelectedWorkSpace().getTextArea().grabFocus();
                        }catch(NullPointerException exc){}
                    }
                });
		//Copy
		menu_copy=new JMenuItem("Copy");
		this.add(menu_copy);
		menu_copy.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        win.getSelectedWorkSpace().getTextArea().copy();
                        try{
                            win.getSelectedWorkSpace().getTextArea().grabFocus();
                        }catch(NullPointerException exc){}
                    }
                });
		// Paste
		menu_paste=new JMenuItem("Paste");
		this.add(menu_paste);
		menu_paste.addActionListener(new ActionListener(){
                    public void actionPerformed(ActionEvent e){
                        win.getSelectedWorkSpace().getTextArea().paste();
                        try{
                            win.getSelectedWorkSpace().getTextArea().grabFocus();
                        }catch(NullPointerException exc){}
                    }
                });
		// Separator
		this.addSeparator();
		//Prediction
		menu_prediction=new JMenuItem[]{new JMenuItem("No suggestion available")};
		menu_prediction[0].setEnabled(false);
	}
	/**
	 * Creates menu buttons for given prediction values
	 * @param prediction : prediction vaslues as string
	 */
	void setPredictions(String[] prediction){
		// Removing old predictions
		for(int i=0;i<menu_prediction.length;i++){
			this.remove(menu_prediction[i]);
		}
		// Adding new predictions
		if(prediction.length==0){
			menu_prediction=new JMenuItem[]{new JMenuItem("No suggestion available")};
			this.add(menu_prediction[0]);
			menu_prediction[0].setEnabled(false);
		}
		else{
			menu_prediction=new JMenuItem[prediction.length];
			for(int i=0;i<prediction.length;i++){
				menu_prediction[i]=new JMenuItem(prediction[i]);
				menu_prediction[i].setFont(win.getComponentFont());
				this.add(menu_prediction[i]);
				menu_prediction[i].addActionListener(new ActionReplacePrediction(win,prediction[i]));
			}
		}
	}
}
/**
 * Action listener for button of replacing by predicted word
 */
class ActionReplacePrediction implements ActionListener{
	String word;
	MainWindow win;
	public ActionReplacePrediction(MainWindow win,String word){
		this.win=win;
		this.word=word;
	}
	public void actionPerformed(ActionEvent e){
		// Extracing the word to be deleted
		int pos=win.getSelectedWorkSpace().getTextArea().getCaretPosition();
		String txt=win.getSelectedWorkSpace().getTextArea().getText();
		try{
			for(;pos>=0;pos--)
				if(CType.isSpace(txt.charAt(pos))||CType.isPunct(txt.charAt(pos)))
					break;
		}catch(StringIndexOutOfBoundsException exc){}
		pos++;
		int length=0;
		try{
			for(;pos+length<txt.length();length++)
				if(CType.isSpace(txt.charAt(pos+length))||CType.isPunct(txt.charAt(pos+length)))
					break;
		}catch(StringIndexOutOfBoundsException exc){}
		// Replacement
		try{
			txt=txt.substring(0,pos)+word+txt.substring(pos+length);
		}catch(IndexOutOfBoundsException exc){}
		win.getSelectedWorkSpace().getTextArea().setText(txt);
		win.getSelectedWorkSpace().getTextArea().setCaretPosition(pos+length);
	}
}

/**
 * Action listener for button "Add to dictionary"
 */
class ActionAddToDictionary implements ActionListener{
	private MainWindow win;
	public ActionAddToDictionary(MainWindow win){
		this.win=win;
	}
	public void actionPerformed(ActionEvent e){
		// Extracting the word
		int pos=win.getSelectedWorkSpace().getTextArea().getCaretPosition();
		String txt=win.getSelectedWorkSpace().getTextArea().getText();
		try{
			for(;pos>=0;pos--)
				if(CType.isSpace(txt.charAt(pos))||CType.isPunct(txt.charAt(pos)))
					break;
		}catch(StringIndexOutOfBoundsException exc){}
		pos++;
		int length=0;
		try{
			for(;pos+length<txt.length();length++)
				if(CType.isSpace(txt.charAt(pos+length))||CType.isPunct(txt.charAt(pos+length)))
					break;
		}catch(StringIndexOutOfBoundsException exc){}
		if(length==0)
			return;
		final String word=txt.substring(pos,pos+length);
		// Checking whether the word is english or not
		boolean english=true;
		for(int i=0;i<word.length();i++)
			if(CType.isBengaliLetter(word.charAt(i))){
				english=false;
				break;
			}
		// English
		if(english){
			win.getEnglishDictionary().addToDictionary(word);
			return;
		}
		// Bengali
		final JDialog dialog=new JDialog(win,"");
		final JPanel panel=new JPanel(new BorderLayout());
		final JPanel npanel=new JPanel(new FlowLayout());
		final JPanel cpanel=new JPanel(new GridLayout(2,5));
		final JPanel spanel=new JPanel(new FlowLayout());
		final JButton btn_ok=new JButton("OK");
		final JButton btn_cancel=new JButton("Cancel");
		final JCheckBox radiobutton[]={
				new JCheckBox("\u09AC\u09BF\u09B6\u09C7\u09B7\u09CD\u09AF"),
				new JCheckBox("\u0995\u09CD\u09B0\u09BF\u09DF\u09BE \u09AC\u09BF\u09B6\u09C7\u09B7\u09CD\u09AF"),
				new JCheckBox("\u09AC\u09BF\u09B6\u09C7\u09B7\u09A3"),
				new JCheckBox("\u09AC\u09BF\u09B6\u09C7\u09B7\u09A3\u09C7\u09B0 \u09AC\u09BF\u09B6\u09C7\u09B7\u09A3"),
				new JCheckBox("\u0995\u09CD\u09B0\u09BF\u09DF\u09BE \u09AC\u09BF\u09B6\u09C7\u09B7\u09A3"),
				new JCheckBox("\u09B8\u09B0\u09CD\u09AC\u09A8\u09BE\u09AE"),
				new JCheckBox("\u0995\u09CD\u09B0\u09BF\u09DF\u09BE"),
				new JCheckBox("\u0985\u09B8\u09AE\u09BE\u09AA\u09BF\u0995\u09BE \u0995\u09CD\u09B0\u09BF\u09DF\u09BE"),
				new JCheckBox("\u0985\u09A8\u09C1\u0995\u09CD\u09B0\u09BF\u09DF\u09BE"),
				new JCheckBox("\u0985\u09AC\u09CD\u09AF\u09DF")
		};
		final JLabel lbl=new JLabel("Choose Parts of Speech of \'"+word+"\' :");
		dialog.setContentPane(panel);
		panel.add(npanel,BorderLayout.NORTH);
		panel.add(cpanel,BorderLayout.CENTER);
		panel.add(spanel,BorderLayout.SOUTH);
		npanel.add(lbl);
		lbl.setFont(win.getComponentFont());
		for(int i=0;i<radiobutton.length;i++){
			cpanel.add(radiobutton[i]);
			radiobutton[i].setFont(win.getComponentFont());
			radiobutton[i].addKeyListener(new KeyAdapter(){
				public void keyReleased(KeyEvent e){
					switch(e.getKeyCode()){
					case KeyEvent.VK_ENTER:
						btn_ok.doClick();
						break;
					case KeyEvent.VK_ESCAPE:
						btn_cancel.doClick();
						break;
					}
				}
			});
		}
		spanel.add(btn_ok);
		spanel.add(btn_cancel);
		dialog.pack();
		Dimension d=dialog.getToolkit().getScreenSize();
		dialog.setLocation((d.width-dialog.getWidth())/2,(d.height-dialog.getHeight())/2);		
		dialog.setResizable(false);
		btn_ok.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				// Adding to dictionary
				String abbr[]={
						"(\u09AC\u09BF)",
						"(\u0995\u09CD\u09B0\u09BF\u09AC\u09BF)",
						"(\u09AC\u09BF\u09A3)",
						"(\u09AC\u09BF\u09A3\u09AC\u09BF\u09A3)",
						"(\u0995\u09CD\u09B0\u09BF\u09AC\u09BF\u09A3)",
						"(\u09B8\u09B0\u09CD\u09AC)",
						"(\u0995\u09CD\u09B0\u09BF)",
						"(\u0985\u09B8\u0995\u09CD\u09B0\u09BF)",
						"(\u0985\u09A8\u09C1\u0995\u09CD\u09B0\u09BF)",
						"(\u0985\u09AC\u09CD\u09AF)"
				};
				try{
					for(int i=0;i<radiobutton.length;i++){
						if(radiobutton[i].isSelected())
							win.getBengaliDictionary().addToDictionary(word,abbr[i]);
					}
				}catch(IndexOutOfBoundsException exc){}
				dialog.dispose();
			}
		});
		btn_cancel.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				dialog.dispose();
			}
		});
		dialog.setVisible(true);
	}
}