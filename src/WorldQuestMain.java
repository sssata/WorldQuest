import java.awt.EventQueue;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;






import javax.sound.sampled.AudioFormat;
import javax.sound.sampled.AudioInputStream;
import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;
import javax.sound.sampled.DataLine;
import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import javax.swing.JFrame;





public class WorldQuestMain extends JFrame
{

	public WorldQuestMain()
	{
		try
		{
			setScreen();
		}
		catch (UnsupportedAudioFileException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (IOException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		catch (LineUnavailableException e)
		{
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		// Load up the icon image (penguin image from www.iconshock.com)
		setIconImage(Toolkit.getDefaultToolkit().getImage("earthIcon.png"));
	}

	private void setScreen() throws UnsupportedAudioFileException, IOException, LineUnavailableException
	{

		add(new WorldQuestEngine());

		setResizable(false);
		pack();

		setTitle("WorldQuest");
		setLocationRelativeTo(null);
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	}

	public static void main(String[] args) throws LineUnavailableException, UnsupportedAudioFileException, IOException
	{
		
		
		
		EventQueue.invokeLater(new Runnable() 
		{
			public void run()
			{
				JFrame ex = new WorldQuestMain();
				ex.setVisible(true);
			}
		});
	}
}
