package app.examples

import java.util.Random;
import java.util.Arrays;

import app.Lifeform

public class TSPScoreLife(genome : ArrayList<Double>?) : Lifeform<Double, ArrayList<Double>>(genome) {

	private val initialscore : Double
	private var length : Double

	companion object Coordinates {
        private val coordinates : Array<DoubleArray> = Array(2500, { doubleArrayOf(Random().nextDouble(), Random().nextDouble())}) 
    }

	init{
	 	initialscore = 0.0
		length = 0.0
	}

	public override fun run() : Unit {
		var sortingpermutationarray = Array( genome.size, {i -> SortIndex(genome.get(i) , i)})
		var sortedlist = sortingpermutationarray.sortedWith(compareBy({ it.score }))
		length = 0.0
		for(i in 0..genome.size - 1){
			var index1 = sortedlist.get(i).index
			var index2 = sortedlist.get((i+1)%genome.size).index
			var coord1 = coordinates[index1]
			var coord2 = coordinates[index2] 
			length +=  euclidicDistance(coord1[0], coord1[1], coord2[0], coord2[1])
		}
	}

	public override fun mutate(genome1: ArrayList<Double>, genome2: ArrayList<Double>) : ArrayList<Double> {
		val tmp = ArrayList<Double>();
		for(i in 0..genome1.size - 1) {
			if(Random().nextBoolean()) {
				tmp.add(genome1.get(i))
			} else {
				tmp.add(genome2.get(i))
			}
		}
		var mutators = arrayOf(::randomPosition, ::exchangePosition, ::gaussianPosition) 
		while(Random().nextBoolean()) {
			mutators[Random().nextInt(mutators.size)](tmp)
		}
		return tmp
	}

	private fun randomPosition(genome1: ArrayList<Double>){
		genome1.set(Random().nextInt(genome1.size), Random().nextDouble())
	}

	private fun gaussianPosition(genome1: ArrayList<Double>){
		var tmpindex = Random().nextInt(genome1.size);
		genome1.set(tmpindex, genome1.get(tmpindex)+Random().nextGaussian()*0.25)
	}

	private fun exchangePosition(genome1: ArrayList<Double>){
		var index1 = Random().nextInt(genome1.size)
		var index2 = Random().nextInt(genome1.size)
		var tmp = genome1.get(index1)
		genome1.set(index1, genome1.get(index2))
		genome1.set(index2, tmp)
	}

	public override fun mutate(genome1: ArrayList<Double>) : ArrayList<Double> {
		return ArrayList();
	}

	override public fun getScore(): Double {
		return -length
	}
	
	override public fun newGenome(): ArrayList<Double> {
		val tmp = ArrayList<Double>();
		Array(2500, { Random().nextDouble() }).forEach({ value -> tmp.add(value) })
		return tmp
	} 

	public override fun output() : Unit {
		println(length)
	}	
}

data class SortIndex(val score: Double, val index: Int)

fun euclidicDistance(x1: Double, x2 : Double , y1: Double, y2 : Double): Double {
	return Math.hypot(x1-x2, y1-y2)
}