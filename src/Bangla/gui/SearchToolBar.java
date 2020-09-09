package Bangla.gui;

import Bangla.language.*;
import Bangla.util.*;
import java.awt.Color;
import java.awt.Font;
import java.io.File;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.JToolBar;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import java.io.IOException;
import java.awt.FontFormatException;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

/**
 * A toolbar for searching and replacing in <code>Workspace</code>s 
 * in <code>MainWindow</code>
 */
public class SearchToolBar extends JToolBar{
	private static final String imagepath="/resources/images/";
	private static final String fontpath="/resources/font/";
	private final JButton btn_close,btn_next,btn_prev,btn_replace,btn_replaceall,btn_highlightall;
	private final JTextField txt_src,txt_rep;
	private final JCheckBox cb_selectiononly;
	private boolean match_case=false;
	private boolean selection_only=false;
	private boolean highlight_all=false;
	private final MainWindow parent;
	private static final DefaultHighlighter hlr=new DefaultHighlighter();
	/**
	 * Creates a search toolbar
	 */
	public SearchToolBar(final MainWindow parent){
		this.parent=parent;
		this.setVisible(false);
		
		// Close Search toolbar
		this.btn_close=new JButton(new ImageIcon(getClass().getResource(imagepath+"close.png")));
		this.add(this.btn_close);
		this.btn_close.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setVisible(false);
				parent.grabFocus();
			}
		});
		this.btn_close.setToolTipText("Close Search Toolbar");
		// Search Next
		this.btn_next=new JButton("Next");
		this.add(this.btn_next);
		this.btn_next.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				searchNext();
			}
		});
		this.btn_next.setToolTipText("Search After cursor");
		// Search Previous
		this.btn_prev=new JButton("Prev");
		this.add(this.btn_prev);
		this.btn_prev.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				searchPrevious();
			}
		});
		this.btn_prev.setToolTipText("Search Before cursor");
		// Search TextField
		this.txt_src=new JTextField(10);
		this.add(this.txt_src);
		this.txt_src.setToolTipText("Search string");
		this.txt_src.addKeyListener(new SearchTextFieldKeyListener(parent,this));
		this.txt_src.setFont(parent.getComponentFont());
		// Replace TextField
		this.txt_rep=new JTextField(10);
		this.add(this.txt_rep);
		this.txt_rep.setToolTipText("Replace string");
		this.txt_rep.addKeyListener(new ReplaceTextFieldKeyListener(parent,this));
		this.txt_rep.setFont(parent.getComponentFont());
		// Replace
		this.btn_replace=new JButton("Replace");
		this.add(btn_replace);
		this.btn_replace.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				replace();
			}
		});
		this.btn_replace.setToolTipText("Replace After cursor");
		// Replace All
		this.btn_replaceall=new JButton("Replace All");
		this.add(btn_replaceall);
		this.btn_replaceall.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				replaceAll();
			}
		});
		this.btn_replaceall.setToolTipText("Replace all occurances");
		// Selection only
		this.cb_selectiononly=new JCheckBox("Selection only");
		this.add(this.cb_selectiononly);
		this.cb_selectiononly.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				setSelectionOnly(!isSelectionOnly());
			}
		});
		this.cb_selectiononly.setToolTipText("Search/Replace only in selected text");
		// Highlight All
		this.btn_highlightall=new JButton(new ImageIcon(getClass().getResource(imagepath+"highlighter.png")));
		this.add(this.btn_highlightall);
		this.btn_highlightall.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				highlightAll();
			}
		});
		this.btn_highlightall.setToolTipText("Highlight all");
		
		// Key Listener
		for(int i=0;i<this.getComponents().length;i++)
			for(int j=0;j<parent.getKeyListeners().length;j++)
				this.getComponent(i).addKeyListener(parent.getKeyListeners()[j]);
	}
	/**
	 * grab focus to search textfield
	 */
	public void grabFocus(){
		this.txt_src.grabFocus();
		this.txt_src.selectAll();
	}
	/**
	 * @return whether the search toolbar is in search selection only mode
	 */
	public boolean isSelectionOnly(){
		return this.selection_only;
	}
	/**
	 * sets selection only flag to given state
	 * @param state : state to be set to <code>selection_only</code> flag 
	 */
	public void setSelectionOnly(boolean state){
		this.selection_only=state;
		this.cb_selectiononly.setSelected(state);
	}
	/**
	 * @return object to search textfield
	 */
	public JTextField getSearchTextField(){
		return this.txt_src;
	}
	/**
	 * @return object to replace textfield
	 */
	public JTextField getReplaceTextField(){
		return this.txt_rep;
	}
	/**
	 * Searches and highlights the string specified in <code>txt_src</code>
	 * in selected workspace after current caret position
	 */
	public void searchNext(){
		String txt=parent.getSelectedWorkSpace().getTextArea().getText();
		String src=txt_src.getText();
		if(this.match_case){
			txt=txt.toLowerCase();
			src=src.toLowerCase();
		}
		if(this.selection_only){
			txt=parent.getSelectedWorkSpace().getTextArea().getSelectedText();
		}
		int init_pos=parent.getSelectedWorkSpace().getTextArea().getCaretPosition();
		try{
			for(int i=init_pos;i!=(init_pos-1+txt.length())%txt.length();i=(i+1)%txt.length()){
				String s=txt.substring(i);
				if(s.startsWith(src)){
					parent.getSelectedWorkSpace().getTextArea().grabFocus();
					parent.getSelectedWorkSpace().getTextArea().select(i,i+src.length());
					return;
				}
			}		
		}catch(ArithmeticException exc){}
	}
	/**
	 * Searches and highlights the string specified in <code>txt_src</code>
	 * in selected workspace before current caret position
	 */
	public void searchPrevious(){
		String txt=parent.getSelectedWorkSpace().getTextArea().getText();
		String src=this.txt_src.getText();
		if(!this.match_case){
			txt=txt.toLowerCase();
			src=src.toLowerCase();
		}
		if(this.selection_only){
			txt=parent.getSelectedWorkSpace().getTextArea().getSelectedText();
		}
		int init_pos=(parent.getSelectedWorkSpace().getTextArea().getCaretPosition()-src.length()-1+txt.length())%txt.length();
		for(int i=init_pos;i!=(init_pos+1)%txt.length();i=(i-1+txt.length())%txt.length()){
			String s=txt.substring(i);
			if(s.startsWith(src)){
				parent.getSelectedWorkSpace().getTextArea().grabFocus();
				parent.getSelectedWorkSpace().getTextArea().select(i,i+src.length());
				return;
			}
		}
	}
	/**
	 * Replaces first occurance of string specified in <code>txt_src</code>
	 * by string specified in <code>txt_rep</code> in selected workspace
	 * after current caret position
	 */
	public void replace(){
		JTextArea txtarea=parent.getSelectedWorkSpace().getTextArea();
		String txt=txtarea.getText();
		String txt_old=txt;
		String src=this.txt_src.getText();
		String rep=this.txt_rep.getText();
		if(src.length()==0)
			return;
		if(this.selection_only){
			txt=txtarea.getSelectedText();
		}
		txt=txt.replaceFirst(src,rep);
		if(this.selection_only){
			txt=txtarea.getText().substring(0,txtarea.getSelectionStart())
					+txt
					+txtarea.getText().substring(txtarea.getSelectionEnd());
		}
		parent.getSelectedWorkSpace().getTextArea().setText(txt);
	}
	/**
	 * Replaces all occurances of string specified in <code>txt_src</code> 
	 * by string specified in <code>txt_rep</code> in selected workspace
	 */
	public void replaceAll(){
		JTextArea txtarea=parent.getSelectedWorkSpace().getTextArea();
		String txt=txtarea.getText();
		String txt_old=txt;
		String src=this.txt_src.getText();
		String rep=this.txt_rep.getText();
		if(src.length()==0)
			return;
		if(this.selection_only){
			txt=txtarea.getSelectedText();
		}
		txt=txt.replaceAll(src,rep);
		if(this.selection_only){
			txt=txtarea.getText().substring(0,txtarea.getSelectionStart())
					+txt
					+txtarea.getText().substring(txtarea.getSelectionEnd());
		}
		txtarea.setText(txt);
	}
	/**
	 * Highlights all occurances of string specified in <code>txt_src</code> in selected workspace
	 */
	public void highlightAll(){
		if(hlr.getHighlights().length==0){
			try{
				parent.getSelectedWorkSpace().getTextArea().setHighlighter(hlr);
			}catch(NullPointerException exc){}
			String txt=parent.getSelectedWorkSpace().getTextArea().getText();
			String src=this.txt_src.getText();
			if(!this.match_case){
				txt=txt.toLowerCase();
				src=src.toLowerCase();
			}
			if(this.selection_only){
				txt=parent.getSelectedWorkSpace().getTextArea().getSelectedText();
			}
			for(int i=0;i<txt.length();i++){
				String s=txt.substring(i);
				if(s.startsWith(src)){
					try{
						hlr.addHighlight(i,i+src.length(),new DefaultHighlighter.DefaultHighlightPainter(new Color(255,255,0,150)));
					}catch(BadLocationException exc){}
				}
			}
		}
		else
			hlr.removeAllHighlights();
	}
}

