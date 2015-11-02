package eu.rossng.camle;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

import static org.junit.Assert.fail;

public class ProgramTest {
    @Test
    public void testPrograms() {
        File dir = new File("src/test/whilst");

        for (File testProgram : dir.listFiles((f, n) -> n.endsWith(".w"))) {
            try {
                ANTLRFileStream input = new ANTLRFileStream(testProgram.getCanonicalPath());
                SynLexer lexer = new SynLexer(input);
                CommonTokenStream tokens = new CommonTokenStream(lexer);
                SynParser parser = new SynParser(tokens);
                parser.addErrorListener(new ErrorListener());
                ParseTree tree = parser.program();
                System.out.println("Successfully parsed " + testProgram.getName());
                System.out.println(tree.toStringTree(parser));
            } catch (IOException e) {
                fail("Could not load test program " + testProgram.getName() + "\n" + e.getMessage());
            }
        }
    }

    private class ErrorListener extends BaseErrorListener {

        public int errors = 0;

        @Override
        public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol, int line, int charPositionInLine, String msg, RecognitionException e) {
            errors++;
            super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e);
        }
    }

}
