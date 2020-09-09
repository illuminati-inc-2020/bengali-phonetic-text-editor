/* Subject			:	Window Interface for Bengali phonetic Software
 * Author			:	Rakesh Malik
 * Date				: 	09/08/2011
 */

package Bangla.gui;

import Bangla.Bangla;
import Bangla.language.*;
import Bangla.util.*;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.Font;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.awt.FontFormatException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextArea;
import java.util.Vector;

/**
 * The main window class
 */
public class MainWindow extends JFrame{
	private static final String resourcepath="/resources/";
	private static final String imagepath="/resources/images/";
	private static final String fontpath="font/";
	private static final String datapath="data/";
	private JTabbedPane tabbedpane;
	private Vector<WorkSpace> workspace_vector=new Vector<WorkSpace>();
	private TextAreaMouseListener textareamouselistener;
	private TextAreaKeyListener textareakeylistener;
	private MainMenuBar mainmenubar;
	private MainToolBar maintoolbar;
	private SearchToolBar searchtoolbar;
	private TextAreaPopupMenu popupmenu;
	private int count=0;														// Counter used to generate new name for new unsaved file
	private Font fonts[];														// All Fonts
	private Font font;															// Currently used font in TextArea
	private Font compfont;														// Font used in other window components
	private Language language=Language.BENGALI;
	private boolean autocorrect_on=false;
	private boolean spell_check_on=true;
	private boolean line_wrap=true;
	private final BengaliDictionary dic_beng;
	private final EnglishDictionary dic_eng;
	private String current_directory="";										// Current directory in file chooser
	private JFrame busywindow=null;
	/**
	 *  Creates the main window
	 */
	public MainWindow(){
		super("-:\u0986mar-\u0987 \u09AC\u09BE\u0982\u09B2\u09BE:-");
		this.setIconImage(new ImageIcon(getClass().getResource(imagepath+"icon.png")).getImage());
		Bangla.setSplashScreenProgressBar(0);
		// Loading Fonts
		this.loadFonts();
		Bangla.setSplashScreenProgressBar(5);
		// Loading Dictionary
		dic_beng=new BengaliDictionary();
		dic_eng=new EnglishDictionary();
		Bangla.setSplashScreenProgressBar(70);
		// Creating window
		this.addWindowListener(new WindowAdapter(){
			public void windowClosing(WindowEvent e){
				exit();
			}
			public void windowActivated(WindowEvent e){
				grabFocus();
			}
		});
		this.addKeyListener(new KeyAdapter(){
			public void keyPressed(KeyEvent e){
				switch(e.getModifiersEx()){
					case 0:
						switch(e.getKeyCode()){
							case KeyEvent.VK_F1:
								changeLanguage();
								break;
							case KeyEvent.VK_F2:
								setAutoCorrectOn(!isAutoCorrectOn());
								break;
							case KeyEvent.VK_F3:
								setSpellCheckOn(!isSpellCheckOn());
								break;
							case KeyEvent.VK_F11:
								showKeyMapWindow();
								break;
							case KeyEvent.VK_F12:
								showHelpWindow();
								break;
							case KeyEvent.VK_ESCAPE:
								searchtoolbar.setVisible(false);
								grabFocus();
								break;
						}
						break;
					case KeyEvent.CTRL_DOWN_MASK:
						switch(e.getKeyCode()){
							case KeyEvent.VK_F:
								try{
									searchtoolbar.setSelectionOnly(getSelectedWorkSpace().getTextArea().getSelectedText()!=null);
									searchtoolbar.setVisible(true);
									searchtoolbar.grabFocus();
								}catch(NullPointerException exc){}
								break;
							case KeyEvent.VK_N:
								newfile();
								break;
							case KeyEvent.VK_O:
								open();
								break;
							case KeyEvent.VK_S:
								save();
								break;
							case KeyEvent.VK_Z:
								undo();
								break;
							case KeyEvent.VK_Y:
								redo();
								break;
						}
						break;
					case KeyEvent.CTRL_DOWN_MASK|KeyEvent.SHIFT_DOWN_MASK:
						switch(e.getKeyCode()){
							case KeyEvent.VK_S:
								saveAll();
								break;
						}
						break;
					case KeyEvent.ALT_DOWN_MASK:
						switch(e.getKeyCode()){
							case KeyEvent.VK_F:
								getMainMenuBar().focusFileMenu();
								break;
							case KeyEvent.VK_E:
								getMainMenuBar().focusEditMenu();
								break;
							case KeyEvent.VK_S:
								getMainMenuBar().focusSettingsMenu();
								break;
							case KeyEvent.VK_T:
								getMainMenuBar().focusToolsMenu();
								break;
							case KeyEvent.VK_H:
								getMainMenuBar().focusHelpMenu();
								break;
							case KeyEvent.VK_X:
								close();
								break;
						}
						break;
				}
			}
		});
		Bangla.setSplashScreenProgressBar(80);
		createInterface();
		Bangla.setSplashScreenProgressBar(90);
		this.setSize(640,480);
		this.setLocation((getToolkit().getScreenSize().width-this.WIDTH)/2,(getToolkit().getScreenSize().height-this.HEIGHT)/2);
		this.loadSettings();
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		Bangla.setSplashScreenProgressBar(100);
		this.setVisible(true);
		this.createBusyIcon();
		new Runnable(){
			private Thread t;
			public void run(){
				while(true){
					try{
						setTitle("-:\u0986mar-\u0987 \u09AC\u09BE\u0982\u09B2\u09BE - "+getSelectedWorkSpace().getName()+":-");
					}catch(NullPointerException exc){}
					try{
						Thread.sleep(500);
					}catch(InterruptedException exc){}
				}
			}
			public void start(){
				t=new Thread(this,"MainWindow");
				t.start();
			}
		}.start();
	}
	/**
	 * Initiates and adds all components on the window
	 */
	private void createInterface(){
		this.setLayout(new BorderLayout());
		//Default MenuBar
		this.mainmenubar=new MainMenuBar(this);
		this.setJMenuBar(this.mainmenubar);
		//Deafult Toolbar
		this.maintoolbar=new MainToolBar(this);
		this.add(this.maintoolbar,BorderLayout.NORTH);
		//Search Toolbar
		this.searchtoolbar=new SearchToolBar(this);
		this.add(this.searchtoolbar,BorderLayout.SOUTH);
		//TabbedPane
		this.tabbedpane=new JTabbedPane();
		this.tabbedpane.setFont(this.compfont);
		this.add(this.tabbedpane,BorderLayout.CENTER);
		textareakeylistener=new TextAreaKeyListener(this);
		textareamouselistener=new TextAreaMouseListener(this);
		popupmenu=new TextAreaPopupMenu(this);
	}
	/**
	 * @return the workspace_vector object currently selected
	 * @throws java.lang.NullPointerException
	 */
	public WorkSpace getSelectedWorkSpace(){
		try{
			return this.workspace_vector.elementAt(tabbedpane.getSelectedIndex());
		}catch(ArrayIndexOutOfBoundsException exc){}
		return null;
	}
	/**
	 * @return the workspace_vector object at a given index
	 * @throws java.lang.NullPointerException
	 */
	public WorkSpace getWorkSpaceAt(int i){
		try{
			return this.workspace_vector.elementAt(i);
		}catch(ArrayIndexOutOfBoundsException exc){}
		return null;
	}
	/**
	 * Adds a workspace_vector in the window
	 * @param ws : an workspace
	 */
	public void add(WorkSpace ws){
		this.showBusyIcon(true);
		this.count++;
		this.workspace_vector.add(ws);
		ws.setLineWrap(this.line_wrap);
		this.tabbedpane.addTab(ws.getName(),ws);
		ws.getTextArea().addKeyListener(textareakeylistener);
		for(int i=0;i<this.getKeyListeners().length;i++)
			ws.getTextArea().addKeyListener(this.getKeyListeners()[i]);
		ws.getTextArea().addMouseListener(textareamouselistener);
		ws.getTextArea().setComponentPopupMenu(popupmenu);
		ws.getTextArea().setFont(this.font);
		this.tabbedpane.setSelectedIndex(tabbedpane.getTabCount()-1);
		this.grabFocus();
		this.showBusyIcon(false);
		ws.setSpellChecker(new SpellChecker(this.dic_beng,this.dic_eng,ws.getTextArea(),this.spell_check_on));
	}
	/**
	 * Removes a workspace
	 * @param index : index of workspace to be removed
	 */
	public void removeWorkSpace(int index){
		if(tabbedpane.getTabCount()>0){
			this.workspace_vector.remove(index);
			this.tabbedpane.remove(index);
		}
		this.grabFocus();
	}
	/**
	 * Removes selected workspace
	 */
	public void removeSelectedWorkSpace(){
		if(tabbedpane.getTabCount()>0){
			this.workspace_vector.remove(tabbedpane.getSelectedIndex());
			this.tabbedpane.remove(tabbedpane.getSelectedIndex());
		}
		this.grabFocus();		
	}
	/** 
	 * Sets Font Style
	 * @param size : font file path
	 */
	public void setFontStyle(String fontname){
		int size=font.getSize();
		for(int i=0;i<fonts.length;i++)
			if(fontname.equals(fonts[i].getName())){
				this.font=fonts[i];
				break;
			}
		this.font=this.font.deriveFont(Font.PLAIN,size);
		for(int i=0;i<this.tabbedpane.getTabCount();i++){
			this.getWorkSpaceAt(i).getTextArea().setFont(this.font);
		}
		this.grabFocus();
	}
	/**
	 * @return font style name
	 */
	public String getFontStyle(){
		return this.font.getName();
	}
	/** 
	 * Sets Font Size
	 * @param size : font point size
	 */
	public void setFontSize(int size){
		try{
			this.font=this.font.deriveFont(Font.PLAIN,size);
			for(int i=0;i<tabbedpane.getTabCount();i++){
				this.getWorkSpaceAt(i).getTextArea().setFont(this.font);
			}
		}catch(NullPointerException exc){}
		this.grabFocus();
	}
	/**
	 * @return font size
	 */
	public int getFontSize(){
		return this.font.getSize();
	}
	/** 
	 * Changes the language between English/Bengali
	 */
	public void changeLanguage(){
		this.language=(language==Language.ENGLISH)?Language.BENGALI:Language.ENGLISH;
		this.grabFocus();
	}
	/** 
	 * @return the language currently selected
	 */
	public Language getLanguage(){
		return this.language;
	}
	/**
	 * @return number of workspaces currently on window
	 */
	public int getWorkSpaceCount(){
		return tabbedpane.getTabCount();
	}
	/**
	 * @return object of tabbedpane in the window
	 */
	public JTabbedPane getTabbedPane(){
		return tabbedpane;
	}
	/**
	 * @return total number of workspaces created so far
	 */
	public int getCount(){
		return count;
	}
	/**
	 * @return the object of Menubar
	 */
	public MainMenuBar getMainMenuBar(){
		return mainmenubar;
	}
	/**
	 * @return the object of Default Toolbar
	 */
	public MainToolBar getMainToolBar(){
		return maintoolbar;
	}
	/**
	 * @return the object of Search Toolbar
	 */
	public SearchToolBar getSearchToolBar(){
		return searchtoolbar;
	}
	/**
	 * @return the dictionary
	 */
	public BengaliDictionary getBengaliDictionary(){
		return dic_beng;
	}
	/**
	 * @return english dicionary
	 */
	public EnglishDictionary getEnglishDictionary(){
		return dic_eng;
	}
	/**
	 * @return the textarea popup menu
	 */
	public TextAreaPopupMenu getPopupMenu(){
		return popupmenu;
	}
	/**
	 * @return value of <code>autocorrect_on</code> flag
	 */
	public boolean isAutoCorrectOn(){
		return this.autocorrect_on;
	}
	/**
	 * Sets state of <code>autocorrect_on</code> flag
	 * @param state : new value
	 */
	public void setAutoCorrectOn(boolean state){
		this.autocorrect_on=state;
		this.grabFocus();
	}
	/**
	 * @return value of <code>spellcheck</code>_on flag
	 */
	public boolean isSpellCheckOn(){
		return this.spell_check_on;
	}
	/**
	 * Sets value of <code>spellcheck</code>_on flag
	 * @param state : new value
	 */
	public void setSpellCheckOn(boolean state){
		for(int i=0;i<tabbedpane.getTabCount();i++)
			workspace_vector.get(i).getSpellChecker().setActive(state);
		this.spell_check_on=state;
		this.grabFocus();
	}
	/**
	 * @return whether all workspace is saved or not
	 */
	public boolean isAllSaved(){
		for(int i=0;i<tabbedpane.getTabCount();i++)
			if(!this.workspace_vector.elementAt(i).isSaved())
				return false;
			return true;
	}
	/**
	 * Saves selected workspace
	 */
	public void save(){
		try{
			if(this.getSelectedWorkSpace().getFile()!=null)
				this.getSelectedWorkSpace().save(null);
			else{
				CustomizedFileChooser chooser=new CustomizedFileChooser(current_directory);
				if(chooser.showSaveDialog(this)==CustomizedFileChooser.APPROVE_OPTION){
					if(this.getSelectedWorkSpace().save(chooser.getSelectedFile()))
						this.tabbedpane.setTitleAt(this.tabbedpane.getSelectedIndex(),chooser.getSelectedFile().getName());
					current_directory=chooser.getCurrentDirectory().getPath();
				}
				this.getSelectedWorkSpace().getTextArea().grabFocus();	
			}
		}catch(NullPointerException exc){}
		this.grabFocus();
	}
	/**
	 * Saves selected workspace as other file
	 */
	public void saveAs(){
		try{
			CustomizedFileChooser chooser=new CustomizedFileChooser(current_directory);
			if(chooser.showSaveDialog(this)==CustomizedFileChooser.APPROVE_OPTION){
				if(this.getSelectedWorkSpace().save(chooser.getSelectedFile()))
					this.tabbedpane.setTitleAt(this.tabbedpane.getSelectedIndex(),chooser.getSelectedFile().getName());
				current_directory=chooser.getCurrentDirectory().getPath();	
			}
			this.getSelectedWorkSpace().getTextArea().grabFocus();	
		}catch(NullPointerException exc){}
		this.grabFocus();
	}
	/**
	 * Saves all workspaces
	 */
	public void saveAll(){
		JOptionPane ask_save=new JOptionPane();
		for(int i=0;i<this.getWorkSpaceCount();i++){
			tabbedpane.setSelectedIndex(i);
			if(!this.getWorkSpaceAt(i).isSaved()){
				if(this.getWorkSpaceAt(i).getFile()!=null)
					this.getWorkSpaceAt(i).save(null);
				else{
					JOptionPane.showMessageDialog(
						this,
						"Specify name and location for \""+this.getWorkSpaceAt(i).getName()+"\"",
												  "Save",
												  JOptionPane.INFORMATION_MESSAGE
					);
					CustomizedFileChooser chooser=new CustomizedFileChooser(current_directory);
					if(chooser.showSaveDialog(this)==CustomizedFileChooser.APPROVE_OPTION){
						if(this.getWorkSpaceAt(i).save(chooser.getSelectedFile()))
							this.tabbedpane.setTitleAt(
								this.tabbedpane.getSelectedIndex(),
													   chooser.getSelectedFile().getName()
							);
						current_directory=chooser.getCurrentDirectory().getPath();
					}
				}
			}		
		}
		this.grabFocus();
	}
	/**
	 * Creates new workspace
	 */
	public void newfile(){
		add(new WorkSpace(this,"New File-"+getCount()));
	}
	/**
	 * Opens new file
	 */
	public void open(){
		CustomizedFileChooser chooser=new CustomizedFileChooser(current_directory);
		if(chooser.showOpenDialog(this)==CustomizedFileChooser.APPROVE_OPTION){
			this.add(new WorkSpace(this,chooser.getSelectedFile()));
			current_directory=chooser.getCurrentDirectory().getPath();
		}
		try{
			this.getSelectedWorkSpace().getTextArea().grabFocus();
		}catch(NullPointerException exc){}
		this.grabFocus();
	}
	/**
	 * Grabs focus to selected workspace
	 */
	public void grabFocus(){
		try{
			this.getSelectedWorkSpace().getTextArea().grabFocus();
		}catch(NullPointerException exc){}
	}
	/**
	 * cuts text from selected workspace
	 */
	public void cut(){
		try{
			this.getSelectedWorkSpace().getTextArea().cut();
		}catch(NullPointerException exc){}
		this.grabFocus();
	}
	/**
	 * copies text from selected workspace
	 */
	public void copy(){
		try{
			this.getSelectedWorkSpace().getTextArea().copy();
		}catch(NullPointerException exc){}
		this.grabFocus();
	}
	/**
	 * pastes text from selected workspace 
	 */
	public void paste(){
		try{
			this.getSelectedWorkSpace().getTextArea().paste();
		}catch(NullPointerException exc){}
		this.grabFocus();
	}
	/**
	 * undoes from selected workspace
	 */
	public void undo(){
		try{
			this.getSelectedWorkSpace().undo();
		}catch(NullPointerException exc){}
		this.grabFocus();
	}
	/**
	 * redoes from selected workspace
	 */
	public void redo(){
		try{
			this.getSelectedWorkSpace().redo();
		}catch(NullPointerException exc){}
		this.grabFocus();
	}
	/**
	 * Closes selected workspace
	 */
	public void close(){
		try{
			if(this.getSelectedWorkSpace().isSaved())
				this.removeSelectedWorkSpace();
			else{
				int result=JOptionPane.showConfirmDialog(
					this,
					"File \""+this.getSelectedWorkSpace().getName()+"\" is not yet saved. Do you want to save this file?",
														 "File not saved",
														 JOptionPane.YES_NO_CANCEL_OPTION
				);
				if(result==JOptionPane.YES_OPTION){
					this.save();
					this.removeSelectedWorkSpace();
				}
				else if(result==JOptionPane.NO_OPTION)
					this.removeSelectedWorkSpace();
			}
		}catch(NullPointerException exc){}
	}
	/**
	 * exits program
	 */
	public void exit(){
		JOptionPane ask_save=new JOptionPane();
		for(int i=0;i<this.getWorkSpaceCount();i++){
			tabbedpane.setSelectedIndex(i);
			if(!this.getWorkSpaceAt(i).isSaved()){
				int result=JOptionPane.showConfirmDialog(
					this,
					"File \""+this.getWorkSpaceAt(i).getName()+"\" is not yet saved. Do you want to save this file?",
														 "File not saved",
														 JOptionPane.YES_NO_CANCEL_OPTION
				);
				if(result==JOptionPane.YES_OPTION){
					if(this.getWorkSpaceAt(i).getFile()!=null){
						this.getWorkSpaceAt(i).save(null);
						this.removeWorkSpace(i--);
					}
					else{
						CustomizedFileChooser chooser=new CustomizedFileChooser(current_directory);
						if(chooser.showSaveDialog(this)==CustomizedFileChooser.APPROVE_OPTION){
							if(this.getWorkSpaceAt(i).save(chooser.getSelectedFile())){
								this.tabbedpane.setTitleAt(
									this.tabbedpane.getSelectedIndex(),
														   chooser.getSelectedFile().getName()
								);
								current_directory=chooser.getCurrentDirectory().getPath();
								this.removeWorkSpace(i--);
							}
						}
					}
				}
				else if(result==JOptionPane.NO_OPTION)
					this.removeWorkSpace(i--);
			}
			else
				this.removeWorkSpace(i--);
		}
		if(this.tabbedpane.getTabCount()==0){
			saveSettings();
			System.exit(0);
		}
		else
			this.grabFocus();
	}
	/**
	 * @return if lines will be wrapped
	 */
	public boolean getLineWrap(){
		return line_wrap;
	}
	/**
	 * @param wrap : indicates if lines should be wrapped
	 */
	public void setLineWrap(boolean wrap){
		this.line_wrap=wrap;
		for(int i=0;i<this.tabbedpane.getTabCount();i++)
			this.workspace_vector.elementAt(i).setLineWrap(wrap);
	}
	/**
	 * Loads all bengali fonts
	 */
	private void loadFonts(){
		File dir=new File(fontpath);
		String fontname[]=dir.list();
		this.fonts=new Font[fontname.length];
		for(int i=0;i<fontname.length;i++){
			try{
				Font font=Font.createFont(Font.TRUETYPE_FONT,new File(fontpath+fontname[i]));
				this.fonts[i]=font;
			}catch(IOException exc){}
			catch(FontFormatException exc){}
		}
		this.font=this.fonts[0];
		this.font=this.font.deriveFont(Font.PLAIN,24);
		try{
			this.compfont=Font.createFont(Font.TRUETYPE_FONT,getClass().getResourceAsStream(resourcepath+"Bangla Akademi.ttf"));
			this.compfont=this.compfont.deriveFont(Font.PLAIN,18);
		}catch(IOException exc){}
		catch(FontFormatException exc){}
	}
	/**
	 * @return all fonts 
	 */
	public Font[] getFonts(){
		return this.fonts;
	}
	/**
	 * @return font used in components
	 */
	public Font getComponentFont(){
		return this.compfont;
	}
	/**
	 * @return font used in textarea
	 */
	public Font getFont(){
		return this.font;
	}
	/**
	 * loads the window settings from file
	 */
	private void loadSettings(){
		File file=new File(datapath+"settings");
		try{
			DataInputStream in=new DataInputStream(new FileInputStream(file));
			this.setLocation(in.readInt(),in.readInt());
			this.setSize(in.readInt(),in.readInt());
			this.spell_check_on=in.readBoolean();
			this.autocorrect_on=in.readBoolean();
			this.line_wrap=in.readBoolean();
			int length=in.readInt();
			String fontname="";
			for(int i=0;i<length;i++)
				fontname=fontname+String.valueOf(new char[]{in.readChar()});
			this.setFontStyle(fontname);
			this.setFontSize(in.readInt());
			in.close();
		}catch(IOException exc){
			JOptionPane.showMessageDialog(this,"File "+file.getPath()+" read error","File Read Error",JOptionPane.ERROR_MESSAGE);
		}
	}
	/**
	 * Saves the window settings in file
	 */
	private void saveSettings(){
		File file=new File(datapath+"settings");
		try{
			DataOutputStream out=new DataOutputStream(new FileOutputStream(file));
			out.writeInt(this.getX());
			out.writeInt(this.getY());			
			out.writeInt(this.getWidth());
			out.writeInt(this.getHeight());
			out.writeBoolean(this.spell_check_on);
			out.writeBoolean(this.autocorrect_on);
			out.writeBoolean(this.line_wrap);
			out.writeInt(this.font.getName().length());
			out.writeChars(this.font.getName());
			out.writeInt(this.font.getSize());
			out.close();
		}catch(IOException exc){
			JOptionPane.showMessageDialog(this,"File "+file.getPath()+" read error","File Write Error",JOptionPane.ERROR_MESSAGE);			
		}
	}
	/**
	 * Creates a busy icon window
	 */
	private void createBusyIcon(){
		busywindow=new JFrame();
		busywindow.setUndecorated(true);
		busywindow.setAlwaysOnTop(true);
		busywindow.setLayout(new FlowLayout());
		ImageIcon busyimage=new ImageIcon(getClass().getResource(imagepath+"busy.gif"));
		busywindow.add(new JLabel(busyimage));
		busywindow.pack();
	}
	/**
	 * Shows a busy icon
	 */
	public void showBusyIcon(boolean busy){
		if(busy)
		 	busywindow.setLocation(this.getX()+(this.getWidth()-busywindow.getWidth())/2,this.getY()+(this.getHeight()-busywindow.getHeight())/2);
		busywindow.setVisible(busy);
	}
	/**
	 * Shows The Help Window
	 */
	public void showHelpWindow(){
		new HelpWindow(this);
	}
	/**
	 * Shows the About Window
	 */
	public void showAboutWindow(){
		new AboutWindow(this);
	}
	/**
	 * Shows the KeyMap Window
	 */
	public void showKeyMapWindow(){
		new KeyMapWindow(this);
	}
}

