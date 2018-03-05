import java.lang.Thread;
import java.util.Random;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Map;
import java.util.Map.Entry;

public class HighEndHabitat extends Habitat{
		final int saveinterval = 150;
		final int habitatsize=25;
		Thread[] threads= new Thread[3];
		final double habitatechancetailcoeficient=0.05;
		
		
	public void genesis(Class lifeformClass){
		genomes = new TreeMap<Double, Collection> ();
		for(int i=0; i<habitatsize; i++){
			Lifeform placeholder = newInstance(null, lifeformClass);
			Collection c=placeholder.newGenome();
			Lifeform lifeform = newInstance(c, lifeformClass);
			lifeform.run();
			genomes.put(lifeform.getScore(), c);
		}
	}
	
	public void run(Class lifeformClass){
		
		genesis(lifeformClass);
		
		long secs = System.currentTimeMillis()/1000;
		
		while(true) {
			Collection[] gencopystore=new Collection[threads.length];
			Lifeform[] lifeformstore= new Lifeform[threads.length]; 
			Lifeform mutator = newInstance(null, lifeformClass);
			
			for(int i=0; i<threads.length; i++){
				gencopystore[i]=mutator.mutate(selectGenome(genomes));
				lifeformstore[i] = newInstance(gencopystore[i], lifeformClass);
				threads[i]=new Thread(lifeformstore[i]);
				threads[i].start();
			}
			
			for(int i=0; i<threads.length; i++){
				try{
					threads[i].join(10000);
					genomes.put(lifeformstore[i].getScore(), gencopystore[i]);
				}catch(InterruptedException e){
					System.out.println(e);
				}
			}
			
			while(genomes.size()>habitatsize){
				genomes.pollFirstEntry().getKey();
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
	
	
	
	Collection selectGenome(TreeMap<Double, Collection> genomes){
		Map<Double, Collection> tmp=genomes.descendingMap();
		for(Entry<Double, Collection> entry: tmp.entrySet()) {
			if((new Random()).nextDouble()<calculateChance()){
				return entry.getValue();
			}
		}
		return genomes.lastEntry().getValue();
	}
	
	double calculateChance(){
		double c = 1.0/(Math.exp(Math.log(habitatechancetailcoeficient)/habitatsize));
		return 1.0-c;
	}
	
}
