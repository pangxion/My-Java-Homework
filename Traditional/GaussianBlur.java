import java.awt.image.BufferedImage;

public class GaussianBlur {
    public static BufferedImage apply(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();
        BufferedImage blurred = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 5x5 高斯卷積核 (權重分佈)
        int[][] kernel = {
            {1,  4,  7,  4, 1},
            {4, 16, 26, 16, 4},
            {7, 26, 41, 26, 7},
            {4, 16, 26, 16, 4},
            {1,  4,  7,  4, 1}
        };
        int weightSum = 273; // 權重總和

        // 邊緣保留原色，從 2 開始避免陣列越界
        for (int y = 2; y < height - 2; y++) {
            for (int x = 2; x < width - 2; x++) {
                int rSum = 0, gSum = 0, bSum = 0;

                for (int i = -2; i <= 2; i++) {
                    for (int j = -2; j <= 2; j++) {
                        int rgb = src.getRGB(x + j, y + i);
                        int r = (rgb >> 16) & 0xFF;
                        int g = (rgb >> 8) & 0xFF;
                        int b = rgb & 0xFF;

                        int weight = kernel[i + 2][j + 2];
                        rSum += r * weight;
                        gSum += g * weight;
                        bSum += b * weight;
                    }
                }
                
                // 計算加權平均
                int finalR = rSum / weightSum;
                int finalG = gSum / weightSum;
                int finalB = bSum / weightSum;
                blurred.setRGB(x, y, (finalR << 16) | (finalG << 8) | finalB);
            }
        }
        return blurred;
    }
}