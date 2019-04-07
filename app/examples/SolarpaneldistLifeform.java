package app.examples;

import app.Lifeform;
import app.GenerationlessHabitat;

import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

public class SolarpaneldistLifeform extends Lifeform<Shape, ArrayList<Shape>> {
	double score = 0;
	static final double[] readings = 
	new double[]{55,150,120,115,310,475,1850,2200,2620,3000,3025,3025,3025,3025,3025,3000,2620,2200,1850,475,310,115,120,150,55};  
	static final double middlepoint = readings.length/2.0-0.5;// to match indicies of a for loop. length 15 -> 0..14 -> -7..7
	static final double peakvalue = 3025;

	public SolarpaneldistLifeform(final ArrayList<Shape> genome){
		super(genome);
	}

	public void start() {
		new GenerationlessHabitat().start(this.getClass());	
	}

	@Override
	public ArrayList<Shape> mutate(final ArrayList<Shape> genome) {
		return null;//not used
	}
	
	@Override
	public ArrayList<Shape> mutate(final ArrayList<Shape> genome1, final ArrayList<Shape> genome2) {
		ArrayList<Shape> result = new ArrayList();		
		if(new Random().nextBoolean()) {
			result.add(genome1.get(0).mutate(readings, peakvalue));
			result.add(genome2.get(1).mutate(readings, peakvalue));
		} else {
			result.add(genome2.get(0).mutate(readings, peakvalue));
			result.add(genome1.get(1).mutate(readings, peakvalue));
		}
		return result;
	}
	
	@Override
	public double getScore() {
		return score;
	}
	
	@Override
	public void output() {
		System.out.println("Score: "+score);
		System.out.println("Cost for second degree equation: "+genome.get(0).cost(readings, middlepoint));
		genome.get(0).printdata();
		System.out.println();
		System.out.println("Cost for custom shape: "+genome.get(1).cost(readings, middlepoint));
		genome.get(1).printdata();
	}

	@Override	
	public ArrayList<Shape> newGenome() {
		ArrayList<Shape> rtn = new ArrayList<>();
		rtn.add(SeconddegreeEquation.newGenome(readings, peakvalue));
		rtn.add(CustomShape.newGenome(readings, peakvalue));
		return rtn;
	}

	@Override
	public void run() {
		score -= genome.get(0).cost(readings, middlepoint);
		score -= genome.get(1).cost(readings, middlepoint);
	}
}

interface Shape{

	public double cost(final double[] readings, final double middlepoint);

	public double area();	

	public void printdata();

	public Shape mutate(final double[] readings, final double peakvalue);
}

class SeconddegreeEquation implements Shape{
	double hightscaler;// 
	double roots;//one positive and one negative

	private SeconddegreeEquation(final double hightscaler, final double roots) {
		this.hightscaler = hightscaler;
		this.roots = roots;
	}

	public static SeconddegreeEquation newGenome(final double[] readings, final double peakvalue){
		Random RND = new Random();
		return new SeconddegreeEquation(RND.nextDouble()*peakvalue, RND.nextDouble()*readings.length);
	}

	@Override
	public double cost(final double[] readings, final double middlepoint) {
		double cost = 0;
		for (int i=0; i<readings.length; i++) {
			double position = i-middlepoint;
			double expectedheight = -hightscaler*
			(position - roots)*
			(position + roots);
			expectedheight = Math.max(0, expectedheight);
			cost += Math.log(0.01+Math.abs(readings[i]-expectedheight));
		}
		return cost;
	}

	@Override
	public double area() {
		return -hightscaler*Math.pow(roots, 3)/3 + hightscaler * Math.pow(roots, 3);
		//half symetric area but scaled up 2 times per hour 
	}

	@Override
	public void printdata(){
		System.out.println("Area: " + area());
		System.out.println("Scaler: " + hightscaler);
		System.out.println("Roots: +-" + roots/2 +" hours");
	}

	@Override
	public SeconddegreeEquation mutate(final double[] readings, final double peakvalue){
		Random RND = new Random();
		if(RND.nextInt(10) == 0) {
			return newGenome(readings, peakvalue);
		}		
		int choice = RND.nextInt(4); 
		final SeconddegreeEquation child;
		if(choice == 0) {
			child = new SeconddegreeEquation((0.95+RND.nextDouble()*0.1)*hightscaler, roots);
		} else if(choice == 1){
			child = new SeconddegreeEquation(hightscaler, (0.95+RND.nextDouble()*0.1)*roots);
		} else if(choice == 2){
			child = new SeconddegreeEquation(hightscaler, roots);
		} else {
			child = new SeconddegreeEquation((0.75+RND.nextDouble()*0.5)*hightscaler, 
				(0.75+RND.nextDouble()*0.5)*roots);
		}
		return child;
	}
}

class CustomShape implements Shape{
	double height;
	double boxlength;
	double expcoefficient;

	private CustomShape(final double height, final double boxlength, final double expcoefficient) {
		this.height = height;
		this.boxlength = boxlength;
		this.expcoefficient = expcoefficient;
	}

	public static CustomShape newGenome(final double[] readings, final double peakvalue){
		Random RND = new Random();
		return new CustomShape(RND.nextDouble()*peakvalue, 
			RND.nextDouble()*readings.length, RND.nextDouble());
	}

	@Override
	public double cost(final double[] readings, final double middlepoint) {
		double cost = 0;
		for (int i=0; i<readings.length; i++) {
			double position = i-middlepoint;
			double expectedheight;
			if(Math.abs(position) < boxlength) {
				expectedheight = height;
			} else if (position > 0) {
				expectedheight = height*Math.exp(-expcoefficient*(position-boxlength));
			} else {
				expectedheight = height*Math.exp(expcoefficient*(position+boxlength));
			}
			cost += Math.log(0.01+Math.abs(readings[i]-expectedheight));
		}
		return cost;
	}

	@Override
	public double area() {
		return boxlength*height+(height/expcoefficient);
		//half symetric area but scaled up 2 times per hour 
	}

	@Override
	public void printdata(){
		System.out.println("Area: " + area());
	}

	@Override
	public CustomShape mutate(final double[] readings, final double peakvalue){
		Random RND = new Random();
		if(RND.nextInt(10) == 0) {
			return newGenome(readings, peakvalue);
		}		
		int choice = RND.nextInt(4); 
		final CustomShape child;
		if(choice == 0) {
			child = new CustomShape(height, boxlength, expcoefficient);
		} else if(choice == 1){
			child = new CustomShape((0.95+RND.nextDouble()*0.1)*height, boxlength, expcoefficient);
		} else if(choice == 2){
			child = new CustomShape(height, (0.95+RND.nextDouble()*0.1)*boxlength, expcoefficient);
		} else {
			child = new CustomShape(height, boxlength, (0.95+RND.nextDouble()*0.1)*expcoefficient);
		}
		return child;
	}
}
