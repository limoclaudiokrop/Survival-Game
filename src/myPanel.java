import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Random;

//Class that constructs and runs the game
public class myPanel extends JPanel implements ActionListener{
	//Set width and height of the window to display game
    static final int S_Width=1200;
    static final int S_Height=600;
    static final int Game_unit_size=20;
    
    //Initialize timer and random
    Timer timer;
    Random random;
    
    //Initialize the player as member of agent class
    Agent player = new Agent('P', S_Width, S_Height, G_Size);
    
    //Initialize arrays to store aliens and food
    Agent aliens[];
    Food foods[];
    
    //Boolean to check when game ends
    boolean game_flag = false;
    static final int DELAY = 160;
    static final int G_Size=(S_Width*S_Height)/(Game_unit_size*Game_unit_size);
    
    //Initialize variable to keep track of game
    long begin_time;
    int days = 0;
    boolean pause= false;
    boolean combat = false;
    long combat_time;
    int intersectedAlien = 0;
    String combat_info;
    
    //Declare variable to store images of player, aliens, food and artifacts
    private BufferedImage image;
    private BufferedImage plant;
    private BufferedImage alien_x;
    private BufferedImage alien_y;
    private BufferedImage sword;
    
    //Constructor class
    myPanel(){
    	
    	//Initialize images from file
    	try {
    		//String filepath = "C:\Users\Hotfix tecs\Desktop\download.jpg";
    		image = ImageIO.read(new File("src/agent.jpg"));
    		plant = ImageIO.read(new File("src/plant.png"));
    		alien_x = ImageIO.read(new File("src/alien_x.jpg"));
    		alien_y = ImageIO.read(new File("src/alien_y.png"));
    		sword = ImageIO.read(new File("src/sword.jpg"));
    		
    	}catch (IOException ex) {
            // handle exception...
    		ex.printStackTrace();
       }
    	//Initialize variables for aliens and food
    	begin_time = System.currentTimeMillis();
    	aliens = new Agent[6];
    	foods = new Food[10];
    	
    	//Populate arrays of food and aliens 
    	for(int i =0; i < 6; i++) {
    		if(i < 3) {
    			aliens[i] = new Agent('Y', S_Width, S_Height, G_Size);
    		}else {
    			aliens[i] = new Agent('X', S_Width, S_Height, G_Size);
    		}
    		
    	}
    	for(int i =0; i < 5; i ++) {
    		foods[i] = new Food('H', S_Width, S_Height);
    	}
    	foods[5] = new Food('P', S_Width, S_Height);
    	foods[6] = new Food('P', S_Width, S_Height);
    	for(int i =6; i < 10; i++) {
    		foods[i] = new Food('A', S_Width, S_Height);
    	}
    	
    	//Set background and size of window to display game
        this.setPreferredSize(new Dimension(S_Width,S_Height));
        this.setBackground(Color.white);
        this.setFocusable(true);
        this.addKeyListener(new MyKey());
        random = new Random();
        Game_start();
    }
    
    //Method tp start game.
    public void Game_start() {
        //newfoodPosition();
        game_flag=true;
        timer=new Timer(DELAY,this);
        timer.start();
    }
    
    //method to paint characers in the canvas
    public void paintComponent(Graphics graphic) {
        super.paintComponent(graphic);
        draw(graphic);
    }
    
