package app.examples

import java.awt.Color
import java.awt.Graphics

import java.util.Random
import java.lang.Math

import javax.swing.JFrame

import app.Lifeform
/**
* A lifeform that tries to make a tower. The goal function is to make as high a tower as possible.
* It implements the Lifeform interface.     
**/
public class TowerLife(genome : HashSet<Construction>?) : Lifeform<Construction, HashSet<Construction>>(genome) {

	val maxweight : Double
	var internalscore : Double
	val windowsize : Int

	init{
		maxweight = 64.0
		internalscore = 0.0
		windowsize = 1000
	}

	override public fun run(){
    	internalscore = testweight(genome).toDouble()
	}

	fun testweight(genome : HashSet<Construction>) : Int{
		val sortedbyheight : List<Construction> = foldoutGenome(genome).sortedByDescending( {a -> a.y } )
		val pressures = makePressureMap(sortedbyheight)
		if(pressures.values.any( { a -> a>maxweight } )) {
			return 0
		}
		return sortedbyheight.first().y
	}

	override public fun mutate(genome1 : HashSet<Construction>, genome2 : HashSet<Construction>) : HashSet<Construction> {
		return mutate(genome1)
	}
	
	override public fun mutate(genome : HashSet<Construction>) : HashSet<Construction> {
		val rtn = HashSet<Construction>()
		val mutationhull = mutationHull(genome)
		var numberofmutations = 0
		while(numberofmutations == 0) {
			for(construction in mutationhull.iterator()) {
				val key = construction.key
				val value = construction.value

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
		if(Random().nextInt(1250) == 0) {
			val highestconstruction = rtn.maxBy( {a -> a.y} ) ?: Construction(0, -1)
			rtn.add(Construction(Math.max(0, highestconstruction.x-1), highestconstruction.y+1))
		}
		if(Random().nextInt(12500) == 0) {
			return clearUnusedGenome(rtn)
		}
		if(Random().nextInt(100) == 0) {
			return addIdenticalLayer(rtn)
		}
		return rtn
	}

	fun addIdenticalLayer(genome : HashSet<Construction>) : HashSet<Construction>{
		val rtn = HashSet<Construction>()
		val hightOfCopy = 1 + Random().nextInt(highestPoint(genome))
		for(block in genome) {
			if(block.y > hightOfCopy) {
				rtn.add(Construction(block.x, block.y+1))
			} else if(block.y == hightOfCopy) {
				rtn.add(Construction(block.x, block.y+1))
				rtn.add(Construction(block.x, block.y))
			} else {
				rtn.add(Construction(block.x, block.y))
			}
		}
		return rtn
	}

	fun mutationHull(genome : HashSet<Construction>) : HashMap<Construction, Boolean> {
		var rtn = HashMap<Construction, Boolean>() 
		genome.forEach( { a -> for(i in -1..1)for(j in -1..1)if(a.y+j>-1)rtn.put(Construction(a.x+i, a.y+j), false) } )
		genome.forEach( { a -> rtn.put(Construction(a.x, a.y), true) } )
		return rtn
	}

	fun clearUnusedGenome(genome : HashSet<Construction>): HashSet<Construction>{
		val rtn = HashSet<Construction>()
		val highestconstruction = genome.maxBy( {a -> a.y} ) ?: Construction(0, -1)
		clearUnusedGenome(genome, highestconstruction.x, highestconstruction.y, rtn)
		return rtn
	}

	fun clearUnusedGenome(genome : HashSet<Construction>, x: Int, y: Int ,  rtn: HashSet<Construction>) {
		if(genome.contains(Construction(x, y))){
			rtn.add(Construction(x, y))
		}
		for(i in -1..1){
			if(genome.contains(Construction(x+i, y-1)) && !rtn.contains(Construction(x+i, y-1))){
				clearUnusedGenome(genome, x+i, y-1, rtn)
			}			
		}
	}
	
	override public fun output() {
		println("score" + internalscore)
		jframe.setVisible(true)
		jframe.setSize(windowsize, windowsize)		
		jframe.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE)
		jframe.getGraphics().setColor(Color.black)
		jframe.getGraphics().fillRect(0, 0, windowsize, windowsize)

		val sortedbyheight : List<Construction> = foldoutGenome(genome).sortedByDescending( {a -> a.y } )
		val scale = windowsize/(4 + highestPoint(genome))

		makePressureMap(sortedbyheight).forEach( {a -> paintBlock(jframe.getGraphics(), a.key.x, a.key.y, scale, a.value) } )
	}

	fun paintBlock(graphics : Graphics, x : Int, y: Int, scale: Int, weight: Double) {
		graphics.setColor(Color( (255*weight/maxweight).toInt(), (255-255*weight/maxweight).toInt() , 0))
		graphics.fillRect(scale+windowsize/2+x*scale, -2*scale+windowsize-y*scale, scale, scale)
	}

	fun makePressureMap(sortedbyheight : List<Construction>) : HashMap<Construction, Double> {
		val pressures = HashMap<Construction, Double>()
		sortedbyheight.forEach( { a -> pressures.put(Construction(a.x, a.y), 1.0) } )
		sortedbyheight.forEach( { a -> addPressureToMap(pressures, a.x, a.y) } )
		return pressures
	}

	fun addPressureToMap(pressures : HashMap<Construction, Double>, x: Int, y: Int) {
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

	override public fun getScore(): Double{
		return internalscore
	}
	
	override public fun newGenome(): HashSet<Construction>{
		val rtn = HashSet<Construction>() 
		for(y in 0..maxweight.toInt()-2) {
			rtn.add(Construction(0, y))
		}
		return rtn
	}

	fun foldoutGenome(genome : HashSet<Construction>): HashSet<Construction> {
		val rtn = HashSet<Construction>()
		genome.forEach({a -> rtn.add(Construction(a.x, a.y)); rtn.add(Construction(-a.x, a.y)) })
		return rtn
	}

	fun highestPoint(genome : HashSet<Construction>): Int{
		return genome.maxBy({ a -> a.y })?.y ?: -1
	}
}

public data class Construction(val x : Int, val y : Int)

val jframe = JFrame()
