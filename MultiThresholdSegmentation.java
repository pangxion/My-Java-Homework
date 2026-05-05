import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class MultiThresholdSegmentation {

    public static void main(String[] args) {
        try {
            // 1. 讀取影像 (請確保專案目錄下有一張名為 input.jpg 的圖片)
            File inputFile = new File("input.jpg");
            if (!inputFile.exists()) {
                System.out.println("找不到 input.jpg，請確認圖片已放置於正確路徑！");
                return;
            }
            BufferedImage image = ImageIO.read(inputFile);
            int width = image.getWidth();
            int height = image.getHeight();
            System.out.println("影像載入完成，大小：" + width + "x" + height);

            // 2. 將影像轉為 2D 灰階陣列
            int[][] grayMatrix = new int[height][width];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;
                    grayMatrix[y][x] = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                }
            }

            // 3. 計算直方圖 (Histogram) 與機率分佈
            int totalPixels = width * height;
            double[] prob = new double[256];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    prob[grayMatrix[y][x]]++;
                }
            }
            // 轉成機率 (0.0 ~ 1.0)
            for (int i = 0; i < 256; i++) {
                prob[i] = prob[i] / totalPixels;
            }

            // 4. 尋找最佳的多重門檻值 T1 與 T2 (讓群內變異數最小化)
            int optimalT1 = 0;
            int optimalT2 = 0;
            double minTotalVariance = Double.MAX_VALUE;

            System.out.println("開始計算最佳門檻值 (T_opt)... 尋找最小群內變異數");
            
            // 暴力搜尋法：T1 從 0~253, T2 從 T1+1~254
            for (int t1 = 0; t1 < 254; t1++) {
                for (int t2 = t1 + 1; t2 < 255; t2++) {
                    
                    // 分別計算三群的 權重乘上變異數 (W * Sigma^2)
                    double varClass0 = calculateClassVariance(prob, 0, t1);
                    double varClass1 = calculateClassVariance(prob, t1 + 1, t2);
                    double varClass2 = calculateClassVariance(prob, t2 + 1, 255);
                    
                    double totalVariance = varClass0 + varClass1 + varClass2;

                    // 找出讓整體群內變異數最小的組合
                    if (totalVariance < minTotalVariance) {
                        minTotalVariance = totalVariance;
                        optimalT1 = t1;
                        optimalT2 = t2;
                    }
                }
            }
            System.out.println("最佳門檻值找到：T1 = " + optimalT1 + ", T2 = " + optimalT2);

            // 5. 根據 T1, T2 進行影像分割 (Segmentation)
            BufferedImage outputImage = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
            
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int originalValue = grayMatrix[y][x];
                    int newValue;
                    
                    // 【符合老師要求的分割邏輯】
                    if (originalValue <= optimalT1) {
                        newValue = 0;       // 最暗群組：背景 (黑色)
                    } else if (originalValue <= optimalT2) {
                        newValue = 252;     // 中間色調：陰影/過渡帶 (灰色) -> 若老師要求只能純黑白，請將 數值 改為 0 或 255
                    } else {
                        newValue = 255;     // 最亮群組：主角 (白色)
                    }
                    
                    // 將灰階值轉回 RGB 格式寫入 BufferedImage
                    int newRgb = (newValue << 16) | (newValue << 8) | newValue;
                    outputImage.setRGB(x, y, newRgb);
                }
            }

            // 6. 儲存結果
            File outputFile = new File("segmented_output.jpg");
            ImageIO.write(outputImage, "jpg", outputFile);
            System.out.println("影像分割完成，已成功分離背景與主角，儲存為 segmented_output.jpg");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    // --- Helper Method: 計算特定區間的「權重乘上變異數 (Weight * Variance)」 ---
    public static double calculateClassVariance(double[] prob, int start, int end) {
        double weight = 0;
        double mean = 0;
        double variance = 0;

        // 1. 計算該群體的權重 (Weight)
        for (int i = start; i <= end; i++) {
            weight += prob[i];
        }
        if (weight == 0) return 0; // 若該區間沒像素，變異數為 0

        // 2. 計算該群體的平均灰階值 (Mean)
        for (int i = start; i <= end; i++) {
            mean += (i * prob[i]) / weight;
        }

        // 3. 計算該群體的變異數 (Variance)
        for (int i = start; i <= end; i++) {
            variance += Math.pow((i - mean), 2) * prob[i];
        }

        // 回傳 W * Sigma^2
        return weight * variance; 
    }
}
