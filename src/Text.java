import java.awt.Color;
import java.awt.Font;
import java.awt.FontMetrics;

public class Text
{
	public int width;
	public boolean getWidth;
	public int x, y;
	public boolean exists;
	public Font font;
	public Color color;
	public String string;
	public boolean doesRise;
	public int pixelsMoved;
	public int alignment;
	public int timeElapsed;
	public int maxTime;
	public boolean doesScroll;

	public Text()
	{
		this.exists = false;
	}

	public void create(String string, int x, int y, Font font, Color color,
			boolean doesRise, int time, int alignment, boolean doesScroll)
	{
		this.string = string;
		this.x = x;
		this.y = y;
		this.exists = true;
		this.getWidth = true;
		this.color = color;
		this.font = font;
		this.doesRise = doesRise;
		this.pixelsMoved = 0;
		this.alignment = alignment;
		this.maxTime = time;
		this.timeElapsed = 0;
		this.doesScroll = doesScroll;

	}

	/**
	 * Align the text however you need to, only after the width of the text has
	 * been worked out!
	 * 
	 */
	public void align()
	{
		if (alignment == 0)
		{
			x = x - width / 2;
		}
		else if (alignment == 1)
		{
			x = x - width;
		}
	}

	public void passTime()
	{

		timeElapsed++;
		if (doesRise)
		{
			rise();
		}

		if (timeElapsed >= 90)
		{
			this.exists = false;
		}
		

	}

	public void rise()
	{

		y -= 1;
		pixelsMoved++;

		
		int alpha = color.getAlpha();
		if (alpha > 0)
		{
			Color newColor = new Color(color.getRed(), color.getGreen(),
					color.getBlue(), alpha - 3);
			color = newColor;

		}
		

	}

	public boolean isOnScreen()
	{
		if (this.x > 1024 || this.x + width < 0)
		{
			return false;
		}
		return true;
	}
}
