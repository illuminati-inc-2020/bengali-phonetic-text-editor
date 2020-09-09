package Bangla.language;

import java.util.Vector;
import javax.swing.JTextArea;
import Bangla.util.*;

public class Predictor{
	private BengaliDictionary dic;
	private JTextArea textarea;
	private String word,new_word;
	/**
	 * Generates predictions for the word currently under cursor
	 * @param dic : dictionary to be used for prediction
	 * @param textarea : Text area to be applied on
	 */
	public Predictor(BengaliDictionary dic,JTextArea textarea){
		this.dic=dic;
		this.textarea=textarea;
		this.word="";
		this.new_word="";
	}
	/**
	 * @return the word that has been replaced while auto-correction
	 */
	public String getDeletedWord(){
		return this.word;
	}
	/**
	 * @return the word that has be inserted as prediction while auto-correction
	 */
	public String getInsertedWord(){
		return this.new_word;
	}
	/**
	 * Auto-correct words on real time
	 */
	public void autoCorrect(){
		int pos=textarea.getCaretPosition();
		String txt=textarea.getText();
		// Extracting word before caret
		int start=0,end=0;
		word="";
		try{
			for(end=pos-1;end>0;end--)
				if(CType.isBengaliLetter(txt.charAt(end))){
					end++;
					break;
				}
			for(start=end-1;start>0;start--)
				if(!CType.isBengaliLetter(txt.charAt(start))){
					start++;
					break;
				}
			word=txt.substring(start,end);
		}catch(StringIndexOutOfBoundsException exc){}
		// Replacing
		if(dic.contains(word))
			return;
		new_word=dic.firstMatch(word);
		if(!new_word.equals(this.word)){
			try{
				txt=txt.substring(0,start)+new_word+txt.substring(end);
				pos=textarea.getCaretPosition();
				textarea.setText(txt);
				pos=pos-this.word.length()+new_word.length();
				textarea.setCaretPosition(pos);
			}catch(IndexOutOfBoundsException exc){}
		}
	}
	/**
	 * @return All words that can be predicted for word under curseor
	 */
	public String[] predict(){
		int pos=textarea.getCaretPosition();
		String txt=textarea.getText();
		// Extracting word under caret
		word="";
		try{
			int start,end;
			for(start=pos-1;start>0;start--)
				if(!CType.isBengaliLetter(txt.charAt(start))){
					start++;
					break;
				}
			for(end=pos;end<txt.length();end++)
				if(!CType.isBengaliLetter(txt.charAt(end)))
					break;
			word=txt.substring(start,end);
		}catch(StringIndexOutOfBoundsException exc){}
		// Generating predictions
		String prediction[];
		Vector<String> prediction_vector=dic.allMatch(word);
		prediction=new String[prediction_vector.size()];
		for(int i=0;i<prediction.length;i++){
			prediction[i]=prediction_vector.get(i);
		}
		return prediction;
	}
}