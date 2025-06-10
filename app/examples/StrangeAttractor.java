package app.examples;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.TreeSet;
import java.util.HashSet;
import java.util.HashMap;

import java.util.Random;
import java.util.function.Consumer;

import javax.imageio.ImageIO;
import javax.swing.JFrame;

import java.awt.Graphics;

import app.Lifeform;
import app.RobustHabitat;

public class StrangeAttractor extends Lifeform<Double, ArrayList<Double>> {

	double score;
	int screendimension = 800;
	
	static JFrame jframe;

	public StrangeAttractor(ArrayList<Double> genome) {
		super(genome);
		score = 0;
	}

	@Override
	public void run() {
		score = 0;
		int [][] buckets = new int[100][100];

		double x = 0.5;
		double y = 0.5;
		double frequencyhits = 1;

		for(int i=0; i<50000; i++) {
			x = getXprim(x, y);
			y = getYprim(x, y);
			//System.out.println("x "+x);
			//System.out.println("y "+y);

			if ((0.5 + x) >= 0 && (0.5 + x) < 1 && (0.5 + y) >= 0 && (0.5 + y) < 1) {
				buckets[(int)(100*(0.5 + x))][(int)(100*(0.5 + y))] ++;
				frequencyhits ++;
			}
		}

		for (int i=0; i<buckets.length; i++) {
			for (int j=0; j<buckets[0].length; j++) {
				if(buckets[i][j] != 0 ) {
					score -= (buckets[i][j]/frequencyhits) * Math.log(buckets[i][j]/frequencyhits);
				}
			}	
		}
	}

	@Override
	public ArrayList<Double> mutate(ArrayList<Double> genome1, ArrayList<Double> genome2) {
		return null;
	}

	@Override
	public ArrayList<Double> mutate(ArrayList<Double> genome) {
		if(new Random().nextInt(100) == 0)
			return newGenome();

		ArrayList<Double> rtn = new ArrayList<Double>();

		int type = new Random().nextInt(2);
		for(int i=0; i<genome.size(); i++) {
			double offset = 0;

			if(type == 0 && new Random().nextInt(10) == 0) {
				offset = (new Random().nextDouble() - new Random().nextDouble());
			}
			if(type == 1) {
				offset = 0.01 * (new Random().nextDouble() - new Random().nextDouble());
			}

			rtn.add(genome.get(i) + offset);
		}
		return rtn;
	}

	@Override
	public double getScore() {
		return score;
	}

	@Override
	public void output() {
		System.out.println(" "+score);
		
		if(jframe == null) {
			jframe= new JFrame();
			jframe.setVisible(true);
			jframe.setSize(screendimension, screendimension);		
			jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		Graphics g = jframe.getGraphics();
		g.setColor(Color.BLACK);
		g.fillRect(0, 0, screendimension, screendimension);


		g.setColor(Color.ORANGE);
		
		double x = 0.5;
		double y = 0.5;

		for(int i=0; i<50000; i++) {
			x = getXprim(x, y);
			y = getYprim(x, y);

			g.fillRect(400+(int)(200*x), 400+(int)(200*y), 1, 1);
		}

	}

	@Override
	public ArrayList<Double> newGenome() {
		ArrayList<Double> genes = new ArrayList<Double>();
		
		for(int i=0; i<14; i++){
			genes.add(2*(new Random().nextDouble()-new Random().nextDouble()));
			
		}
		return genes;
	}

	private double getXprim(double x, double y) {
		return genome.get(1)*x + genome.get(2)*x*x + genome.get(3)*y + genome.get(4)*y*y + genome.get(5)*x*y + x*x*x*genome.get(12);
	}

	private double getYprim(double x, double y) {
		return genome.get(7)*x + genome.get(8)*x*x + genome.get(9)*y + genome.get(10)*y*y + genome.get(11)*x*y + y*y*y*genome.get(13);
	}


}
