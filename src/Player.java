public class Player
{
	public String name;
	public boolean exists;
	public int score;
	public int days;

	public Player()
	{
		this.exists = false;
	}
	
	public void setPlayer (String name, int score, int days)
	{
		this.name = name;
		this.exists = true;
		this.score = score;
		this.days = days;
	}

}
