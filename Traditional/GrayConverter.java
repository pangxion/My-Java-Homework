import java.awt.image.BufferedImage;

public class GrayConverter {
    public static BufferedImage toGray(BufferedImage src) {
        int width = src.getWidth();
        int height = src.getHeight();
        BufferedImage gray = new BufferedImage(width, height, BufferedImage.TYPE_BYTE_GRAY);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int rgb = src.getRGB(x, y);
                int r = (rgb >> 16) & 0xFF;
                int g = (rgb >> 8) & 0xFF;
                int b = rgb & 0xFF;
                // 傳統灰階公式
                int grayVal = (int) (0.299 * r + 0.587 * g + 0.114 * b);
                int newPixel = (grayVal << 16) | (grayVal << 8) | grayVal;
                gray.setRGB(x, y, newPixel);
            }
        }
        return gray;
    }
}