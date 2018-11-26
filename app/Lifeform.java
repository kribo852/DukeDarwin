package app;

import java.util.ArrayList;
import java.util.Collection;

/**
*This is the class to extend to make your own lifeform expressing a particular problem and testing solutions to it. 
**/
public abstract class Lifeform<E, T extends Collection<E>> implements Runnable {
	
	protected T genome;
		
	//sets the genome 
	public Lifeform(final T genome){
		this.genome = genome;
	}
	
	//sexual reproduction
	public abstract T mutate(final T genome1, final T genome2);
	
	//asexual reproduction
	public abstract T mutate(final T genome);
	
	//calculate the score of own genome
	public abstract double getScore();
	
	//in some manner, output the result of the simulation
	public abstract void output();
	
	public abstract T newGenome();//returns a new genome
	
	//override this if a custom habitat is wanted
	public void start() {
		new RobustHabitat().start(this.getClass());	
	}	
}
