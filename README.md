
### This project is used to run genetic algorithms.
This project is free to use. See the MIT License: https://en.wikipedia.org/wiki/MIT_License
The idea of this project is to provide a way to remove some of the work when implementing a genetic algorithm.
All you need to do is to write a "lifeform", a description of solutions to a specific problem.

To run project as is:
-----------------------------------------------------------------------------------------
**Compile main**
	$javac Main.java
	
**Compile habitat** (either RobustHabitat or HighEndHabitat depending on Lifeform specification (or own implementation).)

	$javac HighEndHabitat.java
	
**Compile lifeform** (for example)

	$javac LungLife.java  
	
**Run**

	$java Main LungLife
	
Or similarly for a Kotlin project:

**compile**

	$kotlinc Starter.kt Main.java Lifeform.java Habitat.java KotlinLife.kt

**Run**

	$kotlin StarterKt KotlinLife

----------------------------------------------
For more information, please see the wiki pages.
