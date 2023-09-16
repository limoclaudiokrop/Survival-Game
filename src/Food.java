import java.util.Random;


//Class to represent food
public class Food {
	Random random;
	//Set variables to store information about food
	
	//Food type i.e healhty, poisonous or artifact differentiated by char
	public char T;
		
	public int x;
	public int y;
	
	//Class constructor method
	public Food(char t, int width, int height) {
		random = new Random();
		T = t;
		//Initalize food at random position on canvas
		x = random.nextInt(width);
		y = random.nextInt(height);
	}
}
