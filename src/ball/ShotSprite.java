package ball;

import java.awt.Image;
import java.util.Random;


//�ҽ��� �Է��ϰ� Ctrl+Shift+O�� ������ �ʿ��� ������ �����Ѵ�. 

public class ShotSprite extends Sprite {
	private GalagaGame game;

	public ShotSprite(GalagaGame game, Image image, int x, int y, int z) {
		super(image, x, y);
		this.game = game;
		life = 1;
		dy = z;
	}

	@Override
	public void move() {
		super.move();
		if (y < -100 || y > 1080) {
			game.removeSprite(this);
		}
	}

	@Override
	public void handleCollision(Sprite other) {

		if (other instanceof AlienSprite || other instanceof StarShipSprite) {
			this.life_discount();
			other.life_discount();
			game.removeSprite(this);
			game.removeSprite(other);
		}
		else if (other instanceof ItemSprite){
			this.life_discount();
			other.life_discount();
			game.removeSprite(this);
			if(other.life == 0 && ((ItemSprite) other).item_property == 3){
				Random rd = new Random();
				other.change_property("item.png");
				other.life = 1;
				((ItemSprite) other).item_property = rd.nextInt(3);
			}
			else{
				other.handleCollision(this);
				game.removeSprite(other);
			}
		}
	}
}
