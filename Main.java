import java.io.*;
import java.lang.reflect.*;
import java.lang.Thread;
import java.util.Random;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Map.Entry;


class Main{

	public static void main(String[] args){
		
		//SpeciesContainer species=new SpeciesContainer(loadClass(args[0]), args[1]);
		
		//species.evolve();
		
		Habitat h =new HighEndHabitat();
		
		h.run(loadClass(args[0]));
		
	}
	
	public static Class loadClass(String s) {
		try{
		  return Class.forName(s); 
		}catch(Exception e){
			
		}
		return null;
	}	
}

abstract class Habitat{
	TreeMap<Double, Collection> genomes;
	
	protected abstract void run(Class lifeformClass);
	
	protected Lifeform newInstance(Collection genome, Class lifeformClass){
		try {
			Class[] params=new Class[1];
			params[0]=Collection.class;
			Constructor ct = lifeformClass.getConstructor(params);
			Object[] argslist=new Object[]{genome};
			return(Lifeform)ct.newInstance(argslist);
		}catch(Exception e){
			System.out.println("error "+e);
			e.printStackTrace();
			System.exit(-1);
		}
		return null;
	}
	
}

class SpeciesContainer extends Habitat{
	final int saveinterval = 15;
	final int habitatsize=25;
	
	public void run(Class lifeformClass){
		genomes = new TreeMap<Double, Collection> ();
		
		for(int i=0; i<habitatsize; i++){
			Lifeform placeholder = newInstance(null, lifeformClass);
			Collection c=placeholder.newGenome();
			Lifeform lifeform = newInstance(c, lifeformClass);
			lifeform.run();
			genomes.put(lifeform.getScore(), c);
		}
		
		long secs = System.currentTimeMillis()/1000;
		
		while(true) {
			
			for(Entry<Double, Collection> entry : genomes.descendingMap().entrySet()) {
				if(new Random().nextDouble()<0.1){
					Lifeform mutator = newInstance(null, lifeformClass);
					Collection c=mutator.mutate(entry.getValue());
					Lifeform lifeform = newInstance(c, lifeformClass);
					lifeform.run();
					genomes.put(lifeform.getScore(), c);
					if(genomes.size()>habitatsize){
						genomes.pollFirstEntry();
					}
					break;
				}
			}
			
			
			if(System.currentTimeMillis()/1000>secs+saveinterval) {
				secs = System.currentTimeMillis()/1000;
				Lifeform lifeform = newInstance(genomes.lastEntry().getValue(), lifeformClass);
				lifeform.run();
				lifeform.output();
				System.out.println("--------------------");
			}
		}
	}
}
