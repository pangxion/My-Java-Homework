import java.awt.image.BufferedImage;

public class EdgeDetector {
    public static BufferedImage detect(BufferedImage gray) {
        int width = gray.getWidth();
        int height = gray.getHeight();
        
        // 暫存第一次抓出的細線
        BufferedImage tempEdge = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);
        // 最終輸出的粗線
        BufferedImage finalEdge = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        int[][] gx = {{-1, 0, 1}, {-2, 0, 2}, {-1, 0, 1}};
        int[][] gy = {{-1, -2, -1}, {0, 0, 0}, {1, 2, 1}};
        int[][] magnitude = new int[height][width];

        // 1. Sobel 運算
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                int pixelX = 0, pixelY = 0;
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        int val = gray.getRGB(x + j, y + i) & 0xFF;
                        pixelX += val * gx[i + 1][j + 1];
                        pixelY += val * gy[i + 1][j + 1];
                    }
                }
                magnitude[y][x] = (int) Math.sqrt((pixelX * pixelX) + (pixelY * pixelY));
            }
        }

        // 2. 自適應門檻 (過濾雜訊，抓出乾淨細線)
        int windowSize = 7;
        int offset = windowSize / 2;
        int C = 15; 

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                if (x < offset || x >= width - offset || y < offset || y >= height - offset) {
                    tempEdge.setRGB(x, y, 0xFFFFFF); 
                    continue;
                }

                int sum = 0;
                for (int i = -offset; i <= offset; i++) {
                    for (int j = -offset; j <= offset; j++) {
                        sum += magnitude[y + i][x + j];
                    }
                }
                int mean = sum / (windowSize * windowSize);
                int currentMag = magnitude[y][x];

                if (currentMag > mean + C && currentMag > 40) {
                    tempEdge.setRGB(x, y, 0x000000); // 畫黑線
                } else {
                    tempEdge.setRGB(x, y, 0xFFFFFF); // 畫白底
                }
            }
        }

        // 3. 形態學膨脹 (Morphological Dilation) - 讓輪廓變粗
        // 掃描整張圖，只要 3x3 範圍內有黑色，就把中心點也塗黑
        for (int y = 1; y < height - 1; y++) {
            for (int x = 1; x < width - 1; x++) {
                boolean isBlack = false;
                
                // 檢查周圍九宮格
                for (int i = -1; i <= 1; i++) {
                    for (int j = -1; j <= 1; j++) {
                        if ((tempEdge.getRGB(x + j, y + i) & 0xFF) == 0) {
                            isBlack = true;
                            break;
                        }
                    }
                    if (isBlack) break;
                }
                
                // 如果周圍有黑線，這點就是黑的；否則維持純白
                finalEdge.setRGB(x, y, isBlack ? 0x000000 : 0xFFFFFF);
            }
        }

        return finalEdge;
    }
}