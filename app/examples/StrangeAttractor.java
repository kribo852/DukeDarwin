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

public class StrangeAttractor extends Lifeform<StrangeAttractor.Node, ArrayList<StrangeAttractor.Node>> {

	double score;
	int screendimension = 800;
	
	static JFrame jframe;

	public StrangeAttractor(ArrayList<Node> genome) {
		super(genome);
		score = 0;
	}

	@Override
	public void run() {
		score = 0;
		int [][] buckets = new int[100][100];

		double x = 0;
		double y = 0;
		double frequencyhits = 1;

		for(int i=0; i<50000; i++) {
			x = genome.get(0).getValue(x, y);
			y = genome.get(1).getValue(x, y);

			if ((0.5 + x) >= 0 && (0.5 + x) < 1 && (0.5 + y) >= 0 && (0.5 + y) < 1) {
				buckets[(int)(100*(0.5 + x))][(int)(100*(0.5 + y))] ++;
				frequencyhits ++;
			}

		}

		for (int i=0; i<buckets.length; i++) {
			for (int j=0; j<buckets[0].length; j++) {
				if(buckets[i][j] != 0 ) {
					score -= (buckets[i][j]/frequencyhits) * Math.log(buckets[i][j]/frequencyhits) * 
					Math.log(frequencyhits);
				}
			}	
		}
	}

	@Override
	public ArrayList<Node> mutate(ArrayList<Node> genome1, ArrayList<Node> genome2) {
		return null;
	}

	@Override
	public ArrayList<Node> mutate(ArrayList<Node> genome) {
		ArrayList<Node> rtn = new ArrayList<Node>();

		rtn.add(genome.get(0).mutate());
		rtn.add(genome.get(1).mutate());
		
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


		g.setColor(new Color(255, 100, 0, 25));
		
		double x = 0;
		double y = 0;

		for(int i=0; i<100000; i++) {
			x = genome.get(0).getValue(x, y);
			y = genome.get(1).getValue(x, y);

			g.fillRect(400+(int)(400*x), 400+(int)(400*y), 1, 1);
		}

	}

	@Override
	public ArrayList<Node> newGenome() {
		ArrayList<Node> genes = new ArrayList<Node>();
		
		genes.add(Node.generate());
		genes.add(Node.generate());

		return genes;
	}

	public static class Node {
		double constant;

		Node xUp;
		Node yUp;

		public double getValue(double x, double y) {
			double rtn =  constant + 
			(xUp != null ? x * xUp.getValue(x, y) : 0) +  
			(yUp != null ? y * yUp.getValue(x, y) : 0);

			//System.out.println("rtn: " + rtn);
			return rtn;
		}

		public Node mutate() {
			return mutate(new Random().nextInt(2));
		}

		private Node mutate(int mode) {
			double c = constant;

			if(mode == 0) {
				if(new Random().nextInt(10) == 0) {
					c = (new Random().nextGaussian());	
				} else {
					c += (new Random().nextGaussian());
				}
			} 
			if(mode == 1) {
				c += 0.1*(new Random().nextGaussian())*(new Random().nextGaussian());
			}
			
			Node mxUp = xUp != null ? xUp.mutate(mode) : null;
			Node myUp = yUp != null ? yUp.mutate(mode) : null;

			return new Node(c, mxUp, myUp);
		}

		private Node(double c, Node subXup, Node subYup) {
			constant = c;
			xUp = subXup;
			yUp = subYup;
		}

		private Node() {

		}

		public static Node generate() {
			Node rtn = new Node();
			rtn.generate(4);

			return rtn;
		}

		private void generate(int layersLeft) {
			constant = 1.5 * (new Random().nextGaussian());
			if (layersLeft > 0) {
				xUp = new Node();
				yUp = new Node();
				xUp.generate(layersLeft-1);
				yUp.generate(layersLeft-1);
			}
		} 
	}

}
