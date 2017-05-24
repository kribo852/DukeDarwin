import java.io.*;
import java.lang.reflect.*;
import java.lang.Thread;

class Main{

	public static void main(String[] args){
		
		SpeciesContainer species=new SpeciesContainer(loadClass(args[0]));
		
		species.evolve();
	}
	
	public static Class loadClass(String s) {
		try{
		  return Class.forName(s); 
		}catch(Exception e){
			
		}
		return null;
	}	
}

class SpeciesContainer{
	Class lifeformClass;
	Integer[][] genomes;
	int bestscoreIndex=0;
	final int saveinterval=1500;
	
	public SpeciesContainer(Class lifeformClass){
		this.lifeformClass=lifeformClass;
		genomes=new Integer[20][];
		
		for(int i=0; i<genomes.length; i++){
			genomes[i]=newInstance(null).newGenome();
		}
	}
	
	public void evolve(){	
		long secs=System.currentTimeMillis()/1000;
		Thread[] threads=new Thread[genomes.length];
		while(true) {
			LifeForm[] lifeforms=new LifeForm[genomes.length];
			int bestindex=0;
			
			for(int i=0; i<genomes.length; i++) {
				lifeforms[i]=newInstance(genomes[i]);
				threads[i]=new Thread(lifeforms[i]);
				threads[i].start();
			}
			
			for(Thread t:threads){
				try{
					t.join(10000);
				}catch(InterruptedException e){
					System.out.println(e);
				}
			}
			
			for(int i=0; i<threads.length; i++){
				if(lifeforms[i].getScore()>lifeforms[bestindex].getScore()){
					bestindex=i;
				}
			}
			
			for(int i=0; i<lifeforms.length; i++){
				if(i!=bestindex) genomes[i]=lifeforms[0].mutate(genomes[bestindex]);
			}
			
			if(System.currentTimeMillis()/1000>secs+saveinterval){
				secs=System.currentTimeMillis()/1000;
				lifeforms[bestindex].output();
				System.out.println("###");
			}
			
		}
	}
	
	public LifeForm newInstance(Integer[] genome){
		try {
			Class[] params=new Class[1];
			params[0]=Integer[].class;
			Constructor ct = lifeformClass.getConstructor(params);
			Object[] argslist=new Object[]{genome};
			return(LifeForm)ct.newInstance(argslist);
		}catch(Exception e){
			System.out.println("error");
			System.exit(-1);
		}
		return null;
	}
	
}

abstract class LifeForm implements Runnable {
	
	Integer[] genome;
	
	//sets the genome 
	public LifeForm(final Integer[] genome){
		this.genome=genome;
	}
	
	//sexual reproduction
	public abstract Integer[] mutate(final Integer[] genome1, final Integer[] genome2);
	
	//asexual reproduction
	public abstract Integer[] mutate(final Integer[] genome);
	
	//calculate the score of own genome
	public abstract double getScore();
	
	//in some manner, output the result of the simulation
	public abstract void output();
	
	public abstract Integer[] newGenome();//returns a new gene
}

final class ExampleLife extends LifeForm {
	
	public ExampleLife(final Integer[] genome){
		super(genome);
	}
	
	public Integer[] mutate(final Integer[] genome1, final Integer[] genome2){
		return genome1;
	}
	
	public Integer[] mutate(final Integer[] genome) {
		return genome;
	}
	
	public double getScore() {
		return 0;
	}
	
	public void output(){
		
	}
	
	public Integer [] newGenome(){
		return new Integer[20];
	}
	
	public void run(){
		
	}
}
