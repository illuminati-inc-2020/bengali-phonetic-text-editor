package Bangla.gui;

import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSeparator;
import java.awt.Font;
import javax.swing.JScrollPane;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.FontFormatException;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;
import java.lang.String;
import java.util.HashMap;
import java.util.Set;
import javax.swing.JTextField;
import javax.swing.ImageIcon;


public class KeyMapWindow extends JFrame{
	private JLabel lbl1=new JLabel("Type ");
	private JTextField txt1=new JTextField(5);
	private JLabel lbl2=new JLabel(" for ");
	private JTextField txt2=new JTextField(5);
	private JScrollPane scroll=new JScrollPane(JScrollPane.VERTICAL_SCROLLBAR_AS_NEEDED,JScrollPane.HORIZONTAL_SCROLLBAR_NEVER);
	private JPanel txtpanel=new JPanel();
	private JPanel panel=new JPanel();
	private JPanel sbpanel=new JPanel();
	private JPanel bbpanel=new JPanel();
	private JButton []buttons;
	private JButton []buttonb;
	public KeyMapWindow(MainWindow win){
		super("-:\u0985\u0995\u09CD\u09B7\u09B0 \u09AC\u09BF\u09A8\u09CD\u09AF\u09BE\u09B8:-");
		this.setIconImage(win.getIconImage());
		this.setLayout(new BorderLayout());
				
		this.add(scroll,BorderLayout.CENTER);
		scroll.setViewportView(panel);
		panel.setLayout(new BorderLayout());
		panel.add(sbpanel,BorderLayout.NORTH);
		JPanel sep=new JPanel();
		sep.setSize(50,50);
		panel.add(sep,BorderLayout.CENTER);
		panel.add(bbpanel,BorderLayout.SOUTH);
		sbpanel.setLayout(new GridLayout(3,5));
		bbpanel.setLayout(new GridLayout(9,5));
		String []buttonNameS={"\u0985","\u0986","\u0987","\u0988","\u0989","\u098A","\u098B","\u09E0","\u098C",
				"\u09E1","\u098F","\u0990","\u0993","\u0994","\u09BD"};
		buttons=new JButton[buttonNameS.length];
		String []buttonNameB={"\u0995","\u0996","\u0997","\u0998","\u0999","\u099A","\u099B","\u099C","\u099D",
				"\u099E","\u099F","\u09A0","\u09A1","\u09A2","\u09A3","\u09A4","\u09A5","\u09A6","\u09A7",
				"\u09A8","\u09AA","\u09AB","\u09AC","\u09AD","\u09AE","\u09AF","\u09B0","\u09B2","\u09B6",
				"\u09B7","\u09B8","\u09B9","\u09DC","\u09DD","\u09DF","\u09CE","\u0982","\u0983","\u0981"};
		buttonb=new JButton[buttonNameB.length];		
		for(int i=0;i<buttonNameS.length;i++){
			buttons[i]=new JButton(buttonNameS[i]);
			buttons[i].setFont(win.getComponentFont());
			sbpanel.add(buttons[i]);
			buttons[i].addActionListener(new KeyMapButtonAction(buttonNameS[i],txt1,txt2));
		}
		for(int i=0;i<buttonNameB.length;i++){
			buttonb[i]=new JButton(buttonNameB[i]);
			buttonb[i].setFont(win.getComponentFont());
			bbpanel.add(buttonb[i]);
			buttonb[i].addActionListener(new KeyMapButtonAction(buttonNameB[i],txt1,txt2));
		}

		this.add(txtpanel,BorderLayout.SOUTH);
		txtpanel.setLayout(new FlowLayout());
		txtpanel.add(lbl1);
		txtpanel.add(txt1);
		txtpanel.add(lbl2);
		txtpanel.add(txt2);
		txt2.setFont(win.getComponentFont());
		txt1.setFocusable(false);
		txt2.setFocusable(false);
		
		setSize(275,400);
	 	Dimension d=getToolkit().getScreenSize();
	 	this.setLocation((d.width-getWidth())/2,(d.height-getHeight())/2);
		setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		this.setVisible(true);
		this.setAlwaysOnTop(true);
		this.setResizable(false);
	}
}

