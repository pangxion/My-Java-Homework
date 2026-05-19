import java.awt.image.BufferedImage;

public class CartoonRenderer {
    public static BufferedImage render(BufferedImage quantized, BufferedImage edges) {
        int width = quantized.getWidth();
        int height = quantized.getHeight();
        BufferedImage cartoon = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int y = 0; y < height; y++) {
            for (int x = 0; x < width; x++) {
                int edgePixel = edges.getRGB(x, y) & 0xFF;
                
                if (edgePixel == 0) {
                    // 如果是邊緣(黑線)，就畫黑色
                    cartoon.setRGB(x, y, 0x000000);
                } else {
                    // 否則保留量化後的卡通色塊
                    cartoon.setRGB(x, y, quantized.getRGB(x, y));
                }
            }
        }
        return cartoon;
    }
}