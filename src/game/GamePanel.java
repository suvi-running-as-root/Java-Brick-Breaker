package game;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import javax.sound.sampled.*;
import java.io.IOException;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import game.entities.BrickMap;
import java.util.Random;


public class GamePanel extends JPanel implements KeyListener, ActionListener {

    private int currentLevel = 1; // Start at level 1
    private final int maxLevel = 3; // Total number of levels

    private static final int MAX_SPEED = 5; // Maximum speed of the ball
    private static final int SPEED_INCREMENT = 1;

    private boolean isPlaying = true;
    private int playerScore = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay = 15; // Timer delay for smoother gameplay

    private int screenWidth;
    private int screenHeight;
    private int playerWidth;
    private int playerHeight;
    private int ballSize;
    private int playerX;
    private int ballPosX;
    private int ballPosY;
    private int ballXDir = -1;
    private int ballYDir = -2;

    private BufferedImage backgroundImage;

    private BrickMap brickMap;
    private int speedIncreaseCount = 0;

    private boolean leftPressed = false;
    private boolean rightPressed = false;

    public GamePanel(int width, int height) {
        this.screenWidth = width;
        this.screenHeight = height;

        // Initialize sizes based on screen dimensions
        this.playerWidth = (int) (width * 0.15);
        this.playerHeight = (int) (height * 0.02);
        this.ballSize = (int) (width * 0.03);

        resetGame();

        brickMap = new BrickMap(3, 7, screenWidth, screenHeight, currentLevel); // Pass dimensions to BrickMap for scaling

        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);

        timer = new Timer(delay, this);
        timer.start();

