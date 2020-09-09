package Bangla.language;

import Bangla.util.*;
import Bangla.Bangla;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Vector;
import javax.swing.JOptionPane;
import java.util.Date;

public class BengaliDictionary{
	private static final String dictionarypath="/resources/dictionary/dic_beng.txt";
	private static final String matchmappath="/resources/dictionary/match.txt";
	private static final String additionaldictionarypath="data/dic_beng.txt";
	private static HashMap<String,String> dic=new HashMap<String,String>();
	private static HashMap<String,String[]> match_map=new HashMap<String, String[]>();
	private static int maxwordlength=0;
	/**
	 * Creates a dictionary from file
	 */
	public BengaliDictionary(){
		Bangla.setSplashScreenProgressBar(10);
		loadDictionary();
		Bangla.setSplashScreenProgressBar(20);
		loadAdditionalDictionary();
		Bangla.setSplashScreenProgressBar(30);
		loadMatchMap();
		Bangla.setSplashScreenProgressBar(40);
	}
	/**
	 * Loads dictionary
	 */
	private void loadDictionary(){
		String buf="";
		try{
			InputStreamReader in=new InputStreamReader(
					getClass().getResourceAsStream(dictionarypath),
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
			JOptionPane.showMessageDialog(null,"Bengali dictionary read error","File Read Error",JOptionPane.ERROR_MESSAGE);
		}
		String buf_tok[]=buf.split("\n");
		for(int i=0;i<buf_tok.length;i++){
			if(buf_tok[i].length()>maxwordlength)
				maxwordlength=buf_tok[i].length();
			try{
				addWordWithForms(buf_tok[i].split(" ",2)[0],buf_tok[i].split(" ",3)[1]);
			}catch(ArrayIndexOutOfBoundsException exc){}
		}
	}
	/**
	 * Loads Additional dictionary
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
			JOptionPane.showMessageDialog(null,"Bengali additional dictionary read error","File Read Error",JOptionPane.ERROR_MESSAGE);
		}
		String buf_tok[]=buf.split("\n");
		for(int i=0;i<buf_tok.length;i++){
			if(buf_tok[i].length()>maxwordlength)
				maxwordlength=buf_tok[i].length();
			try{
				addWordWithForms(buf_tok[i].split(" ",2)[0],buf_tok[i].split(" ",3)[1]);
			}catch(ArrayIndexOutOfBoundsException exc){}
		}
	}
	/**
	 * Loads the mapping for creating matching words
	 */
	private void loadMatchMap(){
		String buf="";
		try{
			InputStreamReader in=new InputStreamReader(
					getClass().getResourceAsStream(matchmappath),
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
			JOptionPane.showMessageDialog(null,"Mapping for finding matching word read error","File Read Error",JOptionPane.ERROR_MESSAGE);
		}
		String buf_tok[]=buf.split("\n");
		for(int i=0;i<buf_tok.length;i++){
			try{
				String[] map=new String[buf_tok[i].split(" ").length-1];
				for(int j=0;j<map.length;j++){
					if(buf_tok[i].split(" ")[j+1].equals("null"))
						map[j]="";
					else
						map[j]=buf_tok[i].split(" ")[j+1];
				}
				match_map.put(buf_tok[i].split(" ",2)[0],map);
			}catch(ArrayIndexOutOfBoundsException exc){}
		}
	}
	/**
	 * Flags used for generating verbs from verbal-nouns
	 */
	private final int A_E=1,O_U=2,E_I=4,E_A=8;
	/**
	 * Does modification in verb as parameters sent
	 * @param word : the word in which modification is to be done
	 * @param flag : which modification to be done
	 * @return 
	 */
	private String mod(String word,int flag){
		try{
			if((flag&E_A)!=0 && word.charAt(1)=='\u09C7'/*ে*/)
				return word.substring(0,1)+"\u09BE"/*া*/+word.substring(2);
			if((flag&A_E)!=0 && word.charAt(1)=='\u09BE'/*া*/)
				return word.substring(0,1)+"\u09C7"/*ে*/+word.substring(2);
			if((flag&O_U)!=0 && word.charAt(1)=='\u09CB'/*ো*/)
				return word.substring(0,1)+"\u09C1"/*ু*/+word.substring(2);
			if((flag&E_I)!=0 && word.charAt(1)=='\u09C7'/*ে*/)
				return word.substring(0,1)+"\u09BF"/*ি*/+word.substring(2);
			if((flag&E_A)!=0 && word.charAt(0)=='\u098F'/*এ*/)
				return "\u0986"/*আ*/+word.substring(1);
			if((flag&A_E)!=0 && word.charAt(0)=='\u0986'/*আ*/)
				return "\u098F"/*এ*/+word.substring(1);
			if((flag&O_U)!=0 && word.charAt(0)=='\u0993'/*ও*/)
				return "\u0989"/*উ*/+word.substring(1);
			if((flag&E_I)!=0 && word.charAt(0)=='\u098F'/*এ*/)
				return "\u0987"/*ই*/+word.substring(1);
		}catch(StringIndexOutOfBoundsException exc){}
		return word;
	}
	/**
	 * Adds a word to dictionary and also adds word+ও and word+ই
	 */
	private void addWord(String word){
		dic.put(word,"");
	}
	/**
	 * Adds noun forms of a noun in dictionary
	 */
	private void addNounForms(String word){
		addWord(word+"\u09B0\u09BE");																// +রা
		addWord(word+"\u0995\u09C7");																// +কে
		addWord(word+"\u09A6\u09C7\u09B0");														// +দের
		addWord(word+"\u09A6\u09C7\u09B0\u0995\u09C7");											// +দেরকে
		addWord(word+"\u09A6\u09BF\u0997\u09C7\u09B0");											// +দিগের
		addWord(word+"\u09A6\u09BF\u0997\u0995\u09C7");											// +দিগকে
		addWord(word+"\u09A6\u09BF\u0997\u09C7");													// +দিগে
		addWord(word+"\u09AC\u09C3\u09A8\u09CD\u09A6");											// +বৃন্দ
		addWord(word+"\u09AC\u09C3\u09A8\u09CD\u09A6\u0995\u09C7");								// +বৃন্দকে
		addWord(word+"\u09AC\u09C3\u09A8\u09CD\u09A6\u09C7\u09B0");								// +বৃন্দের
		addWord(word+"\u09B8\u09AE\u09C2\u09B9");													// +সমূহ
		addWord(word+"\u09B8\u09AE\u09C2\u09B9\u0995\u09C7");										// +সমূহকে
		addWord(word+"\u09B8\u09AE\u09C2\u09B9\u09C7\u09B0");										// +সমূহের
		addWord(word+"\u099F\u09BF");																// +টি
		addWord(word+"\u099F\u09BF\u09B0");														// +টির
		addWord(word+"\u099F\u09BF\u0995\u09C7");													// +টিকে
		addWord(word+"\u099F\u09BE");																// +টা
		addWord(word+"\u099F\u09BE\u09B0");														// +টার
		addWord(word+"\u099F\u09BE\u0995\u09C7");													// +টাকে
		addWord(word+"\u0997\u09C1\u09B2\u09CB");													// +গুলো
		addWord(word+"\u0997\u09C1\u09B2\u09CB\u09B0");											// +গুলোর
		addWord(word+"\u0997\u09C1\u09B2\u09CB\u0995\u09C7");										// +গুলোকে
		addWord(word+"\u0997\u09C1\u09B2\u09BF");													// +গুলি
		addWord(word+"\u0997\u09C1\u09B2\u09BF\u09B0");											// +গুলির
		addWord(word+"\u0997\u09C1\u09B2\u09BF\u0995\u09C7");										// +গুলিকে
		if(CType.isBengaliConsonant(word.charAt(word.length()-1))){
			addWord(word+"\u09C7\u09B0");															// +ের
			addWord(word+"\u09C7");																// +ে
			addWord(word+"\u09C7\u09A4\u09C7");													// +েতে
		}
		else if(CType.isBengaliVowel(word.charAt(word.length()-1))){
			addWord(word+"\u09B0");																// +র
			addWord(word+"\u09DF\u09C7");															// +য়ে
			addWord(word+"\u09A4\u09C7");															// +তে
		}
		else if(CType.isBengaliVowelSign(word.charAt(word.length()-1))||
				CType.isBengaliExtraConsonant(word.charAt(word.length()-1))){
			addWord(word+"\u09B0");																// +র
			addWord(word+"\u09DF\u09C7\u09B0");													// +য়ের
			addWord(word+"\u09DF");																// +য়
			addWord(word+"\u09A4\u09C7");															// +তে
			addWord(word+"\u09DF\u09C7\u09A4\u09C7");												// +য়েতে
		}
	}
	/**
	 * Adds Verb forms of a VerbalNoun in dictionary
	 */
	private void addVerbForms(String word){
		if(word.endsWith("\u0993\u09DF\u09BE\u09A8\u09CB")){							//ওয়ানো
			String part=word.substring(0,word.length()-5);//Part of word
			addWord(part+"\u0993\u09DF\u09BE\u0987");												// +ওয়াই
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09A8\u09BF");									// +ওয়াইনি
			addWord(part+"\u0993\u09DF\u09BE\u0993");												// +ওয়াও
			addWord(part+"\u0993\u09DF\u09BE\u0993\u09A8\u09BF");									// +ওয়াওনি
			addWord(part+"\u0993\u09DF\u09BE\u09A8");												// +ওয়ান
			addWord(part+"\u0993\u09DF\u09BE\u09A8\u09A8\u09BF");									// +ওয়াননি
			addWord(part+"\u0993\u09DF\u09BE\u09B8");												// +ওয়াস
			addWord(part+"\u0993\u09DF\u09BE\u09B8\u09A8\u09BF");									// +ওয়াসনি
			addWord(part+"\u0993\u09DF\u09BE");													// +ওয়া
			addWord(part+"\u0993\u09DF\u09BE\u09DF");												// +ওয়ায়
			addWord(part+"\u0993\u09DF\u09BE\u09DF\u09A8\u09BF");									// +ওয়ায়নি
			addWord(part+"\u0993\u09DF\u09BE\u0995");												// +ওয়াক
			
			addWord(part+"\u0993\u09DF\u09BE\u09B2\u09BE\u09AE");									// +ওয়ালাম
			addWord(part+"\u0993\u09DF\u09BE\u09B2\u09C7");										// +ওয়ালে
			addWord(part+"\u0993\u09DF\u09BE\u09B2\u09C7\u09A8");									// +ওয়ালেন
			addWord(part+"\u0993\u09DF\u09BE\u09B2\u09BF");										// +ওয়ালি
			addWord(part+"\u0993\u09DF\u09BE\u09B2");												// +ওয়াল
			
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09B2\u09BE\u09AE");							// +ওয়াইলাম
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09B2\u09C7");									// +ওয়াইলে
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09B2\u09C7\u09A8");							// +ওয়াইলেন
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09B2\u09BF");									// +ওয়াইলি
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09B2");										// +ওয়াইল
			
			addWord(part+"\u0993\u09DF\u09BE\u09AC");												// +ওয়াব
			addWord(part+"\u0993\u09DF\u09BE\u09AC\u09C7");										// +ওয়াবে
			addWord(part+"\u0993\u09DF\u09BE\u09AC\u09C7\u09A8");									// +ওয়াবেন
			addWord(part+"\u0993\u09DF\u09BE\u09AC\u09BF");										// +ওয়াবি
			
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09AC");										// +ওয়াইব
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09AC\u09C7");									// +ওয়াইবে
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09AC\u09C7\u09A8");							// +ওয়াইবেন
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09AC\u09BF");									// +ওয়াইবি
		
			addWord(part+"\u0993\u09DF\u09BE\u09A4\u09BE\u09AE");									// +ওয়াতাম
			addWord(part+"\u0993\u09DF\u09BE\u09A4\u09CB");										// +ওয়াতে
			addWord(part+"\u0993\u09DF\u09BE\u09A4\u09C7\u09A8");									// +ওয়াতেন
			addWord(part+"\u0993\u09DF\u09BE\u09A4\u09BF\u09B8");									// +ওয়াতিস
			addWord(part+"\u0993\u09DF\u09BE\u09A4");												// +ওয়াত
		
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09A4\u09BE\u09AE");							// +ওয়াইতাম
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09A4\u09CB");									// +ওয়াইতে
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09A4\u09C7\u09A8");							// +ওয়াইতেন
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09A4\u09BF\u09B8");							// +ওয়াইতিস
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09A4");										// +ওয়াইত
			
			addWord(part+"\u0993\u09DF\u09BE\u099A\u09CD\u099B\u09BF");							// +ওয়াচ্ছি
			addWord(part+"\u0993\u09DF\u09BE\u099A\u09CD\u099B");									// +ওয়াচ্ছ
			addWord(part+"\u0993\u09DF\u09BE\u099A\u09CD\u099B\u09C7\u09A8");						// +ওয়াচ্ছেন
			addWord(part+"\u0993\u09DF\u09BE\u099A\u09CD\u099B\u09BF\u09B8");						// +ওয়াচ্ছিস
			addWord(part+"\u0993\u09DF\u09BE\u099A\u09CD\u099B\u09C7");							// +ওয়াচ্ছে
			
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09A4\u09C7\u099B\u09BF");						// +ওয়াইতেছি
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09A4\u09C7\u099B");							// +ওয়াইতেছ
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09A4\u09C7\u099B\u09C7\u09A8");				// +ওয়াইতেছেন
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09A4\u09C7\u099B\u09C7");						// +ওয়াইতেছে
			
			addWord(part+"\u0993\u09DF\u09BE\u099A\u09CD\u099B\u09BF\u09B2\u09BE\u09AE");			// +ওয়াচ্ছিলাম
			addWord(part+"\u0993\u09DF\u09BE\u099A\u09CD\u099B\u09BF\u09B2\u09C7");				// +ওয়াচ্ছিলে
			addWord(part+"\u0993\u09DF\u09BE\u099A\u09CD\u099B\u09BF\u09B2\u09C7\u09A8");			// +ওয়াচ্ছিলেন
			addWord(part+"\u0993\u09DF\u09BE\u099A\u09CD\u099B\u09BF\u09B2\u09BF\u09B8");			// +ওয়াচ্ছিলিস
			addWord(part+"\u0993\u09DF\u09BE\u099A\u09CD\u099B\u09BF\u09B2");						// +ওয়াচ্ছিল
		
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09A4\u09C7\u099B\u09BF\u09B2\u09BE\u09AE");	// +ওয়াইতেছিলাম
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09A4\u09C7\u099B\u09BF\u09B2\u09C7");			// +ওয়াইতেছিলে
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09A4\u09C7\u099B\u09BF\u09B2\u09C7\u09A8");	// +ওয়াইতেছিলেন
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09A4\u09C7\u099B\u09BF\u09B2");				// +ওয়াইতেছিল
		
			addWord(part+"\u0987\u09DF\u09C7\u099B\u09BF\u09B2\u09BE\u09AE");						// +ইয়েছিলাম
			addWord(part+"\u0987\u09DF\u09C7\u099B\u09BF\u09B2\u09C7");							// +ইয়েছিলে
			addWord(part+"\u0987\u09DF\u09C7\u099B\u09BF\u09B2\u09BF\u09B8");						// +ইয়েছিলিস
			addWord(part+"\u0987\u09DF\u09C7\u099B\u09BF\u09B2\u09C7\u09AE");						// +ইয়েছিলেন
			addWord(part+"\u0987\u09DF\u09C7\u099B\u09BF\u09B2");									// +ইয়েছিল
			
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09DF\u09BE\u099B\u09BF\u09B2\u09BE\u09AE");	// +ওয়াইয়াছিলাম
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09DF\u09BE\u099B\u09BF\u09B2\u09C7");			// +ওয়াইয়াছিলে
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09DF\u09BE\u099B\u09BF\u09B2\u09C7\u09A8");	// +ওয়াইয়াছিলেন
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09DF\u09BE\u099B\u09BF\u09B2");				// +ওয়াইয়াছিল
			
			addWord(part+"\u0987\u09DF\u09C7\u099B\u09BF");										// +ইয়েছি
			addWord(part+"\u0987\u09DF\u09C7\u099B");												// +ইয়েছ
			addWord(part+"\u0987\u09DF\u09C7\u099B\u09BF\u09B8");									// +ইয়েছিস
			addWord(part+"\u0987\u09DF\u09C7\u099B\u09C7\u09A8");									// +ইয়েছেন
			addWord(part+"\u0987\u09DF\u09C7\u099B\u09C7");										// +ইয়েছে
			
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09DF\u09BE\u099B\u09BF");						// +ওয়াইয়াছি
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09DF\u09BE\u099B");							// +ওয়াইয়াছ
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09DF\u09BE\u099B\u09C7\u09A8");				// +ওয়াইয়াছেন
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09DF\u09BE\u099B\u09C7");						// +ওয়াইয়াছে
		
			addWord(part+"\u0987\u09DF\u09C7");													// +ইয়ে
			
			addWord(part+"\u0993\u09DF\u09BE\u0987\u09DF\u09BE");									// +ওয়াইয়া
			
			addWord(part+"\u0987\u09DF\u09CB");													// +ইয়ো
		}
		else if(word.endsWith("\u0993\u09DF\u09BE")){										//ওয়া
			String part=word.substring(0,word.length()-3);//Part of word
				
			addWord(mod(part,E_I)+"\u0987");														// +ই
			addWord(mod(part,E_I)+"\u0987\u09A8\u09BF");											// +ইনি
			addWord(mod(part,E_A)+"\u0993");														// +ও
			addWord(mod(part,E_A)+"\u0993\u09A8\u09BF");											// +ওনি
			addWord(mod(part,E_I)+"\u09A8");														// +ন
			addWord(mod(part,E_I)+"\u09A8\u09A8\u09BF");											// +ননি
			addWord(mod(part,E_I)+"\u09B8");														// +স
			addWord(mod(part,E_I)+"\u09B8\u09A8\u09BF");											// +সনি
			addWord(part);																		// +
			addWord(part+"\u09DF");																// +য়
			addWord(part+"\u09DF\u09A8\u09BF");													// +য়নি
			addWord(mod(part,E_I)+"\u0995");														// +ক
			
			addWord(mod(part,E_I)+"\u09B2\u09BE\u09AE");											// +লাম
			addWord(mod(part,E_I)+"\u09B2\u09C7");												// +লে
			addWord(mod(part,E_I)+"\u09B2\u09C7\u09A8");											// +লেন
			addWord(mod(part,E_I)+"\u09B2\u09BF");												// +লি
			addWord(mod(part,E_I)+"\u09B2");														// +ল
			
			addWord(part+"\u0987\u09B2\u09BE\u09AE");												// +ইলাম
			addWord(part+"\u0987\u09B2\u09C7");													// +ইলে
			addWord(part+"\u0987\u09B2\u09C7\u09A8");												// +ইলেন
			addWord(part+"\u0987\u09B2\u09BF");													// +ইলি
			addWord(part+"\u0987\u09B2");															// +ইল
			
			addWord(part+"\u09AC");																// +ব
			addWord(part+"\u09AC\u09C7");															// +বে
			addWord(part+"\u09AC\u09C7\u09A8");													// +বেন
			addWord(part+"\u09AC\u09BF");															// +বি
			
			addWord(part+"\u0987\u09AC");															// +ইব
			addWord(part+"\u0987\u09AC\u09C7");													// +ইবে
			addWord(part+"\u0987\u09AC\u09C7\u09A8");												// +ইবেন
			addWord(part+"\u0987\u09AC\u09BF");													// +ইবি
		
			addWord(mod(part,E_I)+"\u09A4\u09BE\u09AE");											// +তাম
			addWord(mod(part,E_I)+"\u09A4\u09CB");												// +তে
			addWord(mod(part,E_I)+"\u09A4\u09C7\u09A8");											// +তেন
			addWord(mod(part,E_I)+"\u09A4\u09BF\u09B8");											// +তিস
			addWord(mod(part,E_I)+"\u09A4");														// +ত
		
			addWord(part+"\u0987\u09A4\u09BE\u09AE");												// +ইতাম
			addWord(part+"\u0987\u09A4\u09CB");													// +ইতে
			addWord(part+"\u0987\u09A4\u09C7\u09A8");												// +ইতেন
			addWord(part+"\u0987\u09A4\u09BF\u09B8");												// +ইতিস
			addWord(part+"\u0987\u09A4");															// +ইত	
			
			addWord(mod(part,E_I)+"\u099A\u09CD\u099B\u09BF");									// +চ্ছি
			addWord(mod(part,E_I)+"\u099A\u09CD\u099B");											// +চ্ছ
			addWord(mod(part,E_I)+"\u099A\u09CD\u099B\u09C7\u09A8");								// +চ্ছেন
			addWord(mod(part,E_I)+"\u099A\u09CD\u099B\u09BF\u09B8");								// +চ্ছিস
			addWord(mod(part,E_I)+"\u099A\u09CD\u099B\u09C7");									// +চ্ছে
			
			addWord(mod(part,E_I)+"\u0987\u099B\u09BF");											// +ইছি
			addWord(mod(part,E_I)+"\u0987\u099B");												// +ইছ
			addWord(mod(part,E_I)+"\u0987\u099B\u09C7\u09A8");									// +ইছেন
			addWord(mod(part,E_I)+"\u0987\u099B\u09BF\u09B8");									// +ইছিস
			addWord(mod(part,E_I)+"\u0987\u099B\u09C7");											// +ইছে
			
			addWord(part+"\u0987\u09A4\u09C7\u099B\u09BF");										// +ইতেছি
			addWord(part+"\u0987\u09A4\u09C7\u099B");												// +ইতেছ
			addWord(part+"\u0987\u09A4\u09C7\u099B\u09C7\u09A8");									// +ইতেছেন
			addWord(part+"\u0987\u09A4\u09C7\u099B\u09C7");										// +ইতেছে
			
			addWord(mod(part,E_I)+"\u099A\u09CD\u099B\u09BF\u09B2\u09BE\u09AE");					// +চ্ছিলাম
			addWord(mod(part,E_I)+"\u099A\u09CD\u099B\u09BF\u09B2\u09C7");						// +চ্ছিলে
			addWord(mod(part,E_I)+"\u099A\u09CD\u099B\u09BF\u09B2\u09C7\u09A8");					// +চ্ছিলেন
			addWord(mod(part,E_I)+"\u099A\u09CD\u099B\u09BF\u09B2\u09BF\u09B8");					// +চ্ছিলিস
			addWord(mod(part,E_I)+"\u099A\u09CD\u099B\u09BF\u09B2");								// +চ্ছিল
			
			addWord(mod(part,E_I)+"\u0987\u099B\u09BF\u09B2\u09BE\u09AE");						// +ইছিলাম
			addWord(mod(part,E_I)+"\u0987\u099B\u09BF\u09B2\u09C7");								// +ইছিলে
			addWord(mod(part,E_I)+"\u0987\u099B\u09BF\u09B2\u09C7\u09A8");						// +ইছিলেন
			addWord(mod(part,E_I)+"\u0987\u099B\u09BF\u09B2\u09BF\u09B8");						// +ইছিলিস
			addWord(mod(part,E_I)+"\u0987\u099B\u09BF\u09B2");									// +ইছিল
		
			addWord(part+"\u0987\u09A4\u09C7\u099B\u09BF\u09B2\u09BE\u09AE");						// +ইতেছিলাম
			addWord(part+"\u0987\u09A4\u09C7\u099B\u09BF\u09B2\u09C7");							// +ইতেছিলে
			addWord(part+"\u0987\u09A4\u09C7\u099B\u09BF\u09B2\u09C7\u09A8");						// +ইতেছিলেন
			addWord(part+"\u0987\u09A4\u09C7\u099B\u09BF\u09B2");									// +ইতেছিল
			
			addWord(mod(part,E_I)+"\u09DF\u09C7\u099B\u09BF\u09B2\u09BE\u09AE");					// +য়েছিলাম
			addWord(mod(part,E_I)+"\u09DF\u09C7\u099B\u09BF\u09B2\u09C7");						// +য়েছিলে
			addWord(mod(part,E_I)+"\u09DF\u09C7\u099B\u09BF\u09B2\u09BF\u09B8");					// +য়েছিলিস
			addWord(mod(part,E_I)+"\u09DF\u09C7\u099B\u09BF\u09B2\u09C7\u09AE");					// +য়েছিলেন
			addWord(mod(part,E_I)+"\u09DF\u09C7\u099B\u09BF\u09B2");								// +য়েছিল
			
			addWord(part+"\u0987\u09DF\u09BE\u099B\u09BF\u09B2\u09BE\u09AE");						// +ইয়াছিলাম
			addWord(part+"\u0987\u09DF\u09BE\u099B\u09BF\u09B2\u09C7");							// +ইয়াছিলে
			addWord(part+"\u0987\u09DF\u09BE\u099B\u09BF\u09B2\u09C7\u09A8");						// +ইয়াছিলেন
			addWord(part+"\u0987\u09DF\u09BE\u099B\u09BF\u09B2\u09BF\u09B8");						// +ইয়াছিলিস
			addWord(part+"\u0987\u09DF\u09BE\u099B\u09BF\u09B2");									// +ইয়াছিল
			
			addWord(mod(part,E_I)+"\u09DF\u09C7\u099B\u09BF");									// +য়েছি
			addWord(mod(part,E_I)+"\u09DF\u09C7\u099B");											// +য়েছ
			addWord(mod(part,E_I)+"\u09DF\u09C7\u099B\u09C7\u09A8");								// +য়েছেন
			addWord(mod(part,E_I)+"\u09DF\u09C7\u099B\u09BF\u09B8");								// +য়েছিস
			addWord(mod(part,E_I)+"\u09DF\u09C7\u099B\u09C7");									// +য়েছে
			
			addWord(part+"\u0987\u09DF\u09BE\u099B\u09BF");										// +ইয়াছি
			addWord(part+"\u0987\u09DF\u09BE\u099B");												// +ইয়াছ
			addWord(part+"\u0987\u09DF\u09BE\u099B\u09C7\u09A8");									// +ইয়াছেন
			addWord(part+"\u0987\u09DF\u09BE\u099B\u09BF\u09B8");									// +ইয়াছিস
			addWord(part+"\u0987\u09DF\u09BE\u099B\u09C7");										// +ইয়াছে
		
			addWord(mod(part,E_I)+"\u09DF\u09C7");												// +য়ে
			
			addWord(part+"\u0987\u09DF\u09BE");													// +ইয়া
			
			addWord(mod(part,E_I|O_U|A_E)+"\u0993");												// +ও
		}
		if(word.endsWith("\u09BE\u09A8\u09CB")){										//ানো
			String part=word.substring(0,word.length()-3);//Part of word
			addWord(part+"\u09BE\u0987");															// +াই
			addWord(part+"\u09BE\u0987\u09A8\u09BF");												// +াইনি
			addWord(part+"\u09BE\u0993");															// +াও
			addWord(part+"\u09BE\u0993\u09A8\u09BF");												// +াওনি
			addWord(part+"\u09BE\u09A8");															// +ান
			addWord(part+"\u09BE\u09A8\u09A8\u09BF");												// +াননি
			addWord(part+"\u09BE\u09B8");															// +াস
			addWord(part+"\u09BE\u09B8\u09A8\u09BF");												// +াসনি
			addWord(part+"\u09BE");																// +া
			addWord(part+"\u09BE\u09DF");															// +ায়
			addWord(part+"\u09BE\u09DF\u09A8\u09BF");												// +ায়নি
			addWord(part+"\u09BE\u0995");															// +াক
			
			addWord(part+"\u09BE\u09B2\u09BE\u09AE");												// +ালাম
			addWord(part+"\u09BE\u09B2\u09C7");													// +ালে
			addWord(part+"\u09BE\u09B2\u09C7\u09A8");												// +ালেন
			addWord(part+"\u09BE\u09B2\u09BF");													// +ালি
			addWord(part+"\u09BE\u09B2");															// +াল
			
			addWord(part+"\u09BE\u0987\u09B2\u09BE\u09AE");										// +াইলাম
			addWord(part+"\u09BE\u0987\u09B2\u09C7");												// +াইলে
			addWord(part+"\u09BE\u0987\u09B2\u09C7\u09A8");										// +াইলেন
			addWord(part+"\u09BE\u0987\u09B2\u09BF");												// +াইলি
			addWord(part+"\u09BE\u0987\u09B2");													// +াইল
			
			addWord(part+"\u09BE\u09AC");															// +াব
			addWord(part+"\u09BE\u09AC\u09C7");													// +াবে
			addWord(part+"\u09BE\u09AC\u09C7\u09A8");												// +াবেন
			addWord(part+"\u09BE\u09AC\u09BF");													// +াবি
			
			addWord(part+"\u09BE\u0987\u09AC");													// +াইব
			addWord(part+"\u09BE\u0987\u09AC\u09C7");												// +াইবে
			addWord(part+"\u09BE\u0987\u09AC\u09C7\u09A8");										// +াইবেন
			addWord(part+"\u09BE\u0987\u09AC\u09BF");												// +াইবি
		
			addWord(part+"\u09BE\u09A4\u09BE\u09AE");												// +াতাম
			addWord(part+"\u09BE\u09A4\u09CB");													// +াতে
			addWord(part+"\u09BE\u09A4\u09C7\u09A8");												// +াতেন
			addWord(part+"\u09BE\u09A4\u09BF\u09B8");												// +াতিস
			addWord(part+"\u09BE\u09A4");															// +াত
		
			addWord(part+"\u09BE\u0987\u09A4\u09BE\u09AE");										// +াইতাম
			addWord(part+"\u09BE\u0987\u09A4\u09CB");												// +াইতে
			addWord(part+"\u09BE\u0987\u09A4\u09C7\u09A8");										// +াইতেন
			addWord(part+"\u09BE\u0987\u09A4\u09BF\u09B8");										// +াইতিস
			addWord(part+"\u09BE\u0987\u09A4");													// +াইত
			
			addWord(part+"\u09BE\u099A\u09CD\u099B\u09BF");										// +াচ্ছি
			addWord(part+"\u09BE\u099A\u09CD\u099B");												// +াচ্ছ
			addWord(part+"\u09BE\u099A\u09CD\u099B\u09C7\u09A8");									// +াচ্ছেন
			addWord(part+"\u09BE\u099A\u09CD\u099B\u09BF\u09B8");									// +াচ্ছিস
			addWord(part+"\u09BE\u099A\u09CD\u099B\u09C7");										// +াচ্ছে
			
			addWord(part+"\u09BE\u0987\u09A4\u09C7\u099B\u09BF");									// +াইতেছি
			addWord(part+"\u09BE\u0987\u09A4\u09C7\u099B");										// +াইতেছ
			addWord(part+"\u09BE\u0987\u09A4\u09C7\u099B\u09C7\u09A8");							// +াইতেছেন
			addWord(part+"\u09BE\u0987\u09A4\u09C7\u099B\u09C7");									// +াইতেছে
			
			addWord(part+"\u09BE\u099A\u09CD\u099B\u09BF\u09B2\u09BE\u09AE");						// +াচ্ছিলাম
			addWord(part+"\u09BE\u099A\u09CD\u099B\u09BF\u09B2\u09C7");							// +াচ্ছিলে
			addWord(part+"\u09BE\u099A\u09CD\u099B\u09BF\u09B2\u09C7\u09A8");						// +াচ্ছিলেন
			addWord(part+"\u09BE\u099A\u09CD\u099B\u09BF\u09B2\u09BF\u09B8");						// +াচ্ছিলিস
			addWord(part+"\u09BE\u099A\u09CD\u099B\u09BF\u09B2");									// +াচ্ছিল
		
			addWord(part+"\u09BE\u0987\u09A4\u09C7\u099B\u09BF\u09B2\u09BE\u09AE");				// +াইতেছিলাম
			addWord(part+"\u09BE\u0987\u09A4\u09C7\u099B\u09BF\u09B2\u09C7");						// +াইতেছিলে
			addWord(part+"\u09BE\u0987\u09A4\u09C7\u099B\u09BF\u09B2\u09C7\u09A8");				// +াইতেছিলেন
			addWord(part+"\u09BE\u0987\u09A4\u09C7\u099B\u09BF\u09B2");							// +াইতেছিল
		
			addWord(part+"\u09BF\u09DF\u09C7\u099B\u09BF\u09B2\u09BE\u09AE");						// +িয়েছিলাম
			addWord(part+"\u09BF\u09DF\u09C7\u099B\u09BF\u09B2\u09C7");							// +িয়েছিলে
			addWord(part+"\u09BF\u09DF\u09C7\u099B\u09BF\u09B2\u09BF\u09B8");						// +িয়েছিলিস
			addWord(part+"\u09BF\u09DF\u09C7\u099B\u09BF\u09B2\u09C7\u09AE");						// +িয়েছিলেন
			addWord(part+"\u09BF\u09DF\u09C7\u099B\u09BF\u09B2");									// +িয়েছিল
			
			addWord(part+"\u09BE\u0987\u09DF\u09BE\u099B\u09BF\u09B2\u09BE\u09AE");				// +াইয়াছিলাম
			addWord(part+"\u09BE\u0987\u09DF\u09BE\u099B\u09BF\u09B2\u09C7");						// +াইয়াছিলে
			addWord(part+"\u09BE\u0987\u09DF\u09BE\u099B\u09BF\u09B2\u09C7\u09A8");				// +াইয়াছিলেন
			addWord(part+"\u09BE\u0987\u09DF\u09BE\u099B\u09BF\u09B2");							// +াইয়াছিল
			
			addWord(part+"\u09BF\u09DF\u09C7\u099B\u09BF");										// +িয়েছি
			addWord(part+"\u09BF\u09DF\u09C7\u099B");												// +িয়েছ
			addWord(part+"\u09BF\u09DF\u09C7\u099B\u09BF\u09B8");									// +িয়েছিস
			addWord(part+"\u09BF\u09DF\u09C7\u099B\u09C7\u09A8");									// +িয়েছেন
			addWord(part+"\u09BF\u09DF\u09C7\u099B\u09C7");										// +িয়েছে
			
			addWord(part+"\u09BE\u0987\u09DF\u09BE\u099B\u09BF");									// +াইয়াছি
			addWord(part+"\u09BE\u0987\u09DF\u09BE\u099B");										// +াইয়াছ
			addWord(part+"\u09BE\u0987\u09DF\u09BE\u099B\u09C7\u09A8");							// +াইয়াছেন
			addWord(part+"\u09BE\u0987\u09DF\u09BE\u099B\u09C7");									// +াইয়াছে
		
			addWord(part+"\u09BF\u09DF\u09C7");													// +িয়ে
			
			addWord(part+"\u09BE\u0987\u09DF\u09BE");												// +াইয়া
			
			addWord(part+"\u09BF\u09DF\u09CB");													// +িয়ো
		}
		else if(word.endsWith("\u09BE")){												//া
			String part=word.substring(0,word.length()-1);//Part of word
			addWord(mod(part,O_U)+"\u09BF");														// +ি
			addWord(mod(part,O_U)+"\u09BF\u09A8\u09BF");											// +িনি
			addWord(part);																		// +
			addWord(part+"\u09CB");																// +ো
			addWord(part+"\u09CB\u09A8\u09BF");													// +োনি
			addWord(part+"\u09C7\u09A8");															// +েন
			addWord(part+"\u09C7\u09A8\u09A8\u09BF");												// +েননি
			addWord(mod(part,O_U)+"\u09C1\u09A8");												// +ুন
			addWord(mod(part,O_U)+"\u09BF\u09B8");												// +িস
			addWord(mod(part,O_U)+"\u09BF\u09B8\u09A8\u09BF");									// +িসনি
			addWord(part+"\u09C7");																// +ে
			addWord(part+"\u09C7\u09A8\u09BF");													// +েনি
			addWord(mod(part,O_U)+"\u09C1\u0995");												// +ুক
			
			addWord(mod(part,O_U)+"\u09B2\u09BE\u09AE");											// +লাম
			addWord(mod(part,O_U)+"\u09B2\u09C7");												// +লে
			addWord(mod(part,O_U)+"\u09B2\u09C7\u09A8");											// +লেন
			addWord(mod(part,O_U)+"\u09B2\u09BF");												// +লি
			addWord(mod(part,O_U)+"\u09B2");														// +ল
			
			addWord(mod(part,O_U)+"\u09BF\u09B2\u09BE\u09AE");									// +িলাম
			addWord(mod(part,O_U)+"\u09BF\u09B2\u09C7");											// +িলে
			addWord(mod(part,O_U)+"\u09BF\u09B2\u09C7\u09A8");									// +িলেন
			addWord(mod(part,O_U)+"\u09BF\u09B2\u09BF");											// +িলি
			addWord(mod(part,O_U)+"\u09BF\u09B2");												// +িল
			
			addWord(mod(part,O_U)+"\u099B\u09BF");												// +ছি
			addWord(mod(part,O_U)+"\u099B");														// +ছ
			addWord(mod(part,O_U)+"\u099B\u09C7\u09A8");											// +ছেন
			addWord(mod(part,O_U)+"\u099B\u09BF\u09B8");											// +ছিস
			addWord(mod(part,O_U)+"\u099B\u09C7");												// +ছে
		
			addWord(mod(part,O_U)+"\u09BF\u09A4\u09C7\u099B\u09BF");								// +িতেছি
			addWord(mod(part,O_U)+"\u09BF\u09A4\u09C7\u099B");									// +িতেছ
			addWord(mod(part,O_U)+"\u09BF\u09A4\u09C7\u099B\u09C7\u09A8");						// +িতেছেন
			addWord(mod(part,O_U)+"\u09BF\u09A4\u09C7\u099B\u09BF\u09B8");						// +িতেছিস
			addWord(mod(part,O_U)+"\u09BF\u09A4\u09C7\u099B\u09C7");								// +িতেছে
				
			addWord(mod(part,O_U)+"\u099B\u09BF\u09B2\u09BE\u09AE");								// +ছিলাম
			addWord(mod(part,O_U)+"\u099B\u09BF\u09B2\u09C7");									// +ছিলে
			addWord(mod(part,O_U)+"\u099B\u09BF\u09B2\u09C7\u09A8");								// +ছিলেন
			addWord(mod(part,O_U)+"\u099B\u09BF\u09B2\u09BF\u09B8");								// +ছিলিস
			addWord(mod(part,O_U)+"\u099B\u09BF\u09B2");											// +ছিল
		
			addWord(mod(part,O_U)+"\u09BF\u09A4\u09C7\u099B\u09BF\u09B2\u09BE\u09AE");			// +িতেছিলাম
			addWord(mod(part,O_U)+"\u09BF\u09A4\u09C7\u099B\u09BF\u09B2\u09C7");					// +িতেছিলে
			addWord(mod(part,O_U)+"\u09BF\u09A4\u09C7\u099B\u09BF\u09B2\u09C7\u09A8");			// +িতেছিলেন
			addWord(mod(part,O_U)+"\u09BF\u09A4\u09C7\u099B\u09BF\u09B2\u09BF\u09B8");			// +িতেছিলিস
			addWord(mod(part,O_U)+"\u09BF\u09A4\u09C7\u099B\u09BF\u09B2");						// +িতেছিল
				
			addWord(mod(part,O_U)+"\u09AC");														// +ব
			addWord(mod(part,O_U)+"\u09AC\u09C7");												// +বে
			addWord(mod(part,O_U)+"\u09AC\u09C7\u09A8");											// +বেন
			addWord(mod(part,O_U)+"\u09AC\u09BF");												// +বি
		
			addWord(mod(part,O_U)+"\u09BF\u09AC");												// +িব
			addWord(mod(part,O_U)+"\u09BF\u09AC\u09C7");											// +িবে
			addWord(mod(part,O_U)+"\u09BF\u09AC\u09C7\u09A8");									// +িবেন
			addWord(mod(part,O_U)+"\u09BF\u09AC\u09BF");											// +িবি
		
			addWord(mod(part,O_U)+"\u09A4\u09BE\u09AE");											// +তাম
			addWord(mod(part,O_U)+"\u09A4\u09C7");												// +তে
			addWord(mod(part,O_U)+"\u09A4\u09C7\u09A8");											// +তেন
			addWord(mod(part,O_U)+"\u09A4\u09BF\u09B8");											// +তিস
			addWord(mod(part,O_U)+"\u09A4");														// +ত
		
			addWord(mod(part,O_U)+"\u09BF\u09A4\u09BE\u09AE");									// +িতাম
			addWord(mod(part,O_U)+"\u09BF\u09A4\u09CB");											// +িতে
			addWord(mod(part,O_U)+"\u09BF\u09A4\u09C7\u09A8");									// +িতেন
			addWord(mod(part,O_U)+"\u09BF\u09A4\u09BF\u09B8");									// +িতিস
			addWord(mod(part,O_U)+"\u09BF\u09A4");												// +িত
		
			addWord(mod(part,O_U|A_E)+"\u09C7\u099B\u09BF\u09B2\u09BE\u09AE");					// +েছিলাম
			addWord(mod(part,O_U|A_E)+"\u09C7\u099B\u09BF\u09B2\u09C7");							// +েছিলে
			addWord(mod(part,O_U|A_E)+"\u09C7\u099B\u09BF\u09B2\u09BF\u09B8");					// +েছিলিস
			addWord(mod(part,O_U|A_E)+"\u09C7\u099B\u09BF\u09B2\u09C7\u09AE");					// +েছিলেন
			addWord(mod(part,O_U|A_E)+"\u09C7\u099B\u09BF\u09B2");								// +েছিল
		
			addWord(mod(part,O_U)+"\u09BF\u09DF\u09BE\u099B\u09BF\u09B2\u09BE\u09AE");			// +িয়াছিলাম
			addWord(mod(part,O_U)+"\u09BF\u09DF\u09BE\u099B\u09BF\u09B2\u09C7");					// +িয়াছিলে
			addWord(mod(part,O_U)+"\u09BF\u09DF\u09BE\u099B\u09BF\u09B2\u09C7\u09A8");			// +িয়াছিলেন
			addWord(mod(part,O_U)+"\u09BF\u09DF\u09BE\u099B\u09BF\u09B2");						// +িয়াছিল
			
			addWord(mod(part,O_U|A_E)+"\u09C7\u099B\u09BF");										// +েছি
			addWord(mod(part,O_U|A_E)+"\u09C7\u099B");											// +েছ
			addWord(mod(part,O_U|A_E)+"\u09C7\u099B\u09BF\u09B8");								// +েছিস
			addWord(mod(part,O_U|A_E)+"\u09C7\u099B\u09C7\u09A8");								// +েছেন
			addWord(mod(part,O_U|A_E)+"\u09C7\u099B\u09C7");										// +েছে
		
			addWord(mod(part,O_U)+"\u09BF\u09DF\u09BE\u099B\u09BF");								// +িয়াছি
			addWord(mod(part,O_U)+"\u09BF\u09DF\u09BE\u099B");									// +িয়াছ
			addWord(mod(part,O_U)+"\u09BF\u09DF\u09BE\u099B\u09C7\u09A8");						// +িয়াছেন
			addWord(mod(part,O_U)+"\u09BF\u09DF\u09BE\u099B\u09C7");								// +িয়াছে
		
			addWord(mod(part,O_U|A_E)+"\u09C7");													// +ে
		
			addWord(mod(part,O_U)+"\u09BF\u09DF\u09BE");											// +িয়া

			addWord(mod(part,O_U|A_E)+"\u09CB");													// +ো
		}
	}
	/**
	 * @param word : add word to dictionary
	 * @param parts_of_speech : parts of speech of the word, 
	 * which can be (বি),(ক্রিবি),(বিণ),(বিণবিণ),(ক্রিবিণ),(সর্ব),(ক্রি),(অসক্রি),(অনুক্রি),(অব্য).
	 */
	private void addWordWithForms(String word,String parts_of_speech){
		addWord(word);
		if(parts_of_speech.equals("(\u09AC\u09BF)") ||											// (বি)
				parts_of_speech.equals("(\u0995\u09CD\u09B0\u09BF\u09AC\u09BF)")){				// (ক্রিবি)
			addNounForms(word);
		}
		if(parts_of_speech.equals("(\u0995\u09CD\u09B0\u09BF\u09AC\u09BF)")){					// (ক্রিবি)
			addVerbForms(word);
		}
	}
	/**
	 * @param word : word to be searched
	 * @return whether a word contains in dictionary
	 */
	public boolean contains(String word){
		if(dic.containsKey(word))
			return true;
		if((word.endsWith("\u0993")||word.endsWith("\u0987")) && dic.containsKey(word.substring(0,word.length()-1)))
			return true;
		return false;
	}
	/**
	 * @param word : word to be added in dictionary
	 */
	public void addToDictionary(String word,String parts_of_speech){
		addWordWithForms(word,parts_of_speech);
		File file=new File(additionaldictionarypath);
		try{
			if(Charset.availableCharsets().containsKey("UTF-8")){				
				OutputStreamWriter out=new OutputStreamWriter(
					new FileOutputStream(file,true),
					Charset.availableCharsets().get("UTF-8")
				);
				out.append(word+" "+parts_of_speech+"\n");
				out.close();
			}
			else{
				JOptionPane.showMessageDialog(null,
					"UTF-8 Character encoder not available",
					"Character Encoding Error",
					JOptionPane.ERROR_MESSAGE);				
			}
		}catch(java.io.IOException e){
			JOptionPane.showMessageDialog(
					null,
					"File \""+file.getPath()+"\" read error",
					"Error",
					JOptionPane.ERROR_MESSAGE
				);
		}
	}
	/**
	 * @param word to be searched
	 * @return word : word that first match in dictionary
	 */
	public String firstMatch(String word){
		Date d=new Date();
		if(word.length()<=maxwordlength){
			if(dic.containsKey(word))
				return word;
			String prediction=null;
			if(prediction==null)
				prediction=firstMatch_rec(word,"",d);
			return (prediction==null)?word:prediction;
		}
		else
			return word;
	}
	/**
	 * recursive version for searching matching word, used by function 
	 * <code>firstMatch()</code>
	 * @param end : end part of the word to be sent word to be searched
	 * @param start : first part of the word, to be sent ""
	 * @param fast : whether the search is to be done fast or not
	 * @return first matching word in dictionary
	 */
	private String firstMatch_rec(String end,String start,Date d){
		if(new Date().getTime()-d.getTime()>3000)
			return null;
		if(end.length()>0){
			try{
				String mapped[];
				for(int key_length=end.length();key_length>0;key_length--){
					String key=end.substring(0,key_length);
					if(match_map.containsKey(key)){
						mapped=match_map.get(key);
						for(int i=0;i<mapped.length;i++){
							String match=firstMatch_rec(end.substring(key_length),start+mapped[i],d);
							if(match!=null)
								return match;
						}
					}
					else if(key_length==1){
						String match=firstMatch_rec(end.substring(key_length),start+key,d);
						if(match!=null)
							return match;
					}
				}
			}catch(IndexOutOfBoundsException exc){}
			catch(NullPointerException exc){}
		}
		else{
			if(dic.containsKey(start))
				return start;
		}
		return null;
	}
	/**
	 * @param word to be searched
	 * @return a vector words that match in dictionary
	 */
	public Vector<String> allMatch(String word){
		Date d=new Date();
		Vector<String> prediction=new Vector<String>();
		if(dic.containsKey(word))
			return prediction;
		if(word.length()<=maxwordlength)
			allMatch_rec(prediction,word,"",d);
		return prediction;
	}
	/**
	 * recursive version for searching matching word, used by function 
	 * <code>allMatch()</code>
	 * @param end : end part of the word to be sent word to be searched
	 * @param start : first part of the word, to be sent ""
	 * @return a vector words that match in dictionary
	 */
	private void allMatch_rec(Vector<String> prediction,String end,String start,Date d){
		if(new Date().getTime()-d.getTime()>10000)
			return;
		if(end.length()>0){
			try{
				String mapped[];
				for(int key_length=end.length();key_length>0;key_length--){
					String key=end.substring(0,key_length);
					if(match_map.containsKey(key)){
						mapped=match_map.get(key);
						for(int i=0;i<mapped.length;i++)
							allMatch_rec(prediction,end.substring(key_length),start+mapped[i],d);
					}
					else if(key_length==1)
						allMatch_rec(prediction,end.substring(key_length),start+key,d);
				}
			}catch(IndexOutOfBoundsException exc){}
			catch(NullPointerException exc){}
		}
		else{
			if(dic.containsKey(start) && !prediction.contains(start))
				prediction.addElement(start);
		}
	}
}