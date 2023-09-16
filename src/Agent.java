import java.util.Random;

//Class that represents agent
public class Agent {
	//set variables to store information about agent
	public int health;
	public int charisma;
	public int strength;
	public int intelligence;
	Random random;
	public char T;
	public int size;
	public int x;
	public int y;
	public boolean alive;
	
	//Constructor method for agent
	public Agent(char t, int width, int height, int s){
		size = s;
		random = new Random();
		health = 20;
		//Set charisma to random variable
		charisma = random.nextInt(10);
		//update charisma for two diffent types of aliens
		if(T =='X') {
			charisma = random.nextInt(10) + 5;
		}
		if(T =='X') {
			charisma = random.nextInt(10) - 2;
		}
		//set intelligence
		intelligence = random.nextInt(40) + 10;
		strength = random.nextInt(40) + 10;
		T = t;
		x = 0;
		y =0;
		alive = true;
		//If agent is not the player, then place them in a random position on canvas
		if (T != 'P') {
			x = random.nextInt(width);
			y = random.nextInt(height);
		}
	}
	
	//Method to set position of the agent
	public void setPosition(int xpos, int ypos) {
		x = xpos;
		y = ypos;
	}
	
	
}
