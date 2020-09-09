package Bangla.gui;

import Bangla.util.*;
import javax.swing.ImageIcon;
import javax.swing.JCheckBoxMenuItem;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JSeparator;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class MainMenuBar extends JMenuBar{
	private static final String fontpath="/resources/font/";
	private final MainWindow parent;
	private final JMenu menu_file,menu_edit,menu_tools,menu_help,menu_settings,menu_settings_fontstyle,menu_settings_fontsize;
	private final JMenuItem menu_file_new,menu_file_open,menu_file_save,menu_file_saveas,menu_file_close,menu_file_exit,menu_file_saveall;
	private final JMenuItem menu_edit_cut,menu_edit_copy,menu_edit_paste,menu_edit_search,menu_edit_undo,menu_edit_redo;
	private final JCheckBoxMenuItem menu_settings_wordwrap,menu_settings_fontstyle_style[],menu_settings_fontsize_size[];
	private final JCheckBoxMenuItem menu_tools_lang,menu_tools_autocorrect,menu_tools_spellcheck;
	private final JMenuItem menu_help_keymap,menu_help_help,menu_help_about;
	public MainMenuBar(final MainWindow parent){
		this.parent=parent;
		// Menu > File
		this.menu_file=new JMenu("File");
		this.menu_file.setIcon(new ImageIcon(parent.getIconImage()));
		this.add(this.menu_file);
		// Menu > File > New 
		this.menu_file_new=new JMenuItem("New (Ctrl+N)");
		this.menu_file.add(this.menu_file_new);
		this.menu_file_new.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
				parent.newfile();
		    }
		});
		// Menu > File > Open 
		this.menu_file_open=new JMenuItem("Open (Ctrl+O)");
		this.menu_file.add(this.menu_file_open);
		this.menu_file_open.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
				parent.open();
		    }
		});
		// Menu > File > Save 
		this.menu_file_save=new JMenuItem("Save (Ctrl+S)");
		this.menu_file.add(this.menu_file_save);
		this.menu_file_save.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
				parent.save();
		    }
		});
		// Menu > File > Save As... 
		this.menu_file_saveas=new JMenuItem("Save As...");
		this.menu_file.add(this.menu_file_saveas);
		this.menu_file_saveas.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
				parent.saveAs();
		    }
		});
		// Menu > File > Save All
		this.menu_file_saveall=new JMenuItem("Save All (Ctrl+Shift+S)");
		this.menu_file.add(this.menu_file_saveall);
		this.menu_file_saveall.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
				parent.saveAll();
		    }
		});
		// Menu > File > Separator
		this.menu_file.add(new JSeparator());
		// Menu > File > Close 
		this.menu_file_close=new JMenuItem("Close (Alt+X)");
		this.menu_file.add(this.menu_file_close);
		this.menu_file_close.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
				parent.close();
		    }
		});
		// Menu > File > Exit 
		this.menu_file_exit=new JMenuItem("Exit (Alt+F4)");
		this.menu_file.add(this.menu_file_exit);
		this.menu_file_exit.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
			    parent.exit();
		    }
		});
		// Menu > Edit 
		this.menu_edit=new JMenu("Edit");
		this.add(this.menu_edit);
		// Menu > Edit > Undo
		this.menu_edit_undo=new JMenuItem("Undo (Ctrl+Z)");
		this.menu_edit.add(this.menu_edit_undo);
		this.menu_edit_undo.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
				parent.undo();
		    }
		});
		// Menu > Edit > Redo
		this.menu_edit_redo=new JMenuItem("Redo (Ctrl+Y)");
		this.menu_edit.add(this.menu_edit_redo);
		this.menu_edit_redo.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e) {
				parent.redo();
		    }
		});
		// Menu > Edit > Separator
		this.menu_edit.add(new JSeparator());
		// Menu > Edit > Cut 
		this.menu_edit_cut=new JMenuItem("Cut (Ctrl+X)");
		this.menu_edit.add(this.menu_edit_cut);
		this.menu_edit_cut.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
				parent.cut();
		    }
		});
		// Menu > Edit > Copy 
		this.menu_edit_copy=new JMenuItem("Copy (Ctrl+C)");
		this.menu_edit.add(this.menu_edit_copy);
		this.menu_edit_copy.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
				parent.copy();
		    }
		});
		// Menu > Edit > Paste 
		this.menu_edit_paste=new JMenuItem("Paste (Ctrl+V)");
		this.menu_edit.add(this.menu_edit_paste);
		this.menu_edit_paste.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
				parent.paste();
		    }
		});
		// Menu > Edit > Separator
		this.menu_edit.add(new JSeparator());
		// Menu > Edit > Search 
		this.menu_edit_search=new JMenuItem("Search/Replace (Ctrl+F)");
		this.menu_edit.add(this.menu_edit_search);
		this.menu_edit_search.addActionListener(new ActionListener(){
		    public void actionPerformed(ActionEvent e){
				try{
					if(parent.getSelectedWorkSpace().getTextArea().getSelectedText()==null)
						parent.getSearchToolBar().setSelectionOnly(false);
					else
						parent.getSearchToolBar().setSelectionOnly(true);
					parent.getSearchToolBar().setVisible(true);
					parent.getSearchToolBar().grabFocus();
				}catch(NullPointerException exc){}
			}
		});
		// Menu > Settings
		this.menu_settings=new JMenu("Settings");
		this.add(this.menu_settings);
		// Menu > Dynamic Word Wrap
		this.menu_settings_wordwrap=new JCheckBoxMenuItem("Dynamic Word Wrap");
		this.menu_settings.add(menu_settings_wordwrap);
		this.menu_settings_wordwrap.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.setLineWrap(!parent.getLineWrap());
			}
		});
		// Menu > Settings > Font Style
		this.menu_settings_fontstyle=new JMenu("Font Style");
		this.menu_settings.add(menu_settings_fontstyle);
		// Menu > Settings > Font Style > *
		this.menu_settings_fontstyle_style=new JCheckBoxMenuItem[parent.getFonts().length];
		for(int i=0;i<parent.getFonts().length;i++){
			final String fontname=parent.getFonts()[i].getName();
			this.menu_settings_fontstyle_style[i]=new JCheckBoxMenuItem(fontname);
			this.menu_settings_fontstyle.add(menu_settings_fontstyle_style[i]);
			this.menu_settings_fontstyle_style[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					parent.setFontStyle(fontname);
				}
			});
		}
		// Menu > Settings > Font Size
		this.menu_settings_fontsize=new JMenu("Font Size");
		this.menu_settings.add(this.menu_settings_fontsize);
		// Menu > Settings> Font Size > *
		int size[]={2,8,10,12,18,24,36,48,72,96};
		this.menu_settings_fontsize_size=new JCheckBoxMenuItem[size.length];
		for(int i=0;i<size.length;i++){
			final int fontsize=size[i];
			this.menu_settings_fontsize_size[i]=new JCheckBoxMenuItem(String.valueOf(fontsize));
			this.menu_settings_fontsize.add(this.menu_settings_fontsize_size[i]);
			this.menu_settings_fontsize_size[i].addActionListener(new ActionListener(){
				public void actionPerformed(ActionEvent e){
					parent.setFontSize(fontsize);
				}
			});
		}
		// Menu > Tools 
		this.menu_tools=new JMenu("Tools");
		this.add(this.menu_tools);
		// Menu > Tools > English/Bengali 
		this.menu_tools_lang=new JCheckBoxMenuItem("Bengali (F1)");
		this.menu_tools.add(this.menu_tools_lang);
		this.menu_tools_lang.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				parent.changeLanguage();
			}
		});
		// Menu > Tools > Prediction
		this.menu_tools_autocorrect=new JCheckBoxMenuItem("Auto Correct (F2)");
		this.menu_tools_autocorrect.setSelected(parent.isAutoCorrectOn());
		this.menu_tools.add(this.menu_tools_autocorrect);		
		this.menu_tools_autocorrect.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				parent.setAutoCorrectOn(!parent.isAutoCorrectOn());
			}
		});
		// Menu > Tools > Check Spellings
		this.menu_tools_spellcheck=new JCheckBoxMenuItem("Spell Check (F3)");
		this.menu_tools.add(this.menu_tools_spellcheck);
		this.menu_tools_spellcheck.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				parent.setSpellCheckOn(!parent.isSpellCheckOn());
			}
		});
		// Menu > Help 
		this.menu_help=new JMenu("Help");
		this.add(this.menu_help);
		// Menu > Help > Key Map
		this.menu_help_keymap=new JMenuItem("Key Map (F11)");
		this.menu_help.add(this.menu_help_keymap);
		this.menu_help_keymap.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.showKeyMapWindow();
			}
		});
		// Menu > Help > Help
		this.menu_help_help=new JMenuItem("Help (F12)");
		this.menu_help.add(this.menu_help_help);
		this.menu_help_help.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.showHelpWindow();
			}
		});
		// Menu > Help > Separator
		this.menu_help.add(new JSeparator());
		// Menu > Help > About 
		this.menu_help_about=new JMenuItem("About");
		this.menu_help.add(this.menu_help_about);
		this.menu_help_about.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.showAboutWindow();
			}
		});
		// Button Activation Checking
		new Runnable(){
			private Thread t;
			public void run(){
				while(true){
					checkButtonState();
					try{
						Thread.sleep(500);
					}catch(InterruptedException exc){}
				}
			}
			public void start(){
				t=new Thread(this,"DefaultMenuBar");
				t.start();
			}
		}.start();
		// Key Listener
		for(int i=0;i<this.getComponents().length;i++)
			for(int j=0;j<parent.getKeyListeners().length;j++)
				this.getComponent(i).addKeyListener(parent.getKeyListeners()[j]);
	}
	/**
	 * grab focus to the file menu
	 */
	public void focusFileMenu(){
		this.menu_file.doClick();
	}
	/**
	 * grab focus to the edit menu
	 */
	public void focusEditMenu(){
		this.menu_edit.doClick();
	}
	/**
	 * grab focus to the settings menu
	 */
	public void focusSettingsMenu(){
		this.menu_settings.doClick();
	}
	/**
	 * grab focus to the tools menu
	 */
	public void focusToolsMenu(){
		this.menu_tools.doClick();
	}
	/**
	 * grab focus to the help menu
	 */
	public void focusHelpMenu(){
		this.menu_help.doClick();
	}
	/**
	 * Performs button state checking
	 */
	public void checkButtonState(){
		try{
			menu_edit_redo.setEnabled(parent.getTabbedPane().getTabCount()>0 && !parent.getSelectedWorkSpace().isRedoStackEmpty()); 
			menu_edit_undo.setEnabled(parent.getTabbedPane().getTabCount()>0 && !parent.getSelectedWorkSpace().isUndoStackEmpty());  
			menu_file_save.setEnabled(parent.getTabbedPane().getTabCount()>0 && !parent.getSelectedWorkSpace().isSaved());
			menu_file_saveas.setEnabled(parent.getTabbedPane().getTabCount()>0);
			menu_file_saveall.setEnabled(!parent.isAllSaved());
			menu_file_close.setEnabled(parent.getTabbedPane().getTabCount()>0);
			menu_edit_cut.setEnabled(parent.getTabbedPane().getTabCount()>0);
			menu_edit_copy.setEnabled(parent.getTabbedPane().getTabCount()>0);
			menu_edit_paste.setEnabled(parent.getTabbedPane().getTabCount()>0);
			menu_edit_search.setEnabled(parent.getTabbedPane().getTabCount()>0);
			menu_settings_wordwrap.setSelected(parent.getLineWrap());
			for(int i=0;i<menu_settings_fontsize_size.length;i++)
				menu_settings_fontsize_size[i].setSelected(Integer.valueOf(menu_settings_fontsize_size[i].getText())==parent.getFontSize());
			for(int i=0;i<menu_settings_fontstyle_style.length;i++)
				menu_settings_fontstyle_style[i].setSelected(menu_settings_fontstyle_style[i].getText().equals(parent.getFontStyle()));
			menu_tools_lang.setSelected(parent.getLanguage()==Language.BENGALI);
			menu_tools_autocorrect.setSelected(parent.isAutoCorrectOn());
			menu_tools_spellcheck.setSelected(parent.isSpellCheckOn());
		}catch(NullPointerException e){}
	}
}
