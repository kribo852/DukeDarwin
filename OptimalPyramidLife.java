import java.util.Random;
import java.util.ArrayList;
import java.util.Collection;


class OptimalPyramidLife extends Lifeform<Double, ArrayList<Double>>{
	Random RND=new Random();
	
	public OptimalPyramidLife(final ArrayList<Double> genome){
		super(genome);
	}
	
	//sexual reproduction
	public ArrayList<Double> mutate(final ArrayList<Double> genome1, final ArrayList<Double> genome2){
		return mutate(genome1);
	}
	
	//asexual reproduction
	public ArrayList<Double> mutate(final ArrayList<Double> genome){
		ArrayList<Double> rtn=new ArrayList<>();
		double magnifier1=0.95+0.1*RND.nextDouble();
		double magnifier2=0.95+0.1*RND.nextDouble();
		
		rtn.add(genome.get(0)*magnifier1);
		rtn.add(genome.get(1)*magnifier2);
		return rtn;
	}
	
	public void run(){
		
	}
	
	//calculate the score of own genome
	public double getScore() {
		
		//genome[0] height
		//genome[1] width
		
		double volume=(genome.get(1)*genome.get(1)*genome.get(0))/3.0;
		
		if(volume>0 && genome.get(0)>=0 && genome.get(1)>=0 && area()<100.0) {
			return volume;
		}
		
		return 0;
	}
	
	private double area(){		
		double hprime=Math.hypot(genome.get(0), genome.get(1)/2.0);
		return (genome.get(1)*genome.get(1))+2.0*hprime*genome.get(1);
	}
	
	//in some manner, output the result of the simulation
	public void output() {
		System.out.println("height: "+genome.get(0));
		System.out.println("width: "+genome.get(1));
		System.out.println("ratio: "+(genome.get(0)/genome.get(1)));

		System.out.println("area: "+area());
		System.out.println("score: "+getScore());
	}
	
	public ArrayList<Double> newGenome() {
		
		ArrayList<Double> rtn=new ArrayList<>();
		
		rtn.add(RND.nextDouble());
		rtn.add(RND.nextDouble());
		
		return rtn;
	}
}
