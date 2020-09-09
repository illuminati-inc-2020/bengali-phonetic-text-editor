package Bangla.language;

import java.io.File;
import Bangla.Bangla;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.charset.Charset;
import java.util.HashMap;
import javax.swing.JOptionPane;

public class EnglishDictionary{
	private static final String smalldictionarypath="/resources/dictionary/dic_eng_small.txt";
	private static final String capitaldictionarypath="/resources/dictionary/dic_eng_cap.txt";
	private static final String additionaldictionarypath="data/dic_eng.txt";
	private final HashMap<String,String> dic=new HashMap<String,String>();
	/**
	 * Creates a English dictionary database
	 */
	public EnglishDictionary(){
		Bangla.setSplashScreenProgressBar(40);
		loadSmallLetterDictionary();
		Bangla.setSplashScreenProgressBar(50);
		loadCapitalLetterDictionary();
		Bangla.setSplashScreenProgressBar(60);
		loadAdditionalDictionary();
		Bangla.setSplashScreenProgressBar(70);
	}
	/**
	 * Loads small letter dictionary
	 */
	private void loadSmallLetterDictionary(){
		String buf="";
		try{
			InputStreamReader in=new InputStreamReader(
					getClass().getResourceAsStream(smalldictionarypath),
					Charset.availableCharsets().get("UTF-8")
				);
			int length=1;
			while(length>0){
				char data[]=new char[1000];
				length=in.read(data);
				try{
					buf=buf+String.valueOf(data,0,length);
				}catch(StringIndexOutOfBoundsException exc){}
			}
			in.close();
		}catch(IOException exc){
			JOptionPane.showMessageDialog(null,"English small letter dictionary read error","File Read Error",JOptionPane.ERROR_MESSAGE);
		}
		String buf_tok[]=buf.split("\n");
		for(int i=0;i<buf_tok.length;i++){
			dic.put(buf_tok[i],"");
		}
	}
	/**
	 * Loads capital letter dictionary
	 */
	private void loadCapitalLetterDictionary(){
		String buf="";
		try{
			InputStreamReader in=new InputStreamReader(
					getClass().getResourceAsStream(capitaldictionarypath),
					Charset.availableCharsets().get("UTF-8")
				);
			int length=1;
			while(length>0){
				char data[]=new char[1000];
				length=in.read(data);
				try{
					buf=buf+String.valueOf(data,0,length);
				}catch(StringIndexOutOfBoundsException exc){}
			}
			in.close();
		}catch(IOException exc){
			JOptionPane.showMessageDialog(null,"English capital letter dictionary read error","File Read Error",JOptionPane.ERROR_MESSAGE);
		}
		String buf_tok[]=buf.split("\n");
		for(int i=0;i<buf_tok.length;i++)
			dic.put(buf_tok[i],"");
	}
	/**
	 * Loads additional dictionary
	 */
	private void loadAdditionalDictionary(){
		String buf="";
		try{
			InputStreamReader in=new InputStreamReader(
					new FileInputStream(additionaldictionarypath),
					Charset.availableCharsets().get("UTF-8")
				);
			int length=1;
			while(length>0){
				char data[]=new char[1000];
				length=in.read(data);
				try{
					buf=buf+String.valueOf(data,0,length);
				}catch(StringIndexOutOfBoundsException exc){}
			}
			in.close();
		}catch(IOException exc){
			JOptionPane.showMessageDialog(null,"English additional dictionary read error","File Read Error",JOptionPane.ERROR_MESSAGE);
		}
		String buf_tok[]=buf.split("\n");
		for(int i=0;i<buf_tok.length;i++)
			dic.put(buf_tok[i],"");
	}
	/**
	 * @param word : Word to be searched
	 * @return whether the word is in dictionary
	 */
	public boolean contains(String word){
		if(dic.containsKey(word))
			return true;
		try{
			if(dic.containsKey(word.substring(0,1).toLowerCase()+word.substring(1)))
				return true;
		}catch(IndexOutOfBoundsException exc){}
		return false;
	}
	/**
	 * Adds a given word to dictionary
	 * @param word : Word to be added
	 */
	public void addToDictionary(String word){
		dic.put(word,"");
		File file=new File(additionaldictionarypath);
		try{
			FileWriter out=new FileWriter(file,true);
			out.append(word+"\n");
			out.close();
		}catch(java.io.IOException e){
			JOptionPane.showMessageDialog(
					null,
					"File \""+file.getPath()+"\" read error",
					"Error",
					JOptionPane.ERROR_MESSAGE
				);
		}
	}
}