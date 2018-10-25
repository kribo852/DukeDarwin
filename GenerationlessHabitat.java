import java.lang.Thread;
import java.util.Random;
import java.util.Collection;
import java.util.TreeMap;
import java.util.Map;
import java.util.Map.Entry;

public class GenerationlessHabitat extends Habitat{
		final int saveinterval = 15;
		final int habitatsize=25;
		Thread[] threads= new Thread[3];
		final double habitatechancetailcoeficient=0.04;
		
		TreeMap<Double, Collection> genomes;
		
		
	public void genesis(Class lifeformClass){
		genomes = new TreeMap<Double, Collection> ();
		for(int i=0; i<habitatsize; i++){
			Lifeform placeholder = newInstance(null, lifeformClass);
			Collection c=placeholder.newGenome();
			Lifeform lifeform = newInstance(c, lifeformClass);
			lifeform.run();
			genomes.put(lifeform.getScore(), c);
		}
		
		testDistribution();
	}
	
	
	
	public void start(Class lifeformClass){
		
		genesis(lifeformClass);
		
		long secs = System.currentTimeMillis()/1000;
		
		while(true) {
			Collection[] gencopystore=new Collection[threads.length];
			Lifeform[] lifeformstore= new Lifeform[threads.length]; 
			Lifeform mutator = newInstance(null, lifeformClass);
			
			for(int i=0; i<threads.length; i++){
				gencopystore[i]=mutator.mutate(selectGenome(genomes), selectGenome(genomes));
				lifeformstore[i] = newInstance(gencopystore[i], lifeformClass);
				threads[i]=new Thread(lifeformstore[i]);
				threads[i].start();
			}
			
			for(int i=0; i<threads.length; i++){
				try{
					threads[i].join(10000);
				}catch(InterruptedException e){
					System.out.println(e);
				}
			}
			
			for(int i=0; i<threads.length; i++){
				if(!genomes.containsKey(lifeformstore[i].getScore())){
					genomes.put(lifeformstore[i].getScore(), gencopystore[i]);
				}else {
					Double nextlowerscore = genomes.lowerKey(lifeformstore[i].getScore());
					if(nextlowerscore != null) {
						double newscore=(lifeformstore[i].getScore()+nextlowerscore)/2;
						genomes.put(newscore, gencopystore[i]);//trick to save an organism with an already existing score in the treemap, put it between 2 other scores.
					}
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
	
	void testDistribution(){
		int[] distribution = new int[habitatsize];
		
		for(int i=0; i<1000; i++){
			for(int j=0; ; j++){
				if((new Random()).nextDouble()<calculateChance()){
					distribution[j%habitatsize]++;
					break;
				}	
			}		
		}
		System.out.println("distribution");
		for(int i : distribution) {
			System.out.print(" "+i+" |" );
		}
	}
	
	Collection selectGenome(TreeMap<Double, Collection> genomes){
		Map<Double, Collection> tmp=genomes.descendingMap();
		while(true) {
			for(Entry<Double, Collection> entry: tmp.entrySet()) {
				if((new Random()).nextDouble()<calculateChance()){
					return entry.getValue();
				}
			}
		}
	}
	
	//derived from equation e^(-a*Hsize)=Tsize. Tsize=habitatechancetailcoeficient, Hsize=habitatsize.
	//restprobability, the wanted probability, is supposed to be e^(-a).
	//corrected 1/5/2018
	double calculateChance(){
		double restprobability = (Math.exp(Math.log(habitatechancetailcoeficient)/habitatsize));
		return 1.0-restprobability;
	}
	
}
