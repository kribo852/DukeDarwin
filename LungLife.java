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

import java.io.IOException;
import java.io.File;

import javax.imageio.ImageIO;
import javax.swing.JFileChooser;
import javax.swing.JFrame;

import java.awt.Graphics;

public class LungLife extends Lifeform {

	double score;
	int dimension = 250;
	
	static JFrame jframe;

	public <T> LungLife(Collection<T> genome) {
		super(genome);
		score=0;
	}

	@Override
	public void run() {
		ScoreConsumer scoreConsumer = new ScoreConsumer(searchable(genome));
		genericFloodfill(dimension/2,dimension/2, 1, searchable(genome), scoreConsumer);
		score = scoreConsumer.calcScore();
	}
	
	boolean[][] searchable(Collection tmp) {
		boolean[][] search = new boolean[dimension][dimension];

		for(int i=0; i<dimension; i++)for(int j=0; j<dimension; j++) {
			search[i][j] = ((ArrayList<Boolean>)tmp).get(i+j*dimension);
		}
		return search;
	}

	@Override
	public Collection mutate(Collection genome1, Collection genome2) {
		List<Boolean> rtn = new ArrayList<Boolean>();
		List<Boolean> tmp1=(List<Boolean>)genome1;
		List<Boolean> tmp2=(List<Boolean>)genome2;

		for(int i=0; i<genome1.size(); i++) {
			rtn.add(new Random().nextBoolean() ? tmp1.get(i) : tmp2.get(i));
		}
		
		boolean search[][]= searchable(rtn);
		
		rtn = new ArrayList<Boolean>();
		
		if(new Random().nextBoolean()){
			disorderMutate(search);
		}else{
			copyMutate(search);
		}
		
		for(int i=0; i<dimension; i++)for(int j=0; j<dimension; j++) {
			rtn.add(search[i][j]);
		}
		return rtn;
	}
	
	public void disorderMutate(boolean search[][]) {
		int mutationrate=1+(new Random()).nextInt(dimension*dimension);
		
		for(int i=0; i<dimension; i++)for(int j=0; j<dimension; j++) {
			if((new Random()).nextInt(mutationrate) == 0) {
				search[i][j] = (new Random()).nextBoolean();
			}
		}
	}
	
	public void copyMutate(boolean search[][]) {
		int copysize=5+new Random().nextInt(15);
		boolean[][] copyArray = new boolean[copysize][copysize];
		
		int offsetx=new Random().nextInt(search.length-copysize);
		int offsety=new Random().nextInt(search[0].length-copysize);
		
		for(int i=0; i<copysize; i++) for(int j=0; j<copysize; j++) {
			copyArray[i][j] = search[i+offsetx][j+offsety];
		}
		
		while(new Random().nextInt(5)!=0) {
			offsetx = new Random().nextInt(search.length - copysize);
			offsety = new Random().nextInt(search[0].length - copysize);
			for(int i=0; i<copysize; i++) for(int j=0; j<copysize; j++) {
				search[i+offsetx][j+offsety] = copyArray[i][j];
			}	
		}
	}

	@Override
	public Collection mutate(Collection genome) {
		return null;
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
			jframe.setSize(1000, 1000);		
			jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		}
		Graphics g=jframe.getGraphics();
		PaintConsumer paintconsumer = new PaintConsumer(dimension);
		genericFloodfill(dimension/2,dimension/2, 1, searchable(genome), paintconsumer);
		
