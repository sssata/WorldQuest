import java.awt.Color;
import java.awt.Cursor;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.Point;
import java.awt.Toolkit;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.event.MouseMotionListener;
import java.util.ArrayList;

import javax.swing.ImageIcon;
import javax.swing.JPanel;

/**
 * Code for the main engine in the game that includes the constant framerate
 * process
 * 
 * @author William Xu & Tony Wu
 * @version December 28, 2014
 *
 */

public class WorldQuestEngine extends JPanel
		implements Runnable, MouseListener, MouseMotionListener, KeyListener
{

	// Width and height of the screen
	public final int SCREEN_WIDTH = 1024;
	public final int SCREEN_HEIGHT = 768;

	// Number of milliseconds between frames
	public final int DELAY = 30;

	// The x and y coordinate of the mouse at a given time
	public int mouseX, mouseY;
	public boolean mousePressed;

	// Images to be imported and used later
	public Image transparent, customCursor, customCursorPressed, skyBackground,
			building,
			buildingGhost, buildingGhostError,
			dirt, homeButton, homeButtonPressed;

	public Image[] grass = new Image[1];
	public Image[] clouds = new Image[4];

	ImageIcon[] images = new ImageIcon[1000];

	public Thread animator;

	// Array of building objects for placement
	Building[] buildings = new Building[1000];
	public boolean canPlace;

	// Array of buttons
	Button[] buttons = new Button[100];

	// Background objects and array of them
	public final int NO_OF_DIRT = 5000;
	public final int NO_OF_GRASS = 5000;
	public final int NO_OF_CLOUDS = 100;

	// Wind direction for blowing the clouds
	public int windDirection;

	Background[] backgrounds = new Background[NO_OF_DIRT + NO_OF_GRASS
			+ NO_OF_CLOUDS];

	public String buildingSelected = "house";
	public int selectedWidth;
	public int selectedHeight;

	// Position of the screen in the world and its scroll speed (positive for
	// right, negative for left, 0 for still)
	public int worldX;
	public int worldSpeed;

	// The x position of home in the world, whether the home button was pressed,
	// and whether the home location was set
	public int homeX;
	public boolean goHome;
	public boolean homeSet;

	// Y coordinate of the ground
	public int groundHeight;

	// Stores whether or not the keyboard is scrolling the screen so it doesn't
	// interfere with the mouse scrolling the screen
	public boolean keyboardIsScrolling;

	/**
	 * Initiate all the variables and arrays when the game begins
	 */
	public void gameStart()
	{

		setBackground(new Color(178, 255, 255));
		setPreferredSize(new Dimension(SCREEN_WIDTH, SCREEN_HEIGHT));
		setDoubleBuffered(true);

		// Initialize all the variables
		// Y coordinate of the ground
		groundHeight = 650;

		mousePressed = false;

		goHome = false;
		homeX = 0;
		homeSet = false;

		canPlace = true;

		// Random wind direction
		if ((int) (Math.random() * 2) == 0)
		{
			windDirection = -1;
		}
		else
		{
			windDirection = 1;
		}

		buildingSelected = "house";

		// Position of the screen in the world and its scroll speed (positive
		// for
		// right, negative for left, 0 for still)
		worldX = 0;
		worldSpeed = 0;

		// Stores whether or not the keyboard is scrolling the screen so it
		// doesn't
		// interfere with the mouse scrolling the screen
		keyboardIsScrolling = false;

		// Add mouse listeners and Key Listeners to the game
		addMouseListener(this);
		setFocusable(true);
		addKeyListener(this);
		requestFocusInWindow();

		// Check for movement of the mouse
		addMouseMotionListener(this);

		loadImages();
		Toolkit toolkit = Toolkit.getDefaultToolkit();
		Cursor cursor = toolkit.createCustomCursor(transparent, new Point(
				getX(), getY()), "img");
		setCursor(cursor);

		// Load the objects
		for (int index = 0; index < buildings.length; index++)
		{
			buildings[index] = new Building();
		}

		int backgroundX = -(dirt.getWidth(null) * (NO_OF_GRASS / 2));

		for (int index = 0; index < buttons.length; index++)
		{
			buttons[index] = new Button();
		}

		buttons[1].create(50, 50, homeButton, homeButtonPressed);

		for (int index = 0; index < NO_OF_DIRT; index++)
		{
			backgrounds[index] = new Background("dirt", backgroundX,
					groundHeight, dirt);
			backgroundX += dirt.getWidth(null);
		}

		backgroundX = -(grass[0].getWidth(null) * (NO_OF_GRASS / 2));

		for (int index = NO_OF_DIRT; index < NO_OF_GRASS + NO_OF_DIRT; index++)
		{
			backgrounds[index] = new Background("grass", backgroundX,
					groundHeight, grass[0]);
			backgroundX += grass[0].getWidth(null);
		}

		backgroundX = -(500 * (NO_OF_CLOUDS / 2));

		for (int index = NO_OF_GRASS + NO_OF_DIRT; index < NO_OF_GRASS
				+ NO_OF_DIRT
				+ NO_OF_CLOUDS; index++)
		{
			int random = (int) (Math.random() * 4);

			backgrounds[index] = new Background("cloud", backgroundX,
					(int) (Math.random() * 400) - 300, clouds[random]);
			backgroundX += (int) (Math.random() * 1000);
		}

		// Make the user place the town house before anything begins
		changeBuilding ("townHouse",images[6],images[7], images[8]);

	}

	// Constructor that is used when the engine is called from the main program
	public WorldQuestEngine()
	{
		gameStart();
	}

	public void loadImages()
	{

		images[0] = new ImageIcon("house.png");
		images[1] = new ImageIcon("houseGhost.png");
		images[2] = new ImageIcon("houseGhostError.png");

		images[3] = new ImageIcon("largeHouse.png");
		images[4] = new ImageIcon("largeHouseGhost.png");
		images[5] = new ImageIcon("largeHouseGhostError.png");

		images[6] = new ImageIcon("townHall.png");
		images[7] = new ImageIcon("townHallGhost.png");
		images[8] = new ImageIcon("townHallGhostError.png");

		images[50] = new ImageIcon("dirt.jpg");
		images[51] = new ImageIcon("grass1.png");
		images[60] = new ImageIcon("cloud1.png");
		images[61] = new ImageIcon("cloud2.png");
		images[62] = new ImageIcon("cloud3.png");
		images[63] = new ImageIcon("cloud4.png");

		images[97] = new ImageIcon("homeButton.png");
		images[98] = new ImageIcon("homeButtonPressed.png");
		images[99] = new ImageIcon("skyBackground.png");

		images[100] = new ImageIcon("transparent.png");
		images[101] = new ImageIcon("customCursor.png");
		images[102] = new ImageIcon("customCursor2.png");

		building = images[0].getImage();
		buildingGhost = images[1].getImage();
		buildingGhostError = images[2].getImage();

		dirt = images[50].getImage();
		grass[0] = images[51].getImage();

		clouds[0] = images[60].getImage();
		clouds[1] = images[61].getImage();
		clouds[2] = images[62].getImage();
		clouds[3] = images[63].getImage();

		homeButton = images[97].getImage();
		homeButtonPressed = images[98].getImage();

		skyBackground = images[99].getImage();

		transparent = images[100].getImage();
		customCursor = images[101].getImage();
		customCursorPressed = images[102].getImage();

	}

	@Override
	public void addNotify()
	{
		super.addNotify();

		animator = new Thread(this);
		animator.start();
	}

	@Override
	public void paintComponent(Graphics g)
	{
		super.paintComponent(g);

		draw(g);
	}

	/**
	 * Move each object that snaps to the world while the user is scrolling and
	 * scroll the screen if the user moves the mouse to the edges of the screen
	 */
	public void scrollScreen()
	{

		worldX += worldSpeed;

		for (int index = 0; index < backgrounds.length; index++)
		{
			backgrounds[index].x += worldSpeed;
		}

		for (int index = 0; index < buildings.length; index++)
		{
			if (buildings[index].exists)
			{
				buildings[index].x += worldSpeed;
			}
		}

		if (!keyboardIsScrolling)
		{
			if (mouseX <= 25)
			{
				worldSpeed = 20;
			}
			else if (mouseX >= SCREEN_WIDTH - 25)
			{
				worldSpeed = -20;
			}
			else
			{
				worldSpeed = 0;
			}
		}

		if (goHome)
		{
			for (int index = 0; index < backgrounds.length; index++)
			{
				backgrounds[index].x -= worldX;
			}

			for (int index = 0; index < buildings.length; index++)
			{
				if (buildings[index].exists)
				{
					buildings[index].x -= worldX;
				}
			}

			worldX = homeX;

			goHome = false;
		}

	}

	/**
	 * Move each object according to their own independent instructions
	 */
	public void moveObjects()
	{
		for (int index = NO_OF_GRASS + NO_OF_DIRT; index < NO_OF_GRASS
				+ NO_OF_DIRT
				+ NO_OF_CLOUDS; index++)
		{
			backgrounds[index].x += backgrounds[index].speed * windDirection;

			if (backgrounds[index].x > (500 * (NO_OF_CLOUDS / 2)))
			{
				backgrounds[index].x -= 500 * NO_OF_CLOUDS;
			}
			else if (backgrounds[index].x < -(500 * (NO_OF_CLOUDS / 2)))
			{
				backgrounds[index].x += 500 * NO_OF_CLOUDS;
			}
		}
	}

	/**
	 * Draw each necessary image on the screen at the set framerate
	 * 
	 * @param graphics the graphics class that contains all the draw methods
	 *            needed
	 */
	public void draw(Graphics graphics)
	{
		graphics.drawImage(skyBackground, 0, 0, this);

		for (int index = 0; index < backgrounds.length; index++)
		{
			if (onScreen(backgrounds[index].x, backgrounds[index].x
					+ backgrounds[index].image.getWidth(null)))
			{
				graphics.drawImage(backgrounds[index].image,
						backgrounds[index].x,
						backgrounds[index].y,
						this);
			}
		}

		for (int index = 0; index < buildings.length; index++)
		{
			if (buildings[index].exists
					&& onScreen(buildings[index].x, buildings[index].x
							+ (buildings[index].image).getWidth(null)))
			{
				graphics.drawImage(buildings[index].image, buildings[index].x,
						buildings[index].y,
						this);
			}
		}

		if (canPlace)
		{
			if (!checkObstaclesBelow())
			{
				graphics.drawImage(buildingGhost, mouseX - selectedWidth / 2,
						groundHeight - selectedHeight, this);
			}
			else
			{
				graphics.drawImage(buildingGhostError, mouseX - selectedWidth
						/ 2,
						groundHeight - selectedHeight,
						this);
			}
		}

		for (int index = 0; index < buttons.length; index++)
		{
			if (buttons[index].exists)
			{
				graphics.drawImage(buttons[index].image,
						buttons[index].x,
						buttons[index].y,
						this);
			}
		}

		if (!mousePressed)
		{
			graphics.drawImage(customCursor, mouseX, mouseY, this);
		}
		else
		{
			graphics.drawImage(customCursorPressed, mouseX, mouseY, this);
		}

		Toolkit.getDefaultToolkit().sync();
	}

	/**
	 * Method that runs each method and action that belongs within the framerate
	 */
	public void run()
	{

		long beforeTime, timeDiff, sleep;

		beforeTime = System.currentTimeMillis();

		while (true)
		{

			scrollScreen();
			moveObjects();
			repaint();

			timeDiff = System.currentTimeMillis() - beforeTime;
			sleep = DELAY - timeDiff;

			if (sleep < 0)
			{
				sleep = 2;
			}

			try
			{
				Thread.sleep(sleep);
			}
			catch (InterruptedException event)
			{
				System.out.println("Interrupted: " + event.getMessage());
			}

			beforeTime = System.currentTimeMillis();
		}
	}

	public void mouseClicked(MouseEvent event)
	{

	}

	public void mouseEntered(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

	public void mouseExited(MouseEvent event)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Handle clicking buttons to change their graphic
	 * 
	 * @param event the mouse details
	 */
	public void mousePressed(MouseEvent event)
	{

		mousePressed = true;
		if (pointIsTouching(mouseX, mouseY, buttons[1].x1,
				buttons[1].x2, buttons[1].y1, buttons[1].y2))
		{
			buttons[1].pressed();
		}

	}

	/**
	 * Handle clicking buttons and clicking to place/remove buildings
	 * 
	 * @param event the mouse details
	 */
	public void mouseReleased(MouseEvent event)
	{

		mousePressed = false;
		if (event.getButton() == MouseEvent.BUTTON1)
		{
			if (pointIsTouching(mouseX, mouseY, buttons[1].x1,
					buttons[1].x2, buttons[1].y1, buttons[1].y2))
			{
				goHome = true;
				buttons[1].released();
			}

			else if (!checkObstaclesBelow() && canPlace)
			{
				int freeBuilding = -1;
				for (int index = 0; index < buildings.length
						&& freeBuilding < 0; index++)
				{
					if (!buildings[index].exists)
					{
						freeBuilding = index;
					}
				}

				buildings[freeBuilding].create(buildingSelected, mouseX, groundHeight,
						building);
				
				if (canPlace)
				{
					homeX = worldX + mouseX - SCREEN_WIDTH/2;
					changeBuilding("house", images[0],images[1],images[2]);
					canPlace = false;
				}
			}
		}

		else if (event.getButton() == MouseEvent.BUTTON3)
		{

			for (int index = 0; index < buildings.length; index++)
			{

				if (pointIsTouching(mouseX, mouseY, buildings[index].x,
						buildings[index].x
								+ buildings[index].width, buildings[index].y
								+ buildings[index].height,
						buildings[index].y))
				{
					buildings[index].demolish();
				}
			}
		}

	}

	/**
	 * Keep track of the location of the mouse when it is dragged with a button
	 * pressed down
	 * 
	 * @param event the mouse details
	 */
	public void mouseDragged(MouseEvent event)
	{
		mouseX = event.getX();
		mouseY = event.getY();

	}

	/**
	 * Keep track of the location of the mouse when it moves without pressing it
	 * down
	 * 
	 * @param event the mouse details
	 */
	public void mouseMoved(MouseEvent event)
	{
		mouseX = event.getX();
		mouseY = event.getY();

	}

	/**
	 * Scrolling with the arrow keys
	 * 
	 * @param event the details of the key that was pressed
	 */
	public void keyPressed(KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.VK_LEFT
				|| event.getKeyCode() == KeyEvent.VK_A)
		{
			worldSpeed = 50;
			keyboardIsScrolling = true;
		}

		else if (event.getKeyCode() == KeyEvent.VK_RIGHT
				|| event.getKeyCode() == KeyEvent.VK_D)
		{
			worldSpeed = -50;
			keyboardIsScrolling = true;
		}

		if (homeSet)
		{
			if (event.getKeyCode() == KeyEvent.VK_1)
			{
				canPlace = true;
				changeBuilding ("house",images[0],images[1], images[2]);

			}
			else if (event.getKeyCode() == KeyEvent.VK_2)
			{
				canPlace = true;
				changeBuilding ("largeHouse",images[3],images[4], images[5]);

			}
		}
	}

	@Override
	public void keyReleased(KeyEvent event)
	{
		if (event.getKeyCode() == KeyEvent.VK_LEFT
				|| event.getKeyCode() == KeyEvent.VK_A
				|| event.getKeyCode() == KeyEvent.VK_D
				|| event.getKeyCode() == KeyEvent.VK_RIGHT)
		{
			worldSpeed = 0;
			keyboardIsScrolling = false;
		}

	}

	@Override
	public void keyTyped(KeyEvent event)
	{
		// TODO Auto-generated method stub

	}

	/**
	 * Checks and returns the result of whether or not the mouse is on the same
	 * x location as another building and whether a building can be placed
	 * 
	 * @return whether a building can be placed
	 */
	public boolean checkObstaclesBelow()
	{
		for (int index = 0; index < buildings.length; index++)
		{
			if (buildings[index].exists
					&& mouseX > buildings[index].x - selectedWidth / 2
					&& mouseX < buildings[index].x
							+ buildings[index].width + selectedWidth / 2)
			{
				return true;
			}
		}

		return false;
	}

	/**
	 * Checks and returns the result of whether or not a certain point is within
	 * a rectangular area
	 * 
	 * @param x the x coordinate of the given point
	 * @param y the y coordinate of the given point
	 * @param x1 the left x coordinate of the given area
	 * @param x2 the right x coordinate of the given area
	 * @param y1 the bottom y coordinate of the given area
	 * @param y2 the top y coordinate of the given area
	 * @return whether the point and area touches
	 */
	public boolean pointIsTouching(int x, int y, int x1, int x2, int y1, int y2)
	{
		if (x >= x1 && x <= x2 && y <= y1 && y >= y2)
		{
			return true;
		}
		return false;
	}

	/**
	 * Checks and returns the result of whether or not a given line is shown on
	 * the screen
	 * 
	 * @param x1 the left x coordinate of the line
	 * @param x2 the right x coordinate of the line
	 * @return
	 */
	public boolean onScreen(int x1, int x2)
	{
		if (x1 <= SCREEN_WIDTH && x2 >= 0)
		{
			return true;
		}

		return false;
	}

	/**
	 * Changes the building type that the user is currently placing
	 * 
	 * @param name the type of building
	 * @param normal the image of the building when it's normal
	 * @param ghost the image of the ghost before placement
	 * @param ghostError the image of the ghost before placement when it cannot
	 *            be placed
	 */
	public void changeBuilding(String name, ImageIcon normal, ImageIcon ghost,
			ImageIcon ghostError)
	{
		buildingSelected = "largeHouse";
		building = normal.getImage();
		buildingGhost = ghost.getImage();
		buildingGhostError = ghostError.getImage();

		selectedWidth = building.getWidth(null);
		selectedHeight = building.getHeight(null);
	}
}