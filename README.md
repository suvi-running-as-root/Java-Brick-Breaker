# Brick Breaker Game 🧱

A simple classic **Brick Breaker** game built in **Java (Swing)**.  
You control a paddle to bounce a ball and break all the bricks.  
As you move up levels, the game gets faster — and at the final level, the bricks even start moving!

---

## 🎯 What the Game Does

- You move the paddle left and right using arrow keys.  
- The ball bounces off the walls, paddle, and bricks.  
- Each brick you break gives you points.  
- When you clear all bricks, you move to the next level.  
- If the ball falls below the paddle, it’s game over — you can restart.  
- The game includes sound effects and increasing difficulty.

---

## 🧠 How It’s Built

The project is written fully in **Java** using **Swing** for graphics.  
It’s organized with Object-Oriented Programming (OOP) principles:

| Class | Purpose |
|--------|----------|
| **Main** | Starts the game and window. |
| **GamePanel** | Handles the game loop, drawing, and object updates. |
| **BrickMap** | Manages the bricks’ layout and movement for each level. |
| **Ball** | Handles ball movement and collisions. |
| **Paddle** | Handles player control and movement. |
| **GameObject** | A base class shared by the ball and paddle. |

---

## 🧩 Requirements

You’ll need:
- **Java JDK 11 or newer**
- **VS Code** with the **Java Extension Pack** (recommended)
- Or, you can also run it in **IntelliJ IDEA**

No extra libraries are required — it runs on pure Java.

---

## ▶️ How to Run (VS Code)

1. **Clone this repo**
   ```bash
   git clone https://github.com/suvi-running-as-root/Brick-Breaker.git
   cd BrickBreakerGame
