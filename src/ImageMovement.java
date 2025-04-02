import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;

public class ImageMovement extends JFrame {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int IMAGE_SIZE = 50;
    private static final int MOVE_AMOUNT = 50;

    private BufferedImage image;
    private int imageX, imageY; // Координаты изображения
    private boolean keyPressed = false;  // Флаг, указывающий, нажата ли клавиша
    private int currentKeyCode; // Для хранения кода нажатой клавиши

    public ImageMovement() {
        setTitle("Движение изображения");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Инициализация начальных координат изображения
        imageX = (WINDOW_WIDTH - IMAGE_SIZE) / 2;
        imageY = (WINDOW_HEIGHT - IMAGE_SIZE) / 2;

        // Загрузка изображения
        try {
            // Замените URL на URL вашей картинки
            URL imageUrl = new URL("https://avatars.mds.yandex.net/i?id=90df36e73df8fa0377357ec011de0da0_l-4890288-images-thumbs&n=13"); // Пример URL картинки
            image = ImageIO.read(imageUrl);
            if (image == null) {
                throw new IOException("Не удалось загрузить изображение.");
            }
        } catch (IOException e) {
            System.err.println("Ошибка при загрузке изображения: " + e.getMessage());
            JOptionPane.showMessageDialog(this, "Ошибка при загрузке изображения: " + e.getMessage(),
                    "Ошибка", JOptionPane.ERROR_MESSAGE);
            System.exit(1);
        }

        // Создание панели для рисования и обработки событий
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);
                if (image != null) {
                    g.drawImage(image, imageX, imageY, IMAGE_SIZE, IMAGE_SIZE, null);
                }
            }
        };

        // Обработчик нажатия клавиши
        panel.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (!keyPressed) { // Разрешаем только одно нажатие клавиши
                    keyPressed = true;
                    currentKeyCode = e.getKeyCode(); // Сохраняем код нажатой клавиши
                }
            }

            @Override
            public void keyReleased(KeyEvent e) {
                if (keyPressed && e.getKeyCode() == currentKeyCode) {
                    keyPressed = false;
                    moveImage(e.getKeyCode());
                    panel.repaint(); // Перерисовываем после перемещения
                }
            }
        });

        panel.setFocusable(true); // Важно для получения событий клавиатуры
        panel.requestFocusInWindow(); // Устанавливаем фокус на панель
        setContentPane(panel);
        setVisible(true);
    }

    private void moveImage(int keyCode) {
        int newX = imageX;
        int newY = imageY;

        switch (keyCode) {
            case KeyEvent.VK_LEFT:
                newX -= MOVE_AMOUNT;
                break;
            case KeyEvent.VK_RIGHT:
                newX += MOVE_AMOUNT;
                break;
            case KeyEvent.VK_UP:
                newY -= MOVE_AMOUNT;
                break;
            case KeyEvent.VK_DOWN:
                newY += MOVE_AMOUNT;
                break;
        }

        // Проверка на выход за границы
        if (newX >= 0 && newX <= WINDOW_WIDTH - IMAGE_SIZE &&
                newY >= 0 && newY <= WINDOW_HEIGHT - IMAGE_SIZE) {
            imageX = newX;
            imageY = newY;
        }
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageMovement::new);
    }
}