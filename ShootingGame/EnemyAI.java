import java.util.*;

public class EnemyAI {
    // 假設地圖是 20x20，0 代表路，1 代表牆壁
    public List<Node> findPath(int[][] map, int startX, int startY, int targetX, int targetY) {
        int rows = map.length;
        int cols = map[0].length;
        
        // 記錄哪些點已經走過了
        boolean[][] visited = new boolean[rows][cols];
        // BFS 專用的隊列 (Queue)
        Queue<Node> queue = new LinkedList<>();

        // 從起點出發
        queue.add(new Node(startX, startY, null));
        visited[startY][startX] = true;

        // 四個移動方向：上、下、左、右
        int[] dx = {0, 0, -1, 1};
        int[] dy = {-1, 1, 0, 0};

        while (!queue.isEmpty()) {
            Node current = queue.poll();

            // 如果找到玩家的座標了，就開始回溯路徑
            if (current.x == targetX && current.y == targetY) {
                return reconstructPath(current);
            }

            // 往四個方向探索
            for (int i = 0; i < 4; i++) {
                int nextX = current.x + dx[i];
                int nextY = current.y + dy[i];

                // 檢查是否在地圖內、是不是路 (0)、而且還沒走過
                if (nextX >= 0 && nextX < cols && nextY >= 0 && nextY < rows &&
                    map[nextY][nextX] == 0 && !visited[nextY][nextX]) {
                    
                    visited[nextY][nextX] = true;
                    queue.add(new Node(nextX, nextY, current));
                }
            }
        }
        return null; // 找不到路徑
    }

    // 將路徑倒回去整理成一個列表 (List)
    private List<Node> reconstructPath(Node targetNode) {
        List<Node> path = new ArrayList<>();
        Node temp = targetNode;
        while (temp != null) {
            path.add(0, temp); // 加在最前面
            temp = temp.parent;
        }
        return path;
    }
}