import java.util.Random;
import java.util.ArrayList;
import java.util.Collection;


class OptimalPyramidLife extends Lifeform{
	Random RND=new Random();
	
	public OptimalPyramidLife(final Collection genome){
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
		
		//genome[0] height
		//genome[1] width
		
		ArrayList<Double> tmp= (ArrayList<Double>)genome;
		
		double volume=(tmp.get(1)*tmp.get(1)*tmp.get(0))/3.0;
		
		if(volume>0 && tmp.get(0)>=0 && tmp.get(1)>=0 && area()<100.0) {
			return volume;
		}
		
		return 0;
	}
	
	private double area(){
		ArrayList<Double> tmp= (ArrayList<Double>)genome;
		
		double hprime=Math.hypot(tmp.get(0), tmp.get(1)/2.0);
		return (tmp.get(1)*tmp.get(1))+2.0*hprime*tmp.get(1);
	}
	
	//in some manner, output the result of the simulation
	public void output() {
		ArrayList<Double> tmp= (ArrayList<Double>)genome;
		
		System.out.println("height: "+tmp.get(0));
		System.out.println("width: "+tmp.get(1));
		System.out.println("ratio: "+(tmp.get(0)/tmp.get(1)));

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
