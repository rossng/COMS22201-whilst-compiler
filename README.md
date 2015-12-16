# Whilst Compiler - CAMLE

A port of the University of Bristol CAMLE (Compiler to Abstract Machine for Language Engineering) to a standard Maven project format.

The original CAMLE is a skeleton compiler built on ANTLRv3, and is located in the `uk.ac.bris.cs.camle` package. It contains an incomplete implementation of a language which I'll call *Whilst*. *Whilst* is based on the *While* language from *Principles of Program Analysis* (Nielson, Nielson and Hankin).

I have used ANTLR4 and Kotlin to re-implement the compiler. This is in the `eu.rossng.camle` package. The aims of my implementation were to:

* Minimise complexity (e.g. embedded Java in the ANTLR grammar, functions with side effects)
* Represent the IR tree in a typesafe manner, using classes to represent different kinds of nodes and dispatching methods based on those types (rather than switching on `instanceof` or tokens)
* Use a 'functional' style for as much of the code as possible
    * It turns out that this is very difficult with ANTLR, which does not allow you to inject custom dependencies into your visitor methods (all you get it the `WhilstParser.<Something>Context` object)
    * It has become clear that doing this properly requires a language with proper ADTs and pattern matching
    * Because Java/Kotlin don't have these (or the right kind of dynamic dispatch), it's difficult even when you control the implementation of the tree
        * ANTLR uses the Visitor pattern (i.e. double-dispatch) to get around this
        * My IR tree implementation takes inspiration from [this article](https://apocalisp.wordpress.com/2009/08/21/structural-pattern-matching-in-java/) about emulating ADTs in Java. I've updated this approach by using lambdas to make it more terse.
            * But it's not ideal - keeping the interface implementation up to date is fiddly
* Learn [Kotlin](https://kotlinlang.org/) by using it for all the compiler code
    * It's really good!
    
Both compilers produce assembly for a modified version of the *Jouette* architecture described in *Modern Compiler Implementation in Java* (Appel).

## Using the compiler

To run the compiler, you will first need to package it using Maven. A Maven wrapper is included, so you can run:

```
./mvnw package
```

The wrapper will download Maven, ANTLR and the Kotlin compiler for you.

### Skeleton compiler

Once packaged, you can test the compiler skeleton by running:

```
 java -jar target/antlr3-camle-jar-with-dependencies.jar -lex src/test/whilst/testsk.w
```

This executes the `uk.ac.bris.cs.eu.rossng.camle.Camle` class, asking it to lex the `testsk.w` test program. The subset of the language in `testsk.w` *is* implemented already, so this will output something like:

```
10 "write"         
7 "("              
9 "'hello world'"  
4 ")"              
8 ";"              
11 "writeln"       
8 ";"              
10 "write"         
7 "("              
6 "0"              
4 ")"              
... etc.
```

You can also supply other flags - `-syn` and `-irt`.

### Implemented compiler

Once packaged, you can test the main compiler by running:

```
java -jar target/antlr4-camle-jar-with-dependencies.jar -cg "src/test/whilst/test7.w"
```

This executes the `main` method in `eu.rossng.camle`, asking it to generate code for the `test7.w` test program. This program is designed to use all of the language's features.

The output is written to the file `/out.ass`.

## Executing the assembly

The assembly produced by the compilers can be executed using the `assmule` program. This is included as a binary (the source code is not available), tested with CentOS 6.7. To make it easier to test on any platform, you can use Vagrant. With Vagrant installed, simply run `vagrant up` to create a CentOS VM, then `vagrant ssh` to connect to it. `cd` to the `/vagrant` directory, where you will find the `assmule` binary. You can use it as follows:

```
./assmule out.ass
```

(where `out.ass` is the Jouette ASM file you want to assemble and execute)

## Future

* Implement optimisations (register allocation, fewer instructions)
* Re-implement in a functional language (OCaml? Haskell?)