/**
 * <code>KeyListener</code> for search textarea in search toolbar
 */
class SearchTextFieldKeyListener extends KeyAdapter{
	private MainWindow win;
	private SearchToolBar stb;
	private Converter conv=new Converter();
	/**
	 * Creates the <code>KeyListener</code>
	 */
	public SearchTextFieldKeyListener(MainWindow win,SearchToolBar stb){
		this.win=win;
		this.stb=stb;
	}
	/**
	* Invoked when a key has been typed.
	* This event occurs when a key press is followed by a key release.
	*/
	public void keyTyped(KeyEvent e){
		if(win.getLanguage()==Language.BENGALI)
			if(this.conv.startConvertionAt(stb.getSearchTextField(),e)==Converter.SUCCESSFUL)
				e.setKeyChar('\0');
	}
	/**
	* Invoked when a key has been released.
	*/
    public void keyReleased(KeyEvent e){
    	switch(e.getModifiersEx()){
    	case 0:
    		switch(e.getKeyCode()){
    		case KeyEvent.VK_ENTER:
    			e.setKeyChar('\0');
    			stb.searchNext();
    			break;
    		case KeyEvent.VK_ESCAPE:
    			stb.setVisible(false);
    			win.getSelectedWorkSpace().getTextArea().grabFocus();
    			break;
    		}
    		break;
    	}
    }
}

