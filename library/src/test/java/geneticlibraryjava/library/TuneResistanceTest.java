package geneticlibraryjava.library;

import java.util.Random;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


//Tries to construct a electronic network of resistors, that shall have the total resistance of WANTED_RESISTANCE 
class TuneResistanceTest {

	private static final double AVAILABLE_RESISTOR = 50;
    private static final double WANTED_RESISTANCE = 37;
    private static final double TOLERANCE = 0.15;
	private static final Random RND = new Random();

	@Test
	void testConnectResistors() {
        GeneticLibrary<ArrayList<Integer>> geneticLibrary = new GeneticLibrary<ArrayList<Integer>>(ArrayList.class);
        
        ArrayList<Integer> result = 
        geneticLibrary.simplified_run(this::newGenome, this::mutate, this::scoreFunction, this::finished);

        System.out.println("Found solution has: " + calculateResistance(result) + " Ohm resistance");
        System.out.println("Wanted resistance: " + WANTED_RESISTANCE);
        System.out.println("Ω is: " + AVAILABLE_RESISTOR + " Ohm in the blueprint below");

        int max = result.stream().reduce(0, (a, b) -> Math.max(a, b));

        //print the electronic network
        for (int y=0; y< result.size(); y++) {

            if(y==result.size()/2) {
                System.out.print("---|");    
            } else {
                System.out.print("   |");
            }
            for (int x=0; x<max; x++) {
                if(x<result.get(y)) {
                    System.out.print("Ω");
                } else {
                    System.out.print("-");
                }
            }
            if(y==result.size()/2) {
                System.out.println("|---");    
            } else {
                System.out.println("|   ");
            }
        }

    }

    ArrayList<Integer> newGenome() {
        ArrayList<Integer> result = new ArrayList<>();

    	int width = 1 + RND.nextInt(10);

        for (int i=0; i< width; i++) {
            result.add(1 + RND.nextInt(10));
        }

        return result;
    }

    ArrayList<Integer> mutate(ArrayList<Integer> oldGenome) {
        ArrayList<Integer> newGenome = new ArrayList(oldGenome);

    	if (RND.nextBoolean() && newGenome.size() > 1) {
            newGenome.remove(RND.nextInt(newGenome.size()));
        } else {
            newGenome.add(RND.nextInt(1 + RND.nextInt(10)));
        }

        return newGenome;
    }

    double scoreFunction(ArrayList<Integer> genome) {

    	return -Math.abs(calculateResistance(genome) - WANTED_RESISTANCE);
    }

    double calculateResistance(ArrayList<Integer> genome) {
        double conductance = genome.stream().map(r -> (double)r*AVAILABLE_RESISTOR)
        .map(r -> 1.0/r).reduce(0.0, (a, b) -> a + b);

        return (1.0/conductance);
    }

    boolean finished(ArrayList<Integer> genome) {
    	return -scoreFunction(genome) < WANTED_RESISTANCE * TOLERANCE ;
    }

}
