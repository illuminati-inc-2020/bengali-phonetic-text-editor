package Bangla.util;

public class CType{
	/**
	 * checks whether c is a bengali or english puncuation
	 * @param c : a character
	 * @return 
	 */
	public static boolean isPunct(char c){
		char set[]={'~','`','!','@','#','$','%','^','^','*','(',')','-',
					'_','=','+','[','{',']','}','\\','|',';',':','\'',
					'\"',',','<','.','>','/','?',
					'\u09E5','\u09F2','\u09F3','\u09F4','\u09F5','\u09F6','\u09F7','\u09F8',
					'\u09F9','\u09FA','\u09FB','\u09FB','\u09FC'};
		for(int i=0;i<set.length;i++){
			if(c==set[i])
				return true;
		}
		return false;
	}
	/**
	 * checks whether c is a white space character
	 * @param c : a character
	 * @return 
	 */
	public static boolean isSpace(char c){
		char set[]={' ','\t','\n'};
		for(int i=0;i<set.length;i++){
			if(c==set[i])
				return true;
		}
		return false;
	}
	/**
	 * Checks whether c is a bengali digit
	 * @param c : a character
	 * @return 
	 */
	public static boolean isBengaliDigit(char c){
		if(c>='\u09E6' && c<='\u09EF')
			return true;
		else
			return false;
	}
	/**
	 * Checks whether c is a bengali vowel
	 * @param c : a character
	 * @return 
	 */
	public static boolean isBengaliVowel(char c){
		char set[]={'\u0985','\u0986','\u0987','\u0988','\u0989','\u098A','\u098B',
				'\u098C','\u098F','\u0990','\u0993','\u0994','\u09E0','\u09E1'};
		for(int i=0;i<set.length;i++){
			if(c==set[i])
				return true;
		}
		return false;
	}
	/**
	 * Checks whether c is a bengali vowel sign
	 * @param c : a character
	 * @return 
	 */
	public static boolean isBengaliVowelSign(char c){
		char set[]={'\u09BE','\u09BF','\u09C0','\u09C1','\u09C2','\u09C3','\u09C4','\u09C7','\u09C8',
				'\u09CB','\u09CC','\u09D7','\u09E2','\u09E3'};
		for(int i=0;i<set.length;i++){
			if(c==set[i])
				return true;
		}
		return false;
	}
	/**
	 * checks whether c is a bengali consonant
	 * @param c : a character
	 * @return 
	 */
	public static boolean isBengaliConsonant(char c){
		char set[]={'\u0995','\u0996','\u0997','\u0998','\u0999','\u099A','\u099B','\u099C',
				'\u099D','\u099E','\u099F','\u09A0','\u09A1','\u09A2','\u09A3','\u09A4','\u09A5',
				'\u09A6','\u09A7','\u09A8','\u09AA','\u09AB','\u09AC','\u09AD','\u09AE','\u09AF',
				'\u09B0','\u09B2','\u09B6','\u09B7','\u09B8','\u09B9','\u09DC','\u09DD','\u09DF',};
		for(int i=0;i<set.length;i++){
			if(c==set[i])
				return true;
		}
		return false;
	}
	/**
	 * Checks whether c is anusvara/khandata/visarga/chandrabindu
	 * @param c : a character
	 * @return 
	 */
	public static boolean isBengaliExtraConsonant(char c){
		char set[]={'\u0981','\u0982','\u0983','\u09CE'};
		for(int i=0;i<set.length;i++){
			if(c==set[i])
				return true;
		}
		return false;
	}
	/**
	 * Checks whether c is a bengali letter
	 * @param c : a character
	 * @return 
	 */
	public static boolean isBengaliLetter(char c){
		if(c>='\u0980' && c<='\u09E5')
			return true;
		else
			return false;
	}
	/**
	 * Checks whether s is a english number
	 * @param s : string
	 * @return 
	 */
	public static boolean isNumber(String s){
		for(int i=0;i<s.length();i++)
			if(!Character.isDigit(s.charAt(i)))
				return false;
		return true;
	}
	/**
	 * Checks whether s is a bengali number
	 * @param s : string
	 * @return 
	 */
	public static boolean isBengaliNumber(String s){
		for(int i=0;i<s.length();i++)
			if(!isBengaliDigit(s.charAt(i)))
				return false;
		return true;
	}
}