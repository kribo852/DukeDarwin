import java.util.ArrayList;
import java.util.Collection;

abstract class Lifeform<E, T extends Collection<E>> implements Runnable {
	
	T genome;
	
	//sets the genome 
	public Lifeform(final T genome){
		this.genome=genome;
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
}
