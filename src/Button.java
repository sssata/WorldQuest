import java.awt.Image;


public class Button
{
	public int width, height;
	public int x1, y1, x2, y2,x,y;
	public boolean exists;
	public Image image, image1, image2;
	public boolean isPressed;

	public Button()
	{
		this.exists = false;
	}

	public void create(int x, int y, Image image1, Image image2)
	{
		this.image = image1;
		this.image1 = image1;
		this.image2 = image2;
		
		this.height = image.getHeight(null);
		this.width = image.getWidth(null);
		
		this.x = x;
		this.y = y;
		this.x1 = x;
		this.y1 = y + height;
		this.x2 = x + width;
		this.y2 = y;
		
		this.exists = true;
	}

	public void remove()
	{
		this.exists = false;
	}
	
	public void pressed()
	{
		this.image = image2;
		this.isPressed = true;
	}
	
	public void released()
	{
		this.image = image1;
		this.isPressed = false;
	}
	
	public void move(int amount)
	{
		this.x1+=amount;
		this.x2+=amount;
		this.x +=amount;
	}
	
	public void sizeAdjust(int x1, int y1, int x2, int y2)
	{
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
	}
}
