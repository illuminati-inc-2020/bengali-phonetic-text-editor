/* Subject			:	Window Interface for Bengali phonetic Software
 * Author			:	Romit Kr Atta
 * Date			: 	09/08/2011
 */

package Bangla.language;

import Bangla.util.*;
import javax.print.DocFlavor.STRING;
import javax.swing.JFrame;
import javax.swing.JTextArea;
import javax.swing.text.JTextComponent;
import java.util.HashMap;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

public class Converter{
	public static int SUCCESSFUL=0,FAILURE=1;
	private int pos;
	private char hold_key;
	/**
	* Creates a converter that converts input from english keyboard to bengali phonetic
	*/
	public Converter()
	{
			int x;
			this.pos=0;
			this.hold_key='\0';
			ConMap();
			GoshMap();
			SoroBorno();
	}	
	private HashMap<Character,String> cm;
	private HashMap<String,String> gm;
	private HashMap<String,String> sb;	
	/**
	* Deletes hasant when swarabarna is written after byanjanbarna
	* @param txt : text component to be used into
	* @param x : number of characters to be deleted
	*/
	private void delhasant(JTextComponent txt,int x)
	{
		String Buff=txt.getText();
		pos=txt.getCaretPosition();
		try{
			Buff=Buff.substring(0,txt.getCaretPosition()-x)+Buff.substring(txt.getCaretPosition());
			txt.setText(Buff);
			pos=pos-x;
			txt.setCaretPosition(pos);
		}catch(StringIndexOutOfBoundsException exc){}
		catch(IllegalArgumentException exc){}
	}
	/**
	* Creates map for Swarabarno
	*/
	public void SoroBorno()
	{
		sb=new HashMap<String,String>();
		sb.put("\u09CDA","\u09BE");	//্A -> া
		sb.put("\u09CDa","");		//্a -> NULL
		sb.put("\u09CDi","\u09BF");	//্i -> ি
		sb.put("\u09CDI","\u09C0");	//্I -> ী
		sb.put("\u09CDe","\u09C7");	//্e -> ে
		sb.put("\u09CDE","\u09C8");	//্E -> ৈ
		sb.put("\u09CDu","\u09C1");	//্u -> ু
		sb.put("\u09CDU","\u09C2");	//্U -> ূ
		sb.put("\u09CDO","\u09CC");	//্O -> ৌ
		sb.put("\u09CDo","\u09CB");	//্o -> ো
		sb.put("rr","\u09C3");		//rr -> ৃ
		sb.put("RR","\u09C4");		//RR -> ৄ
		sb.put("\u09CDL","\u09E2");	//্L -> ৢ
		sb.put("\u09E2L","\u09E3");	//ৢL -> ৣ
		sb.put("\u098CL","\u09E1");	//ঌL -> ৡ
	}
	/**
	* Creates map for Ghoshbarno
	*/
	public void GoshMap()
	{
		gm=new HashMap<String,String>();
		gm.put("rr","\u098B");						//rr -> ঋ
		gm.put("RR","\u09E0");						//RR -> ৠ
		gm.put("LL","\u09E1");						//LL -> ৡ
		gm.put("bh","\u09AD\u09CD");				//bh -> ভ
		gm.put("Bh","\u09AD\u09CD");				//Bh -> ভ
		gm.put("ch","\u099A\u09CD");				//ch -> চ
		gm.put("Ch","\u099B\u09CD");				//Ch -> ছ
		gm.put("dh","\u09A7\u09CD");				//dh -> ধ 
		gm.put("Dh","\u09A2\u09CD");				//Dh -> ঢ
		gm.put("gh","\u0998\u09CD");				//gh -> ঘ
		gm.put("Gh","\u0998\u09CD");				//Gh -> ঘ
		gm.put("jh","\u099D\u09CD");				//jh -> ঝ
		gm.put("Jh","\u099D\u09CD");				//Jh -> ঝ
		gm.put("kh","\u0996\u09CD");				//kh -> খ
		gm.put("Kh","\u0996\u09CD");				//Kh -> খ
		gm.put("nh","\u0999\u09CD");				//nh -> ঙ্
		gm.put("Nh","\u0981");						//Nh -> ঁ
		gm.put("ph","\u09AB\u09CD");				//ph -> ফ
		gm.put("Ph","\u09AB\u09CD");				//Ph -> ফ
		gm.put("rh","\u09DD\u09CD");				//rh -> ঢ়
		gm.put("Rh","\u09DD\u09CD");				//Rh -> ঢ়
		gm.put("sh","\u09B6\u09CD");				//sh -> শ
		gm.put("Sh","\u09B7\u09CD");				//Sh -> ষ
		gm.put("th","\u09A5\u09CD");				//th -> থ
		gm.put("Th","\u09A0\u09CD");				//Th -> ঠ
		gm.put("yn","\u099E\u09CD");				//yh -> ঞ
		gm.put("Ng","\u0982");						//Ng -> ং
	}
	/**
	* Creates map for Consonant
	*/
	public void ConMap()
	{
		cm=new HashMap<Character,String>();
		cm.put('a',"\u0985");						//a -> অ
		cm.put('A',"\u0986");						//A -> আ
		cm.put('e',"\u098F");						//e	-> এ
		cm.put('E',"\u0990");						//E -> ঐ
		cm.put('i',"\u0987");						//i -> ই
		cm.put('I',"\u0988");						//I -> ই
		cm.put('L',"\u098C");						//L -> ঌ
		cm.put('o',"\u0993");						//o -> ও
		cm.put('O',"\u0994");						//O -> ঔ
		cm.put('u',"\u0989");						//u -> উ
		cm.put('U',"\u098A");						//U	-> ঊ
	
		cm.put('`',"\u09F2");						//`	->	৲
		cm.put('~',"\u09FA");						//~	->	৺
		cm.put('!',"!");							//!	->	!
		cm.put('@',"\u09E5");						//@	->	৥
		cm.put('#',"\u09F5");						//#	->	৵
		cm.put('$',"\u09F3");						//$	->	৳
		cm.put('%',"\u09F6");						//%	->	৶
		cm.put('^',"\u09F9");						//^	->	৹
		cm.put('&',"\u09F8");						//&	->	৸
		cm.put('*',"*");							//*	->	*
		cm.put('(',"(");							//(	->	(
		cm.put(')',")");							//)	->	)
		cm.put('-',"-");							//-	->	-
		cm.put('_',"\u09F4");						//_	->	৴
		cm.put('+',"+");							//+	->	+
		cm.put('=',"=");							//=	->	=
		cm.put('{',"{");							//{	->	{
		cm.put('}',"}");							//}	->	}
		cm.put('[',"[");							//[	->	[
		cm.put(']',"]");							//]	->	]
		cm.put('\\',"\u09BD");						//\	->	ঽ
		cm.put('|',"\u09F7");						//|	->	৷
		cm.put(':',":");							//:	->	:
		cm.put(';',";");							//;	->	;
		cm.put('\"',"\"");							//"	->	"
		cm.put('\'',"\'");							//'	->	'
		cm.put(',',",");							//,	->	,
		cm.put('<',"<");							//<	->	<
		cm.put('.',"\u09FB");						//.	-> ৻
		cm.put('>',"\u09FC");						//>	-> ৼ
		cm.put('?',"?");							//?	->	?
		cm.put('/',"/");							///	->	/
		
		cm.put(' '," ");
		
		cm.put('1',"\u09E7");
		cm.put('2',"\u09E8");
		cm.put('3',"\u09E9");
		cm.put('4',"\u09EA");
		cm.put('5',"\u09EB");
		cm.put('6',"\u09EC");
		cm.put('7',"\u09ED");
		cm.put('8',"\u09EE");
		cm.put('9',"\u09EF");
		cm.put('0',"\u09E6");
			
		cm.put('b',"\u09AC\u09CD");	//b -> ব্
		cm.put('B',"\u09AC\u09CD");	//B -> ব্	
		cm.put('c',"\u0995\u09CD");	//c -> ক্
		cm.put('C',"\u0995\u09CD");	//c -> ক্
		cm.put('d',"\u09A6\u09CD");	//d -> দ্
		cm.put('D',"\u09A1\u09CD");	//D -> ড্
		cm.put('f',"\u09AB\u09CD");	//f -> ফ্
		cm.put('F',"\u09AB\u09CD");	//F -> ফ্
		cm.put('g',"\u0997\u09CD");	//g -> গ্
		cm.put('G',"\u0997\u09CD");	//G -> গ্
		cm.put('h',"\u09B9\u09CD");	//h -> হ্
		cm.put('H',"\u0983");		//H -> ঃ
		cm.put('j',"\u099C\u09CD");	//j -> জ্
		cm.put('J',"\u09AF\u09CD");	//J -> য্
		cm.put('k',"\u0995\u09CD");	//k -> ক্
		cm.put('K',"\u0995\u09CD");	//K -> ক্
		cm.put('l',"\u09B2\u09CD");	//l -> ল্
		cm.put('m',"\u09AE\u09CD");	//m -> ম্
		cm.put('M',"\u09AE\u09CD");	//M -> ম্
		cm.put('n',"\u09A8\u09CD");	//n -> ন্
		cm.put('N',"\u09A3\u09CD");	//N -> ণ
		cm.put('p',"\u09AA\u09CD");	//p -> প্
		cm.put('q',"\u0995\u09CD");	//q -> ক্
		cm.put('r',"\u09B0\u09CD");	//r -> র্
		cm.put('R',"\u09DC\u09CD");	//R -> ড়
		cm.put('s',"\u09B8\u09CD");	//s -> স্
		cm.put('S',"\u09B8\u09CD");	//S -> স্
		cm.put('t',"\u09A4\u09CD");	//t -> ত
		cm.put('T',"\u099F\u09CD");	//T -> ট
		cm.put('v',"\u09AD\u09CD");	//v -> ভ্
		cm.put('V',"\u09AD\u09CD");	//V -> ভ্
		cm.put('w',"\u09AC\u09CD");	//w -> ব্
		cm.put('W',"\u09AC\u09CD");	//W -> ব্	
		cm.put('x',"\u09BD");		//x -> ঽ
		cm.put('X',"\u09CE");		//X ->ৎ
		cm.put('y',"\u09AF\u09CD");	//y -> য্
		cm.put('Y',"\u09DF\u09CD");	//Y -> য়
		cm.put('z',"\u099C\u09CD");	//z -> জ্
		cm.put('Z',"\u099C\u09CD");	//Z -> জ্
	}
	/**
	* Converts input from english keyboard to bengali phonetic
	* @param txt : text component to be used into
	* @e : current key event of text component
	*/
	public int startConvertionAt(JTextComponent txt,KeyEvent e)
	{
		// Deleting Selected Text
		if(txt.getSelectedText()!=null)
		{
			int pos=txt.getSelectionStart();
			try{
				txt.setText(txt.getText().substring(0,txt.getSelectionStart())+
						txt.getText().substring(txt.getSelectionEnd()));
			}catch(IndexOutOfBoundsException exc){}
			txt.setCaretPosition(pos);
		}
		// Convertion
		char key=e.getKeyChar();
		char TData[]=new char[2];
		String Tkey;
		if(key=='\b')
			return FAILURE;
		else if(key==' '||CType.isPunct(key))
		{
			try{
				TData[0]=txt.getText().charAt(txt.getCaretPosition()-1);
				if(TData[0]=='\u09CD')
					delhasant(txt,1);
			}catch(StringIndexOutOfBoundsException exc){}
			insert(txt,cm.get(key), txt.getCaretPosition());
			e.setKeyChar('\0');
		}
		else if(key=='h'||key=='g' ||key=='j'||key=='n'||key=='r'||key=='R')
		{	
			TData[0]=hold_key;
			TData[1]=key;
			Tkey=new String(String.valueOf(TData));
			if(gm.containsKey(Tkey))
			{
				if(key=='L'&&hold_key=='L')
					delhasant(txt,1);
				else
					delhasant(txt,2);
				insert(txt,gm.get(Tkey), txt.getCaretPosition());
			}
			else
			{
				insert(txt,cm.get(key), txt.getCaretPosition());
			}
			try{
				if(txt.getText().charAt(txt.getCaretPosition()-2)=='\u09CD')
				{
					if(key=='r'&&hold_key=='r'||key=='R'&&hold_key=='R')
						delhasant(txt,2);
					insert(txt,sb.get(Tkey), txt.getCaretPosition());
				}
			}catch(StringIndexOutOfBoundsException exc){}
			e.setKeyChar('\0');
		}
		else
		{
			if(key=='A'||key=='a'||key=='e'||key=='i'||key=='I'||key=='O'||key=='o'||key=='u'||key=='E'||key=='U'||key=='L')
			{
				if(txt.getCaretPosition()>1)
					TData[0]=txt.getText().charAt(txt.getCaretPosition()-1);
				else
					TData[0]='\0';
				TData[1]=key;
				Tkey=new String(String.valueOf(TData));
				if(TData[0]=='\u09CD'||TData[0]=='\u09E2'||TData[0]=='\u098C')
				{
					delhasant(txt,1);
					insert(txt,sb.get(Tkey), txt.getCaretPosition());
				}
				else
				{
					insert(txt,cm.get(key), txt.getCaretPosition());
				}
				e.setKeyChar('\0');
			}
			else
			{
				if(cm.containsKey(key))
				{
					insert(txt,cm.get(key), txt.getCaretPosition());
					e.setKeyChar('\0');
				}
				else
				{
					e.setKeyChar('\0');
					return FAILURE;
				}
			}
		}
		hold_key=key;
		return SUCCESSFUL;
	}
	/**
	* inserts character at given position
	* @param txt : text component to be inserted into
	* @param str : string to be inserted
	* @param pos : position to be inserted at
	*/
	private void insert(JTextComponent txt,String str,int pos){
		if(txt instanceof JTextArea )
			((JTextArea)txt).insert(str,pos);
		else{
			try{
				txt.setText(txt.getText().substring(0,pos)+
						str+
						txt.getText().substring(pos)
					);
				txt.setCaretPosition(pos+str.length());
			}catch(IndexOutOfBoundsException exc){}
		}
	}
}