        try {
            backgroundImage = ImageIO.read(new File("C:\\Users\\Vishwanath BK\\JavaShi\\LABPROGS\\CP3\\src\\BGs.jpg")); // Update the path to your image file
        } catch (IOException e) {
            e.printStackTrace(); // Handle the error if the image can't be loaded
        }
    }

    @Override
    public void paint(Graphics g) {
        // Draw the background image
        if (backgroundImage != null) {
            g.drawImage(backgroundImage, 0, 0, screenWidth, screenHeight, null);
        } else {
            // Fallback color in case the image is not loaded
            g.setColor(new Color(0xFFFFE0)); // Light Yellow
            g.fillRect(0, 0, screenWidth, screenHeight);
        }

        // Draw Bricks
        brickMap.draw((Graphics2D) g, currentLevel);

        // Draw Borders
        g.setColor(Color.BLACK);
        g.fillRect(0, 0, 3, screenHeight);
        g.fillRect(0, 0, screenWidth, 3);
        g.fillRect(screenWidth - 3, 0, 3, screenHeight);

        // Draw Paddle
        g.setColor(new Color(0x4169E1)); // Royal Blue
        g.fillRect(playerX, screenHeight - 50, playerWidth, playerHeight);

        // Draw Ball
        g.setColor(new Color(0xDC143C)); // Crimson
        g.fillOval(ballPosX, ballPosY, ballSize, ballSize);

        // Score
        g.setColor(Color.BLACK);
        g.setFont(new Font("MV Boli", Font.BOLD, 25));
        g.drawString("Score: " + playerScore, screenWidth - 150, 30);

        // Win/Lose Messages
        if (totalBricks <= 0) {
            if (currentLevel == maxLevel) {
                if (totalBricks == 0) {
                    isPlaying = false;  // Stop the ball movement after the final level
                    // Set color and font for the first message
                    playWinSound();
                    g.setColor(new Color(0x18182E));
                    g.setFont(new Font("MV Boli", Font.BOLD, 30));

// First message
                    String message1 = "Congratulations! You've completed all levels!";
                    FontMetrics metrics1 = g.getFontMetrics();
                    int message1Width = metrics1.stringWidth(message1);
                    int x1 = (screenWidth - message1Width) / 2;  // Center horizontally
                    int y1 = screenHeight / 2;  // Center vertically

                    g.drawString(message1, x1, y1);  // Draw the first message

// Set font for the second message
                    g.setFont(new Font("MV Boli", Font.BOLD, 20));

// Second message
                    String message2 = "Press 'R' to Restart or 'E' to Exit";
                    FontMetrics metrics2 = g.getFontMetrics();
                    int message2Width = metrics2.stringWidth(message2);
                    int x2 = (screenWidth - message2Width) / 2;  // Center horizontally
                    int y2 = screenHeight / 2 + 50;  // Position it below the first message

                    g.drawString(message2, x2, y2);  // Draw the second message

                    repaint();
                    g.dispose();
                    return;
                }
            }
            isPlaying = false;
            playWinSound();
            g.setColor(new Color(0x131323));
            g.setFont(new Font("MV Boli", Font.BOLD, 30));
            g.drawString("You Won, Score: " + playerScore, screenWidth / 4, screenHeight / 2);
            g.setFont(new Font("MV Boli", Font.BOLD, 20));
            g.drawString("Press Enter to Continue to Next Level.", screenWidth / 3, screenHeight / 2 + 50);
            repaint();
        } else if (ballPosY > screenHeight - 30) {
            isPlaying = false;
            playGameOverSound(); // Play game over sound when the ball falls
            g.setColor(Color.BLACK);
            g.setFont(new Font("MV Boli", Font.BOLD, 30));
            g.drawString("Game Over, Score: " + playerScore, screenWidth / 4, screenHeight / 2);
            g.setFont(new Font("MV Boli", Font.BOLD, 20));
            g.drawString("Press Enter to Restart", screenWidth / 3, screenHeight / 2 + 50);
        }

        g.dispose();
    }

    private void nextLevel() {
        currentLevel = (currentLevel % maxLevel) + 1;  // Loop back to level 1 after maxLevel
        resetGame();  // Reset the game for the new level
        repaint();    // Redraw the screen with the new level
    }


    @Override
    public void keyPressed(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = true;
        }

        if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = true;
        }

        if (arg0.getKeyCode() == KeyEvent.VK_ENTER) {
            if (!isPlaying) {
                if (totalBricks <= 0) {
                    nextLevel(); // Advance to the next level if the game is over and player won
                } else {
                    resetGame(); // Reset the game state if game over was from losing
                }
                repaint();
            }
        }
    }

    @Override
    public void actionPerformed(ActionEvent e) {
        if (isPlaying) {

            if (rightPressed && playerX + playerWidth + 20 < screenWidth) {
                playerX += 11; // Move paddle to the right
            }
            if (leftPressed && playerX - 20 > 0) {
                playerX -= 11; // Move paddle to the left
            }
            // Update ball position
            ballPosX += ballXDir;
            ballPosY += ballYDir;

            // Paddle collision detection
            if (new Rectangle(ballPosX, ballPosY, ballSize, ballSize).intersects(new Rectangle(playerX, screenHeight - 50, playerWidth, playerHeight))) {
                ballYDir = -ballYDir;
            }

            // Brick collision detection
            for (int i = 0; i < brickMap.brickLayout.length; i++) {
                for (int j = 0; j < brickMap.brickLayout[0].length; j++) {
                    if (brickMap.brickLayout[i][j] > 0) {
                        int brickX = j * brickMap.brickWidth + 80;
                        int brickY = i * brickMap.brickHeight + 50;
                        Rectangle brickRect = new Rectangle(brickX, brickY, brickMap.brickWidth, brickMap.brickHeight);
                        Rectangle ballRect = new Rectangle(ballPosX, ballPosY, ballSize, ballSize);

                        if (ballRect.intersects(brickRect)) {
                            brickMap.setBrickValue(0, i, j);
                            totalBricks--;
                            playerScore += 5;

                            // Play sound and increase speed
                            playHitSound();
                            increaseBallSpeed();  // Increment speed on hitting a brick

                            // Change direction based on the hit side
                            if (ballPosX + ballSize - 1 <= brickRect.x || ballPosX + 1 >= brickRect.x + brickRect.width) {
                                ballXDir = -ballXDir; // Horizontal collision
                            } else {
                                ballYDir = -ballYDir; // Vertical collision
                            }
                        }
                    }
                }
            }

            // Wall collision logic
            if (ballPosX < 0) {
                ballXDir = -ballXDir;
            }
            if (ballPosY < 0) {
                ballYDir = -ballYDir;
            }
            if (ballPosX > screenWidth - ballSize) {
                ballXDir = -ballXDir;
            }
            brickMap.update(currentLevel, screenWidth);
            repaint();
        }
    }

    private void resetGame() {
        isPlaying = true;
        playerScore = 0;

        // Initialize the brick map for the current level
        brickMap = new BrickMap(3, 7, screenWidth, screenHeight, currentLevel);

        // Count total bricks based on the current layout
        totalBricks = 0;
        for (int i = 0; i < brickMap.brickLayout.length; i++) {
            for (int j = 0; j < brickMap.brickLayout[0].length; j++) {
                if (brickMap.brickLayout[i][j] > 0) {
                    totalBricks++;
                }
            }
        }

        speedIncreaseCount = 0;
        playerX = screenWidth / 2 - playerWidth / 2;

        Random random = new Random();
        ballPosX = random.nextInt(screenWidth - ballSize);
        ballPosY = random.nextInt(screenHeight / 2) + screenHeight / 4;
        ballXDir = random.nextBoolean() ? 1 : -1;
        ballYDir = -2;
    }

    private void playHitSound() {
        playSound("src/sounds/Hit.wav");
    }

    private void playGameOverSound() {
        playSound("src/sounds/GameOver.wav");
    }

    private void playWinSound() {
        playSound("src/sounds/GameWin.wav");
    }

    private void playSound(String soundFilePath) {
        new Thread(() -> {
            try {
                File soundFile = new File(soundFilePath);
                AudioInputStream audioInputStream = AudioSystem.getAudioInputStream(soundFile);
                Clip clip = AudioSystem.getClip();
                clip.open(audioInputStream);
                clip.start();
            } catch (IOException | LineUnavailableException | UnsupportedAudioFileException ex) {
                ex.printStackTrace();
            }
        }).start();
    }

    private void increaseBallSpeed() {
        if (Math.abs(ballXDir) < MAX_SPEED) {
            ballXDir += (ballXDir > 0) ? SPEED_INCREMENT : -SPEED_INCREMENT;
        }
        if (Math.abs(ballYDir) < MAX_SPEED) {
            ballYDir += (ballYDir > 0) ? SPEED_INCREMENT : -SPEED_INCREMENT;
        }
    }

    private void moveRight() {
        if (playerX + playerWidth + 20 < screenWidth) {
            playerX += 45; // Move paddle to the right
        }
    }

    private void moveLeft() {
        if (playerX - 20 > 0) {
            playerX -= 45; // Move paddle to the left
        }
    }

    @Override
    public void keyReleased(KeyEvent arg0) {
        if (arg0.getKeyCode() == KeyEvent.VK_RIGHT) {
            rightPressed = false;  // Reset rightPressed to false
        }

        if (arg0.getKeyCode() == KeyEvent.VK_LEFT) {
            leftPressed = false;  // Reset leftPressed to false
        }

        // Handle 'R' and 'E' keys when isPlaying is false (game over or final level completed)
        if (!isPlaying) {
            if (arg0.getKeyCode() == KeyEvent.VK_R) {
                System.out.println("Restarting game from level 1...");
                currentLevel = 1; // Restart to level 1
                resetGame();      // Reset the game state
                repaint();
            } else if (arg0.getKeyCode() == KeyEvent.VK_E) {
                System.out.println("Exiting game...");
                System.exit(0);  // Exit the game
            }
        }
    }


    @Override
    public void keyTyped(KeyEvent arg0) {}
}