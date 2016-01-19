import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Random;

import javax.swing.*;

public class Pong extends JFrame
{
	static Random rand = new Random();
	
	Font font = new Font("Calibri", Font.BOLD, 20);
	Font font2 = new Font("Calibri", Font.BOLD, 16);
	
	final String SPACE = "       ";

	final int PANEL_WIDTH = 800;
	final int PANEL_HEIGHT = 500;
	
	final int MIN_BALL_SPEED = 3;
	final int MAX_BALL_SPEED = 7;
	final int MIN_BALL_SLOPE = 2;
	final int MAX_BALL_SLOPE = 4;
	
	private JPanel mainPanel = new GamePanel();
	
	private int pointsToWin = 7;
	
	private int playerScore = 0;
	private int computerScore = 0;
	
	private JPanel scorePanel = new JPanel();
	private JLabel playerScoreLabel = new JLabel("Player - " + playerScore);
	private JLabel computerScoreLabel = new JLabel(computerScore + " - Computer");
	private JLabel infoLabel = new JLabel(SPACE + "Welcome!", SwingConstants.CENTER);
	
	private JPanel instructionsPanel = new JPanel();
	private JLabel instructionsLabel = new JLabel("Space Bar - Start/Pause     Up/Down Arrow Keys - Move Paddle");
	
	private int widthBuffer = 30;
	
	private int ballSize = 13;
	
	private int ballXPos = (PANEL_WIDTH / 2) - (ballSize / 2);
	private int ballYPos = PANEL_HEIGHT / 2 - (ballSize / 2);
	private int ballXIncrementer;
	private int ballYIncrementer;
	
	private int paddleWidth = 12;
	private int paddleHeight = 100;
	
	private int playerPaddleXPos = widthBuffer;
	private int playerPaddleYPos = (PANEL_HEIGHT / 2) - (paddleHeight / 2);
	
	private int compPaddleXPos = PANEL_WIDTH - paddleWidth - widthBuffer;
	private int compPaddleYPos = (PANEL_HEIGHT / 2) - (paddleHeight / 2);
	
	private int playerPaddleIncrementer = 5;
	private int compPaddleIncrementer = 4;
	
	boolean paused = false;
	boolean gameOver = true;

	private int countdown = 4;

	Timer timer = new Timer(10, new BallListener());

	Timer countdownTimer = new CountdownTimer(1000, new CountdownListener());
	
	Timer compPaddleTimer = new Timer(5, new ComputerPaddleListener());
	
	public synchronized void setBallXPos(int pos){ this.ballXPos  = pos; }
	public synchronized int getBallXPos(){ return ballXPos; }
	public synchronized void setBallYPos(int pos){ this.ballYPos  = pos; }
	public synchronized int getBallYPos(){ return ballYPos; }

	public synchronized void setPlayerPaddleXPos(int pos){ this.playerPaddleXPos = pos; }
	public synchronized int getPlayerPaddleXPos(){ return playerPaddleXPos; }
	public synchronized void setPlayerPaddleYPos(int pos){ this.playerPaddleYPos = pos; }
	public synchronized int getPlayerPaddleYPos(){ return playerPaddleYPos; }
	
	public synchronized void setCompPaddleXPos(int pos){ this.compPaddleXPos = pos; }
	public synchronized int getCompPaddleXPos(){ return compPaddleXPos; }
	public synchronized void setCompPaddleYPos(int pos){ this.compPaddleYPos = pos; }
	public synchronized int getCompPaddleYPos(){ return compPaddleYPos; }

		
	public static void main(String args[])
	{
		new Pong();
	}
	
