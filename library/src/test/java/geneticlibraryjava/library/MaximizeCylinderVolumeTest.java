/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package geneticlibraryjava.library;

import java.util.Random;
import java.time.Instant;
import java.util.function.Predicate;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class MaximizeCylinderVolumeTest {
    
    //this test tries to maximize the the volume of a cylinder, where the total area is constrained to a
    //maximum value 
    @Test
    void testSolveMaximizeVolume() {
        GeneticLibrary<CylinderRepresentation> geneticLibrary = 
        new GeneticLibrary<CylinderRepresentation>(CylinderRepresentation.class);
        
        CylinderRepresentation result = 
        geneticLibrary.simplified_run(this::newGenome, this::mutate, this::scoreFunction, finishedPredicateGenerator());
    }

    CylinderRepresentation newGenome() {
        return new CylinderRepresentation(new Random().nextDouble(), new Random().nextDouble());
    }

    CylinderRepresentation mutate(CylinderRepresentation oldGenome) {

        double newRadius = oldGenome.radius();
        double newHeight = oldGenome.height();

        double delta = new Random().nextGaussian()*0.1;

        newRadius += delta;
        if(new Random().nextBoolean()) {
            newHeight -= delta;    
        } else {
            newHeight += new Random().nextGaussian()*0.1;
        }
        return new CylinderRepresentation(newRadius, newHeight);
    }

    Predicate<CylinderRepresentation> finishedPredicateGenerator() {
        Instant start = Instant.now();

        return (genome) -> Instant.now().minusSeconds(10L).isAfter(start);
    }

    double scoreFunction(CylinderRepresentation genome) {
        if(genome.radius() < 0.0 || genome.height() < 0.0) {
            return 0.0;
        }

        if(areaOfCylinder(genome) > 4.0 * Math.PI) {
            return 0.0;
        }


        return genome.radius() * genome.radius() * Math.PI * genome.height();
    }

    double areaOfCylinder(CylinderRepresentation genome) {
        double topBottomArea = 2.0 * genome.radius() * genome.radius() * Math.PI;
        double wallArea = 2.0 * genome.radius() * genome.height() * Math.PI;

        return topBottomArea + wallArea;
    }

    record CylinderRepresentation(double radius, double height) {} 

}
