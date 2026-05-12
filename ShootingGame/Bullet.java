import java.awt.Color;
import java.awt.Graphics;

public class Bullet {
    public int x, y;
    public int speed = 10;
    public boolean active = true; // 用來判斷子彈是否還有效

    public Bullet(int startX, int startY) {
        // 為了讓子彈從戰機中間射出，我們對 X 座標做點微調 (+12)
        this.x = startX + 12; 
        this.y = startY;
    }

    public void update() {
        y -= speed;
        // 如果子彈飛出螢幕上半部，就標記為失效，等待系統回收
        if (y < 0) active = false; 
    }

    // 畫出黃色的雷射子彈
    public void draw(Graphics g) {
        g.setColor(Color.YELLOW);
        g.fillRect(x, y, 5, 15);
    }
}