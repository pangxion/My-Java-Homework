import java.awt.Color;
import java.awt.Graphics;

public class Player {
    public int x, y;
    public int speed = 5;
    public int hp = 3;
    public int width = 30;
    public int height = 30;

    // 建構子：初始化玩家位置
    public Player(int startX, int startY) {
        this.x = startX;
        this.y = startY;
    }

    // 繪製玩家 (用藍色方塊代替戰機)
    public void draw(Graphics g) {
        g.setColor(Color.BLUE);
        g.fillRect(x, y, width, height);
    }

    // 移動邏輯
    public void moveUp() { y -= speed; }
    public void moveDown() { y += speed; }
    public void moveLeft() { x -= speed; }
    public void moveRight() { x += speed; }
}