import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;

class TSPLifeform extends LifeForm {
	
	static double[][] coordinates;
	static double initialscore;
    double currentscore;
	
	//sets the genome 
	public TSPLifeform(final Integer[] genome){
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
	public Integer[] mutate(final Integer[] genome1, final Integer[] genome2){
		return null;
	}
	
	//asexual reproduction
	public Integer[] mutate(final Integer[] genome){
		Integer[] rtn=new Integer[genome.length];
		for(int i=0; i<genome.length; i++)rtn[i]=genome[i];
		
		int index1=(new Random()).nextInt(rtn.length);
		int index2=(new Random()).nextInt(rtn.length);
		int tmp=rtn[index1];
		rtn[index1]=rtn[index2];
		rtn[index2]=tmp;
		return rtn;
	}
	
	//calculate the score of own genome
	public double getScore(){
		return initialscore-currentscore;
	}
	
	//in some manner, output the result of the simulation
	public void output(){
		System.out.println("initial length: "+initialscore);
		System.out.println("current length: "+currentscore);
	}
	
	//returns a new gene
	public Integer[] newGenome(){
		ArrayList<Integer> remaping=new ArrayList<Integer>();
		
		for(int i=0; i<coordinates.length; i++){
			remaping.add(i);
		}
		Integer[] rtn=new Integer[coordinates.length];
		
		for(int i=0; i<coordinates.length; i++){
			rtn[i]=remaping.remove((new Random()).nextInt(remaping.size()));
		}
		
		return rtn;
	}
	
	//remaps genome to the problem instance
	public void run(){
		currentscore=0;
		//System.out.println(genome);
		for(int i=0; i<coordinates.length; i++){
			currentscore+=calcLen(coordinates[genome[i]],
			 coordinates[genome[(i+1)%genome.length]]);
		}
	}
	
	private double calcLen(double[] point1, double[] point2){
		return Math.hypot(point1[0]-point2[0], point1[1]-point2[1]);
	}
	
}
