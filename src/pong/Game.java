package pong;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;

import javax.swing.JFrame;

public class Game extends Canvas implements Runnable, KeyListener{
	
	private static final long serialVersionUID = 1L;
	public static int WIDTH = 160, HEIGHT = 120, SCALE = 3; //window variables 
	
	public BufferedImage layer = new BufferedImage(WIDTH, HEIGHT, BufferedImage.TYPE_INT_RGB);
	//layer to draw the objects before render them in the screen (BufferedImage)
	
	public static Player player;
	public static Enemy enemy;
	public static Ball ball;
	
	//Constructor Method 
	public Game() {
		this.setPreferredSize(new Dimension(WIDTH * SCALE, HEIGHT * SCALE)); //window dimension 
		this.addKeyListener(this);
		player = new Player(100, HEIGHT-5);
		enemy = new Enemy(100, 0);
		ball = new Ball(100, HEIGHT/2 - 1); //ball position center - height of the ball
	}

	public static void main(String[] args) {
		Game game = new Game();
		JFrame frame = new JFrame("Pong");
		frame.setResizable(false);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.add(game); //add object 'game' in the window
		frame.pack(); 
		frame.setLocationRelativeTo(null); //centralize the window 
		frame.setVisible(true);
		
		new Thread(game).start();
		//new Thread to execute the run() method separately from the main method
	}
	
	public void tick() {
		player.tick();
		enemy.tick();
		ball.tick();
	}
	public void render() { //draw the elements of the game 
		
		BufferStrategy bs = this.getBufferStrategy();  
		if(bs == null) {
			this.createBufferStrategy(3); ///to avoid flickering
			return;
		}
		Graphics g = layer.getGraphics(); 
		//g = graphics associeted with the BufferedImage layer 
		
		g.setColor(Color.black);
		g.fillRect(0,0, WIDTH, HEIGHT); 
		player.render(g);
		enemy.render(g);
		ball.render(g);
		//create a black Rectangule in the screen dimensions and add the objects in this layer
		
		g = bs.getDrawGraphics();
		//g now represents the draw area in the screen (after the BufferStrategy)
		g.drawImage(layer, 10, 10, WIDTH * SCALE, HEIGHT * SCALE, null); 
		bs.show();
		//now the layer with the objects will be rendered and it will be shown in the screen
	}
	
	public void run() {
		while(true) {
			tick();
			render();
			try {
				Thread.sleep(1000/60);
			} catch (InterruptedException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	}

	@Override
	public void keyTyped(KeyEvent e) {
		 
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.right = true;
		}else if(e.getKeyCode()== KeyEvent.VK_LEFT) {
			player.left = true;
		}	
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT) {
			player.right = false;
		}else if(e.getKeyCode()== KeyEvent.VK_LEFT) {
			player.left = false;
		}
	}
}
