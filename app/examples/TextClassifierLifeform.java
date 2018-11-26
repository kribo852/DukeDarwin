package app.examples;

import java.util.Random;
import java.util.ArrayList;
import java.util.HashSet;
import java.io.BufferedReader; 
import java.io.FileReader; 
import java.io.IOException; 

import app.Lifeform;

public class TextClassifierLifeform extends Lifeform<StringScore, ArrayList<StringScore>> {
	 
	 double score = 0;
	 
	 static String trainingcode, trainingtext;
	 
	 static ArrayList<String> codeevaluation, textevaluation;
	
	public TextClassifierLifeform(final ArrayList<StringScore> genome){
		super(genome);
		String textsample, codesample;
		if(trainingcode == null) {
			codesample = read("CodeReference.txt");
			codeevaluation = new ArrayList<String>();
			trainingcode = divideText(codeevaluation, codesample);
		}
		if(trainingtext == null) {
			textsample = read("TextReference.txt");
			textevaluation = new ArrayList<String>();
			trainingtext = divideText(textevaluation, textsample);
		}
		//System.out.println("created object");
	}
	
	@Override
	public void run() {
		score = calculateScoreWhenCode(trainingcode) + calculateScoreWhenText(trainingtext);
	}
	
	@Override
	public ArrayList<StringScore> mutate(final ArrayList<StringScore> genome1, final ArrayList<StringScore> genome2) {
		
		ArrayList<StringScore> rtn= new ArrayList<StringScore>();
		
		for(int i=0; i<genome1.size(); i++) {
			
			StringScore stringscore = (new Random()).nextBoolean() ? genome1.get(i) : genome2.get(i);
			
			StringScore stringscorecopy = new StringScore();
			
			stringscorecopy.string=stringscore.string;
			stringscorecopy.score1=stringscore.score1;
			stringscorecopy.score2=stringscore.score2; 
			
			rtn.add(stringscorecopy);
		}
		
		for(int i=0; i<3; i++) {
			StringScore stringscore = rtn.get((new Random()).nextInt(genome1.size()));
			StringScore replace = newGene();
		
			int mutateoption = (new Random()).nextInt(4); 
		
			/*if(mutateoption == 0) {
				stringscore.string = mutateString(stringscore.string);
			}*/
		
			if(mutateoption == 1) {
				stringscore.string = replace.string;
			}
		
			if(mutateoption == 2) {
				stringscore.score1 = replace.score1;
			}
		
			if(mutateoption == 3) {
				stringscore.score2= replace.score2;
			}
		}
		
		return rtn;
	}
	
	@Override
	public ArrayList<StringScore> mutate(final ArrayList<StringScore> genome) {
		
		return newGenome();
	}
	
	@Override
	public double getScore() {
		
		return score;
	}
	
	@Override
	public void output() {
		int correctanswers = 0; 
		for(String s : codeevaluation) {
			if(calculateScoreWhenCode(s) > calculateScoreWhenText(s)) {
				correctanswers++;
			}
		}
		
		for(String s : textevaluation) {
			if(calculateScoreWhenCode(s) < calculateScoreWhenText(s)) {
				correctanswers++;
			}
		}
		
		System.out.println("reference score:"+(correctanswers)+" out of "+ (codeevaluation.size()+ textevaluation.size()));
		System.out.println(((double)correctanswers/(codeevaluation.size()+ textevaluation.size())));
	}
	
	private double calculateScoreWhenCode(String code) {
		double sumscoretext = 0;
		double sumscorecode = 0;
		
		for(StringScore stringscore : genome) {
			if(code.contains(stringscore.string)){
				sumscoretext -= stringscore.score1;
				sumscorecode += stringscore.score2;
			}
		}
		return sumscorecode+sumscoretext;
	}
	
	private double calculateScoreWhenText(String text) {
		double sumscoretext = 0;
		double sumscorecode = 0;
		
		for(StringScore stringscore : genome) {
			if(text.contains(stringscore.string)){
				sumscoretext += stringscore.score1;
				sumscorecode -= stringscore.score2;
			}
		}
		return sumscorecode+sumscoretext;
	}
	
	@Override
	public final ArrayList<StringScore> newGenome() {
		ArrayList<StringScore> rtn = new ArrayList<>();
		
		for(int i=0; i<500; i++){
			rtn.add(newGene());
		}
		return rtn;
	}
	
	StringScore newGene() {
		StringScore s= new StringScore();	
		s.string="";
		for(int len=0; len<3 && (new Random()).nextBoolean(); len++){
			s.string = s.string+(char)((new Random()).nextInt(0x7F));
		}
		s.score1 = (new Random()).nextDouble();
		s.score2 = (new Random()).nextDouble();
		return s;
	}
	
	String mutateString(final String s){
		if((new Random()).nextBoolean()) {
			if(!s.isEmpty()){
				return s.substring(1);
			}
		} else {
			if(s.length()<2) {
				return s + (char)((new Random()).nextInt(0x7F));
			}
		}
		return s;
	}
	
	String read(String file){
		String rtn;
		try {
			BufferedReader br = new BufferedReader(new FileReader(file));
			StringBuilder sb = new StringBuilder();
			String line = br.readLine();

			while (line != null) {
				sb.append(line);
				sb.append(System.lineSeparator());
				line = br.readLine();
			}
		rtn = sb.toString();
		br.close();
		} catch(IOException e){
			System.out.println("error " +e);
			return "";
		} 
		return rtn;
	}
	
	//divides text sample into training data and evaluation data 
	private String divideText(final ArrayList<String> evaluation, final String sample) {
		final int runlength = 40;
		StringBuilder sb = new StringBuilder();
		HashSet<String> evaluationtmp = new HashSet<>();
		for(int i=0; i<sample.length(); i += runlength){
			if((i/runlength)%2 == 0){
				sb.append(sample.substring(i, Math.min(sample.length(), i+runlength)));
			}else{
				evaluationtmp.add(sample.substring(i, Math.min(sample.length(), i+runlength)));
			}
		}
		evaluation.addAll(evaluationtmp);
		return sb.toString();
	}
	
}

class StringScore {
	
	String string;
	
	double score1;
	double score2;
}
