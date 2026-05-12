import java.awt.Color;
import java.awt.Graphics;
import java.util.List;

public class Enemy {
    public int x, y;
    public int speed = 2; // 敵機飛得比玩家慢
    public boolean active = true;
    
    private EnemyAI ai;
    private int[][] map; // 敵機腦中的網格地圖

    // --- 替換 Enemy.java 的建構子 ---
    public Enemy(int startX, int startY, int[][] realMap) {
        this.x = startX;
        this.y = startY;
        this.ai = new EnemyAI();
        this.map = realMap; // 接收真實有牆壁的地圖
    }

    // 更新敵機位置 (追蹤玩家)
    public void update(int playerX, int playerY) {
        // 1. 將「像素座標」轉換為「網格座標」(假設一格磁磚是 40x40)
        int gridX = x / 40;
        int gridY = y / 40;
        int pGridX = playerX / 40;
        int pGridY = playerY / 40;

        // 安全機制：避免座標超出陣列範圍導致程式崩潰
        gridX = Math.max(0, Math.min(19, gridX));
        gridY = Math.max(0, Math.min(14, gridY));
        pGridX = Math.max(0, Math.min(19, pGridX));
        pGridY = Math.max(0, Math.min(14, pGridY));

        // 2. 呼叫大腦計算最短路徑
        List<Node> path = ai.findPath(map, gridX, gridY, pGridX, pGridY);

        // 3. 如果有路徑，就往路徑的「下一步」移動
        if (path != null && path.size() > 1) {
            Node nextStep = path.get(1); // 第 0 個是自己目前的位置，第 1 個是下一步
            int targetPixelX = nextStep.x * 40;
            int targetPixelY = nextStep.y * 40;

            if (this.x < targetPixelX) this.x += speed;
            if (this.x > targetPixelX) this.x -= speed;
            if (this.y < targetPixelY) this.y += speed;
            if (this.y > targetPixelY) this.y -= speed;
        } else {
            // 如果剛好在地圖邊緣或找不到路徑，直接直線朝玩家逼近 (防止卡死)
            if (this.x < playerX) this.x += speed;
            if (this.x > playerX) this.x -= speed;
            if (this.y < playerY) this.y += speed;
            if (this.y > playerY) this.y -= speed;
        }
    }

    // 畫出紅色的敵機
    public void draw(Graphics g) {
        g.setColor(Color.RED);
        g.fillRect(x, y, 30, 30);
    }
}