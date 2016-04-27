import java.awt.Image;

/** Keeps Track of the background's information
 * 
 * @author William
 *
 */


public class Background
{
	public int width, height;
	public int x,y;
	public int speed;
	public String type;
	public Image image;
	
	public Background(String type,int x, int y, Image image)
	{
		this.type = type;
		if (type == "cloud")
		{
			speed = (int) (Math.random() * 2)+1;
		}
		
		
		this.image = image;
		
		this.x = x;
		this.y = y - height;
		
		this.height = image.getHeight(null);
		this.width = image.getWidth(null);
	}

}
