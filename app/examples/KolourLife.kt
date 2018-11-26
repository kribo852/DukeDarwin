package app.examples

import java.awt.Color
import java.util.Random
import java.lang.Math

import app.Lifeform

/**
*Test of a Kotlin lifeform. Tries to maximize the sum of minimum distance between |numberofcolours| colours.
*Extends Lifeform template.
**/

public class KolourLife(genome : ArrayList<Color>?) : Lifeform<Color, ArrayList<Color>>(genome) {

	val numberofcolours: Int
	var sumdistance : Double

	init{
		numberofcolours = 32
		sumdistance = 0.0
	}

	override public fun run(): Unit{
		sumdistance = 1.0

		for(colour1 in genome) {
			var minimum = Double.MAX_VALUE
			for(colour2 in genome) {

				if(colour1!=colour2 && euclidicColorDistance(colour1, colour2) < minimum) {
					minimum = euclidicColorDistance(colour1, colour2)
				}
			}
			sumdistance *= Math.pow(minimum, 1/32.0)
		}
	}

	//sexual reproduction
	override public fun mutate(genome1 : ArrayList<Color>, genome2 : ArrayList<Color>) : ArrayList<Color> {
		return mutate(genome1)
	}
	
	//asexual reproduction
	override public fun mutate(genome : ArrayList<Color>) : ArrayList<Color>{
		var rtn = ArrayList<Color>()

		for(colour in genome) {
			rtn.add(mutateColour(colour));
		}
		return rtn
	}
	
	//in some manner, output the result of the simulation
	override public fun output(): Unit{
		run()
		println(sumdistance)
	}

	override public fun getScore(): Double{
		return sumdistance
	}
	
	override public fun newGenome(): ArrayList<Color>{
		var rtn = ArrayList<Color>()
		for(i in 0..1)for(j in 0..1)for(k in 0..1) rtn.add(Color(i*255, j*255, k*255))

		for(i in 1..numberofcolours-8) rtn.add(Color(Random().nextInt(256), Random().nextInt(256), Random().nextInt(256)))
		genome = rtn
		run();
		println("Baseline: ${sumdistance}")
		println("Number of nodes: ${rtn.size}")
		println("sqrt(64²+64²+64²): ${Math.sqrt(64.0*64.0+64.0*64.0+64.0*64.0)} \n")
		return rtn;
	}

	private fun euclidicColorDistance(colour1 : Color, colour2: Color): Double {
		return Math.hypot(Math.hypot( (colour1.getRed()-colour2.getRed()).toDouble(),
		(colour1.getGreen()-colour2.getGreen()).toDouble()), (colour1.getBlue()-colour2.getBlue()).toDouble()); 
	}

	private fun mutateColour(colour: Color) : Color {
		if(Random().nextInt(numberofcolours/2)!=0){
			return Color(colour.getRed(), colour.getGreen(), colour.getBlue())
		}

		if(Random().nextBoolean()) {
			var red = colour.getRed() + Random().nextInt(2) - Random().nextInt(2);
			var green = colour.getGreen() + Random().nextInt(2) - Random().nextInt(2);
			var blue = colour.getBlue() + Random().nextInt(2) - Random().nextInt(2);

			red = Math.min(255, Math.max(0, red));
			green = Math.min(255, Math.max(0, green));
			blue = Math.min(255, Math.max(0, blue));

			return Color(red, green, blue)
		} else {
			return Color(Random().nextInt(256), Random().nextInt(256), Random().nextInt(256))
		}
	}
}
