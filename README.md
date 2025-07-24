# DukeDarwin

## A library for running genetic algorithms

## Use
Call the functions `run_algorithm` or `simplified_run` in the class GeneticLibrary. There is an example of how to use the library in the test, 
GeneticLibraryTest.


## API
The api is made to look similar to my other libraries for genetic algorithms, for Rust and Python. The implementation of the libraries may however differ.

There are two function that you can call in the library, in order to run the algorithm, the `simplified_run` is supposed to be a bit smaller.

```java
public GenomeType simplified_run(Supplier<GenomeType> newGenomeSupplier, 
        UnaryOperator<GenomeType> mutator,
        ToDoubleFunction<GenomeType> scoreFunction, 
        Predicate<GenomeType> finishPredicate)
```

```java
public GenomeType run_algorithm(Supplier<GenomeType> newGenomeSupplier, 
        List<Function<GenomeType[], GenomeType>> mutators,
        Consumer<GenomeType> printGenome,
        ToDoubleFunction<GenomeType> scoreFunction,
        Predicate<GenomeType> finishPredicate)
```

Due to a quirk in how java works, you also have to supply the class of the genome at instantiation of the genetic library

```java
//instantiate the geneticlibrary with the class of the genome, double[].class
GeneticLibrary<double[]> geneticLibrary = new GeneticLibrary<double[]>(double[].class);
//run the algorithm and return the found solution for further manipulation 
double[] result = geneticLibrary.simplified_run(this::newGenome, this::mutate, this::scoreFunction, this::finished);
```