		paintconsumer.paint(g);	
	}

	@Override
	public Collection newGenome() {
		List<Boolean> coordinategenes = new ArrayList<Boolean>();
		
		for(int i=0; i<dimension*dimension; i++){
			
			int xpos=i%dimension;
			int ypos=i/dimension;
			
			if((xpos==dimension/2 || ypos==dimension/2) && (Math.abs(xpos-dimension/2)<dimension/5 &&  Math.abs(ypos-dimension/2)<dimension/5)){
				coordinategenes.add(true);
			}else{
				coordinategenes.add(new Random().nextBoolean());
			}
		}
		return coordinategenes;
	}
	
	private void genericFloodfill(int initx, int inity, int distance, boolean[][] search, Consumer<Integer> consumer){
		TreeSet<Integer> old = new TreeSet<Integer>();
		HashSet<Integer> all = new HashSet<Integer>();

		old.add(initx+(inity<<16));
		all.add(initx+(inity<<16));
		TreeSet<Integer> recent = new TreeSet<Integer>(); 
		
		while(!old.isEmpty()){
		
			while(!old.isEmpty()){
				int position=old.pollFirst();
				int x=position&0xFFFF;
				int y=position>>16;
				
				for(int i=-1; i<2; i++)for(int j=-1; j<2; j++){
					if(i+x>=0 && i+x<dimension && j+y>=0 && j+y<dimension) {
						if(Math.abs(i+j) == 1) {
							int parse=(i+x)+((j+y)<<16);
							if(search[i+x][j+y] && !all.contains(parse)) {
								recent.add(parse);
								consumer.accept(i+x);
								consumer.accept(j+y);
								consumer.accept(distance);
							}
						}
					}
				}	
			}
			distance++;
			old.addAll(recent);
			all.addAll(recent);
			recent = new TreeSet<Integer>(); 
		}			
	}
}

class PaintConsumer implements Consumer<Integer> {
	int x=-1;
	int y=-1;
	int dimension;
	int colormultiplier=357;
	int colornounce=127;
	HashMap<Integer, Integer> distances = new HashMap<Integer, Integer>();
	HashMap<Integer, Integer> colours = new HashMap<Integer, Integer>();
	
	public PaintConsumer(int dimension) {
		this.dimension = dimension;
		
		int c=colornounce;
		for(int i=0; i<dimension; i++) {
			colours.put(i, c);
			c=c*colormultiplier+colornounce;
		}
	}
	
	
	@Override
	public void accept(Integer i){
		if(x == -1){
			x = i;
		}
		else if(y == -1){
			y = i;
		}
		else {
			distances.put(x+dimension*y, i);
			x = -1;
			y = -1;			
		}
	}
	
	public void paint(Graphics g) {
		for(int i=0; i<dimension; i++)for(int j=0; j<dimension; j++) {
			if(distances.containsKey(i+j*dimension)) {
				Integer colourvalue=colours.get(distances.get(i+j*dimension));
				if(colourvalue == null ){
					colourvalue = new Random().nextInt();
				}
				
				g.setColor(new Color(colourvalue&0x00FFFFFF));
			} else {
				g.setColor(Color.black);
			}
			g.fillRect(i*2+100, j*2+100, 2, 2);
		}
	}
	
	
}

class ScoreConsumer implements Consumer<Integer>{
	int x=-1;
	int y=-1;

	final boolean[][] searchable;
	HashMap<Integer, Integer> coveredoxygene = new HashMap<Integer, Integer>();
	
	public ScoreConsumer(boolean[][] searchable) {
		this.searchable=searchable;	
	}
	
	@Override
	public void accept(Integer integer){
		if(x == -1){
			x = integer;
		}
		else if(y == -1){
			y = integer;
		}
		else {
			for(int i = -1; i< 2; i++)for(int j = -1; j< 2; j++) {
				if(Math.abs(i+j) == 1 && i+x>=0 && i+x<searchable.length && j+y>=0 && j+y<searchable.length) {
					if(!searchable[i+x][j+y] && !coveredoxygene.containsKey(x+i+(y+j)*searchable.length)){
						coveredoxygene.put(x+i+(y+j)*searchable.length, integer);
					}
				}
			}
			x = -1;
			y = -1;			
		}
	}
	
	public double calcScore() {
		double rtn=0;
		for(Integer value: coveredoxygene.values()){
			double distance=value;
			rtn+=1.0/distance;
		}
		return rtn;
	}
}
