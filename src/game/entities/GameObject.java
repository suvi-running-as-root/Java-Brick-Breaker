package game.entities;

import java.awt.Graphics2D;

public abstract class GameObject {
    protected int x, y;

    public GameObject(int x, int y) {
        this.x = x;
        this.y = y;
    }

    // Abstract method that needs to be implemented by subclasses
    public abstract void draw(Graphics2D g);
}
