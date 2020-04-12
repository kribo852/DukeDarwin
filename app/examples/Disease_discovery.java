package app.examples;

import app.Lifeform;
import app.GenerationlessHabitat;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Arrays;

/**
* Example trying to discover a specific disease among several (4) diseases.
* Each disease has a unique range of symptoms that makes up its signature.
*
* Made during the Corona pandemic :) This toy example (in its sandbox) tries to determine
* if a office-sized population has a specific disease among several diseases.
*
* The idea is that the relevant disease has a marker/profile that can be identified, even if
* there are several diseases that share symptoms and that doing a measurement on many (office-many)
* people at once exposes the signature. 
*
* This application does neural networking in a naive way (genetic algorithm) 
* by a matrix multiplication (also naive) which looks like:
*
* signature * edges1 * edges2 * edges3 ... * collector 
*
* this 1x1 result is compared with the cutoff, which is the last gene in the genome.
*
**/
public class Disease_discovery extends Lifeform<double[][], ArrayList<double[][]>> {
	int score = 0; 
	static final int symptom_vector_size = 10;
	static final int number_of_diseases = 4;
	static final int number_of_neural_layers = 8;
	static final ArrayList<double[]> disease_symptoms = new ArrayList<>();
	final Random RND = new Random();

	public Disease_discovery(final ArrayList<double[][]> genome){
		super(genome);
		populateSymptoms();
	}

	private void populateSymptoms(){
		if(disease_symptoms.size() == 0 ) {
			for(int i = 0; i < number_of_diseases; i++) {
				disease_symptoms.add(populateSymptom());
			}
		}
	}

	private double[] populateSymptom(){
		ArrayList<Integer> listedsymtoms = 
		new ArrayList(Arrays.asList(new Integer[]{0,1,2,3,4,5,6,7,8,9}));
		double[] result = new double[listedsymtoms.size()];
		for (int i=0; i<listedsymtoms.size()/2; i++ ) {
			int pick = listedsymtoms.remove(RND.nextInt(listedsymtoms.size()));
			result[pick] = RND.nextDouble();
		}
		return result;
	}	

	
	@Override
	public ArrayList<double[][]> mutate(final ArrayList<double[][]> genome1, final ArrayList<double[][]> genome2) {
		ArrayList<double[][]> child_genome = new ArrayList<>();
		for(int i = 0; i< genome1.size(); i++) {
			double[][] gene1 = genome1.get(i);
			double[][] gene2 = genome2.get(i);

			double[][] childgene = new double[gene1.length][gene1[0].length];
			for (int x=0; x<gene1.length; x++) {
				for (int y=0; y<gene1[0].length; y++) {
					childgene[x][y] = copyGene(gene1[x][y], gene2[x][y]);
				}		
			}
			child_genome.add(childgene);
		}
		return child_genome;
	}

	private double copyGene(double gene1, double gene2){
		if(RND.nextBoolean()) {
			return gene1;
		} else if (RND.nextBoolean()) {
			return gene2;
		} else if (RND.nextBoolean()) {
			return -1.0/2000.0+RND.nextDouble()/1000.0+(gene1+gene2)/2;
		} 
		return RND.nextDouble();
	}
	
	@Override
	public ArrayList<double[][]> mutate(final ArrayList<double[][]> genome) {
		return null;
	}
	
	@Override
	public double getScore() {
		return score;
	}
	
	@Override
	public void output() {
		System.out.println("score in percent: " + score/100.0 + "% correct");
	}

	@Override	
	public ArrayList<double[][]> newGenome() {
		ArrayList<double[][]> new_genome = new ArrayList<>();
		for(int i=0; i<number_of_neural_layers; i++) {//neural layers
			double[][] layer = new double[symptom_vector_size][symptom_vector_size];
			for (int x=0; x<symptom_vector_size; x++) {
				for (int y=0; y<symptom_vector_size; y++) {
					layer[x][y] = (RND.nextDouble()-0.5);
				}		
			}
			new_genome.add(layer);
		}
		double[][] collector = new double[1][symptom_vector_size];
		for (int i=0; i<symptom_vector_size; i++) {
			collector[0][i] = RND.nextDouble();
		}
		new_genome.add(collector);
		//cutoff 1x1 matrix new_genome.add();
		new_genome.add(new double[][]{{RND.nextDouble()}});
		
		return new_genome;
	}

	@Override
	public void run() {
		score = 0;
		for (int number_of_tests=0; number_of_tests<10000; number_of_tests++) {
			boolean has_the_relevant_disease = RND.nextBoolean();
			double allsymptoms[] = populateDiseasePicture(has_the_relevant_disease);
			
			double[][] symptoms_input_vector = new double[symptom_vector_size][1];
			for (int i=0; i<symptom_vector_size; i++) {
				symptoms_input_vector[i][0] = allsymptoms[i];
			}	
			
			double[][] result = genome.subList(0, genome.size()-2).stream().reduce(
				symptoms_input_vector,
			 	(a, b) -> matrix_multiply(a, b));

			if((result[0][0] > genome.get(genome.size()-1)[0][0]) == has_the_relevant_disease) {
				score ++;
			}
		}

	}

	private double[][] matrix_multiply(double[][] a, double[][] b) {
		double result[][] = new double[b.length][a[0].length];

		for (int resultx=0; resultx<result.length; resultx++) {
			for (int resulty=0; resulty<result[0].length; resulty++) {
				for(int i=0; i<a.length; i++) {
					result[resultx][resulty] += a[i][resulty]*b[resultx][i];
				} 
			}			
		}
		return result;
	}

	private double[] populateDiseasePicture(boolean has_the_relevant_disease) {
		int minpeople = 30;
		int peoplespan = 40;
		double[] result = new double[symptom_vector_size];
		if(has_the_relevant_disease) {
			for (int i=0; i<symptom_vector_size; i++) {
				double[] relevant_disease_symptoms = disease_symptoms.get(0);
				int number_of_people = minpeople + RND.nextInt(peoplespan);
				for(int counter=0; counter<number_of_people; counter++) {
					if(RND.nextDouble() < relevant_disease_symptoms[i]) {
						result[i] ++;
					}
				}
			}
		}

		for (int other_disease=1; other_disease<disease_symptoms.size(); other_disease++) {
			if(RND.nextBoolean()) {
				for (int i=0; i<symptom_vector_size; i++) {
					double[] other_disease_symptoms = disease_symptoms.get(other_disease);
					int number_of_people = minpeople + RND.nextInt(peoplespan);
					for(int counter=0; counter<number_of_people; counter++) {
						if(RND.nextDouble() < other_disease_symptoms[i]) {
							result[i] ++;
						}
					}
				}
			}			
		}
		return result;
	} 

	public void start() {
		new GenerationlessHabitat().start(this.getClass());	
	}	

}