    //method to draw characters on canvas
    public void draw(Graphics graphic) {
    	
    	//Check if game is still on
        if(game_flag){
        	//Draw foods 
            for(int i =0; i < 10; i++) {
            	Food f = foods[i];
            	if(i <= 6) {
            		graphic.drawImage(plant, f.x, f.y, this);
            	}
            	if(i  >= 6) {
            		graphic.drawImage(sword, f.x, f.y, this);
            	}
            	
                
            }
            
            //Draw aliens
            graphic.setColor(Color.blue);
            for(int i =0; i < 6; i++) {
            	Agent alien = aliens[i];
            	if(alien.alive) {
            		if(alien.T == 'X') {
            			graphic.drawImage(alien_x, alien.x, alien.y, this);
            		}else {
            			graphic.drawImage(alien_y, alien.x, alien.y, this);
            		}
            		
            		//graphic.fillRect(alien.x,alien.y,Game_unit_size,Game_unit_size);
            	}
            	
            }
            
            //Draw player
            graphic.drawImage(image, player.x, player.y, this);
        	long endtime = System.currentTimeMillis();
        	
        	//Update number of days passed
        	if((endtime - begin_time)/1000 > 1) {
        		begin_time = endtime;
        		days ++;
        		player.health --;
        	}
        	
        	//Write title and status of player
           graphic.setFont(new Font("Times New Roman",Font.BOLD,20));
           FontMetrics font_me=getFontMetrics(graphic.getFont());
           String s = "Days: " + days + "    Health: "+player.health+"    Strenght: " +
        		   		player.strength + "    Intelligence: " + player.intelligence +
        		   		"    Charisma: "+ player.charisma;
           graphic.drawString(s,100,graphic.getFont().getSize());
           
           //Handle combat case
           if (pause) {
        	   if(combat) {
        		   //Indicate that player has collided with alien
        		   if((endtime- combat_time)/1000 > 3) {
        	           	pause = false;
        	           	combat = false;
                   }
        		   s = combat_info;
        		   //print string of combat
        		   graphic.drawString(s,100,graphic.getFont().getSize()+100);
        	   }else {
        		   //Provide more info of interaction with alien
        		   s = "INTERACT WITH ALIEN";
        		   graphic.drawString(s,100,graphic.getFont().getSize()+80);
        		   s = "ALIEN STATUS::  Charisma:  " + aliens[intersectedAlien].charisma +"   ";
        		   s += "Strength:  " + aliens[intersectedAlien].strength + "   ";
        		   s += "Intelligence:  " + aliens[intersectedAlien].intelligence + "   ";
        		   graphic.drawString(s,100,graphic.getFont().getSize()+100);
        		   s = "ACTIONS: Enter 1: All out War,  Enter 2: Run Away,  Enter 3: Chat";
        		   graphic.drawString(s,100,graphic.getFont().getSize()+120);
        	   }
        	   
           }
           
           

        }
        else{
        	//When game ends
        	  String s;
        	//check if player has won
        	if(days>= 48) {
     		  s = "You Win!!!! Score: " + player.intelligence;
	     	   }else {
	     		   s = "You Loose!!!";
	     	   }
        	//Write end game message depending on how game ended
        	graphic.setFont(new Font("Ink Free",Font.BOLD,20));
            FontMetrics font_me=getFontMetrics(graphic.getFont());
	     	   graphic.drawString(s,300,graphic.getFont().getSize()+300);
	        }
    }
   
    
    
    
    //Method to handle case of combat with alien
    public void war(int action) {
    	combat = true;
    	combat_time = System.currentTimeMillis();
    	//Get alien which has collided with player
    	Agent alien = aliens[intersectedAlien];
    	
    	//Handle case where player chooses combat
    	if(action == 1) {
    		//Check if alien is more powerful than player
    		if(alien.intelligence + alien.strength > player.strength + player.intelligence) {
    			player.health -= 10;
    			combat_info = "Going to combat: You loose";
    			game_flag = false;
    		}else {
    			//Alien is less powerful and player wins combat.
    			combat_info = "Going to combat: You Win";
    			player.health -= 3;
    			player.intelligence += 10;
    			alien.alive = false;
    		}
    		
    	}
    	//case where player opts to run away from alien
    	else if(action == 2) {
    		combat_info = "Running away from Alien";
    		player.strength -= 10;
    	}
    	
    	//case where player opts to chat with alien
    	else if(action == 3){
    		Random random = new Random();
    		int prob = random.nextInt(10);
    		//check if alien also chooses to chat
    		if(prob > alien.charisma && alien.T == 'X') {
    			//Alien has chosen to combat instead
    			if(alien.intelligence + alien.strength > player.strength + player.intelligence) {
        			//alien is more powerful and wins combat
    				player.health -= 10;
        			combat_info = "Going to combat: You loose";
        			game_flag = false;
        		}else {
        			//alien is less powerful and player wins combat
        			player.health -= 3;
        			player.intelligence += 10;
        			combat_info = "Going to combat: You Win";
        			alien.alive = false;
        		}
    		}else {
    			//alien has chosen to chat
    			alien.charisma += 1;
    			player.charisma += 1;
    			player.intelligence += 10;
    			combat_info = "Chatting with Alien";
    		}
    	}
    	aliens[intersectedAlien] = alien;
    }
    
    //Method to handle keyboard inputs from player
    //Handles movement of player and combat actions
    public class MyKey extends KeyAdapter{
        @Override
        public void keyPressed(KeyEvent e) {
            switch (e.getKeyCode()) {
            	//1 pressed, combat
            	case KeyEvent.VK_1:
            		if(pause) {
            			war(1);
            		}
            		break;
            	//3 pressed, chat
            	case KeyEvent.VK_3:
            		if(pause) {
            			war(3);
            		}
            		break;
            	//2 pressed, run away
            	case KeyEvent.VK_2:
            		if(pause) {
            			war(2);
            		}
            		break;
            	//arrow left, move left
                case KeyEvent.VK_LEFT:
                    player.x -= 10;
                    if(player.x < 0) {
                    	player.x = 0;
                    }
                    break;
                //arrow up, move up
                case KeyEvent.VK_UP:
                    player.y -= 10;
                    if(player.y < 0) {
                    	player.y = 0;
                    }
                    break;
                //arrow right, move right
                case KeyEvent.VK_RIGHT:
                    player.x += 10;
                    if(player.x > S_Width) {
                    	player.x = S_Width - G_Size;
                    }
                    break;
                //arrow down, move down
                case KeyEvent.VK_DOWN:
                    player.y += 10;
                    if(player.y > S_Height) {
                    	player.y = S_Height - G_Size;
                    }
                    break;
                
            }            
        }
    }
    
