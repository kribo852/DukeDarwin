
### This project is used to run genetic algorithms.
This project is free to use. See the MIT License: https://en.wikipedia.org/wiki/MIT_License
The idea of this project is to provide a way to remove some of the work when implementing a genetic algorithm.
All you need to do is to write a "lifeform", a description of solutions to a specific problem.

To run project as is:
-----------------------------------------------------------------------------------------
**Compile main**
	$javac Main.java
	
**Compile lifeform** (for example)

	$javac app/examples/VindtunnelLife.java 
	
**Run**

	$java Main app.examples.VindtunnelLife
	
Or similarly for a Kotlin project:

**compile**

	$kotlinc Starter.kt Main.java app/Lifeform.java app/Habitat.java app/examples/KolourLife.kt

**Run**

	$kotlin StarterKt app.examples.KolourLife

----------------------------------------------
For more information, please see the wiki pages.
