//import JFrame
import javax.swing.JFrame;


public class VolumeFrame
{

	public static void main(String[] args)
	{
		//change to match your values for width/height
		//these can be changed
		int w = 1000;
		int h = 650;
		
		//sets up a JFrame object with title "Template"
		JFrame frame = new JFrame("Poverty");
		//make sure the jframe closes when you hit the 'x'
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		//adds the drawing panel to the frame
		frame.getContentPane().add(new VolumePanel(w,h));
		//resizes the frame to fit the panel
		frame.pack();
		frame.setVisible(true);
	}

}
