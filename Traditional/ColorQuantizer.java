import java.awt.image.BufferedImage;

public class ColorQuantizer {
    public static BufferedImage quantize(BufferedImage src, int levels) {
        int width = src.getWidth();
        int height = src.getHeight();
        BufferedImage quantized = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        // 計算每個色階區間的大小
        int binSize = 256 / levels;

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = src.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;

                // 核心修正：強制將顏色「吸附」到該區間的中心點，消滅漸層
                r = (r / binSize) * binSize + (binSize / 2);
                g = (g / binSize) * binSize + (binSize / 2);
                b = (b / binSize) * binSize + (binSize / 2);

                // 安全防護，避免數值溢位
                r = Math.min(255, Math.max(0, r));
                g = Math.min(255, Math.max(0, g));
                b = Math.min(255, Math.max(0, b));

                int newPixel = (r << 16) | (g << 8) | b;
                quantized.setRGB(x, y, newPixel);
            }
        }
        return quantized;
    }
}