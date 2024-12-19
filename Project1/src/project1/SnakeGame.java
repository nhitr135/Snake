/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Main.java to edit this template
 */
package project1;
import java.awt.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.*;
import java.io.*;
/**
 *
 * @author sophi
 */
public class SnakeGame extends JPanel implements ActionListener, KeyListener {
    int boardWidth;
    int boardHeight;
    int tileSize = 30;

    Tile snakeHead;
    LinkedList snakeBody;

    Tile food;
    Random random;

    int velocityX;
    int velocityY;
    Timer gameLoop;

    
    boolean gameOver = false;
    
    private int highScore = 0;
    private final String highscoreFile = "highscore.txt";

    public SnakeGame(int boardWidth, int boardHeight) {
        this.boardWidth = boardWidth;
        this.boardHeight = boardHeight;
        setPreferredSize(new Dimension(this.boardWidth, this.boardHeight));
        setBackground(Color.black);
        addKeyListener(this);
        setFocusable(true);
        
        loadHighScore();
        initializeGame();
    }
    private void loadHighScore() {
        try (BufferedReader in = new BufferedReader(new FileReader(highscoreFile))) {
        String line = in.readLine();
        if (line != null) {
            highScore = Integer.parseInt(line);
        }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }		
    private void initializeGame() {
        snakeHead = new Tile(10, 5);
        snakeBody = new LinkedList();
        food = new Tile(15, 10);
        random = new Random();
        placeFood();
        velocityX = 1;
        velocityY = 0;           
        gameOver = false;           		
    	gameLoop = new Timer(300, this); 
        gameLoop.start();     
}	
    
    public void paintComponent(Graphics g) {
        super.paintComponent(g);
	draw(g);
	}

    public void draw(Graphics g) {
        for(int i = 0; i < boardWidth / tileSize; i++) {
            g.drawLine(i*tileSize, 0, i*tileSize, boardHeight);
            g.drawLine(0, i*tileSize, boardWidth, i*tileSize); 
        }
        g.setColor(Color.red);
        g.fill3DRect(food.x * tileSize, food.y * tileSize, tileSize, tileSize, true);       
        g.setColor(Color.green);
        g.fill3DRect(snakeHead.x * tileSize, snakeHead.y * tileSize, tileSize, tileSize, true);
        
        Node current = snakeBody.head;
        while (current != null) {
            Tile snakePart = current.data;
            g.fill3DRect(snakePart.x * tileSize, snakePart.y * tileSize, tileSize, tileSize, true);
            current = current.next;
        }
        g.setFont(new Font("Arial", Font.PLAIN, 16));
        g.setColor(Color.YELLOW);  
        g.drawString("Score: " + String.valueOf(snakeBody.size()), tileSize - 16, tileSize);
        g.drawString("High Score: " + String.valueOf(highScore), tileSize - 16, tileSize + 20);

	}

    public void placeFood(){
        food.x = random.nextInt(boardWidth/tileSize);
	food.y = random.nextInt(boardHeight/tileSize);
	}

    public void move() {
        Tile previousPosition = new Tile(snakeHead.x, snakeHead.y);
        if (collision(snakeHead, food)) {
            snakeBody.add(new Tile(food.x, food.y));
            placeFood();
    }           
        snakeHead.x += velocityX;
        snakeHead.y += velocityY;
    
        Tile currentPosition = previousPosition; 
        Node current = snakeBody.head;

        while (current != null) {
            Tile temp = current.data; 
            current.data = currentPosition; 
            currentPosition = temp; 
            current = current.next;
    }             
        for (int i = 0; i < snakeBody.size(); i++) {
            if (collision(snakeHead, snakeBody.get(i))) {
                gameOver = true;
            }
        }    
        if (snakeHead.x*tileSize < 0 || snakeHead.x*tileSize > boardWidth || 
            snakeHead.y*tileSize < 0 || snakeHead.y*tileSize > boardHeight ) { 
            gameOver = true;
        }
    }

    public boolean collision(Tile tile1, Tile tile2) {
        return tile1.x == tile2.x && tile1.y == tile2.y;
    }

    @Override
    public void actionPerformed(ActionEvent e) { 
        move();
        repaint();
        if (gameOver) {
            gameLoop.stop();
            if (snakeBody.size() > highScore) {
                highScore = snakeBody.size();
                saveHighScore();  
            }
            int response = JOptionPane.showConfirmDialog(this, "Game Over! Score: " 
                    + snakeBody.size() + "\nDo you want to play again?",
                    "Game Over", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (response == JOptionPane.YES_OPTION) {
                initializeGame();
            } else {
                System.exit(0);
            }
        }
    }  

    private void saveHighScore() {
        try (PrintWriter out = new PrintWriter(new FileWriter(highscoreFile))) {
	    out.println(highScore);
	} catch (IOException e) {
	    e.printStackTrace();
	}
   }
		 
	@Override
    public void keyPressed(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_UP && velocityY != 1) {
            velocityX = 0;
            velocityY = -1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_DOWN && velocityY != -1) {
            velocityX = 0;
            velocityY = 1;
        }
        else if (e.getKeyCode() == KeyEvent.VK_LEFT && velocityX != 1) {
            velocityX = -1;
            velocityY = 0;
        }
        else if (e.getKeyCode() == KeyEvent.VK_RIGHT && velocityX != -1) {
            velocityX = 1;
            velocityY = 0;
        }
    }

    @Override
    public void keyTyped(KeyEvent e) {}

    @Override
    public void keyReleased(KeyEvent e) {}
   
}