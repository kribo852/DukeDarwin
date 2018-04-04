

This project is used to run genetic algorithms.
This project is free to use. See the MIT License: https://en.wikipedia.org/wiki/MIT_License

Main class: runs application, includes Habitat abstract class, which contains the lifeforms.

Habitat, a place to run evolution of lifeforms

Lifeform: interface of a lifeform, with a genome of a collection of type T. Has methods for running an evaluation of the lifeform, mutation, output of results and making of a new genome.

HighEndHabitat: Habitat that has the following features:

	-Sexual reproduction.
	
	-A method of evolution that is "generationless" 2 lifeforms in the habitat are chosen from an array, with higher probabilities for lifeforms with higher fitness/score.
	 The child of the 2 lifeforms is run and evaluated, and then placed in the habitat. the habitat size is limited and the worst performing lifeforms are thrown away.
	 
	-Runs the evaluation on 3 threads simultaniously
	 





Some examples of lifeforms:

TSPLifeform: gives a soloution to a Traveling salesman problem (non optimal of course)

OptimalPyramidLife: calculates the optimal ratio between height and width of a pyramid concering volume per surfacearea

OptimalCylinderLife: calculates the optimal ratio between height and width of a cylinder with a lid and bottom concering volume per surfacearea

LungLife: makes a lunglike organic structure by maximizing area covered by air.




