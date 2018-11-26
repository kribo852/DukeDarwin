package app.examples;

import java.util.Random;
import java.util.ArrayList;
import java.util.Collection;
import app.Lifeform;

public class BallWeight extends Lifeform<Integer, ArrayList<Integer>> {
	
	double score=0;
	
	public BallWeight(final ArrayList<Integer> genome){
		super(genome);
	}
	
	public ArrayList<Integer> mutate(final ArrayList<Integer> genome1, ArrayList<Integer> genome2){
		ArrayList<Integer> rtn=new ArrayList<Integer>();
		ArrayList<Integer> currentgenome=genome1;
		for(int i=0; i<genome1.size(); i++) {
			if(i%8==0)currentgenome=(new Random()).nextBoolean() ? genome1: genome2;
			rtn.add(i, currentgenome.get(i));
		}
		
		mutate(rtn);//mutation of reference object
		
		return rtn;
	}
	
	//modifies reference, not to be used from outside this class
	public ArrayList<Integer> mutate(final ArrayList<Integer> genome) {
		
		int place=8 + (new Random()).nextInt(genome.size() - 8);//change this position, first 8 positions are always 1,2,3,4  5,6,7,8
				
		if(place < 7*8) {
			ArrayList<Integer> freeballs=new ArrayList<Integer>();
			for(int i=0; i<12; i++)freeballs.add(i);
			for(int i=0; i<8; i++) {
				freeballs.remove(genome.get((place-place%8)+i));//calculate free balls that can be used
            }
			genome.set(place,freeballs.remove((new Random()).nextInt(freeballs.size())));//
       }else {
               genome.set(place, (new Random()).nextInt(12));
       }
       
       return genome;
	}
		
	public double getScore() {
		return score*(25/6.0);//into percent
	}
	
	public void output(){
		System.out.println("score: "+getScore()+"%");
		for(int i=0; i<8*7; i++){
			if(i%4==0){
				if(i%8!=0)System.out.print("   ");
				else System.out.println();
			}
			System.out.print(" "+genome.get(i));
		}
		
		System.out.println("\nresult");
		for(int i=8*7; i<8*7+27; i++){
			System.out.print(" "+genome.get(i));
		}
		
		BallWeight baseline=new BallWeight(newGenome());
		baseline.run();
		
		System.out.println("\nbaseline: "+ baseline.getScore()+"%"); //score of unevolved lifeform
		
	}
	
	public ArrayList<Integer> newGenome(){
		ArrayList<Integer> genome=new ArrayList<Integer>();//[8*7+27] 8 balls per turn, 1+2+4 choices/options
		ArrayList<Integer> freeballs=new ArrayList<Integer>();
		for(int i=0; i<8; i++){genome.add(i);}
		
		for(int i=8; i<8*7+27; i++){
			if(i%8==0){
				freeballs=new ArrayList<Integer>();
				for(int j=0; j<12; j++)freeballs.add(j);
			}
			if(i<8*7)
				genome.add(freeballs.remove((new Random()).nextInt(freeballs.size())));
			else
				genome.add((new Random()).nextInt(12));
		} 
		return genome;
	}
	
	public void run(){
		for(int i=0; i<24; i++) {//tests all possibilities
			int[] probleminstance=new int[12];
				probleminstance[i%12]=i/12 == 0 ? -1 : 1;
				int weight1=weigh(probleminstance, genome, 0);
				int weight2=0, weight3=0;
				if(weight1==0){
					weight2=weigh(probleminstance, genome, 8);
					if(weight2==0){
						weight3=weigh(probleminstance, genome, 24);
					}else{
						weight3=weigh(probleminstance, genome, 32);
					}
				}else{
					weight2=weigh(probleminstance, genome, 16);
					if(weight2==0){
						weight3=weigh(probleminstance, genome, 40);
					}else{
						weight3=weigh(probleminstance, genome, 48);
					}
				}
				
				weight1++;//make results indexable
				weight2++;
				weight3++;
				
				if(weight1<0 || weight1>2 || weight2<0 || weight2>2 || weight3<0 || weight3>2){
					System.out.println("indexing error");
					System.out.println("w1: "+weight1);
					System.out.println("w2: "+weight2);
					System.out.println("w3: "+weight3);
				}
			
				Integer choosenball=genome.get(7*8+weight1*9+3*weight2+weight3);
				score+=probleminstance[choosenball]!=0 ? 1: 0;
		}
	}
	
	private int weigh(int[] probleminstance, ArrayList<Integer> active, int place){
		int rtn=0;
		for(int i=0; i<4; i++)rtn+=probleminstance[active.get(place+i)];
		for(int i=4; i<8; i++)rtn-=probleminstance[active.get(place+i)];
		return rtn;
	}
}
