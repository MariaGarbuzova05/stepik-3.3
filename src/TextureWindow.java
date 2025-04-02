import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class TextureWindow extends JFrame {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int TEXTURE_SIZE = 50;
    private BufferedImage texture;

    public TextureWindow() {
        setTitle("Текстурированное окно");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null); // Центрировать окно

        try {
            URL textureUrl = new URL("https://i.pinimg.com/originals/6c/85/9c/6c859c8e788f99ea489c62f0ed0a1215.jpg");
            texture = ImageIO.read(textureUrl);

            // Проверка, что текстура загружена успешно
            if (texture == null) {
                throw new IOException("Не удалось загрузить текстуру.");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке текстуры: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Ошибка при загрузке текстуры: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            System.exit(1); // Завершить программу, если не удалось загрузить текстуру
        }

        setContentPane(new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (texture != null) {
                    // Заполнение окна текстурой
                    for (int y = 0; y < getHeight(); y += TEXTURE_SIZE) {
                        for (int x = 0; x < getWidth(); x += TEXTURE_SIZE) {
                            g.drawImage(texture, x, y, TEXTURE_SIZE, TEXTURE_SIZE, null);
                        }
                    }
                }
            }
        });

        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(TextureWindow::new);
    }
}