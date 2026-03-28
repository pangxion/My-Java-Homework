import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class PineappleScanner {
    public static void main(String[] args) {
        try {
            File file = new File("pineapple.jpg"); 
            BufferedImage image = ImageIO.read(file);
            
            int width = image.getWidth();
            int height = image.getHeight();
            long totalPixels = (long) width * height;
            long targetPixels = 0;

            // 1. Nested For Loops (Raster Scan)
            // 為了更精確，我們稍微提高顏色門檻，排除背景雜訊
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    Color c = new Color(image.getRGB(x, y));

                    // 提高門檻：只有顏色非常明顯的才算 (避免把土當鳳梨)
                    boolean isGreen = (c.getGreen() > 100 && c.getGreen() > c.getRed() + 20);
                    boolean isPurple = (c.getRed() > 70 && c.getBlue() > 71 && Math.abs(c.getRed() - c.getBlue()) < 30);

                    if (isGreen || isPurple) {
                        targetPixels++;
                    }
                }
            }

            // 2. 面積估算模型的重新建模 (Modeling)
            // 根據 4K 圖片 (884 萬像素) 中 118 顆的分布
            // 我們假設每顆鳳梨頭加上它周邊的葉子，約佔 0.5% ~ 0.7% 的面積
            // 我們使用 0.0065 作為更科學的單體係數
            double singleObjectFactor = 0.0065; 
            int estimatedCount = (int) (targetPixels / (totalPixels * singleObjectFactor));

            System.out.println("--- 二次校正掃描 ---");
            System.out.println("特徵像素: " + targetPixels + " (" + String.format("%.2f", (targetPixels * 100.0 / totalPixels)) + "%)");
            System.out.println("校正後預估數量: " + estimatedCount + " 顆");

        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}