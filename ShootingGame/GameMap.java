import java.awt.Color;
import java.awt.Graphics;

public class GameMap {
    // 0 代表可以走的路，1 代表牆壁障礙物
    public int[][] grid = new int[15][20];

    public GameMap() {
        // 我們在畫面中間設置一排水平的牆壁 (橫跨第 5 到 14 格)
        for (int i = 5; i < 15; i++) {
            grid[7][i] = 1; 
        }
        
        // 隨機放幾塊方形障礙物
        grid[10][3] = 1; grid[10][4] = 1;
        grid[11][3] = 1; grid[11][4] = 1;
        
        grid[3][15] = 1; grid[3][16] = 1;
        grid[4][15] = 1; grid[4][16] = 1;
    }

    // 將牆壁畫在畫面上 (一格 40x40 像素)
    public void draw(Graphics g) {
        g.setColor(Color.GRAY);
        for (int r = 0; r < 15; r++) {
            for (int c = 0; c < 20; c++) {
                if (grid[r][c] == 1) {
                    g.fillRect(c * 40, r * 40, 40, 40);
                }
            }
        }
    }
}