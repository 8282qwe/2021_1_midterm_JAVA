package ball;

import java.awt.Image;

//소스를 입력하고 Ctrl+Shift+O를 눌러서 필요한 파일을 포함한다. 

public class StarShipSprite extends Sprite {
	private GalagaGame game;
	private int have_rocket = 1;

	public int have_rocket_now(){
		return have_rocket;
	}

	public StarShipSprite(GalagaGame game, Image image, int x, int y) {
		super(image, x, y);
		super.set_owner(0);
		this.game = game;
		life = 3;
		dx = 0;
		dy = 0;
	}



	public void changed_have_rocket(int a){
		have_rocket+=a;
	}

	@Override
	public void move() {
		if ((dx < 0) && (x < 0)) {
			dx = 0;
		}
		if ((dx > 0) && (x > 1580)) {
			dx = 0;
		}
		if ((dy < 0) && (y < 0)) {
			dy = 0;
		}
		if ((dy > 0) && (y > 980)) {
			dy = 0;
		}
		super.move();
	}

	@Override
	public void handleCollision(Sprite other) {
		if(other instanceof AlienSprite){
			this.life_discount();
			other.life_discount();
			this.x = 800;
			this.y = 500;
		}

		game.removeSprite(this);
		game.removeSprite(other);
	}
}