package app.examples;

import app.Lifeform;
import java.util.ArrayList;
import java.util.Random;

/**
*Implementation of a solver of the 8 queens problem.
**/
public class EightQueens extends Lifeform<Queen, ArrayList<Queen>> {
	static int iterationstilldone = 0;
	static boolean finished = false;
	final int boardsize=8;
	int score = 0; 

	public EightQueens(final ArrayList<Queen> genome){
		super(genome);
	}
	
	@Override
	public ArrayList<Queen> mutate(final ArrayList<Queen> genome1, final ArrayList<Queen> genome2) {
		return null;
	}
	
	@Override
	public ArrayList<Queen> mutate(final ArrayList<Queen> genome) {
		ArrayList<Queen> rtn = new ArrayList<>();
		genome.forEach(a -> rtn.add(new Queen(a.getX(),  a.getY())));
		do{
			int index = randomposition();
			Queen q = rtn.get(index);
			rtn.set(index, new Queen(randomposition(), q.getY()));
		}while(randomposition() == 0);

		return rtn;
	}
	
	@Override
	public double getScore() {
		return score;
	}
	
	@Override
	public void output() {
		System.out.println("The score was: "+ score+ " where 0 is a solution.");
		printBoard();
		System.out.println(iterationstilldone+ " lifeforms were tested.");
		if(finished) {
			System.exit(0);
		}
	}
	
	void printBoard(){
		for (int i = 0; i<boardsize; i++) {
			for(int j = 0; j<boardsize; j++) {
				if (genome.get(i).getX() == j) {
					System.out.print("Q");
				} else{
					System.out.print((i + j)%2 == 0 ? "#":" ");
				}
				System.out.print((i + j)%2 == 0 ? "#":" ");
			}
			System.out.println();
		}
	}

	@Override	
	public ArrayList<Queen> newGenome() {
		ArrayList<Queen> rtn = new ArrayList<>();		
		for (int i = 0; i<boardsize; i++) {
			rtn.add(new Queen(randomposition(), i));
		}
		return rtn;
	}

	int randomposition() {
		return new Random().nextInt(boardsize);
	}

	@Override
	public void run() {
		score = calculateScore();
		if (score == 0) {
			finished = true;
		}
		if(!finished){
			iterationstilldone++;
		}
	}

	int calculateScore() {
		int score = 0;
		for (int i = 0; i<boardsize; i++) {
			for(int j = i+1; j<boardsize; j++) {
				if (genome.get(i).blocks(genome.get(j))) {
					score--;
				}
			}
		}
		return score;
	}
}

class Queen{
	byte x, y;

	public Queen(int x, int y){
		this.x = (byte)x;
		this.y = (byte)y;
	}

	byte getX(){
		return x;
	}

	byte getY(){
		return y;
	}

	boolean blocks(Queen other){
		if(other.getX() == getX() || other.getY() == getY()) {
			return true;
		}

		if(other.getX() - getX() == other.getY() - getY()) {
			return true;
		}

		// position {1,8} unsettles {8,1}
		if(other.getX() - getY() == getX() - other.getY()) {
			return true;
		}
		return false;
	}
}
