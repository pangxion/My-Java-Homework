import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import javax.imageio.ImageIO;

public class ImageLoader {
    public static BufferedImage load(String path) throws IOException {
        return ImageIO.read(new File(path));
    }

    public static void save(BufferedImage image, String path) throws IOException {
        ImageIO.write(image, "png", new File(path));
    }
}