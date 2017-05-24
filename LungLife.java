import java.util.HashSet;
import java.util.HashMap;
import java.util.Random;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.File;
import javax.imageio.ImageIO;
import java.awt.Color;
import java.awt.Graphics;

final class LungLife extends LifeForm {
	
	HashSet<Integer> veins;
	final int startx=1000;
	final int starty=1000;
	
	public LungLife(final Integer[] genome){
		super(genome);
		veins=new HashSet<Integer>();
	}
	
	public Integer[] mutate(final Integer[] genome1, final Integer[] genome2){
		return genome1;
	}
	
	public Integer[] mutate(final Integer[] genome) {
		
		Integer[] rtn=new Integer[genome.length];
		
		int rate=5+(new Random()).nextInt(20);
		rate*=10;
		
		for(int i=0; i<rtn.length; i++) {
			
			int fallout=(new Random()).nextInt(rate);
			
			if(fallout==0){
				rtn[i]=newGene(i);
			}else if(fallout==1){
				rtn[i]=((newGene(i)>>2)<<2)+(genome[i]&3);
			}else if(fallout==2){
				
				int direction=(new Random()).nextBoolean()? 3: 5;
				
				for(int j=i; j<rtn.length; j++){
					int newdir=((direction+(genome[i]&3))%4);
					rtn[i]=((genome[i]>>2)<<2)+newdir;
				}
				
			}else{
				rtn[i]=genome[i];
			}
		}
		
		return rtn;
	}
		
	public double getScore() {
		HashSet<Integer> visited=new HashSet<Integer>();
		ArrayList<Integer> active=new ArrayList<Integer>(); 
		HashSet<Integer> airused=new HashSet<Integer>(); 
		
		active.add(getIndex(startx,starty));
		visited.add(getIndex(startx,starty));	
		while(!active.isEmpty()){
			int[] position=getPosition(active.remove(0));
			
			for(int i=-1; i<2; i++)for(int j=-1; j<2; j++){
				if(!veins.contains(getIndex(position[0]+i, position[1]+j))){
					airused.add(getIndex(position[0]+i, position[1]+j));
				}else{
					if(!visited.contains(getIndex(position[0]+i, position[1]+j))){
						active.add(getIndex(position[0]+i, position[1]+j));
						visited.add(getIndex(position[0]+i, position[1]+j));
					}
				}
			}
		}
		return airused.size();
	}
	
	public void output(){
		int minx=Integer.MAX_VALUE, miny=Integer.MAX_VALUE, maxx=Integer.MIN_VALUE, maxy=Integer.MIN_VALUE;
		System.out.println("veins size: "+veins.size()+" score: "+getScore());
		for(Integer i: veins){
			int[] position=getPosition(i);
			if(position[0]<minx)minx=position[0];
			if(position[1]<miny)miny=position[1];
			if(position[0]>maxx)maxx=position[0];
			if(position[1]>maxy)maxy=position[1];
		}
		
		BufferedImage image=new BufferedImage(4*(maxx-minx+1), 4*(maxy-miny+1), 1);
		Graphics g=image.getGraphics();
		g.setColor(new Color(0,100,255));
		for(Integer i: veins){
			int[] position=getPosition(i);
			g.fillRect(4*(position[0]-minx), 4*(position[1]-miny),4,4);
		}
		
		try{
			ImageIO.write(image, "png" , new File("sav"+(getScore())+".png"));
		}catch(Exception e){
			System.out.println(e);
		}
	}
	
	public Integer [] newGenome(){
		Integer[] genome=new Integer[15000];
		for(int i=0; i<genome.length; i++){
			genome[i]=newGene(i);
		} 
		return genome;
	}
	
	private Integer newGene(int previousindexes){
		return (((new Random()).nextInt(previousindexes+1))<<2)+(new Random()).nextInt(4);//
	}
	
	public void run(){
		run(startx,starty);
	}
	
	private void run(int x, int y){
		insertVein(x,y);
		HashMap<Integer,int[]> previous=new HashMap<Integer,int[]>();
		previous.put(0, new int[]{x,y});
		
		for(int i=0; i<genome.length; i++){
			int previd=genome[i]>>2;
			int dir=genome[i]&3;
			int[] prevpos=previous.get(previd);
			
			//if(prevpos==null)continue;
			//System.out.println("direction "+dir);
			
			if(dir==0){
				previous.put(i+1, new int[]{prevpos[0]+1,prevpos[1]});
				insertVein(prevpos[0]+1,prevpos[1]);
			}
			if(dir==1){
				previous.put(i+1, new int[]{prevpos[0],prevpos[1]+1});
				insertVein(prevpos[0],prevpos[1]+1);
			}
			if(dir==2){
				previous.put(i+1, new int[]{prevpos[0]-1,prevpos[1]});
				insertVein(prevpos[0]-1,prevpos[1]);
			}
			if(dir==3){
				previous.put(i+1, new int[]{prevpos[0],prevpos[1]-1});
				insertVein(prevpos[0],prevpos[1]-1);
			}
		}
	}
	
	private void insertVein(int x, int y){
		veins.add(getIndex(x,y));
	}
	
	private Integer getIndex(int x, int y){
		return ((y<<16)+x);
	}
	
	private int[] getPosition(Integer i){
		return new int[]{i&0x0000FFFF , i>>16};
	}
}
