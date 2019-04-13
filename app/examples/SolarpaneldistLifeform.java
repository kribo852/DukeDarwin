package app.examples;

import app.Lifeform;
import app.GenerationlessHabitat;

import java.util.ArrayList;
import java.util.Random;
import java.util.Arrays;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.awt.Graphics;

import javax.swing.JFrame;

public class SolarpaneldistLifeform extends Lifeform<Shape, ArrayList<Shape>> {
	double score = 0;
	static final double[] readings = 
	new double[]{55,150,120,115,310,475,1850,2200,2620,3000,3025,3025,3025,3025,3025,3000,2620,2200,1850,475,310,115,120,150,55};  
	static final double middlepoint = readings.length/2.0-0.5;// to match indicies of a for loop. length 15 -> 0..14 -> -7..7
	static final double peakvalue = 3025;
	static JFrame jframe;

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
		if(jframe == null) {
			jframe = new JFrame();
			jframe.setVisible(true);
			jframe.setSize(1000, 1000);		
			jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}

		BufferedImage bufferedImage1 = new BufferedImage(1000, 500, 1);
		BufferedImage bufferedImage2 = new BufferedImage(1000, 500, 1);
		
		System.out.println("Score: "+score);
		genome.get(0).printdata(bufferedImage1, readings, middlepoint);
		System.out.println();
		genome.get(1).printdata(bufferedImage2, readings, middlepoint);

		jframe.getGraphics().drawImage(bufferedImage1, 0, 0, null);
		jframe.getGraphics().drawImage(bufferedImage2, 0, 500, null);
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

	public void printdata(final BufferedImage bufferedImage, final double[] readings, final double middlepoint);

	public Shape mutate(final double[] readings, final double peakvalue);

}

final class SeconddegreeEquation implements Shape{
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
			cost += Math.pow(readings[i]-getHeightAtPosition(position), 2);
		}
		return Math.sqrt(cost);
	}

	@Override
	public double area() {
		return -hightscaler*Math.pow(roots, 3)/3 + hightscaler * Math.pow(roots, 3);
		//half symetric area but scaled up 2 times per hour 
	}

	@Override
	public void printdata(final BufferedImage bufferedImage, final double[] readings, final double middlepoint){
		System.out.println("Cost for second degree equation: "+cost(readings, middlepoint));
		System.out.println("Area: " + area());
		System.out.println("Scaler: " + hightscaler);
		System.out.println("Roots: +-" + roots/2 +" hours");

		Graphics graphics = bufferedImage.getGraphics();
		graphics.setColor(Color.RED);
		double scale = 0.1;
		
		for(int i=0; i<bufferedImage.getWidth(); i++) {
			double position = ((double)i/bufferedImage.getWidth())*readings.length;
			double height = scale*getHeightAtPosition(position-middlepoint);
			graphics.fillRect(i, bufferedImage.getHeight()-(int)height, 1, 1);
		}
		graphics.setColor(Color.blue);
		for(int i=0; i<readings.length; i++) {
			graphics.fillRect((i*bufferedImage.getWidth())/readings.length-5, 
				bufferedImage.getHeight()-5-(int)(scale*readings[i]), 10, 10);
		}
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

	private double getHeightAtPosition(final double position) {
		double expectedheight = -hightscaler*
			(position - roots)*
			(position + roots);
			expectedheight = Math.max(0, expectedheight);
		return expectedheight;
	}
}

final class CustomShape implements Shape{
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
			cost += Math.pow((readings[i]-getHeightAtPosition(position)), 2);
		}
		return Math.sqrt(cost);
	}

	@Override
	public double area() {
		return boxlength*height+(height/expcoefficient);
		//half symetric area but scaled up 2 times per hour 
	}

	@Override
	public void printdata(final BufferedImage bufferedImage, final double[] readings, final double middlepoint) {
		System.out.println("Cost for custom shape: "+cost(readings, middlepoint));
		System.out.println("Area: " + area());

		Graphics graphics = bufferedImage.getGraphics();
		graphics.setColor(Color.RED);
		double scale = 0.1;
		
		for(int i=0; i<bufferedImage.getWidth(); i++) {
			double position = ((double)i/bufferedImage.getWidth())*readings.length;
			double height = scale*getHeightAtPosition(position-middlepoint);
			graphics.fillRect(i, bufferedImage.getHeight()-(int)height, 1, 1);
		}
		graphics.setColor(Color.blue);
		for(int i=0; i<readings.length; i++) {
			graphics.fillRect((i*bufferedImage.getWidth())/readings.length-5, 
				bufferedImage.getHeight()-5-(int)(scale*readings[i]), 10, 10);
		}
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
			child = new CustomShape(mutateScale()*height, mutateScale()*boxlength, expcoefficient);
		} else if(choice == 2){
			child = new CustomShape(height, mutateScale()*boxlength, mutateScale()*expcoefficient);
		} else {
			child = new CustomShape(mutateScale()*height, boxlength, mutateScale()*expcoefficient);
		}
		return child;
	}

	private double mutateScale(){
		Random RND = new Random();
		return (0.99+RND.nextDouble()*0.02);
	}

	private double getHeightAtPosition(final double position) {
		double expectedheight;
			if(Math.abs(position) < boxlength) {
				expectedheight = height;
			} else if (position > 0) {
				expectedheight = height*Math.exp(-expcoefficient*(position-boxlength));
			} else {
				expectedheight = height*Math.exp(expcoefficient*(position+boxlength));
			}
		return expectedheight;
	}
}
