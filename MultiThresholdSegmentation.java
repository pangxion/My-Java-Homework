import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;

public class MultiThresholdSegmentation {
    // 計算特定區間的「權重乘上變異數 (Weight * Variance)」
    public static double calculateClassVariance(double[] prob, int start, int end) {
        double weight = 0;
        double mean = 0;
        double variance = 0;

        // 計算權重 (W)
        for (int i = start; i <= end; i++) {
            weight += prob[i];
        }
        if (weight == 0) return 0; // 避免除以零

        // 計算平均值 (Mean)
        for (int i = start; i <= end; i++) {
            mean += (i * prob[i]) / weight;
        }

        // 計算變異數 (Variance)
        for (int i = start; i <= end; i++) {
            variance += Math.pow((i - mean), 2) * prob[i];
        }

        return weight * (variance / weight); // 回傳 W * Sigma^2
    }

    public static void main(String[] args) {
        try {
            // 1. 讀取影像 (請將 "input.jpg" 替換成你的測試圖片路徑)
            File inputFile = new File("input.jpg");
            BufferedImage image = ImageIO.read(inputFile);
            int width = image.getWidth();
            int height = image.getHeight();

            // 2. 將影像轉為 2D 灰階陣列
            int[][] grayMatrix = new int[height][width];
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y);
                    int r = (rgb >> 16) & 0xFF;
                    int g = (rgb >> 8) & 0xFF;
                    int b = rgb & 0xFF;
                    // 灰階轉換公式 (Grayscale conversion)
                    grayMatrix[y][x] = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                }
            }

            // 接下來的步驟將寫在這裡...
            System.out.println("影像載入完成，大小：" + width + "x" + height);

            // 3. 計算直方圖 (Histogram) 與機率 (Probability)
            int totalPixels = width * height;
            double[] prob = new double[256];
            
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    prob[grayMatrix[y][x]]++;
                }
            }
            // 轉成機率分佈 (0.0 ~ 1.0)
            for (int i = 0; i < 256; i++) {
                prob[i] = prob[i] / totalPixels;
            }

            // 4. 尋找最佳的多重門檻值 T1 與 T2
            int optimalT1 = 0;
            int optimalT2 = 0;
            double minTotalVariance = Double.MAX_VALUE;

            System.out.println("開始計算最佳門檻值 (T_opt)...");
            
            // T1 從 0 掃描到 253, T2 從 T1+1 掃描到 254
            for (int t1 = 0; t1 < 254; t1++) {
                for (int t2 = t1 + 1; t2 < 255; t2++) {
                    
                    // 分別計算三群的 W * Sigma^2
                    double varClass0 = calculateClassVariance(prob, 0, t1);
                    double varClass1 = calculateClassVariance(prob, t1 + 1, t2);
                    double varClass2 = calculateClassVariance(prob, t2 + 1, 255);
                    
                    double totalVariance = varClass0 + varClass1 + varClass2;

                    // 找出讓群內變異數最小的組合
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
                    
                    if (originalValue <= optimalT1) {
                        newValue = 0;       // 背景 (黑色)
                    } else if (originalValue <= optimalT2) {
                        newValue = 127;     // 前景 1 (灰色)
                    } else {
                        newValue = 255;     // 前景 2 (白色)
                    }
                    
                    // 將 0-255 的灰階值轉回 RGB 格式寫入 BufferedImage
                    int newRgb = (newValue << 16) | (newValue << 8) | newValue;
                    outputImage.setRGB(x, y, newRgb);
                }
            }

            // 6. 儲存結果
            File outputFile = new File("segmented_output.jpg");
            ImageIO.write(outputImage, "jpg", outputFile);
            System.out.println("影像分割完成，已儲存為 segmented_output.jpg");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
