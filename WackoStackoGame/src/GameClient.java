import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.WindowConstants;

public class GameClient extends JPanel{

	/**
	 * 
	 */
	private static final long serialVersionUID = 6748325367132904432L;
	SocketClient client;
	public static boolean isRunning = true;
	//Player player = new Player();
	List<Player> players = new ArrayList<Player>();
	UI ui = new UI();
	public GameClient() {
		client = SocketClient.connect("localhost", 3001);
		
	}
	
	public static void main(String[] args) {
		JFrame frame = new JFrame("WackoStacko");
		//Terminates program when we click the x (close) button)
		frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		frame.setLayout(new BorderLayout());
		frame.setSize(new Dimension(600,600));
		
		//Game area
		JPanel playArea = new GameClient();
		playArea.setPreferredSize(new Dimension(600,600));
		//playArea.getBounds()
		//Init players
		for(int i = 0; i < 5; i++) {
			Player p = new Player();
			((GameClient)playArea).players.add(p);
			System.out.println("Added player " + i);
		}
		
		frame.add(playArea, BorderLayout.CENTER);
		frame.pack();
		frame.setVisible(true);
		((GameClient)playArea).run();
	}
	
	@Override
	public void paintComponent(Graphics g) {
		super.paintComponent(g);
		Graphics2D g2d = (Graphics2D)g;
		
		for(int i = 0; i < players.size(); i++) {
			players.get(i).draw(g2d);
		}
		//player.draw(g2d);
		//System.out.println(getFPS(FPSOldTime));
		//Sample of enabling/disabling FPS and showing on screen
		ui.draw(g2d);
	}
	//running logic (each frame what do we do?)
	public void run() {
		
		Random random = new Random();
		//give a random direction
		//based on 0 or 1
		for(int i = 0; i < players.size(); i++) {
			Player player = players.get(i);
			player.direction.x = random.nextInt(2) == 0?-1:1;
			player.direction.y = random.nextInt(2) == 0?-1:1;
			
			//give a random speed between 1 and 3
			player.speed.x = random.nextInt(3)+1;
			player.speed.y = random.nextInt(3)+1;
		}
		while(isRunning) {
			//apply direction and speed
			for(int i = 0; i < players.size(); i++) {
				Player player = players.get(i);
				player.move(this.getBounds());
			}
			
			repaint();
			try {
				Thread.sleep(16);//16 ms is ~60 fps
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
class UI{
	double FPSOldTime = 0;
	Color myColor = Color.BLACK;
	double getFPS(double oldTime) {
	    double newTime = System.nanoTime();
	    double delta = newTime - oldTime;

	    //double FPS = 1d / (delta * 1000);
	    double FPS = 1000000000.0 / delta;
	    FPSOldTime = newTime;

	    return FPS;
	}
	public void draw(Graphics2D g2d) {
		g2d.setColor(myColor);
		boolean doesUserWantFPS = true;
		if(doesUserWantFPS) {
			String str = String.format("FPS Counter: %.2f",getFPS(FPSOldTime));
			g2d.drawString(str, 50f, 50f);
		}
	}
}
class Player{
	public Point position = new Point(300,300);
	public Point speed = new Point(0,0);
	public Point direction = new Point(0,0);
	public Dimension size = new Dimension(50,50);
	public Color myColor;
	public Player() {
		Random random = new Random();
		myColor = new Color(random.nextFloat(), random.nextFloat(), random.nextFloat());
	}
	public void draw(Graphics2D g2d) {
		g2d.setColor(myColor);
		g2d.fillRect(
				position.x - (size.width/2),
				position.y - (size.height/2),
				size.width,
				size.height);
	}
	public void move(Rectangle bounds) {
		position.x += (direction.x * speed.x);
		position.y += (direction.y * speed.y);
		int halfWidth = size.width /2;
		int halfHeight = size.height/2;
		if((position.x - halfWidth <= bounds.getMinX()) 
				|| (position.x + halfWidth >= bounds.getMaxX())) {
			direction.x *= -1;
		}
		//hit top/bottom reflect direction y
		if((position.y - halfHeight <= bounds.getMinY())
				|| (position.y+halfHeight >= bounds.getMaxY())) {
			direction.y *= -1;
		}
	}
}

