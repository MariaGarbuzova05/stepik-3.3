import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.net.URL;
import java.util.HashMap;

public class FastMovement extends JFrame {

    private static final int WINDOW_WIDTH = 800;
    private static final int WINDOW_HEIGHT = 600;
    private static final int IMAGE_SIZE = 50;
    private static final int MOVE_AMOUNT = 5; // Стандартный шаг перемещения

    private BufferedImage image;
    private int imageX, imageY;
    private final HashMap<Integer, Boolean> keys = new HashMap<>(); // отслеживание нажатых клавиш

    public FastMovement() {
        setTitle("Телепортация изображения");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);

        // Инициализация начальных координат изображения
        imageX = (WINDOW_WIDTH - IMAGE_SIZE) / 2;
        imageY = (WINDOW_HEIGHT - IMAGE_SIZE) / 2;

        // Загрузка изображения
        try {
            URL imageUrl = new URL("https://avatars.mds.yandex.net/i?id=90df36e73df8fa0377357ec011de0da0_l-4890288-images-thumbs&n=13");
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

        // Настройка InputMap и ActionMap для управления клавишами
        InputMap im = panel.getInputMap(JComponent.WHEN_IN_FOCUSED_WINDOW);
        ActionMap am = panel.getActionMap();

        // Определяем действия для клавиш
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, false), "left.pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_LEFT, 0, true), "left.released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, false), "right.pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_RIGHT, 0, true), "right.released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, false), "up.pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_UP, 0, true), "up.released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, false), "down.pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_DOWN, 0, true), "down.released");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0, false), "shift.pressed");
        im.put(KeyStroke.getKeyStroke(KeyEvent.VK_SHIFT, 0, true), "shift.released");

        // Определяем действия
        am.put("left.pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keys.put(KeyEvent.VK_LEFT, true);
            }
        });
        am.put("left.released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keys.put(KeyEvent.VK_LEFT, false);
            }
        });

        am.put("right.pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keys.put(KeyEvent.VK_RIGHT, true);
            }
        });
        am.put("right.released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keys.put(KeyEvent.VK_RIGHT, false);
            }
        });

        am.put("up.pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keys.put(KeyEvent.VK_UP, true);
            }
        });
        am.put("up.released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keys.put(KeyEvent.VK_UP, false);
            }
        });

        am.put("down.pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keys.put(KeyEvent.VK_DOWN, true);
            }
        });
        am.put("down.released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keys.put(KeyEvent.VK_DOWN, false);
            }
        });

        am.put("shift.pressed", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keys.put(KeyEvent.VK_SHIFT, true);
            }
        });
        am.put("shift.released", new AbstractAction() {
            @Override
            public void actionPerformed(ActionEvent e) {
                keys.put(KeyEvent.VK_SHIFT, false);
            }
        });

        // Таймер для анимации
        Timer timer = new Timer(5, e -> {
            int currentMoveAmount = MOVE_AMOUNT;
            if (keys.getOrDefault(KeyEvent.VK_SHIFT, false)) {
                currentMoveAmount *= 2; // Увеличиваем скорость при удерживании Shift
            }

            if (keys.getOrDefault(KeyEvent.VK_LEFT, false)) {
                imageX -= currentMoveAmount;
                if (imageX + IMAGE_SIZE < 0) {
                    imageX = WINDOW_WIDTH;
                }
            }
            if (keys.getOrDefault(KeyEvent.VK_RIGHT, false)) {
                imageX += currentMoveAmount;
                if (imageX > WINDOW_WIDTH) {
                    imageX = -IMAGE_SIZE;
                }
            }
            if (keys.getOrDefault(KeyEvent.VK_UP, false)) {
                imageY -= currentMoveAmount;
                if (imageY + IMAGE_SIZE < 0) {
                    imageY = WINDOW_HEIGHT;
                }
            }
            if (keys.getOrDefault(KeyEvent.VK_DOWN, false)) {
                imageY += currentMoveAmount;
                if (imageY > WINDOW_HEIGHT) {
                    imageY = -IMAGE_SIZE;
                }
            }
            panel.repaint(); // Перерисовываем панель
        });
        timer.start();

        panel.setFocusable(true); // Важно для получения событий клавиатуры
        panel.requestFocusInWindow(); // Устанавливаем фокус на панель
        setContentPane(panel);
        setVisible(true);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(ImageTeleport::new);
    }
}
