package ball;

import javax.imageio.ImageIO;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Random;

//소스를 입력하고 Ctrl+Shift+O를 눌러서 필요한 파일을 포함한다. 

public class AlienSprite extends Sprite{
	private GalagaGame game;
	private Random rd = new Random();
	private BufferedImage Item_drop_Image;

	public AlienSprite(GalagaGame game, Image image, int x, int y) {
		super(image, x, y);
		super.set_owner(1);
		this.game = game;
		this.life = 3;
		dx = 0;
		try {
			Item_drop_Image = ImageIO.read(new File("item.png"));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void move() {
		dx = rd.nextInt(11)-5;
		dy = rd.nextInt(11)-5;
		if (((dx < 0) && (x < 10)) || ((dx > 0) && (x > 1640))) {
			dx = -dx;
		}
		if (((dy < 0) && (y < 10)) || ((dy > 0) && (y > 1000))) {
			dy = -dy;
		}
		super.move();
	}

}