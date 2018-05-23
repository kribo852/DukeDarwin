import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.io.File;
import java.util.Collection;

class TSPLifeform extends Lifeform {
	
	static double[][] coordinates;
	static double initialscore;
	double length;
	
	//sets the genome 
	public TSPLifeform(final Collection genome){
		super(genome);

		if(coordinates==null){
			coordinates=new double[2500][2];
			for(int i=0; i<coordinates.length; i++){
				coordinates[i][0]=(new Random()).nextDouble();
				coordinates[i][1]=(new Random()).nextDouble();
			}
			for(int i=0; i<coordinates.length; i++){
				initialscore+=calcLen(coordinates[i], coordinates[(i+1)%coordinates.length]);
			}
		}
	}
	
	//sexual reproduction
	public ArrayList<Integer> mutate(final Collection genome1, final Collection genome2){
		return mutate(genome1);
	}
	
	//asexual reproduction
	public ArrayList<Integer> mutate(final Collection genome){
		ArrayList<Integer> rtn=new ArrayList<Integer>();
		rtn.addAll(genome);
			
		int index1=(new Random()).nextInt(rtn.size());
		int p1=rtn.get(index1);
		int index2=(new Random()).nextInt(rtn.size());
		int p2=rtn.get(index2);
		
		rtn.remove(index1);
		rtn.add(index1, p2);
		
		rtn.remove(index2);
		rtn.add(index2, p1);
		
		return rtn;
	}
	
	//calculate the score of own genome
	public double getScore(){
		return initialscore-length;
	}
	
	//in some manner, output the result of the simulation
	public void output(){
		System.out.println("initial length: "+initialscore);
		System.out.println("current length: "+length);
	}
	
	protected double calculateLength(){
		length = 0;
		ArrayList<Integer> tmp=(ArrayList<Integer>)genome;
		for(int i=0; i<coordinates.length; i++){
			length+=calcLen(coordinates[tmp.get(i)],
			 coordinates[tmp.get((i+1)%tmp.size())]);
		}
		return length;
	}
	
	//returns a new gene
	public ArrayList<Integer> newGenome(){
		ArrayList<Integer> remaping=new ArrayList<Integer>();
		ArrayList<Integer> rtn=new ArrayList<Integer>();
		
		for(int i=0; i<coordinates.length; i++){
			remaping.add(i);
		}
		
		for(int i=0; i<coordinates.length; i++){
			rtn.add(remaping.remove((new Random()).nextInt(remaping.size())));
		}
		
		return rtn;
	}
	
	//remaps genome to the problem instance
	public void run(){
		calculateLength();
	}
	
	private double calcLen(double[] point1, double[] point2){
		return Math.hypot(point1[0]-point2[0], point1[1]-point2[1]);
	}
	
}
