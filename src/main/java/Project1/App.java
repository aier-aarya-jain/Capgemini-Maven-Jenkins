package Project1;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.Timer;

/**
 * Crazy Snake Game
 */
public class App extends JPanel implements ActionListener, KeyListener {

    // Screen size
    static final int WIDTH = 600;
    static final int HEIGHT = 600;

    static final int UNIT_SIZE = 25;
    static final int TOTAL_UNITS = (WIDTH * HEIGHT) / (UNIT_SIZE * UNIT_SIZE);

    // Snake body coordinates
    final int[] x = new int[TOTAL_UNITS];
    final int[] y = new int[TOTAL_UNITS];

    int bodyParts = 5;
    int applesEaten = 0;

    int appleX;
    int appleY;

    char direction = 'R';
    boolean running = false;

    Timer timer;
    Random random;

    // Snake color and speed
    Color snakeColor = Color.GREEN;
    int speed = 120;

    // Constructor
    App() {

        random = new Random();

        this.setPreferredSize(new Dimension(WIDTH, HEIGHT));
        this.setBackground(Color.BLACK);
        this.setFocusable(true);
        this.addKeyListener(this);

        startGame();
    }

    // Start game
    public void startGame() {

        newApple();

        running = true;

        timer = new Timer(speed, this);
        timer.start();
    }

    // Generate new apple
    public void newApple() {

        appleX = random.nextInt(WIDTH / UNIT_SIZE) * UNIT_SIZE;
        appleY = random.nextInt(HEIGHT / UNIT_SIZE) * UNIT_SIZE;
    }

    // Paint component
    @Override
    protected void paintComponent(Graphics g) {

        super.paintComponent(g);

        draw(g);
    }

    // Draw everything
    public void draw(Graphics g) {

        if (running) {

            // Draw grid
            g.setColor(Color.DARK_GRAY);

            for (int i = 0; i < HEIGHT / UNIT_SIZE; i++) {

                g.drawLine(i * UNIT_SIZE, 0, i * UNIT_SIZE, HEIGHT);
                g.drawLine(0, i * UNIT_SIZE, WIDTH, i * UNIT_SIZE);
            }

            // Draw apple
            g.setColor(Color.RED);
            g.fillOval(appleX, appleY, UNIT_SIZE, UNIT_SIZE);

            // Draw snake
            for (int i = 0; i < bodyParts; i++) {

                if (i == 0) {

                    // Head
                    g.setColor(Color.WHITE);

                } else {

                    // Random crazy colors
                    snakeColor = new Color(
                            random.nextInt(255),
                            random.nextInt(255),
                            random.nextInt(255));

                    g.setColor(snakeColor);
                }

                g.fillRect(x[i], y[i], UNIT_SIZE, UNIT_SIZE);
            }

            // Draw score
            g.setColor(Color.YELLOW);
            g.setFont(new Font("Ink Free", Font.BOLD, 30));

            FontMetrics metrics = getFontMetrics(g.getFont());

            g.drawString(
                    "Score: " + applesEaten,
                    (WIDTH - metrics.stringWidth("Score: " + applesEaten)) / 2,
                    g.getFont().getSize());

        } else {

            gameOver(g);
        }
    }

    // Move snake
    public void move() {

        for (int i = bodyParts; i > 0; i--) {

            x[i] = x[i - 1];
            y[i] = y[i - 1];
        }

        switch (direction) {

            case 'U':
                y[0] = y[0] - UNIT_SIZE;
                break;

            case 'D':
                y[0] = y[0] + UNIT_SIZE;
                break;

            case 'L':
                x[0] = x[0] - UNIT_SIZE;
                break;

            case 'R':
                x[0] = x[0] + UNIT_SIZE;
                break;
        }
    }

    // Check if apple eaten
    public void checkApple() {

        if ((x[0] == appleX) && (y[0] == appleY)) {

            bodyParts++;
            applesEaten++;

            // Increase speed
            if (speed > 40) {

                speed -= 5;
                timer.setDelay(speed);
            }

            newApple();
        }
    }

    // Check collisions
    public void checkCollisions() {

        // Snake collides with itself
        for (int i = bodyParts; i > 0; i--) {

            if ((x[0] == x[i]) && (y[0] == y[i])) {

                running = false;
            }
        }

        // Wall collision
        if (x[0] < 0 || x[0] >= WIDTH ||
                y[0] < 0 || y[0] >= HEIGHT) {

            running = false;
        }

        if (!running) {

            timer.stop();
        }
    }

    // Game over screen
    public void gameOver(Graphics g) {

        // GAME OVER text
        g.setColor(Color.RED);
        g.setFont(new Font("Ink Free", Font.BOLD, 50));

        FontMetrics metrics1 = getFontMetrics(g.getFont());

        g.drawString(
                "GAME OVER",
                (WIDTH - metrics1.stringWidth("GAME OVER")) / 2,
                HEIGHT / 2);

        // Final score
        g.setColor(Color.GREEN);
        g.setFont(new Font("Ink Free", Font.BOLD, 30));

        FontMetrics metrics2 = getFontMetrics(g.getFont());

        g.drawString(
                "Final Score: " + applesEaten,
                (WIDTH - metrics2.stringWidth("Final Score: " + applesEaten)) / 2,
                HEIGHT / 2 + 50);

        g.drawString(
                "Press SPACE to Restart",
                (WIDTH - metrics2.stringWidth("Press SPACE to Restart")) / 2,
                HEIGHT / 2 + 100);
    }

    // Game loop
    @Override
    public void actionPerformed(ActionEvent e) {

        if (running) {

            move();
            checkApple();
            checkCollisions();
        }

        repaint();
    }

    // Keyboard controls
    @Override
    public void keyPressed(KeyEvent e) {

        switch (e.getKeyCode()) {

            case KeyEvent.VK_LEFT:

                if (direction != 'R') {

                    direction = 'L';
                }
                break;

            case KeyEvent.VK_RIGHT:

                if (direction != 'L') {

                    direction = 'R';
                }
                break;

            case KeyEvent.VK_UP:

                if (direction != 'D') {

                    direction = 'U';
                }
                break;

            case KeyEvent.VK_DOWN:

                if (direction != 'U') {

                    direction = 'D';
                }
                break;

            case KeyEvent.VK_SPACE:

                if (!running) {

                    resetGame();
                }
                break;
        }
    }

    // Reset game
    public void resetGame() {

        bodyParts = 5;
        applesEaten = 0;
        direction = 'R';
        speed = 120;

        for (int i = 0; i < TOTAL_UNITS; i++) {

            x[i] = 0;
            y[i] = 0;
        }

        running = true;

        newApple();

        timer.stop();

        timer = new Timer(speed, this);
        timer.start();

        repaint();
    }

    @Override
    public void keyReleased(KeyEvent e) {
    }

    @Override
    public void keyTyped(KeyEvent e) {
    }

    // Main method
    public static void main(String[] args) {

        JFrame frame = new JFrame();

        App gamePanel = new App();

        frame.add(gamePanel);

        frame.setTitle("Crazy Snake Game 🐍");

        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        frame.setResizable(false);

        frame.pack();

        frame.setVisible(true);

        frame.setLocationRelativeTo(null);
    }
}