
### This project is used to run genetic algorithms.
This project is free to use. See the MIT License: https://en.wikipedia.org/wiki/MIT_License

-----------------------------------------------------------------
### UML-diagram:
http://www.plantuml.com/plantuml/uml/VLDVQ_em57rkFiK5Nohw4tvx5X54ji76Xkvjx25jhrqWVoIvsIRYTr_InHPBDKzDpZd9oJdfqX6tv9LafUJEmJCN-iWOX361Om19b2VuCg825Q1yIrRe-lq3kAtT-8yuweYhPXDfUDLy9JeCC9_43dV6Aj3u_QZ3gYunJ_84nLbKdXZZXIFBIuB6PIkUhwVm1lX3g2i7AoCbbYICdgyN2n1gBr6X9WSRhpKl94AwMXX1LwCs2bcMiMnotZ7V2Srb2uvP18_duqy1K9uuOO_tVmgN2tStbBTFg8oFJckaRMaivZr2AiDusdlA1qu9UTutEoUmeJSjvp7BrFFBdeJYSdMGGbTeu_LJZbswbzJNW-LALCFCNZyFl134gKjx1BqB94U3XMUjQlYLT3sd3ATmxU4aEwB-NEigkMhTftaYRKpX7LtX06EjbTciLRBuky2_HHV4CgYyahy0

-----------------------------------------------------------------
**Main**: loads a Lifeform and runs it as an application. Main contains a class loader that currently is used to load a/the lifeform.

**Habitat**: an abstract class representing a place to contain and run evolution of lifeforms. default implementation is RobustHabitat.

**Lifeform**: abstract class representing a lifeform, with a genome of a collection of type T. Has abstract methods for running an evaluation of the lifeform, 
returning the score, mutation, output of results and making of a new genome. Lifeform has a start method that can be implemented to allow overriding of the default Habitat
implementation. This can be used to tweak the Habitat to the Lifeform. Lifeform implements Runnable so that many instances can be tried at once.

**RobustHabitat**:  Single threaded habitat that can manage Lifeforms that implements either sexual or asexual mutation properly. 

**HighEndHabitat**: Habitat that has the following features:

	-Sexual reproduction.
	
	-A method of evolution that is "generationless" 2 lifeforms in the habitat are chosen from an array, with higher probabilities for lifeforms with higher fitness/score.
	 The child of the 2 lifeforms is run and evaluated, and then placed in the habitat. the habitat size is limited and the worst performing lifeforms are thrown away.
	 
	-Runs the evaluation on 3 threads simultaniously

--------------------------------------------------------------
The idea of this project is that you only need to implement a lifeform in order to run the genetic algorithm.
Comparing scores and repopulation a habitat with new generations can be made by the Habitat (which you can implement on your own if you want to, but it isn't necessary).
The habitats are tested to some extent, so you can concentrate on writing your lifeform specification.
Inversion of control makes it so that you can use your own tailored habitat.

The classloader makes it possible to swap lifeforms without modifying the code, the name of a lifeform is taken as an argument to the main class in the command prompt. 

--------------------------------------------------------------
Some examples of lifeforms:

TSPLifeform: gives a soloution to a Traveling salesman problem (non optimal of course)

OptimalPyramidLife: calculates the optimal ratio between height and width of a pyramid concering volume per surfacearea

OptimalCylinderLife: calculates the optimal ratio between height and width of a cylinder with a lid and bottom concering volume per surfacearea

LungLife: makes a lunglike organic structure by maximizing area covered by "air".

--------------------------------------------------------------
To run project as is:

**Compile main**
	-javac Main.java
	
**Compile habitat** (either RobustHabitat or HighEndHabitat depending on Lifeform specification (or own implementation).)

	-javac HighEndHabitat.java
	
**Compile lifeform** (for example)

	-javac LungLife.java  
	
**Run**

	-java Main LungLife

	
