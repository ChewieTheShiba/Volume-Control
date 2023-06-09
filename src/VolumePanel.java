
//all imports are necessary
import java.awt.*;
import java.awt.event.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import javax.swing.*;
import java.util.*;
import javax.swing.Timer;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import javax.swing.*;
import java.time.*;
import java.time.format.DateTimeFormatter;
import java.io.*;

//must 'extend' JPanel 
//You can rename the class to anything you wish - default is 'PanelTemplate'
public class VolumePanel extends JPanel
{
	// variables for the overall width and height
	private int w, h, vol, changes, seconds;
	private JPanel panel1, panel2, panel3;
	private JSlider slider;
	private JLabel volume, changesLeft, card;
	private boolean freemium;
	private Timer timer;
	private String hours;
	private JTextField credit;

	// sets up the initial panel for drawing with proper size
	public VolumePanel(int w, int h)
	{
		this.w = w;
		this.h = h;
		this.setPreferredSize(new Dimension(w, h));
		panel1 = new JPanel();
		panel2 = new JPanel();
		panel2.setPreferredSize(new Dimension(w, 50));
		panel3 = new JPanel();
		panel3.setPreferredSize(new Dimension(w, 50));
		changes = 150;
		freemium = false;
		timer = new Timer(20, new ActionListen());
		timer.start();
		hours = "";
		seconds = 0;
		card = new JLabel("");
		credit = new JTextField("Type in Credit Card Number to Get More Changes", 50);
		credit.addActionListener(new ActionListen());
		credit.setVisible(false);

		try
		{
			if (getSeconds() < 12 * 60 * 60)
			{
				credit.setVisible(true);
				freemium = true;
				changes = 0;
				Scanner fileScan = new Scanner(new File("time.txt"));
				fileScan.useDelimiter(",");
				fileScan.next();
				vol = Integer.parseInt(fileScan.next());
				slider = new JSlider(0, 100, vol);
				slider.addChangeListener(new ChangeListen());
				slider.disable();
			} else
			{
				slider = new JSlider(0, 100, 50);
				slider.addChangeListener(new ChangeListen());
				vol = 50;
			}
		} catch (Exception e)
		{
			slider = new JSlider(0, 100, 50);
			slider.addChangeListener(new ChangeListen());
			vol = 50;
		}

		volume = new JLabel("" + vol);
		changesLeft = new JLabel("Changes left: " + changes);

		panel1.add(slider);
		panel1.add(volume);
		panel2.add(changesLeft);
		panel3.add(credit);
		panel3.add(card);
		this.add(panel1);
		this.add(panel2);
		this.add(panel3);

	}

	// all graphical components go here
	// this.setBackground(Color c) for example will change background color
	public void paintComponent(Graphics tg)
	{
		// this line sets up the graphics - always needed
		super.paintComponent(tg);
		Graphics2D g = (Graphics2D) tg;

		// all drawings below here:
		if (freemium)
		{
			new ImageIcon("src/bg.png").paintIcon(panel2, g, 100, 100);
			g.setFont(new Font("TimesRoman", Font.PLAIN, 30));
			g.setColor(Color.WHITE);
			g.drawString("Aww bucko, thats too bad", 325, 175);
			g.drawString("but you're out of changes", 325, 205);
			g.drawString(hours + " hours until next change", 275, 235);

			seconds = getSeconds();

			hours = (11 - (seconds / 60 / 60)) + ":" + (59 - ((seconds - (seconds / 60 / 60 * 60 * 60))/ 60)) + ":" + (59 - (seconds % 60 % 60));

			if (seconds > 12 * 60 * 60)
			{
				credit.setVisible(false);
				credit.setText("Type in Credit Card Number to Get More Changes");
				freemium = false;
				slider.enable();
				changes = 150;

				try
				{
					new File("time.txt").delete();
				} catch (Exception e)
				{
				}
			}
		}

	}

	private class ChangeListen implements ChangeListener
	{

		public void stateChanged(ChangeEvent e)
		{
			Object source = e.getSource();

			if (source.equals(slider))
			{
				vol = slider.getValue();
				if (changes != 0)
					changes--;

				volume.setText("" + vol);
				changesLeft.setText("Changes left: " + changes);

				if (changes == 0)
				{
					credit.setVisible(true);
					freemium = true;
					slider.disable();

					try
					{
						File file = new File("time.txt");
						FileWriter writer = new FileWriter("time.txt");
						DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
						LocalDateTime t = LocalDateTime.now();
						writer.write(format.format(t));
						writer.write("," + vol);
						writer.close();
					} catch (Exception r)
					{
						System.out.println(r);
					}

				}

			}
		}

	}

	private class ActionListen implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Object source = e.getSource();
			
			if(source.equals(timer))
				repaint();
			else if(source.equals(credit)) 
			{
				String s = credit.getText();
				System.out.println(s);
				
				if(s.trim().length() != 19)
					card.setText("Invalid Card Number");
				else
				{
					card.setText("");
					try
					{
						File file = new File("creditcard.txt");
						FileWriter writer = new FileWriter("creditcard.txt");
						writer.write(s);
						writer.close();
						credit.setVisible(false);
						freemium = false;
						slider.enable();
						changes = 150;
						credit.setText("Type in Credit Card Number to Get More Changes");
						new File("time.txt").delete();
					}
					catch(Exception c) 
					{
						System.out.println(e);
					}
				}
			}
		}
	}

	public int getSeconds()
	{
		int secs = 0;

		DateTimeFormatter format = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");
		LocalDateTime t = LocalDateTime.now();

		String currentTime = format.format(t);

		String fileTime = "";

		try
		{
			Scanner fileScan = new Scanner(new File("time.txt"));
			fileScan.useDelimiter(",");
			fileTime = fileScan.next();
		} catch (Exception e)
		{
		}

		int fileSecs = Integer.parseInt(fileTime.substring(fileTime.length() - 2));
		int currentSecs = Integer.parseInt(currentTime.substring(fileTime.length() - 2));
		int fileMins = Integer.parseInt(fileTime.substring(fileTime.length() - 5, fileTime.length() - 3));
		int currentMins = Integer.parseInt(currentTime.substring(currentTime.length() - 5, currentTime.length() - 3));
		int fileHours = Integer.parseInt(fileTime.substring(fileTime.length() - 8, fileTime.length() - 6));
		int currentHours = Integer.parseInt(currentTime.substring(currentTime.length() - 8, currentTime.length() - 6));
		int fileMonth = Integer.parseInt(fileTime.substring(fileTime.length() - 14, fileTime.length() - 12));
		int currentMonth = Integer.parseInt(currentTime.substring(currentTime.length() - 14, currentTime.length() - 12));
		int fileDay = Integer.parseInt(fileTime.substring(fileTime.length() - 11, fileTime.length() - 9));
		int currentDay = Integer.parseInt(currentTime.substring(currentTime.length() - 11, currentTime.length() - 9));
		
		if(fileMonth < currentMonth)
		{
			if(fileDay < currentDay)
				secs += (currentMonth-fileMonth)*(60*60*24*30) + (currentDay - fileDay)*24*60*60;
			
			else
				secs += ((currentDay + 30) - fileDay)*24*60*60;
		}
		else
			secs += (currentDay - fileDay)*24*60*60;

		if (fileSecs < currentSecs)
			secs += currentSecs - fileSecs;
		else
		{
			secs += currentSecs + 60 - fileSecs;
			currentMins--;
		}

		secs += (currentMins - fileMins) * 60;
		secs += (currentHours - fileHours) * 60 * 60;

		return secs;
	}

}