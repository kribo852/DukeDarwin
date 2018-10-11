
### This project is used to run genetic algorithms.
This project is free to use. See the MIT License: https://en.wikipedia.org/wiki/MIT_License

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

VindtunnelLife:

BallWeight:

KotlinLife:

TextClassifierLifeform:

--------------------------------------------------------------
To run project as is:

**Compile main**
	$javac Main.java
	
**Compile habitat** (either RobustHabitat or HighEndHabitat depending on Lifeform specification (or own implementation).)

	$javac HighEndHabitat.java
	
**Compile lifeform** (for example)

	$javac LungLife.java  
	
**Run**

	$java Main LungLife

	
