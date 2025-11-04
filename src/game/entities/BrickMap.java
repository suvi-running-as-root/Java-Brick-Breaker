package game.entities;

import java.awt.*;
import java.awt.image.BufferedImage;
import javax.imageio.ImageIO;
import java.io.File;
import java.io.IOException;

public class BrickMap {
    public int[][] brickLayout;
    public int brickWidth;
    public int brickHeight;
    private BufferedImage brickImage;

    private int[] moveOffsets;       // Array to store the offset for each row
    private boolean[] moveRight;      // Array to track direction for each row
    private static final int MOVE_STEP = 5; // Speed of movement

    public BrickMap(int rows, int columns, int screenWidth, int screenHeight, int level) {
        // Set brick dimensions according to the screen size and desired density
        brickWidth = (int) (screenWidth * 0.7 / columns);
        brickHeight = (int) (screenHeight * 0.2 / rows);

        brickLayout = getLevelLayout(level, rows, columns);

        // Initialize movement offsets and directions for each row
        moveOffsets = new int[rows];
        moveRight = new boolean[rows];
        for (int i = 0; i < rows; i++) {
            moveOffsets[i] = 0;
            moveRight[i] = i % 2 == 0; // Alternate directions for each row
        }

        // Load the brick image
        try {
            brickImage = ImageIO.read(new File("src/Block.jpeg"));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private int[][] getLevelLayout(int level, int rows, int columns) {
        int[][] layout = new int[rows][columns];
        switch(level) {
            case 1:
                // Level 1: Basic grid pattern
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        layout[i][j] = 1;
                    }
                }
                break;
            case 2:
                // Level 2: Checkerboard pattern
                for (int i = 0; i < rows; i++) {
                    for (int j = 0; j < columns; j++) {
                        layout[i][j] = (i + j) % 2;
                    }
                }
                break;
            case 3:
                // Level 3: Diamond pattern
                for (int i = 0; i < rows; i++) {
                    int start = Math.abs((rows - 1) / 2 - i);
                    int end = columns - start;
                    for (int j = start; j < end; j++) {
                        layout[i][j] = 1;
                    }
                }
                break;
        }
        return layout;
    }
    public void setBrickValue(int value, int row, int col) {
        if (row >= 0 && row < brickLayout.length && col >= 0 && col < brickLayout[0].length) {
            brickLayout[row][col] = value;
        }
    }


    public void draw(Graphics2D g, int level) {
        for (int i = 0; i < brickLayout.length; i++) {
            for (int j = 0; j < brickLayout[0].length; j++) {
                if (brickLayout[i][j] > 0) {
                    // Calculate the x-position with the moving offset for each row in level 3
                    int xPosition = j * brickWidth + 80 + (level == 3 ? moveOffsets[i] : 0);
                    int yPosition = i * brickHeight + 50;

                    g.drawImage(brickImage, xPosition, yPosition, brickWidth, brickHeight, null);
                    g.setStroke(new BasicStroke(4));
                    g.setColor(Color.BLACK);
                    g.drawRect(xPosition, yPosition, brickWidth, brickHeight);
                }
            }
        }
    }

    public void update(int level, int screenWidth) {
        if (level == 3) {
            int maxOffset = (int)(screenWidth * 0.1); // Define max movement range based on screen width

            for (int i = 0; i < moveOffsets.length; i++) {
                // Move in the current direction
                if (moveRight[i]) {
                    moveOffsets[i] += MOVE_STEP;
                    if (moveOffsets[i] > maxOffset) { // Reached the max range
                        moveRight[i] = false; // Switch direction to left
                    }
                } else {
                    moveOffsets[i] -= MOVE_STEP;
                    if (moveOffsets[i] < -maxOffset) { // Reached the max range in the left
                        moveRight[i] = true; // Switch direction to right
                    }
                }
            }
        }
    }
}
