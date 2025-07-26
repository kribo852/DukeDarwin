package geneticlibraryjava.library;

import java.util.Random;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;


//solves the eight queens problem
class EightQueensTest {

	private static final int PROBLEM_SIZE = 8;
	private static final Random RND = new Random();

	@Test
	void testSolveEightQueensProblem() {
        GeneticLibrary<int[]> geneticLibrary = new GeneticLibrary<int[]>(int[].class);
        
        int[] result = 
        geneticLibrary.simplified_run(this::newGenome, this::mutate, this::scoreFunction, this::finished);

        for (int i=0; i<PROBLEM_SIZE; i++) {
        	for (int j=0; j<PROBLEM_SIZE; j++) {
        		if(result[i] == j) {
        			System.out.print("🨁 ");
        		} else {
        			if((i+j) % 2 == 0) {
        				System.out.print("◽");
        			} else {
        				System.out.print("◾");
        			}
        		}
        	}	
        	System.out.println();
        }


    }

    int[] newGenome() {
    	int rtn[] = new int[PROBLEM_SIZE];

    	for (int i=0; i< PROBLEM_SIZE; i++) {
    		rtn[i] = i;
    	}

    	for (int i=0; i< 24; i++) {
    		int swapIndexA = RND.nextInt(PROBLEM_SIZE);
    		int swapIndexB = RND.nextInt(PROBLEM_SIZE);

    		int tmpSwapValue = rtn[swapIndexA];
    		rtn[swapIndexA] = rtn[swapIndexB];
    		rtn[swapIndexB] = tmpSwapValue;
    	}
    	return rtn;
    }

    int[] mutate(int[] oldGenome) {
    	int rtn[] = new int[PROBLEM_SIZE];

    	for (int i=0; i< PROBLEM_SIZE; i++) {
    		rtn[i] = oldGenome[i];
    	}

    	int swapIndexA = RND.nextInt(PROBLEM_SIZE);
    	int swapIndexB = RND.nextInt(PROBLEM_SIZE);

    	int tmpSwapValue = rtn[swapIndexA];
    	rtn[swapIndexA] = rtn[swapIndexB];
    	rtn[swapIndexB] = tmpSwapValue;

    	return rtn;
    }

    double scoreFunction(int[] genome) {
    	double returnScore = 0.0;

    	//compare queen positions diagonally
    	for (int i=0; i< PROBLEM_SIZE; i++) {
    		for (int j=i+1; j< PROBLEM_SIZE; j++) {
    			int deltax = j - i;
    			int deltay = genome[j] - genome[i];
    			
    			if(deltax == deltay || deltax == -deltay) {
    				returnScore--;
    			}

    		}		
    	}
    	return returnScore;
    }

    boolean finished(int[] genome) {
    	return scoreFunction(genome) >= -0.5;
    }

}