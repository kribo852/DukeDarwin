import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;

final class BallWeight extends LifeForm {
	
	double score;
	
	public BallWeight(final Integer[] genome){
		super(genome);
		score=0;
	}
	
	public Integer[] mutate(final Integer[] genome1, final Integer[] genome2){
		Integer[] rtn=new Integer[genome1.length];
		Integer currentgenome[]=genome1;
		for(int i=0; i<genome1.length; i++) {
			if(i%8==0)currentgenome=(new Random()).nextBoolean() ? genome1: genome2;
			rtn[i]= currentgenome[i];
		}
		return mutate(rtn);
	}
	
	public Integer[] mutate(final Integer[] genome) {
		int place=(new Random()).nextInt(genome.length);
		Integer[] rtn=new Integer[genome.length];
		
		for(int i=0; i<genome.length; i++)rtn[i]=genome[i];
		
		if(place<7*8) {
			HashSet<Integer> freeballs=new HashSet<Integer>();
			for(int i=0; i<12; i++)freeballs.add(i);
			for(int i=0; i<8; i++) {
				if(i!=place%8)freeballs.remove(rtn[(place-place%8)+i]);
			}
			ArrayList<Integer> freeballlist=new ArrayList<Integer>();
			freeballlist.addAll(freeballs);
			rtn[place]=freeballlist.remove((new Random()).nextInt(freeballlist.size()));//
		}else {
			rtn[place]=newGene();
		}
		return rtn;
	}
		
	public double getScore() {
		return score;
	}
	
	public void output(){
		System.out.println("score: "+score/10+"%");
		for(int i=0; i<8*7; i++){
			if(i%4==0){
				if(i%8!=0)System.out.print("   ");
				else System.out.println();
			}
			System.out.print(" "+genome[i]);
		}
		
		System.out.println("result");
		for(int i=8*7; i<8*7+27; i++){
			System.out.print(" "+genome[i]);
		}
	}
	
	public Integer [] newGenome(){
		Integer[] genome=new Integer[8*7+27];
		ArrayList<Integer> freeballs=new ArrayList<Integer>();
		for(int i=0; i<genome.length; i++){
			if(i%8==0){
				freeballs=new ArrayList<Integer>();
				for(int j=0; j<12; j++)freeballs.add(j);
			}
			if(i<8*7)
				genome[i]=freeballs.remove((new Random()).nextInt(freeballs.size()));
			else
				genome[i]=newGene();
		} 
		return genome;
	}
	
	private Integer newGene(){
		return (new Random()).nextInt(12);//
	}
	
	public void run(){
		for(int i=0; i<1000; i++) {
			int[] probleminstance=new int[12];
				probleminstance[(new Random()).nextInt(probleminstance.length)]=(new Random()).nextBoolean() ? -1 : 1;
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
			
				Integer choosenball=genome[7*8+weight1*9+3*weight2+weight3];
				score+=probleminstance[choosenball]!=0 ? 1: 0;
		}
	}
	
	private int weigh(int[] probleminstance, Integer[] active, int place){
		int rtn=0;
		for(int i=0; i<4; i++)rtn+=probleminstance[active[place+i]];
		for(int i=4; i<8; i++)rtn-=probleminstance[active[place+i]];
		return rtn;
	}
	
}
