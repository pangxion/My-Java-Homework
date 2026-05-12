import java.awt.Rectangle;
import java.util.List;

public class CollisionManager {
    
    // 檢查所有碰撞，並回傳這次更新獲得的分數
    public static int checkCollisions(Player player, List<Bullet> bullets, List<Enemy> enemies) {
        int scoreGained = 0;

        // 1. 檢查 [子彈] 是否擊中 [敵機]
        for (Bullet b : bullets) {
            if (!b.active) continue;
            // 把子彈當作一個小矩形
            Rectangle bRect = new Rectangle(b.x, b.y, 5, 15); 

            for (Enemy e : enemies) {
                if (!e.active) continue;
                // 把敵機當作一個大矩形
                Rectangle eRect = new Rectangle(e.x, e.y, 30, 30); 

                // 如果兩個矩形重疊了 (打中了)
                if (bRect.intersects(eRect)) {
                    b.active = false; // 子彈消失
                    e.active = false; // 敵機死亡
                    scoreGained += 100; // 加 100 分
                    break; // 這顆子彈已經用掉了，不用再檢查其他敵機
                }
            }
        }

        // 2. 檢查 [敵機] 是否撞到 [玩家]
        Rectangle pRect = new Rectangle(player.x, player.y, player.width, player.height);
        for (Enemy e : enemies) {
            if (!e.active) continue;
            Rectangle eRect = new Rectangle(e.x, e.y, 30, 30);
            
            if (pRect.intersects(eRect)) {
                e.active = false; // 敵機自毀
                player.hp -= 1;   // 玩家扣血
            }
        }

        return scoreGained;
    }
}