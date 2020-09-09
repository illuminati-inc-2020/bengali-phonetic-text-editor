package Bangla.language;

import java.awt.Color;
import java.util.Vector;
import javax.swing.JTextArea;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultHighlighter;
import javax.swing.text.JTextComponent;

import Bangla.util.*;

/**
 * Checks all spellings in a textarea
 */
public class SpellChecker implements Runnable{
	private final BengaliDictionary dic_beng;
	private final EnglishDictionary dic_eng;
	private final JTextArea textarea;
	private final DefaultHighlighter hlr;
	private final Thread th;
	private boolean active;
	private int caretposition=0;
	private Vector<Object> h=new Vector<Object>();
	/**
	 * Creates a spellchecker for textareas
	 * @param dic : a dictionary to be used by the spell checker
	 */
	public SpellChecker(BengaliDictionary dic_beng,EnglishDictionary dic_eng,JTextArea textarea,boolean active){
		this.dic_beng=dic_beng;
		this.dic_eng=dic_eng;
		this.hlr=new DefaultHighlighter();
		hlr.setDrawsLayeredHighlights(true);
		this.textarea=textarea;
		textarea.setHighlighter(hlr);
		this.th=new Thread(this,"SpellChecker");
		this.active=active;
		this.th.start();
	}
	/**
	 * Called by thread
	 */
	public void run(){
		while(true){
			while(!textarea.isShowing()||!active){
				try{
					Thread.sleep(3000);
				}catch(InterruptedException exc){}				
			}
			checkSpellings();
			try{
				Thread.sleep(1000);
			}catch(InterruptedException exc){}
			while(caretposition-textarea.getCaretPosition()>=-5 && caretposition-textarea.getCaretPosition()<=5){
				try{
					Thread.sleep(1000);
				}catch(InterruptedException exc){}
			}
		}
	}
	/**
	 * Checks all spellings in a textarea and highlights all incorrect spellings
	 */
	private void checkSpellings(){
		DefaultHighlighter.DefaultHighlightPainter hp=new DefaultHighlighter.DefaultHighlightPainter(new Color(255,230,230));
		String text=textarea.getText();
		String word[]=text.split("[ \n\t\\p{Punct}[\u09F2-\u09FF]]");
		int start=0;
		for(int i=0;i<word.length;i++){
			int end=start+word[i].length();
			if(word[i].length()>1&&!(dic_beng.contains(word[i])||dic_eng.contains(word[i])||CType.isNumber(word[i])||CType.isBengaliNumber(word[i]))){
				try{
					if(i<h.size())
						hlr.changeHighlight(h.get(i),start,end);
					else
						h.add(hlr.addHighlight(start,end,hp));
				}catch(BadLocationException exc){}
			}
			start=end+1;
		}
		while(word.length<h.size()){
			hlr.removeHighlight(h.get(word.length));
			h.remove(word.length);
		}
	}
	/**
	 * sets the spell checker activation
	 * @param active : state to be set 
	 */
	public void setActive(boolean active){
		if(active==false){
			h.clear();
			try{
				hlr.removeAllHighlights();
			}catch(NullPointerException exc){}
		}
		this.active=active;
	}
	/**
	 * @return whether the spell checker is active or not
	 */
	public boolean isActive(){
		return this.active;
	}
}
