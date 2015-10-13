package eu.rossng.camle;

import org.antlr.v4.runtime.ANTLRFileStream;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.antlr.v4.runtime.tree.ParseTree;

import java.lang.reflect.Array;

/**
 * A compiler for the Whilst language, built for COMS22201 at the University of Bristol.
 */
public class Camle {
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

        try {
            ANTLRFileStream input = new ANTLRFileStream(inFile);
            SynLexer lexer = new SynLexer(input);
            CommonTokenStream tokens = new CommonTokenStream(lexer);
            SynParser parser = new SynParser(tokens);
            ParseTree tree = parser.program();

            switch (opt) {
                case "-lex":
                    Token T;
                    T = lexer.nextToken();
                    while (T.getType() != -1) {
                        System.out.println(T.getType() + " \"" + T.getText() + "\"");
                        T = lexer.nextToken();
                    }
                    System.exit(0);
                case "-syn":
                    System.out.println(tree.toStringTree(parser));
                    System.exit(0);
                default:
                    System.out.println("Support for -irt has not yet been implemented.");
                    System.exit(0);
            }
        } catch (Exception e) {
            System.out.print(e.getLocalizedMessage());
            System.exit(1);
        }
    }
}
