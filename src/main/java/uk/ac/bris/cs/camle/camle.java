package uk.ac.bris.cs.camle;// COMS22201: Skeleton of CAMLE compiler */

import java.io.*;
import java.lang.reflect.Array;

import org.antlr.runtime.*;
import org.antlr.runtime.tree.*;
import uk.ac.bris.cs.camle.Lex;
import uk.ac.bris.cs.camle.Syn;

class Camle {
    public static void main(String[] args) {
        System.out.println("\n=============================================================");
        System.out.println("CAMLE - Compiler to Abstract Machine for Language Engineering");
        System.out.println("=============================================================\n");
        String opt = "", inFile = "", outFile = "";
        int pos;
        if (Array.getLength(args) == 1 && args[0].charAt(0) != '-') {
            opt = "";
            inFile = args[0];
        } else if (Array.getLength(args) == 2 && args[0].charAt(0) == '-' &&
                args[1].charAt(0) != '-') {
            opt = args[0];
            inFile = args[1];
        } else {
            System.out.println("Usage: <jar> <option> filename");
            System.out.println();
            System.out.println("Where <jar> executes the CAMLE jar, for example:");
            System.out.println(" java -jar target/camle-maven-1.0-SNAPSHOT-jar-with-dependencies.jar");
            System.out.println();
            System.out.println("And where <option> is one of:\n -lex, -syn, -irt");
            System.out.println();
            System.out.println("And where filename is the name of a file containing Whilst source code");
            System.exit(1);
        }
        outFile = inFile;
        if ((pos = outFile.lastIndexOf('.')) != -1)
            outFile = outFile.substring(0, pos);
        outFile = outFile + ".ass";

        try {
            CharStream cs = new ANTLRFileStream(inFile);
            Lex lexO = new Lex(cs);
            if (opt.equals("-lex")) {
                Token T;
                T = lexO.nextToken();
                while (T.getType() != -1) {
                    System.out.println(T.getType() + " \"" + T.getText() + "\"");
                    T = lexO.nextToken();
                }
                System.exit(0);
            }
            CommonTokenStream tokens = new CommonTokenStream(lexO);
            Syn synO = new Syn(tokens);
            Syn.program_return parserResult = synO.program();//start rule
            CommonTree parserTree = (CommonTree) parserResult.getTree();
            if (opt.equals("-syn")) {
                System.out.println(parserTree.toStringTree());
                System.exit(0);
            }
            CommonTreeNodeStream ast = new CommonTreeNodeStream(parserTree);
            IRTree newIrt = Irt.convert(parserTree);
            if (opt.equals("-irt")) {
                System.out.println(newIrt);
                Memory.dumpData(System.out);
                System.exit(0);
            }
            PrintStream o = new PrintStream(new FileOutputStream(outFile));
            Cg.program(newIrt, o);
        } catch (Exception e) {
            System.err.println("exception: " + e);
            e.printStackTrace();
        }
    }
}
