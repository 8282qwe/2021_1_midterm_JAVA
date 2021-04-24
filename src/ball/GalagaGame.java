package ball;

import java.awt.*;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;

import javax.imageio.ImageIO;
import javax.swing.*;

//소스를 입력하고 Ctrl+Shift+O를 눌러서 필요한 파일을 포함한다. 

public class GalagaGame extends JPanel implements KeyListener {

	private boolean running = true;

	private ArrayList sprites = new ArrayList();
	private Sprite starship;

	private BufferedImage alienImage;
	private BufferedImage shotImage;
	private BufferedImage shipImage;
	private BufferedImage item_carImage;
	private BufferedImage life;

	protected int score = 0;
	private String item_string = " ";
	private Random rd = new Random();

	public GalagaGame() {
		JFrame frame = new JFrame("Galaga Game");

		frame.setSize(1680, 1080);
		frame.add(this);
		frame.setResizable(false);
		frame.setVisible(true);
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		try {
			shotImage = ImageIO.read(new File("fire.png"));
			shipImage = ImageIO.read(new File("myteam.png"));
			alienImage = ImageIO.read(new File("enemy.png"));
			item_carImage = ImageIO.read(new File("item_car.png"));
			life = ImageIO.read(new File("life.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.requestFocus();
		this.initSprites();
		addKeyListener(this);

	}

	private void initSprites() {
		starship = new StarShipSprite(this, shipImage, 800, 500);
		sprites.add(starship);
	}

	private void startGame() {
		sprites.clear();
		initSprites();
	}

	public void endGame() {
		JOptionPane.showMessageDialog(null,"당신의 점수는 "+score+"입니다!\n");
		System.exit(0);
	}

	public void removeSprite(Sprite sprite) {
		if (sprite.life == 0 || sprite.life <= 0) {
			if(sprite instanceof AlienSprite){
				score+=100;
			}
			sprites.remove(sprite);
		}
	}

	public void fire() {
		for (int i = 0; i < ((StarShipSprite)starship).have_rocket_now();i++){
			ShotSprite shot = new ShotSprite(this, shotImage, starship.getX() + 10+i*12,
					starship.getY() - 30,-3);
			shot.set_owner(0);
			sprites.add(shot);
		}
	}

	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.black);
		g.fillRect(0, 0, 1680, 1080);
		for (int i = 0; i < sprites.size(); i++) {
			Sprite sprite = (Sprite) sprites.get(i);
			sprite.draw(g);
		}
		if(starship != null){
			for (int i = 0; i < starship.life;i++){
				g.drawImage(life,1500+i*50,970,this);
			}
		}
		g.setColor(Color.YELLOW);
		g.setFont(new Font("고딕체", Font.BOLD,20));
		g.drawString("점수: "+ score, 10, 1000);
		g.drawString(item_string, 10, 1025);
	}

	public void gameLoop() {
		int shot_timer = 0;
		int enemy_alive_timer = 0;
		int item_timer = 0;

		while (running) {

			if (enemy_alive_timer == 400){
				int randomX = rd.nextInt(1580);
				int randomY = rd.nextInt(900);
				Sprite alien = new AlienSprite(this, alienImage,
						randomX, randomY);
				sprites.add(alien);
				enemy_alive_timer = 0;
			}

			if (item_timer == 400){
				int randomX = rd.nextInt(1580);
				int randomY = rd.nextInt(900);
				Sprite item = new ItemSprite(this, item_carImage,
						randomX, randomY);
				sprites.add(item);
				item_timer = 0;
			}

			for (int i = 0; i < sprites.size(); i++) {
				Sprite sprite = (Sprite) sprites.get(i);
				if ( sprite instanceof AlienSprite && shot_timer == 100){
					ShotSprite shot = new ShotSprite(this,shotImage,((Sprite)sprites.get(i)).getX()+15,
							((Sprite)sprites.get(i)).getY()+45,3);
					shot.set_owner(1);
					sprites.add(shot);
					if (i == sprites.size() - 1){
						shot_timer = 0;
					}
				}
				sprite.move();
			}

			if (shot_timer > 100){
				shot_timer = 0;
			}
			shot_timer++;
			enemy_alive_timer++;
			item_timer++;

			for (int p = 0; p < sprites.size(); p++) {
				for (int s = p + 1; s < sprites.size(); s++) {
					Sprite me = (Sprite) sprites.get(p);
					Sprite other = (Sprite) sprites.get(s);

					if (me.checkCollision(other)) {
						me.handleCollision(other);
						other.handleCollision(me);
					}
				}
			}

			if(starship.life <= 0){
				this.endGame();
			}
			repaint();
			try {
				Thread.sleep(10);
			} catch (Exception e) {
			}
		}
	}

	@Override
	public void keyPressed(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			starship.setDx(-3);
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			starship.setDx(+3);
		if (e.getKeyCode() == KeyEvent.VK_UP)
			starship.setDy(-3);
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			starship.setDy(+3);
		if (e.getKeyCode() == KeyEvent.VK_SPACE)
			fire();
	}

	@Override
	public void keyReleased(KeyEvent e) {
		if (e.getKeyCode() == KeyEvent.VK_LEFT)
			starship.setDx(0);
		if (e.getKeyCode() == KeyEvent.VK_RIGHT)
			starship.setDx(0);
		if (e.getKeyCode() == KeyEvent.VK_UP)
			starship.setDy(0);
		if (e.getKeyCode() == KeyEvent.VK_DOWN)
			starship.setDy(0);
	}

	@Override
	public void keyTyped(KeyEvent arg0) {
	}

	public void active_item(ItemSprite item){
		switch(item.item_property){
			case 0:
				if(((StarShipSprite)starship).have_rocket_now() < 3){
					((StarShipSprite)starship).changed_have_rocket(1);
					item_string = item.map.get(0);
				}
				else{
					item_string = "Max Rocket!";
				}
				break;
			case 1:
				if(((StarShipSprite)starship).life < 3){
					((StarShipSprite)starship).life++;
					item_string = item.map.get(1);
				}
				else {
					item_string = "Max Life!";
				}
				break;
			case 2:
				for(int i = 0; i < sprites.size(); i++){
					Sprite search = (Sprite) sprites.get(i);
					if( search instanceof AlienSprite){
						score += 100;
						((Sprite)sprites.get(i)).life = 0;
						removeSprite((Sprite)sprites.get(i));
					}
				}
				item_string = item.map.get(2);
				break;
			default:
				item_string = " ";
		}
	}
	public static void main(String argv[]) {
		GalagaGame g = new GalagaGame();
		g.gameLoop();
	}
}