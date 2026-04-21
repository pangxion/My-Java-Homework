import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class EdgeDetector {

    public static void main(String[] args) {
        System.out.println("=== 啟動邊緣偵測系統 (Edge Detection) ===");

        try {
            // 1. 讀取影像
            File inputFile = new File("input.jpg"); // 請確保資料夾內有一張測試圖片
            BufferedImage image = ImageIO.read(inputFile);
            int width = image.getWidth();
            int height = image.getHeight();

            // 建立一張新的空白圖片來儲存輸出的邊緣影像
            BufferedImage edgeImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // 建立一個二維陣列來儲存灰階亮度值 f(x,y)
            int[][] grayPixels = new int[width][height];

            // 2. 將彩圖轉換為灰階亮度值 (Intensity)
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color c = new Color(image.getRGB(x, y));
                    // 簡單的灰階公式：(R+G+B)/3
                    int gray = (c.getRed() + c.getGreen() + c.getBlue()) / 3;
                    grayPixels[x][y] = gray;
                }
            }

            System.out.println("影像大小: " + width + " x " + height + "，開始進行卷積運算...");

            // 3. 套用 Finite Difference 公式尋找邊緣
            // 注意：我們的迴圈從 1 開始，到 width-1 結束，避免抓取周圍像素時超出陣列範圍 (OutOfBounds)
            for (int y = 1; y < height - 1; y++) {
                for (int x = 1; x < width - 1; x++) {
                    
                    // 根據作業講義實作 Ix 公式： (右邊像素 - 左邊像素) / 2
                    double Ix = (grayPixels[x + 1][y] - grayPixels[x - 1][y]) / 2.0;
                    
                    // 根據作業講義實作 Iy 公式： (下方像素 - 上方像素) / 2
                    double Iy = (grayPixels[x][y + 1] - grayPixels[x][y - 1]) / 2.0;

                    // 計算梯度的絕對強度 (Magnitude)
                    // 這裡使用畢氏定理：強度 = √(Ix^2 + Iy^2)
                    // 只看 X 軸變化，並加上 128 的灰色基準偏移量
                    int edgeColorValue = 128 + (int) Ix;
                    if (edgeColorValue > 255) edgeColorValue = 255;
                    if (edgeColorValue < 0) edgeColorValue = 0;

                    // 將算出的邊緣強度轉為灰階顏色，並畫在新圖片上
                    Color edgeColor = new Color(edgeColorValue, edgeColorValue, edgeColorValue);
                    edgeImage.setRGB(x, y, edgeColor.getRGB());
                }
            }

            // 4. 輸出結果圖片
            File outputFile = new File("output_edge.jpg");
            ImageIO.write(edgeImage, "jpg", outputFile);
            System.out.println("✅ 邊緣偵測完成！結果已儲存為 output_edge.jpg");

        } catch (Exception e) {
            System.out.println("發生錯誤: " + e.getMessage());
            System.out.println("請確認專案資料夾內是否有 input.jpg 這張圖片。");
        }
    }
}
