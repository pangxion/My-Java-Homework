import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.ArrayList;
import java.util.List;
import javax.swing.JPanel;
import javax.swing.Timer;

public class GamePanel extends JPanel implements ActionListener {
    private Player player;
    private GameMap gameMap;
    private Timer timer;
    private boolean up, down, left, right;
    
    private List<Bullet> bullets = new ArrayList<>();
    private List<Enemy> enemies = new ArrayList<>();
    private int frameCount = 0;
    private int score = 0;
    
    private boolean isGameOver = false;
    private boolean isPaused = false; // 【新增】暫停狀態的開關

    public GamePanel() {
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        initGame();

        this.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                int key = e.getKeyCode();
                
                // 按 R 重新開始
                if (isGameOver && key == KeyEvent.VK_R) {
                    initGame(); 
                    return;
                }
                
                // 【新增】按下 P 鍵切換暫停狀態 (且遊戲不能是 Game Over 狀態)
                if (key == KeyEvent.VK_P && !isGameOver) {
                    isPaused = !isPaused; // 反轉狀態 (true變false, false變true)
                    return; 
                }
                
                // 如果在暫停狀態，就不處理玩家的移動和射擊按鍵
                if (isPaused) return;

                if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) up = true;
                if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) down = true;
                if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) left = true;
                if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) right = true;
                
                if (key == KeyEvent.VK_SPACE && !isGameOver) {
                    bullets.add(new Bullet(player.x, player.y));
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                int key = e.getKeyCode();
                if (key == KeyEvent.VK_W || key == KeyEvent.VK_UP) up = false;
                if (key == KeyEvent.VK_S || key == KeyEvent.VK_DOWN) down = false;
                if (key == KeyEvent.VK_A || key == KeyEvent.VK_LEFT) left = false;
                if (key == KeyEvent.VK_D || key == KeyEvent.VK_RIGHT) right = false;
            }
        });

        timer = new Timer(16, this);
        timer.start();
    }

    private void initGame() {
        player = new Player(385, 500); 
        gameMap = new GameMap();
        bullets.clear();
        enemies.clear();
        score = 0;
        frameCount = 0;
        isGameOver = false;
        isPaused = false; // 重設遊戲時也要解除暫停
    }

    private void updateGame() {
        // 【新增】如果遊戲結束，或處於「暫停狀態」，就直接 return，不更新任何資料
        if (isGameOver || isPaused) return; 
        
        frameCount++;

        if (up && player.y > 0) player.moveUp();
        if (down && player.y < 530) player.moveDown();
        if (left && player.x > 0) player.moveLeft();
        if (right && player.x < 750) player.moveRight();

        if (frameCount % 60 == 0) {
            int randomX = (int)(Math.random() * 19) * 40; 
            enemies.add(new Enemy(randomX, 0, gameMap.grid));
        }

        score += CollisionManager.checkCollisions(player, bullets, enemies);

        if (player.hp <= 0) isGameOver = true;

        bullets.removeIf(b -> !b.active);
        enemies.removeIf(e -> !e.active);

        for (Bullet b : bullets) b.update();
        for (Enemy e : enemies) e.update(player.x, player.y);
    }

    @Override
    protected void paintComponent(Graphics g) {
        super.paintComponent(g);
        
        gameMap.draw(g); 
        player.draw(g);
        for (Bullet b : bullets) b.draw(g);
        for (Enemy e : enemies) e.draw(g);
        
        g.setColor(Color.WHITE);
        g.setFont(new Font("Arial", Font.BOLD, 16));
        g.drawString("HP: " + player.hp, 10, 20);
        g.drawString("SCORE: " + score, 10, 40);

        if (isGameOver) {
            g.setColor(Color.RED);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("GAME OVER", 280, 250);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press 'R' to Restart", 310, 300);
        } 
        // 【新增】繪製 PAUSED 字樣
        else if (isPaused) {
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Arial", Font.BOLD, 40));
            g.drawString("PAUSED", 310, 250);
            g.setColor(Color.WHITE);
            g.setFont(new Font("Arial", Font.PLAIN, 20));
            g.drawString("Press 'P' to Resume", 305, 300);
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        updateGame();
        repaint();
    }
}