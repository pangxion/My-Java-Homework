public class Node {
    int x, y;      // 在網格地圖上的座標
    Node parent;   // 記錄是誰走過來的，這樣最後才能「倒回去」畫出路徑

    public Node(int x, int y, Node parent) {
        this.x = x;
        this.y = y;
        this.parent = parent;
    }
}