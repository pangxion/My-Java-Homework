import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.LinkedList;
import java.util.Queue;
import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileNameExtensionFilter;

public class OtsuBFS_Interactive {

    public static void main(String[] args) {
        System.out.println("--- [Lecture 8] BFS 與大津演算法進階：交互式影像分割系統 ---");

        // ==========================================
        // 0. 檔案選擇器 (解決找不到圖片的問題)
        // ==========================================
        JFileChooser fileChooser = new JFileChooser();
        fileChooser.setDialogTitle("請選擇要進行分割的圖片 (jpg/png)");
        // 過濾副檔名，只允許圖片
        FileNameExtensionFilter filter = new FileNameExtensionFilter("圖片檔案 (*.jpg, *.png)", "jpg", "png", "jpeg");
        fileChooser.setFileFilter(filter);

        int result = fileChooser.showOpenDialog(null);
        if (result != JFileChooser.APPROVE_OPTION) {
            System.out.println("你取消了選擇，程式結束。");
            return;
        }

        File selectedFile = fileChooser.getSelectedFile();
        System.out.println("已選取檔案: " + selectedFile.getAbsolutePath());

        BufferedImage img = null;
        try {
            img = ImageIO.read(selectedFile);
            if (img == null) throw new Exception("讀取失敗，檔案格式不支援。");
        } catch (Exception e) {
            System.err.println("錯誤: " + e.getMessage());
            return;
        }

        int width = img.getWidth();
        int height = img.getHeight();
        int totalPixels = width * height;

        // ==========================================
        // 1. 建立機率直方圖 h(i) - [作業要求 1]
        // ==========================================
        double[] h = new double[256];
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);
                int gray = (((rgb >> 16) & 0xFF) + ((rgb >> 8) & 0xFF) + (rgb & 0xFF)) / 3;
                h[gray] += 1.0;
            }
        }
        // 正規化為機率
        for (int i = 0; i < 256; i++) h[i] /= totalPixels;

        // ==========================================
        // 2. 建立累積總和 P(i), S(i), S2(i) - [作業要求 2]
        // ==========================================
        double[] P = new double[256]; // 累積機率
        double[] S = new double[256]; // 累積期望值
        double[] S2 = new double[256]; // 累積平方期望值

        P[0] = h[0];
        S[0] = 0;
        S2[0] = 0;

        for (int i = 1; i < 256; i++) {
            P[i] = P[i - 1] + h[i];
            S[i] = S[i - 1] + (i * h[i]);
            S2[i] = S2[i - 1] + (i * i * h[i]);
        }

        // ==========================================
        // 3. BFS 廣度優先搜尋最佳 Threshold - [作業要求 3]
        // ==========================================
        Queue<Integer> queue = new LinkedList<>();
        boolean[] visited = new boolean[256];
        queue.add(0);
        visited[0] = true;

        double minVariance = Double.MAX_VALUE;
        int bestT = 0;

        while (!queue.isEmpty()) {
            int t = queue.poll();

            // ==========================================
            // 4. 利用 Cumulative Sum 算統計量 (O(1)) - [作業要求 4]
            // ==========================================
            double wB = P[t];
            double wF = P[255] - P[t];

            if (wB > 0 && wF > 0) {
                double uB = S[t] / wB;
                double uF = (S[255] - S[t]) / wF;

                double varB = (S2[t] / wB) - (uB * uB);
                double varF = ((S2[255] - S2[t]) / wF) - (uF * uF);

                double withinVar = (wB * varB) + (wF * varF);

                // ==========================================
                // 5. 選出最佳組合 - [作業要求 5]
                // ==========================================
                if (withinVar < minVariance) {
                    minVariance = withinVar;
                    bestT = t;
                }
            }

            if (t + 1 < 256 && !visited[t + 1]) {
                queue.add(t + 1);
                visited[t + 1] = true;
            }
        }

        System.out.println("BFS 搜尋完成！最佳閾值 (T_opt) = " + bestT);

        // ==========================================
        // 6. 根據 Threshold 做影像分割 - [作業要求 6]
        // ==========================================
        BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = img.getRGB(x, y);
                int gray = (((rgb >> 16) & 0xFF) + ((rgb >> 8) & 0xFF) + (rgb & 0xFF)) / 3;
                int binary = (gray <= bestT) ? 0 : 255;
                out.setRGB(x, y, (binary << 16) | (binary << 8) | binary);
            }
        }

        try {
            String outName = "output_binarized.png";
            ImageIO.write(out, "png", new File(outName));
            System.out.println("影像分割成功！結果已儲存至: " + outName);
        } catch (Exception e) {
            System.err.println("儲存失敗: " + e.getMessage());
        }

        // ==========================================
        // 7. 印出時間複雜度 - [作業要求 7]
        // ==========================================
        System.out.println("\n--- [時間複雜度分析] ---");
        System.out.println("1. 直方圖統計: O(N), N = 像素總數");
        System.out.println("2. 累積總和預處理: O(L), L = 灰階色階 (256)");
        System.out.println("3. BFS 搜尋: O(L)");
        System.out.println("4. 區間統計量計算: O(1) (歸功於累積總和)");
        System.out.println("5. 影像重繪分割: O(N)");
        System.out.println("總複雜度: O(N + L)，效率極佳！");
    }
}