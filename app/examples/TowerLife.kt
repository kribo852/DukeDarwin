package app.examples

import java.awt.Color
import java.util.Random
import java.lang.Math

import javax.swing.JFrame;
import java.awt.Graphics;

import app.Lifeform
/**
*
*
**/
public class TowerLife(genome : HashSet<Construction>?) : Lifeform<Construction, HashSet<Construction>>(genome) {

	val maxweight : Double
	var internalscore : Double

	init{
		maxweight = 20.0
		internalscore = 0.0
	}

	override public fun run(): Unit{
    	internalscore = testweight(foldoutGenome(genome)).toDouble()
	}

	override public fun mutate(genome1 : HashSet<Construction>, genome2 : HashSet<Construction>) : HashSet<Construction> {
		return mutate(genome1)
	}
	
	override public fun mutate(genome : HashSet<Construction>) : HashSet<Construction>{
		var collector = HashMap<Construction, Boolean>() 
		
		genome.forEach( { a -> for(i in -1..1)for(j in -1..1)if(a.y+j>-1)collector.put(Construction(a.x+i, a.y+j), false) } )
		genome.forEach( { a -> collector.put(Construction(a.x, a.y), true) } )
		
		var rtn = HashSet<Construction>()
		var numberofmutations = 0

		while(numberofmutations == 0) {
			for(construction in collector.iterator()) {

				var key = construction.key
				var value = construction.value

				if(key.x >= 0) {
					if( Random().nextInt(genome.size) == 0 ) {
						if( !value ) {
							rtn.add(key)
						} 
					} else {
						if( value ) {
							rtn.add(key)
						}
					}
				}
			}
			numberofmutations++
		}
		if(Random().nextInt(10000) == 0) {
			rtn.add(Construction(0, highestPoint(rtn) + 1));
		}
		if(Random().nextInt(1250) == 0) {
			return clearUnusedGenome(rtn)
		}
		return rtn
	}
	
	override public fun output(): Unit {
		val size = 1000
		println("score"+internalscore)
		jframe.setVisible(true)
		jframe.setSize(size, size)		
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)

		val sortedbyheight : List<Construction> = foldoutGenome(genome).sortedByDescending( {a -> a.y } )
		val pressures = HashMap<Construction, Double>()
		val highestpoint = highestPoint(genome)
		val scale = size/(4 + highestpoint)

		sortedbyheight.forEach( { a -> pressures.put(Construction(a.x, a.y), 1.0) } )
		sortedbyheight.forEach( { a -> addPressureToMap(pressures, a.x, a.y) } )

		jframe.getGraphics().setColor(Color.black)
		jframe.getGraphics().fillRect(0, 0, size, size)

		pressures.forEach( {a -> paintBlock(jframe.getGraphics(), a.key.x, a.key.y, scale, a.value) } )
	}

	override public fun getScore(): Double{
		return internalscore//internalscore
	}
	
	override public fun newGenome(): HashSet<Construction>{
		var rtn = HashSet<Construction>() 
		for(y in 0..maxweight.toInt()-2) {
			rtn.add(Construction(0, y))
		}
		return rtn
	}

	fun testweight(genome : HashSet<Construction>) : Int{

		val sortedbyheight : List<Construction> = genome.sortedByDescending( {a -> a.y } )
		val pressures = HashMap<Construction, Double>()

		sortedbyheight.forEach( { a -> pressures.put(Construction(a.x, a.y), 1.0) } )
		sortedbyheight.forEach( { a -> addPressureToMap(pressures, a.x, a.y) } )

		if(pressures.values.any( { a -> a>maxweight } )) {
			return 0
		}
		return sortedbyheight.first().y
	}

	fun addPressureToMap(pressures : HashMap<Construction, Double>, x: Int, y: Int) : Unit {
		if( y > 0 ) {
			var numbersofunderlayingblocks = 0.0
			for(i in -1..1) {
				if(pressures.contains(Construction(x+i, y-1))) {
					numbersofunderlayingblocks ++
				}
			}
			if(numbersofunderlayingblocks == 0.0) { 
				pressures.put(Construction(x, y), maxweight + 1.0)
			}
			for(i in -1..1) {
				if(pressures.contains(Construction(x+i, y-1))){
					var currentweight = pressures.getOrDefault(Construction(x, y), 0.0)
					var currentweightbelow = pressures.getOrDefault(Construction(x+i, y-1), 0.0)

					currentweight = currentweightbelow + currentweight / numbersofunderlayingblocks
					pressures.put(Construction(x+i, y-1), currentweight)
				}
			}
		}
	}

	fun paintBlock(graphics : Graphics, x : Int, y: Int, scale: Int, weight: Double) : Unit {
		graphics.setColor(Color( (255*weight/maxweight).toInt(), (255-255*weight/maxweight).toInt() , 0))
		graphics.fillRect(scale+500+x*scale, -2*scale+1000-y*scale, scale, scale)
	}

	fun clearUnusedGenome(genome : HashSet<Construction>): HashSet<Construction>{
		val rtn = HashSet<Construction>()
		val highestconstruction = genome.maxBy( {a -> a.y} );
		clearUnusedGenome(genome, highestconstruction?.x ?: -1, highestconstruction?.y ?: -1, rtn)
		return rtn
	}

	fun clearUnusedGenome(genome : HashSet<Construction>, x: Int, y: Int ,  rtn: HashSet<Construction>): Unit{
		if(genome.contains(Construction(x,y))){
			rtn.add(Construction(x,y))
		}
		for(i in -1..1){
			if(genome.contains(Construction(x+i,y-1)) && !rtn.contains(Construction(x+i,y-1))){
				clearUnusedGenome(genome, x+i, y-1, rtn)
			}			
		}
	}

	fun foldoutGenome(genome : HashSet<Construction>): HashSet<Construction> {
		val rtn = HashSet<Construction>()

		genome.forEach({a -> rtn.add(Construction(a.x, a.y)); rtn.add(Construction(-a.x, a.y)); });

		return rtn;
	}

	fun highestPoint(genome : HashSet<Construction>): Int{
		return genome.maxBy({ a -> a.y })?.y ?: -1;
	}

}

public data class Construction(val x : Int, val y : Int)

val jframe = JFrame()