/**
 * The common <code>KeyListener</code> for textareas on main window
 */
class TextAreaKeyListener extends KeyAdapter{
	private MainWindow win;
	private Converter conv;
	/**
	 * Creates the key listener
	 */
	public TextAreaKeyListener(MainWindow win){
		this.win=win;
		this.conv=new Converter();
	}
	/**
	 * Invoked when a key has been typed.
	 * This event occurs when a key press is followed by a key release.
	 */
	public void keyTyped(KeyEvent e){
		final BengaliDictionary dic_beng=win.getBengaliDictionary();
		final EnglishDictionary dic_eng=win.getEnglishDictionary();
		final int pos=win.getSelectedWorkSpace().getTextArea().getCaretPosition();
		final char keychar=e.getKeyChar();
		if(e.getModifiersEx()==0 || e.getModifiersEx()==KeyEvent.SHIFT_DOWN_MASK){
			// Convertion to bengali
			if(win.getLanguage()==Language.BENGALI){
				int result=this.conv.startConvertionAt(win.getSelectedWorkSpace().getTextArea(),e);
			}
			// Auto Correct
			if(CType.isPunct(keychar) || CType.isSpace(keychar) || Character.isDigit(keychar)){
				if(win.getLanguage()==Language.BENGALI && win.isAutoCorrectOn()==true){
					Predictor pred=new Predictor(dic_beng,win.getSelectedWorkSpace().getTextArea());
					pred.autoCorrect();
				}
			}
		}
	}
}

/**
 * TextArea <code>MouseListener</code>
 */
class TextAreaMouseListener extends MouseAdapter{
	private MainWindow win;
	private Predictor pred;
	/**
	 * Creates Mouse listener
	 */
	public TextAreaMouseListener(MainWindow win){
		this.win=win;
	}
	/**
	 * Invoked when the mouse button has been clicked (pressed
	 * and released) on a component.
	 */
	public void mouseClicked(MouseEvent e){
		final BengaliDictionary dic_beng=win.getBengaliDictionary();
		final EnglishDictionary dic_eng=win.getEnglishDictionary();
		// Prediction
		try{
			pred=new Predictor(dic_beng,win.getSelectedWorkSpace().getTextArea());
		}catch(NullPointerException exc){}
		win.getPopupMenu().setPredictions(pred.predict());
	}
}