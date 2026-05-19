import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.swing.*;

public class CartoonApp extends JFrame {
    private BufferedImage originalImage;
    private BufferedImage processedImage;
    private JLabel imageLabel;

    public CartoonApp() {
        setTitle("Traditional Cartoon Style App");
        setSize(1000, 700);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLayout(new BorderLayout());

        imageLabel = new JLabel("請先載入圖片", SwingConstants.CENTER);
        add(new JScrollPane(imageLabel), BorderLayout.CENTER);

        JPanel controlPanel = new JPanel();
        JButton loadBtn = new JButton("Load Image");
        JButton processBtn = new JButton("Apply Cartoon Filter");
        JButton saveBtn = new JButton("Save Image");

        loadBtn.addActionListener(e -> loadImage());
        processBtn.addActionListener(e -> processImage());
        saveBtn.addActionListener(e -> saveImage());

        controlPanel.add(loadBtn);
        controlPanel.add(processBtn);
        controlPanel.add(saveBtn);
        add(controlPanel, BorderLayout.SOUTH);
    }

    private void loadImage() {
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showOpenDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                originalImage = ImageLoader.load(file.getAbsolutePath());
                imageLabel.setIcon(new ImageIcon(originalImage));
                imageLabel.setText("");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "讀取失敗: " + ex.getMessage());
            }
        }
    }

    private void processImage() {
        if (originalImage == null) {
            JOptionPane.showMessageDialog(this, "請先載入圖片！");
            return;
        }

        // ====== 雙管線分離架構 (極致簡單卡通風) ======

        // [管線 A：處理極簡色塊]
        // 核心修正 1：將 Kuwahara 半徑從 4 加大到 6。這會讓像素大幅簡單化，細節被徹底吃掉，變成大色塊。
        BufferedImage colorBase = KuwaharaFilter.apply(originalImage, 4);
        
        // 核心修正 2：色彩量化降至 4 階。顏色種類變少，色塊邊界會更明顯、更生硬。
        BufferedImage quantized = ColorQuantizer.quantize(colorBase, 6);

        // [管線 B：處理粗黑輪廓線]
        BufferedImage gray = GrayConverter.toGray(originalImage);
        gray = GaussianBlur.apply(gray);
        
        // 使用具備「形態學膨脹 (Dilation)」的新版邊緣偵測器，線條會明顯變粗
        BufferedImage edges = EdgeDetector.detect(gray);

        // [合併渲染]
        // 粗黑線疊加在極簡大色塊上
        processedImage = CartoonRenderer.render(quantized, edges);

        imageLabel.setIcon(new ImageIcon(processedImage));
    }

    private void saveImage() {
        if (processedImage == null) {
            JOptionPane.showMessageDialog(this, "沒有可儲存的影像！");
            return;
        }
        JFileChooser fileChooser = new JFileChooser();
        if (fileChooser.showSaveDialog(this) == JFileChooser.APPROVE_OPTION) {
            try {
                File file = fileChooser.getSelectedFile();
                ImageLoader.save(processedImage, file.getAbsolutePath() + ".png");
                JOptionPane.showMessageDialog(this, "儲存成功！");
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, "儲存失敗: " + ex.getMessage());
            }
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new CartoonApp().setVisible(true);
        });
    }
}