	Pong()
	{
		int direction = rand.nextInt(2);
		if (direction == 0)
			ballXIncrementer = (rand.nextInt(MAX_BALL_SPEED - MIN_BALL_SPEED) + MIN_BALL_SPEED);
		else 
			ballXIncrementer = -(rand.nextInt(MAX_BALL_SPEED - MIN_BALL_SPEED) + MIN_BALL_SPEED);
		
		direction = rand.nextInt(2);
		if(direction == 0)
			ballYIncrementer = (rand.nextInt(MAX_BALL_SLOPE - MIN_BALL_SLOPE) + MIN_BALL_SLOPE);
		else
			ballYIncrementer = -(rand.nextInt(MAX_BALL_SLOPE - MIN_BALL_SLOPE) + MIN_BALL_SLOPE);
		
		this.setTitle("Pong");
		this.setSize(PANEL_WIDTH, PANEL_HEIGHT);
		this.setResizable(false);
		this.setDefaultCloseOperation(EXIT_ON_CLOSE);	
		this.addKeyListener(new PaddleListener());
		this.setLayout(new BorderLayout());
		
		playerScoreLabel.setFont(font);
		playerScoreLabel.setForeground(Color.WHITE);
		computerScoreLabel.setFont(font);
		computerScoreLabel.setForeground(Color.WHITE);
		infoLabel.setFont(font);
		infoLabel.setForeground(Color.WHITE);
		
		scorePanel.setBackground(Color.BLACK);
		scorePanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		scorePanel.setLayout(new BorderLayout());
		scorePanel.add(playerScoreLabel, BorderLayout.WEST);
		scorePanel.add(infoLabel, BorderLayout.CENTER);
		scorePanel.add(computerScoreLabel, BorderLayout.EAST);
		this.add(scorePanel, BorderLayout.NORTH);
		
		mainPanel.setBackground(Color.BLACK);
		this.add(mainPanel, BorderLayout.CENTER);

		instructionsLabel.setFont(font2);
		instructionsLabel.setForeground(Color.WHITE);
		instructionsPanel.setBorder(BorderFactory.createLineBorder(Color.WHITE));
		instructionsPanel.add(instructionsLabel);
		instructionsPanel.setBackground(Color.BLACK);
		this.add(instructionsPanel, BorderLayout.SOUTH);

		this.setLocationRelativeTo(null);
		this.setVisible(true);
	}
	
	class ComputerPaddleListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			//change the computers paddle position
			if (ballYPos < compPaddleYPos)
				if (compPaddleYPos >= compPaddleIncrementer)
					compPaddleYPos -= compPaddleIncrementer;
			if (ballYPos > compPaddleYPos + paddleHeight)
				if (compPaddleYPos <= mainPanel.getHeight() - compPaddleIncrementer - paddleHeight)
					compPaddleYPos += compPaddleIncrementer;
			
