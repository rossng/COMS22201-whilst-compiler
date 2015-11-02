# Whilst Compiler - CAMLE

A port of the University of Bristol CAMLE (Compiler to Abstract Machine for Language Engineering) to a standard Maven project format.

CAMLE is a skeleton compiler using ANTLRv3. It is meant to compile a language, which we'll call *Whilst*, but incomplete. *Whilst* is based on the *While* language from *Principles of Program Analysis* (Nielson, Nielson and Hankin).

## Using the compiler

To run the compiler, you will first need to package it using Maven. A Maven wrapper is included, so you can run:

```
./mvnw package
```

And the wrapper will do everything for you. There is no need to install Maven or ANTLR globally.

Once packaged, you can test the compiler skeleton by running:

```
 java -jar target/antlr3-camle-jar-with-dependencies.jar -lex src/test/while/testsk.w
```

This executes the `uk.ac.bris.cs.camle.Camle` class, asking it to lex the `testsk.w` test program. The subset of the language in `testsk.w` *is* implemented already, so this will output something like:

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

When completed, the compiler should be able to lex, parse and compile `test0.w`.

## Future development

I am currently updating the skeleton to use ANTLR4 - you can try a basic version with

```
 java -jar target/antlr4-camle-jar-with-dependencies.jar -syn src/test/while/testsk.w
```