    //Method to get distance between pair of coordinates
    public int getDist(int x1, int x2, int y1, int y2) {
    	int difx = x1-x2;
    	int dify = y1 - y2;
    	//returns manhattan distance squared
    	return difx*difx + dify*dify;
    }
    
    //Method to move aliens around the canvas
    //Aliens move to the closest food
    public void moveAliens() {
    	//initalize array to store foods being searched
    	int[] arr = new int[10];
    	for(int i =0; i < 6; i++) {
    		Agent alien = aliens[i];
    		Food f = foods[0];
    		//get distance between alien and current food
    		int d = getDist(alien.x, foods[0].x, alien.y, foods[0].y);
    		int selected = 0;
    		//Find food closest which is not already being searched by another alien
    		for(int j =0; j < 10; j++) {
    			if(arr[j] == 1) {
    				continue;
    			}
    			int dis = getDist(alien.x, foods[j].x, alien.y, foods[j].y);
    			if(d > dis) {
    				f = foods[j];
    				d = dis;
    				selected = j;
    			}
    		}
    		arr[selected] = 1;
    		//update alien position to move towards food
    		if(alien.x - f.x > 5 ) {
    			alien.x = alien.x - 10;
    		}else if(alien.x - f.x < -5 ) {
    			alien.x = alien.x + 10;
    		}
    		int dif = alien.x - f.x;
    		dif = dif*dif;
    		if(dif < 30) {

        		if(alien.y - f.y > 0) {
        			alien.y = alien.y - 10;
        		}else if(alien.y < f.y) {
        			alien.y = alien.y + 10;
        		}
    		}
    		//update alien
    		aliens[i] = alien;
    	}
    }
    
    //Method to check collision between alien and food or artifact
    public void checkHits() {
    	Random random = new Random();
    	for(int i =0; i < 6; i++) {
    		Agent alien = aliens[i];
    		if(!alien.alive) {
    			continue;
    		}
    		//loop through all foods
    		for(int j =0; j < 10; j++) {
    			Food f = foods[j];
    			//check distance between alien and current food
    			int d = getDist(alien.x, f.x, alien.y, f.y);
    			
    			//check if alien collides with food
    			if(d < 100) {
    				//remove food and draw it in another place randomly
    				f.x = random.nextInt(S_Width);
    				f.y = random.nextInt(S_Height);
    				if(f.T == 'H') {
    					aliens[i].strength += 5;
    				}
    				if(f.T == 'A') {
    					aliens[i].intelligence += 5;
    				}
    				if(f.T == 'P') {
    					aliens[i].strength -= 5;
    				}
    			}
    			foods[j] = f;
    		}
    		
    	}
    }
    
    //Method to check collison between player and food
    public void checkPlayerHit() {
    	for(int i =0; i < 10; i++) {
    		Food f = foods[i];
    		//get distance between player and current food
    		int d = getDist(player.x, f.x, player.y, f.y);
    		//check if player collides with current food
    		if(d < 200) {
    			//player eats food and another food is created in another place
    			f.x = random.nextInt(S_Width);
				f.y = random.nextInt(S_Height);
				foods[i] = f;
    			if(f.T == 'H') {
					player.health += 5;
				}
				if(f.T == 'A') {
					player.intelligence += 5;
				}
				if(f.T == 'P') {
					player.health -= 5;
				}
    		}
    	}
    }
    
    //Method to check collision between alien and player
    public void checkCollision() {
    	for(int i =0; i < 6; i++) {
    		//Check if alien is not one already collided with
    		if (i == intersectedAlien) {
    			continue;
    		}
    		//No impact of player collides with dead alien
    		Agent alien = aliens[i];
    		if(!alien.alive) {
    			continue;
    		}
    		//get distance between player and current alien.
    		int d = getDist(player.x, alien.x, player.y, alien.y);
    		if(d < 200) {
    			pause = true;
    			intersectedAlien = i;
    		}
    	}
    }
    
    //Method to handle actions perfomed in the enviroment
    @Override
    public void actionPerformed(ActionEvent arg0) {
    	//Check game end 
    	//Game ends if player's health is 0 or 
    	//player has survived 48 hours
    	if(player.health < 0 || days >= 48) {
    		game_flag = false;
    	}
        if (game_flag) {
        	//Game has not ended
        	if(!pause && !combat) {
        		//Player is not currently on combat 
        		//Neither is the game paused
        		checkHits();
            	moveAliens();
            	checkPlayerHit();
            	checkCollision();
        	}
        }
        repaint();
    }
    
}