			mainPanel.repaint();
		}
		
	}
	
	class CountdownListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent arg0) 
		{
			if (countdown > 1)
			{
				countdown--;
			}
			else
			{
				countdownTimer.stop();
				countdown = 5;
				infoLabel.setText(SPACE + "Have Fun!");
				timer.start();
				compPaddleTimer.start();
			}
			
			mainPanel.repaint();
	
		}
		
	}
	
	class BallListener implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			
			//change the balls position
			setBallXPos(getBallXPos() + ballXIncrementer);
			setBallYPos(getBallYPos() + ballYIncrementer);
			
			//if it hits left paddle
			if (getBallXPos() <= widthBuffer + paddleWidth && getBallXPos() > widthBuffer && getBallYPos() >= getPlayerPaddleYPos() && getBallYPos() <= getPlayerPaddleYPos() + paddleHeight)
			{
				if (Math.abs(ballXIncrementer) > MIN_BALL_SPEED && Math.abs(ballXIncrementer) < MAX_BALL_SPEED)	//set speed min and max
					ballXIncrementer = (rand.nextInt(3) - 1) + (ballXIncrementer * -1);	
				else if (Math.abs(ballXIncrementer) >= MAX_BALL_SPEED)
					ballXIncrementer = (ballXIncrementer * -1) - 2; //decrease the speed
				else
					ballXIncrementer = (ballXIncrementer * -1) + 2; //increase the speed
				
				setBallXPos(getBallXPos() + 2);
			}
			
			//if it hits right paddle
			if (getBallXPos() >= mainPanel.getWidth() - widthBuffer - paddleWidth - MAX_BALL_SPEED && getBallXPos() < PANEL_WIDTH - widthBuffer - paddleWidth + ballXIncrementer && getBallYPos() >= getCompPaddleYPos() && getBallYPos() <= getCompPaddleYPos() + paddleHeight)
			{
				if (Math.abs(ballXIncrementer) > MIN_BALL_SPEED && Math.abs(ballXIncrementer) < 8)	//set speed min and max
					ballXIncrementer = (rand.nextInt(3) - 1) + (ballXIncrementer * -1);
				else if (Math.abs(ballXIncrementer) >= MAX_BALL_SPEED)
					ballXIncrementer = (ballXIncrementer * -1) + 2; //decrease the speed
				else
					ballXIncrementer = (ballXIncrementer * -1) - 2; //increase the speed
				
				setBallXPos(getBallXPos() - 2);
			}
			
			//if hits the top
			if (getBallYPos() <= 0)
			{
				if (Math.abs(ballYIncrementer) > MIN_BALL_SLOPE && Math.abs(ballYIncrementer) < MAX_BALL_SLOPE) //set slope min and max
					ballYIncrementer = (ballYIncrementer * -1) + (rand.nextInt(3) - 1);
				else if (Math.abs(ballYIncrementer) >= MAX_BALL_SLOPE)
					ballYIncrementer = (ballYIncrementer * -1) - 1; //decrease the slope
				else
					ballYIncrementer = (ballYIncrementer * -1) + 1; //increase the slope
			
				setBallYPos(getBallYPos() + 5);
			}
			
			//if it hits the bottom
			if (ballYPos >= mainPanel.getHeight() - ballSize)
			{
				if (Math.abs(ballYIncrementer) > MIN_BALL_SLOPE && Math.abs(ballYIncrementer) < MAX_BALL_SLOPE) //set slope min and max
					ballYIncrementer = (ballYIncrementer * -1) + (rand.nextInt(3) - 1);
				else if (Math.abs(ballYIncrementer) >= MAX_BALL_SLOPE)
					ballYIncrementer = (ballYIncrementer * -1) + 1; //decrease the slope
				else
					ballYIncrementer = (ballYIncrementer * -1) - 1; //increase the slope
				
				setBallYPos(getBallYPos() - 5);
			}
			
			//check if ball goes out of bounds
			if (getBallXPos() < 0) //computer gets a point
			{
				paddleUp.stop();
				paddleDown.stop();
				timer.stop();
				resetBoard();
				computerScore++;
				computerScoreLabel.setText(computerScore + " - Computer");
				
				if (!checkForLoss())
				{
					infoLabel.setText(getComputerScoredMessage());
					countdownTimer.start();
				}
				else
				{
					gameOver = true;
					infoLabel.setText(SPACE + "You Lost.");
				}
			}
			if (getBallXPos() > mainPanel.getWidth()) //player gets a point
			{
				paddleUp.stop();
				paddleDown.stop();
				timer.stop();
				resetBoard();
				playerScore++;
				playerScoreLabel.setText("Player - " + playerScore);
				
				if (!checkForWin())
				{
					infoLabel.setText(getPlayerScoredMessage());
					countdownTimer.start();
				}
				else
				{
					gameOver = true;
					infoLabel.setText(SPACE + "You've Won!");
				}
			}
			
			mainPanel.repaint();
		}
		
	}
	
	public void resetBoard()
	{
		setBallXPos((PANEL_WIDTH / 2) - (ballSize / 2));
		setBallYPos((PANEL_HEIGHT / 2) - (ballSize / 2));
		
		int direction = rand.nextInt(2);
		if (direction == 0)
			ballXIncrementer = (rand.nextInt(MAX_BALL_SPEED - MIN_BALL_SPEED) + MIN_BALL_SPEED);
		else 
			ballXIncrementer = -(rand.nextInt(MAX_BALL_SPEED - MIN_BALL_SPEED) + MIN_BALL_SPEED);
		
		direction = rand.nextInt(2);
		if(direction == 0)
			ballYIncrementer = (rand.nextInt(MAX_BALL_SLOPE - MIN_BALL_SLOPE) + MIN_BALL_SLOPE);
		else
			ballYIncrementer = -(rand.nextInt(MAX_BALL_SLOPE - MIN_BALL_SLOPE) + MIN_BALL_SLOPE);
		
		setCompPaddleYPos((PANEL_HEIGHT / 2) - (paddleHeight / 2));
		setPlayerPaddleYPos((PANEL_HEIGHT / 2) - (paddleHeight / 2));
	}
	
	public boolean checkForWin()
	{
		if (playerScore == pointsToWin)
			return true;
		
		return false;
	}
	
	public boolean checkForLoss()
	{
		if (computerScore == pointsToWin)
			return true;
		
		return false;
	}
	
	class CountdownTimer extends Timer
	{

		public CountdownTimer(int interval, ActionListener listener) {
			super(interval, listener);
			this.setInitialDelay(0);
			this.setDelay(interval);
		}
		
	}
	
	Timer paddleUp = new PaddleMoveUpTimer(15, new PaddleUp());
	Timer paddleDown = new PaddleMoveDownTimer(15, new PaddleDown());
	
	class PaddleMoveUpTimer extends Timer
	{

		public PaddleMoveUpTimer(int interval, ActionListener listener) 
		{
			super(interval, listener);
			this.setInitialDelay(0);
			this.setDelay(interval);
		}
		
	}
	
	class PaddleMoveDownTimer extends Timer
	{

		public PaddleMoveDownTimer(int interval, ActionListener listener) 
		{
			super(interval, listener);
			this.setInitialDelay(0);
			this.setDelay(interval);
		}
		
	}
	
	class PaddleUp implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			if (getPlayerPaddleYPos() >= playerPaddleIncrementer)
				setPlayerPaddleYPos(getPlayerPaddleYPos() - playerPaddleIncrementer);
		}
		
	}
	
	class PaddleDown implements ActionListener
	{

		@Override
		public void actionPerformed(ActionEvent e) 
		{
			
			if (getPlayerPaddleYPos() <= mainPanel.getHeight() - playerPaddleIncrementer - paddleHeight)
				setPlayerPaddleYPos(getPlayerPaddleYPos() + playerPaddleIncrementer);
		}
		
	}
	
	class PaddleListener implements KeyListener
	{

		@Override
		public void keyPressed(KeyEvent e) 
		{
			int keyCode = e.getKeyCode();
			
			if(!gameOver)
				if (countdownTimer.isRunning() || timer.isRunning())
				{
					if (keyCode == KeyEvent.VK_SPACE)
					{
						countdownTimer.stop();
						compPaddleTimer.stop();
						timer.stop();
						paused = true;
						mainPanel.repaint();
						infoLabel.setText("");
					}
					if(timer.isRunning())
					{
						if (keyCode == KeyEvent.VK_UP)
						{
							paddleDown.stop();
							paddleUp.start();
						}
						if (keyCode == KeyEvent.VK_DOWN)
						{
							paddleUp.stop();
							paddleDown.start();
						}
						
						mainPanel.repaint();
					}
				}
				else
				{
					if (keyCode == KeyEvent.VK_SPACE)
					{
						countdown = 4;
						countdownTimer.start();
						paused = false;
						infoLabel.setText(SPACE + "Have Fun!");
					}
				}
			else
			{
				
				if (keyCode == KeyEvent.VK_SPACE)
				{
					playerScore = 0;
					computerScore = 0;
					playerScoreLabel.setText("Player - " + playerScore);
					computerScoreLabel.setText(computerScore + " - Computer");
					gameOver = false;
					infoLabel.setText(SPACE + "Have Fun!");
					countdownTimer.start();
				}
				
			}
			
		}

		@Override
		public void keyReleased(KeyEvent e) 
		{
			int keyCode = e.getKeyCode();

			if (keyCode == KeyEvent.VK_UP)
			{
				paddleUp.stop();
			}
			else if (keyCode == KeyEvent.VK_DOWN)
			{
				paddleDown.stop();
			}
			
		}

		@Override
		public void keyTyped(KeyEvent e) {}
		
	}
	
	class GamePanel extends JPanel {
        
		public void paintComponent(Graphics g) 
        {
	        super.paintComponent(g); 
	        
	        g.setColor(Color.GRAY);
	        g.fillRect((PANEL_WIDTH / 2 - 2), 0, 5, PANEL_HEIGHT);
	        
	        g.setColor(Color.WHITE);  
	        g.fillRect(getPlayerPaddleXPos(), getPlayerPaddleYPos(), paddleWidth, paddleHeight); 
	        
	        g.fillRect(getCompPaddleXPos(), getCompPaddleYPos(), paddleWidth, paddleHeight);  
	        
	        g.fillOval( getBallXPos(), getBallYPos(), ballSize, ballSize);
	        
	        if (countdownTimer.isRunning() && countdown > 0 && countdown <= 3)
	        {
		        g.setFont(new Font("Calibri", Font.BOLD, 72));
	        	g.drawString(countdown + "", PANEL_WIDTH / 2 - 20, PANEL_HEIGHT / 3);
	        }
	        else if (paused)
	        {
		        g.setFont(new Font("Calibri", Font.BOLD, 60));
	        	g.drawString("PAUSED", PANEL_WIDTH / 2 - PANEL_WIDTH / 8, PANEL_HEIGHT / 3);

		        g.setFont(new Font("Calibri", Font.BOLD, 18));
	        	g.drawString("PRESS THE SPACE BAR TO CONTINUE", PANEL_WIDTH / 3, PANEL_HEIGHT / 3 + 30);
	        }
	        else if (gameOver)
	        {

		        g.setFont(new Font("Calibri", Font.BOLD, 22));
	        	g.drawString("PRESS THE SPACE BAR TO", PANEL_WIDTH / 3 + 25, PANEL_HEIGHT / 3);
	        	g.drawString("BEGIN A NEW GAME", PANEL_WIDTH / 3 + 55, PANEL_HEIGHT / 3 + 30);
	        }
	        
        }
        
	}
	
	public String getPlayerScoredMessage()
	{
		String s = "";
		int num = 0;
		
		if (playerScore < 3)
			num = rand.nextInt(2);
		else if (playerScore >= 3 && playerScore < 6)
			num = rand.nextInt(4);
		else
			num = rand.nextInt(6);
		
		switch (num)
		{
		case 0: s = "Good Job!"; break;
		case 1: s = "Nice Score!"; break;
		case 2: s = "GOAAAAAALLLLL!!"; break;
		case 3: s =  playerScore + " down only " + (7 - playerScore) + " to go!"; break;
		case 4: s = "You've got " + playerScore + " points already!"; break;
		case 5: s = "You've Nearly Won!"; break;
		case 6: s = "Keep Going! You're Nearly There!"; break;
		}
		
		s = "     " + s;
		
		return s;
	}
	
	public String getComputerScoredMessage()
	{
		String s = "";
		int num = 0;

		if (computerScore < 3)
			num = rand.nextInt(2);
		else if (computerScore >= 3 && computerScore < 5)
			num = rand.nextInt(4);
		else
			num = rand.nextInt(6);
		
		switch (num)
		{
		case 0: s = "That's not how you win..."; break;
		case 1: s = "Oops!"; break;
		case 2: s = "Thats not good!"; break;
		case 3: s = "Need some help?"; break;
		case 4: s = "It may be made of 1's and 0s but its score sure isnt!"; break;
		case 5: s = "You're going to lose soon..."; break;
		case 6: s = "Uh, oh..."; break;
		}
		
		s = SPACE + s;
		
		return s;
	}
	
	
	
}
