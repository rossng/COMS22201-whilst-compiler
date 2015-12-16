# Whilst Compiler - CAMLE

A port of the University of Bristol CAMLE (Compiler to Abstract Machine for Language Engineering) to a standard Maven project format.

The original CAMLE is a skeleton compiler built on ANTLRv3, and is located in the `uk.ac.bris.cs.camle` package. It contains an incomplete implementation of a language which I'll call *Whilst*. *Whilst* is based on the *While* language from *Principles of Program Analysis* (Nielson, Nielson and Hankin).

I have re-implemented the compiler with ANTLR4 in the `eu.rossng.camle` package. The aims of my implementation were to:

* Minimise complexity (e.g. embedded Java in the ANTLR grammar, functions with side effects)
* Represent the IR tree in a typesafe manner, using classes to represent different kinds of nodes and dispatching methods based on those types (rather than switching on `instanceof` or tokens)
* Use a 'functional' style for as much of the code as possible
    * It turns out that this is very difficult with ANTLR, which does not allow you to inject custom dependencies into your visitor methods (all you get it the `WhilstParser.<Something>Context` object)
* Learn [Kotlin](https://kotlinlang.org/) by using it for all the compiler code
    * It's really good!

## Using the compiler

To run the compiler, you will first need to package it using Maven. A Maven wrapper is included, so you can run:

```
./mvnw package
```

The wrapper will download Maven, ANTLR and the Kotlin compiler for you.

### Skeleton compiler

Once packaged, you can test the compiler skeleton by running:

```
 java -jar target/antlr3-camle-jar-with-dependencies.jar -lex src/test/while/testsk.w
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
java -jar target/antlr4-camle-jar-with-dependencies.jar -cg "src/test/while/test7.w"
```

This executes the `main` method in `eu.rossng.camle`, asking it to generate code for the `test7.w` test program. This program is designed to use all of the language's features.