class KeyMapButtonAction implements ActionListener{
	private String name;
	private JTextField txt1;
	private JTextField txt2;
	private HashMap<String, String> rm;
	KeyMapButtonAction(String name,JTextField txt1,JTextField txt2){
		this.name=name;
		this.txt1=txt1;
		this.txt2=txt2;
		returnMap();
	}
	
	public void returnMap()
	{
		rm=new HashMap<String,String>();
		rm.put("\u0985","a");		//অ -> a
		rm.put("\u0986","A");		//আ	-> A
		rm.put("\u0987","i");		//ই	-> i
		rm.put("\u0988","I");		//ঈ	-> I
		rm.put("\u0989","u");		//উ	-> u
		rm.put("\u098A","U");		//ঊ	-> U
		rm.put("\u098B","rr");		//ঋ -> rr
		rm.put("\u09E0","RR");		//ৠ -> RR
		rm.put("\u098C","L");		//ঌ -> L
		rm.put("\u09E1","LL");		//ৡ -> LL
		rm.put("\u098F","e");		//এ	-> e
		rm.put("\u0990","E");		//ঐ -> E
		rm.put("\u0993","o");		//ও	-> o
		rm.put("\u0994","O");		//ঔ -> O
		rm.put("\u09BD","x");		//ঽ -> x

		rm.put("\u0995","c/k");	//ক্	->	c/k
		rm.put("\u0996","kh");	//খ	->	kh
		rm.put("\u0997","g");	//গ্	->	g
		rm.put("\u0998","gh");	//ঘ	->	gh
		rm.put("\u0999","nh");	//ঙ	->	nh
		rm.put("\u099A","ch");	//চ	->	ch
		rm.put("\u099B","Ch");	//ছ	->	Ch
		rm.put("\u099C","j");	//জ্	->	j
		rm.put("\u099D","jh");	//ঝ	->	jh
		rm.put("\u099E","yn");	//ঞ	->	yn
		rm.put("\u099F","T");	//ট	->	T
		rm.put("\u09A0","Th");	//ঠ	->	Th
		rm.put("\u09A1","D");	//ড্	->	D 
		rm.put("\u09A2","Dh");	//ঢ	->	Dh
		rm.put("\u09A3","N");	//ণ	->	N
		rm.put("\u09A4","t");	//ত	->	t
		rm.put("\u09A5","th");	//থ	->	th
		rm.put("\u09A6","d");	//দ্	->	d		
		rm.put("\u09A7","dh");	//ধ	->	dh
		rm.put("\u09A8","n");	//ন্	->	n
		rm.put("\u09AA","p");	//প্	->	p
		rm.put("\u09AB","ph/f");//ফ	->	ph/f
		rm.put("\u09AC","b/w");	//ব্	->	b/w
		rm.put("\u09AD","bh/v");//ভ	->	bh/v
		rm.put("\u09AE","m");	//ম্	->	m
		rm.put("\u09AF","J/y");	//য্ -> 	J/y
		rm.put("\u09B0","r");	//র্	->	r
		rm.put("\u09B2","l");	//ল্	->	l
		rm.put("\u09B6","sh");	//শ	->	sh
		rm.put("\u09B7","Sh");	//ষ	->	Sh
		rm.put("\u09B8","s");	//স্	->	s		
		rm.put("\u09B9","h");	//হ্	->	h
		rm.put("\u09DC","R");	//ড়	->	R
		rm.put("\u09DD","Rh");	//ঢ়	->	Rh
		rm.put("\u09DF","Y");	//য়	->	Y
		rm.put("\u0982","Ng");	//ং	->	Ng
		rm.put("\u09CE","X");	//ৎ	->	t
		rm.put("\u0983","H");	//ঃ	->	H		
		rm.put("\u0981","Nh");	//ঁ	->	Nh
	}
	public void actionPerformed(ActionEvent e){
		txt1.setText(rm.get(name));
		txt2.setText(name);
	}
}