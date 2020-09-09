/* Subject			:	Bengali phonetic Software
 * Date				: 	06/08/2011
 */

package Bangla;

import Bangla.gui.*;
import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.awt.GradientPaint;
import java.awt.Graphics2D;
import java.awt.Color;
import java.awt.SplashScreen;

import javax.swing.ImageIcon;

public class Bangla{
	/**
	 * 	Sets the value of splash screen progressbar
	 * @param n : value to be set
	 */
	public static void setSplashScreenProgressBar(int n){
		SplashScreen splash=SplashScreen.getSplashScreen();
		Graphics2D g=splash.createGraphics();
		g.setColor(new Color(100,200,100,100));
		for(int i=0;i<n/5;i++)
			g.fillRoundRect(splash.getBounds().width/3+i*splash.getBounds().width/60,splash.getBounds().height-25,splash.getBounds().width/60-2,20,5,5);
		splash.update();
	}
	/**
	 * Main function
	 * @param filename : path of files to be opened initially are received as argument
	 */
	public static void main(String[] filename){
		MainWindow win=new MainWindow();
		if(filename.length>0)
			for(int i=0;i<filename.length;i++)
				win.add(new WorkSpace(win,new File(filename[i])));
		else
			win.add(new WorkSpace(win,"New File"));
		win.grabFocus();
	}
}