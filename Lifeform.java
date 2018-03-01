import java.io.*;
import java.lang.reflect.*;
import java.lang.Thread;
import java.util.Random;
import java.util.ArrayList;
import java.util.Collection;

abstract class Lifeform<T> implements Runnable {
	
	Collection<T> genome;
	
	//sets the genome 
	public Lifeform(final Collection<T> genome){
		this.genome=genome;
	}
	
	//sexual reproduction
	public abstract Collection<T> mutate(final Collection<T> genome1, final Collection<T> genome2);
	
	//asexual reproduction
	public abstract Collection<T> mutate(final Collection<T> genome);
	
	//calculate the score of own genome
	public abstract double getScore();
	
	//in some manner, output the result of the simulation
	public abstract void output();
	
	public abstract Collection<T> newGenome();//returns a new genome
}


class Lifeform2 extends Lifeform{
	
	public Lifeform2(final ArrayList<Integer> genome){
		super(genome);
	}
	
	public ArrayList<Integer> mutate(final Collection genome1, final Collection genome2){
		
		
		return (ArrayList<Integer>)genome1;
	}
	
	//asexual reproduction
	public ArrayList<Integer> mutate(final Collection genome){
		return null;
	}
	
	//calculate the score of own genome
	public double getScore(){
		return 0;
	}
	
	//in some manner, output the result of the simulation
	public void output(){
		
	}
	
	public ArrayList<Integer> newGenome(){
		return new ArrayList<Integer>();
	}
	
	public void run(){
		
	}
	
}