/**
 * <code>KeyListener</code> for search textarea in replace toolbar
 */
class ReplaceTextFieldKeyListener extends KeyAdapter{
	private MainWindow win;
	private SearchToolBar stb;
	private Converter conv=new Converter();
	/**
	 * Creates the <code>KeyListener</code>
	 */
	public ReplaceTextFieldKeyListener(MainWindow win,SearchToolBar stb){
		this.win=win;
		this.stb=stb;
	}
	/**
	* Invoked when a key has been typed.
	* This event occurs when a key press is followed by a key release.
	*/
	public void keyTyped(KeyEvent e){
		if(win.getLanguage()==Language.BENGALI)
			if(this.conv.startConvertionAt(stb.getReplaceTextField(),e)==Converter.SUCCESSFUL)
				e.setKeyChar('\0');
	}
	/**
	* Invoked when a key has been released.
	*/
    public void keyReleased(KeyEvent e){
    	switch(e.getModifiersEx()){
    	case 0:
    		switch(e.getKeyCode()){
    		case KeyEvent.VK_ENTER:
    			e.setKeyChar('\0');
    			stb.replace();
    			break;
    		case KeyEvent.VK_ESCAPE:
    			stb.setVisible(false);
    			win.getSelectedWorkSpace().getTextArea().grabFocus();
    			break;
    		}
    		break;
    	}
    }
}