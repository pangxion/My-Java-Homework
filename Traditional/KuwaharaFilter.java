import java.awt.image.BufferedImage;

public class KuwaharaFilter {
    public static BufferedImage apply(BufferedImage src, int radius) {
        int width = src.getWidth();
        int height = src.getHeight();
        BufferedImage result = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 為了避免邊界溢位，我們從半徑距離開始掃描
        for (int y = radius; y < height - radius; y++) {
            for (int x = radius; x < width - radius; x++) {
                
                double[] minVariance = {Double.MAX_VALUE};
                int[] bestMeanRGB = {0, 0, 0};

                // 將周圍區域切分為四個象限 (左上、右上、左下、右下)
                evaluateQuadrant(src, x - radius, y - radius, x, y, minVariance, bestMeanRGB); // 左上
                evaluateQuadrant(src, x, y - radius, x + radius, y, minVariance, bestMeanRGB); // 右上
                evaluateQuadrant(src, x - radius, y, x, y + radius, minVariance, bestMeanRGB); // 左下
                evaluateQuadrant(src, x, y, x + radius, y + radius, minVariance, bestMeanRGB); // 右下

                // 填入變異數最小（最平坦）那個象限的平均顏色
                int outRgb = (bestMeanRGB[0] << 16) | (bestMeanRGB[1] << 8) | bestMeanRGB[2];
                result.setRGB(x, y, outRgb);
            }
        }
        return result;
    }

    // 評估單一象限的變異數與平均顏色
    private static void evaluateQuadrant(BufferedImage src, int x1, int y1, int x2, int y2, 
                                         double[] minVar, int[] bestMean) {
        int rSum = 0, gSum = 0, bSum = 0;
        double lumSum = 0, lumSqSum = 0;
        int count = 0;

        for (int y = y1; y <= y2; y++) {
            for (int x = x1; x <= x2; x++) {
                int rgb = src.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // 計算亮度 (Luminance) 來代表該點的強度
                double lum = 0.299 * r + 0.587 * g + 0.114 * b; 

                rSum += r; gSum += g; bSum += b;
                lumSum += lum; 
                lumSqSum += lum * lum;
                count++;
            }
        }

        double meanLum = lumSum / count;
        // 變異數公式：E(X^2) - (E(X))^2
        double variance = (lumSqSum / count) - (meanLum * meanLum);

        // 如果這個象限比之前的更平坦，就記錄它的顏色
        if (variance < minVar[0]) {
            minVar[0] = variance;
            bestMean[0] = rSum / count;
            bestMean[1] = gSum / count;
            bestMean[2] = bSum / count;
        }
    }
}