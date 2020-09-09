package Bangla.gui;

import Bangla.util.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;
import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

public class MainToolBar extends JToolBar{
	private static final String imagepath="/resources/images/";
	/**
	 * parent
	 */
	private final MainWindow parent;
	/**
	 * components
	 */
	private final JButton btn_close,btn_new,btn_open,btn_save,btn_saveall,btn_cut,btn_copy,btn_paste,btn_undo,btn_redo,btn_search;
	private final JToggleButton btn_lang;
	/**
	 * Creates the tool bar for the main window
	 * @param parent 
	 */
	public MainToolBar(final MainWindow parent){
		this.parent=parent;
		this.setFloatable(false);
		// Default Toolbar > Close
		this.btn_close=new JButton(new ImageIcon(getClass().getResource(imagepath+"close.png")));
		this.add(btn_close);
		this.btn_close.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.close();
			}
		});
		this.btn_close.setToolTipText("Close File (Alt+X)");
		// Default Toolbar > Separator 
		this.add(new JToolBar.Separator());
		// Default Toolbar > New 
		this.btn_new=new JButton(new ImageIcon(getClass().getResource(imagepath+"new.png")));
		this.add(btn_new);
		this.btn_new.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.newfile();
			}
		});
		this.btn_new.setToolTipText("New File (Ctrl+N)");
		// Default Toolbar > Open 
		this.btn_open=new JButton(new ImageIcon(getClass().getResource(imagepath+"open.png")));
		this.add(btn_open);
		this.btn_open.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.open();
			}
		});
		this.btn_open.setToolTipText("Open File (Ctrl+O)");
		// Default Toolbar > Save 
		this.btn_save=new JButton(new ImageIcon(getClass().getResource(imagepath+"save.png")));
		this.add(btn_save);
		this.btn_save.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.save();
			}
		});
		// Default Toolbar > Save All
		this.btn_saveall=new JButton(new ImageIcon(getClass().getResource(imagepath+"saveall.png")));
		this.add(btn_saveall);
		this.btn_saveall.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.saveAll();
			}
		});
		this.btn_saveall.setToolTipText("Save File (Ctrl+S)");
		// Default Toolbar > Separator 
		this.add(new JToolBar.Separator());
		// Default Toolbar > Undo
		this.btn_undo=new JButton(new ImageIcon(getClass().getResource(imagepath+"undo.png")));
		this.add(btn_undo);
		this.btn_undo.setToolTipText("Undo (Ctrl+Z)");
		this.btn_undo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.getSelectedWorkSpace().undo();
			}
		});
		// Default Toolbar > Redo
		this.btn_redo=new JButton(new ImageIcon(getClass().getResource(imagepath+"redo.png")));
		this.add(btn_redo);
		this.btn_redo.setToolTipText("Redo (Ctrl+Y)");
		this.btn_redo.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.getSelectedWorkSpace().redo();
			}
		}); 
		// Default Toolbar > Separator 
		this.add(new JToolBar.Separator());
		// Default Toolbar > Cut
		this.btn_cut=new JButton(new ImageIcon(getClass().getResource(imagepath+"cut.png")));
		this.add(btn_cut);
		this.btn_cut.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.cut();
			}
		});
		this.btn_cut.setToolTipText("Cut (Ctrl+X)");
		// Default Toolbar > Copy 
		this.btn_copy=new JButton(new ImageIcon(getClass().getResource(imagepath+"copy.png")));
		this.add(btn_copy);
		this.btn_copy.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.copy();
			}
		});
		this.btn_copy.setToolTipText("Copy (Ctrl+C)");
		// Default Toolbar > Paste 
		this.btn_paste=new JButton(new ImageIcon(getClass().getResource(imagepath+"paste.png")));
		this.add(btn_paste);
		this.btn_paste.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				parent.paste();
			}
		});
		this.btn_paste.setToolTipText("Paste (Ctrl+V)");
		// Default Toolbar > Seperator 
		this.add(new JToolBar.Separator());
		// Default Toolbar > English/Bengali
		this.btn_lang=new JToggleButton(new ImageIcon(getClass().getResource(imagepath+"lange.png")));
		btn_lang.setSelectedIcon(new ImageIcon(getClass().getResource(imagepath+"langb.png")));
		this.add(btn_lang);
		this.btn_lang.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e) {
				parent.changeLanguage();
			}
		});
		this.btn_lang.setToolTipText("Switch between English and Bengali (F1)");
		// Default Toolbar > Seperator 
		this.add(new JToolBar.Separator());
		// Default ToolBar > Search
		this.btn_search=new JButton(new ImageIcon(getClass().getResource(imagepath+"search.png")));
		this.add(this.btn_search);
		this.btn_search.addActionListener(new ActionListener(){
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
		this.btn_search.setToolTipText("Search (Ctrl+F)");
		// Button Activation Checking
		new Runnable(){
			private Thread t;
			public void run(){
				while(true){
					checkButtonActivation();
					try{
						Thread.sleep(200);
					}catch(InterruptedException exc){}
				}
			}
			public void start(){
				t=new Thread(this,"DefaultToolBar");
				t.start();
			}
		}.start();
		// Key Listener
		for(int i=0;i<this.getComponents().length;i++)
			for(int j=0;j<parent.getKeyListeners().length;j++)
				this.getComponent(i).addKeyListener(parent.getKeyListeners()[j]);
	}
	/**
	 * check and activate/deactivate buttons in thread as requirement
	 */
	public void checkButtonActivation(){
		try{
			btn_redo.setEnabled(parent.getTabbedPane().getTabCount()>0 && !parent.getSelectedWorkSpace().isRedoStackEmpty()); 
			btn_undo.setEnabled(parent.getTabbedPane().getTabCount()>0 && !parent.getSelectedWorkSpace().isUndoStackEmpty());
			btn_save.setEnabled(parent.getTabbedPane().getTabCount()>0 && !parent.getSelectedWorkSpace().isSaved());
			btn_saveall.setEnabled(!parent.isAllSaved());
			btn_close.setEnabled(parent.getTabbedPane().getTabCount()>0);
			btn_cut.setEnabled(parent.getTabbedPane().getTabCount()>0);
			btn_copy.setEnabled(parent.getTabbedPane().getTabCount()>0);
			btn_paste.setEnabled(parent.getTabbedPane().getTabCount()>0);
			btn_search.setEnabled(parent.getTabbedPane().getTabCount()>0);
			btn_lang.setSelected(parent.getLanguage()==Language.BENGALI);
		}catch(NullPointerException e){}
	}
}
