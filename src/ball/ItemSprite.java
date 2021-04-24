package ball;

import javax.imageio.ImageIO;
import java.awt.*;
import java.io.File;
import java.util.HashMap;
import java.util.Random;

public class ItemSprite extends Sprite{
    private GalagaGame game;
    private Random rd = new Random();
    protected int item_property = 3; //3은 아이템 케이스
    protected HashMap<Integer, String> map = new HashMap<Integer, String>();

    public ItemSprite(GalagaGame game,Image image, int x, int y) {
        super(image, x, y);
        super.set_owner(1);
        map.put(0, "Rocket_plus");
        map.put(1, "Heal_life");
        map.put(2, "Alien_clear");
        this.game = game;
        life = 2;
    }

    @Override
    public void move() {
        dx = (rd.nextInt(3)-1)*3;
        dy = (rd.nextInt(3)-1)*3;
        if (((dx < 0) && (x < 10)) || ((dx > 0) && (x > 1670))) {
            dx = -dx;
        }
        if (((dy < 0) && (y < 10)) || ((dy > 0) && (y > 1070))) {
            dy = -dy;
        }
        super.move();
    }

    public void handleCollision(Sprite other) {
        if(this.item_property != 3 && this.life == 0){
            game.active_item(this);
        }
    }
}
