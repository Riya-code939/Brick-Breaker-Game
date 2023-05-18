import org.w3c.dom.css.Rect;

import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import javax.swing.Timer;

public class GamePlay extends JPanel implements KeyListener, ActionListener
{
    private  boolean play = false;
    private int score = 0;
    private int totalBricks = 21;
    private Timer timer;
    private int delay=0;
    private int playerX = 310;
    private int ballposX= 120;
    private int ballposY = 350;
    private int ballXdir = -1;
    private int ballYdir= -2;

    private  MapGenerator map;


    public GamePlay()
    {
        map= new MapGenerator(3,7);
        addKeyListener(this);
        setFocusable(true);
        setFocusTraversalKeysEnabled(false);
        timer= new Timer(delay,this);
        timer.start();

    }

    public void paint(Graphics g)
    {
        //adding background
        g.setColor(Color.black);
        g.fillRect(1,1,692,592);

        //drawing maps-->calling draw() function.
        map.draw((Graphics2D)g);


        //setting borders
        g.setColor(Color.yellow);
        g.fillRect(0,0,3,592);
        g.fillRect(0,0,692,3);
        g.fillRect(691,0,3,592);

        //scores
        g.setColor(Color.white);
        g.setFont(new Font("serif",Font.BOLD,25));
        g.drawString(""+score,590,30);

        //setting paddle
        g.setColor(Color.green);
        g.fillRect(playerX,550,100,8);

        //setting the ball
        g.setColor(Color.yellow);
        g.fillOval(ballposX,ballposY,20,20);

        //code to show you won the game..
        if(totalBricks <= 0)
        {
            play = false;
            ballXdir = 0;
            ballYdir = 0;
            g.setColor(Color.red);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("YOU WON", 260,300);

            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press Enter to Restart",230,350);
        }

        //code when ball falls down and game over.
        if(ballposY > 570)
        {
            play = false;
            ballXdir=0;
            ballYdir=0;
            g.setColor(Color.red);
            g.setFont(new Font("serif",Font.BOLD,30));
            g.drawString("Game Over, Scores: ",190,300);

            g.setFont(new Font("serif",Font.BOLD,20));
            g.drawString("Press Enter to Restart",230,350);
        }

        g.dispose();
    }
    @Override
    public void actionPerformed(ActionEvent e)
    {
        timer.start();

        //code to move the ball
        if(play)
        {
            //code to detect intersection of ball with paddle.
            if(new Rectangle(ballposX,ballposY,20,20).intersects(new Rectangle(playerX,550,100,8)))
            {
                ballYdir= -ballYdir;
            }

            //iterating through every brick
         A:     for(int i=0; i < map.map.length;i++)
                {
                    for(int j=0; j < map.map[0].length;j++)
                    {
                        int brickX = j*map.brickWidth +80;
                        int brickY = i*map.brickHeight + 50;
                        int brickWidth= map.brickWidth;
                        int brickHeight = map.brickHeight;

                        Rectangle rect = new Rectangle(brickX,brickY,brickWidth,brickHeight);
                        Rectangle ballRect= new Rectangle(ballposX,ballposY,20,20);
                        Rectangle brickRect = rect;
                        if(ballRect.intersects(brickRect))
                        {
                            map.setBrickValue(0,i,j);
                            totalBricks--;
                            score +=5;

                            if(ballposX + 19 <= brickRect.x || ballposX+1 >= brickRect.x + brickRect.width)
                            {
                                ballXdir = -ballXdir;
                            }
                            else {
                                ballYdir = -ballYdir;
                            }
                            break A;
                     }
                }
            }
            ballposX += ballXdir;
            ballposY += ballYdir;

            //left border
            if(ballposX < 0)
            {
                ballXdir = -ballXdir;

            }
            //top border
            if(ballposY < 0)
            {
                ballYdir = -ballYdir;

            }
            //right border
            if(ballposX > 670)
            {
                ballXdir = -ballXdir;
            }

        }
        repaint();         //--recalls the paint method

    }

    @Override
    public void keyTyped(KeyEvent e) {

    }

    @Override
    public void keyPressed(KeyEvent e)
    {

        //detecting right key
        if(e.getKeyCode() == KeyEvent.VK_RIGHT )
        {
            if(playerX >= 600)
            {
                playerX = 600;
            }
            else {
                moveRight();
            }

        }
        
        if(e.getKeyCode() == KeyEvent.VK_LEFT)
        {
            if(playerX < 10)
            {
                playerX = 10;
            }
            else {
                moveLeft();
            }
        }

        //code when ENTER key is pressed to restart the game..
        if(e.getKeyCode() == KeyEvent.VK_ENTER){
            if(!play)
            {
                play = true;
                ballposX = 120;
                ballposY = 350;
                ballXdir = -1;
                ballYdir = -2;
                playerX = 310;
                score=0;
                totalBricks= 21;
                map = new MapGenerator(3,7);


                repaint();
            }
        }


    }

    public void moveLeft()
    {
        play = true;
        playerX-= 20;
    }

    public void moveRight()
    {
        play = true;
        playerX+= 20;
    }


    @Override
    public void keyReleased(KeyEvent e) {

    }
}
