import javax.swing.JFrame;

public class GameFrame extends JFrame {
    
    public GameFrame() {
        this.setTitle("AI Shooter Challenge"); // 視窗標題
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE); // 按下叉叉時關閉程式
        this.setSize(800, 600); // 講義建議的視窗大小
        this.setResizable(false); // 禁止玩家調整視窗大小，避免地圖座標錯亂
        
        GamePanel panel = new GamePanel();
        this.add(panel);
        
        this.setLocationRelativeTo(null); // 讓視窗在螢幕正中央開啟
        this.setVisible(true); // 顯示視窗
    }

    // 程式真正的起點
    public static void main(String[] args) {
        new GameFrame();
    }
}