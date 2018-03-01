import java.util.Random;
import java.util.ArrayList;
import java.util.Collection;

class OptimalCylinderLife extends Lifeform{
	Random RND=new Random();
	
	public OptimalCylinderLife(final Collection genome){
		super(genome);
	}
	
	//sexual reproduction
	public ArrayList<Integer> mutate(final Collection genome1, final Collection genome2){
		return null;
	}
	
	//asexual reproduction
	public ArrayList<Double> mutate(final Collection genome){
		ArrayList<Double> rtn=new ArrayList<>();
		double magnifier1=0.95+0.1*RND.nextDouble();
		double magnifier2=0.95+0.1*RND.nextDouble();
		ArrayList<Double> tmp= (ArrayList<Double>)genome;
		
		rtn.add(tmp.get(0)*magnifier1);
		rtn.add(tmp.get(1)*magnifier2);
		return rtn;
	}
	
	public void run(){
		
	}
	
	//calculate the score of own genome
	public double getScore() {
		
		ArrayList<Double> tmp= (ArrayList<Double>)genome;
		
		double volume=(Math.PI*(double)tmp.get(0)*tmp.get(0)*tmp.get(1));
		
		if(area()>0 && volume>0 && tmp.get(0)>=0 && tmp.get(1)>=0 && area()<100.0) {
			return volume;
		}
		
		return 0;
	}
	
	private double area(){
		ArrayList<Double> tmp= (ArrayList<Double>)genome;
		//2 lids + wall of can
		return (Math.PI*2.0*tmp.get(0)*tmp.get(0))+(Math.PI*2.0*tmp.get(0)*tmp.get(1));
	}
	
	//in some manner, output the result of the simulation
	public void output() {
		ArrayList<Double> tmp= (ArrayList<Double>)genome;
		
		System.out.println("radius: "+tmp.get(0));
		System.out.println("height: "+tmp.get(1));
		System.out.println("ratio: "+tmp.get(0)/tmp.get(1));
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
