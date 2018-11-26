package app.examples;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Random;

import app.Lifeform;

/**
* 
**/
public class TowerLife extends Lifeform<Integer, ArrayList<Integer>> {

	private static final int SIZE = 100;

	private static final double MAXWEIGHT = 25;	

	double score = 0;

	//sets the genome 
	public TowerLife(final ArrayList<Integer> genome){
		super(genome);
	}
	
	//sexual reproduction
	public ArrayList<Integer> mutate(final ArrayList<Integer> genome1, final ArrayList<Integer> genome2) {
		boolean[][] filltower1 =  fillTower(genome1);
		boolean[][] filltower2 =  fillTower(genome2);
		boolean[][] hölje = hölje(filltower1, filltower2);

		int x = getMutationXPosition();
		int y = (new Random()).nextInt(SIZE); 
		if(hölje[x][y]) {
			filltower1[x][y] = !filltower1[x][y];
			filltower2[x][y] = !filltower2[x][y];
		}
		while(true) {
		
			if((new Random()).nextDouble() < 0.1){
				break;
			}

			x = getMutationXPosition();
			y = (new Random()).nextInt(SIZE); 
			
			if(!hölje[x][y]) continue;
			
			filltower1[x][y] = !filltower1[x][y];
			filltower2[x][y] = !filltower2[x][y];	
			
		}

		ArrayList<Integer> rtn = new ArrayList<Integer>();
		for(int i=0; i<SIZE; i++) for(int j=0; j<SIZE; j++) {		
			if((new Random()).nextBoolean()) {
				if(filltower1[i][j]) {
					rtn.add(i);
					rtn.add(j);
				}
			} else {
				if(filltower2[i][j]) {
					rtn.add(i);
					rtn.add(j);
				}
			}
		}
		return rtn;
	}

	private int getMutationXPosition() {
		int rtn = (int)(SIZE/2+(SIZE/2)*(new Random()).nextGaussian());
		while(rtn<0 || rtn>=SIZE) {
			rtn = (int)(SIZE/2+(SIZE/2)*(new Random()).nextGaussian());			
		}
		return rtn;
	}



	boolean[][] hölje(final boolean[][] filltower1, final boolean[][] filltower2) {
		boolean [][] rtn = new boolean[SIZE][SIZE];
		for(int i=0; i<SIZE; i++) for(int j=0; j<SIZE; j++) {
			for(int k=-1; k<2; k++) for(int l=-1; l<2; l++) {
				if(i+k>=0 && i+k<SIZE && j+l>=0 && j+l<SIZE) {
					if(filltower1[i+k][j+l] || filltower2[i+k][j+l]) {
						rtn[i][j] = true;
					}
				}
			}			
		}
		return rtn;
	}
	
	//asexual reproduction
	public ArrayList<Integer> mutate(final ArrayList<Integer> genome) {
		return null;
	}
	
	//calculate the score of own genome
	public double getScore() {
		return score;
	}
	
	//in some manner, output the result of the simulation
	public void output() {
		boolean[][] filltower = fillTower(genome);
		for(int ity = filltower[0].length-1; ity >= 0; ity--) {
			System.out.println("");	
			for(int itx = 0; itx < filltower.length; itx++) {
				if(filltower[itx][ity]) {
					System.out.print("#");
				} else {
					System.out.print(" ");
				}
			} 
		}
		System.out.println("Score: " +score);
	}
	
	public ArrayList<Integer> newGenome() {
		ArrayList<Integer> rtn = new ArrayList<Integer>();
		for(int i=0; i<2; i++){
			rtn.add(SIZE/2);
			rtn.add(i);
		}
		return rtn;
	}	

	public void run(){
		score = findTop(fillTower(genome));
	}
	
	boolean[][] fillTower(final ArrayList<Integer> genome) {
		boolean[][] filltower =  new boolean[SIZE][SIZE];
		for(int i=0; i<genome.size()/2; i++) {
			filltower[genome.get(2*i)][genome.get(2*i+1)] = true;
		}
		return filltower;
	} 

	int findTop(boolean[][] filltower){
		for(int i = filltower[0].length-1; i >= 0; i--) {
			for(int j=0; j<filltower.length; j++) {
				if(filltower[j][i]) {
					return floodfill(filltower, j, i);
				}
			}
		}
		return 0;
	}

	int floodfill(boolean[][] filltower, int px, int py){
		double[][] weight = new double[SIZE][SIZE];
		for(int ity = filltower[0].length-1; ity >= 0; ity--) for(int itx = 0; itx < filltower.length; itx++) {
			if(weight[itx][ity] >= MAXWEIGHT) {
				return 0;			
			}
			if(filltower[itx][ity]){
				if(ity == 0) {
					return py;
				}
				double numberofblocksbelow = 0;
				for(int i = -1; i < 2 ; i++) {
					if(i+itx >= 0 && i+itx < SIZE) {
						if(filltower[itx+i][ity-1]) {
							numberofblocksbelow++;
						}
					}
				}
				if(numberofblocksbelow == 0) return 0;
				double dividedweight = (weight[itx][ity]+1.0)/ numberofblocksbelow;  
				for(int i = -1; i < 2 ; i++) {
					if(i+itx >= 0 && i+itx < SIZE) {
						if(filltower[itx+i][ity-1]) {
							weight[itx+i][ity-1] += dividedweight;
						}
					}
				}
			}	
		}
		return 0;
	}
}
