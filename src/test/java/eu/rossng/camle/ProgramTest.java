package eu.rossng.camle;

import org.antlr.v4.runtime.*;
import org.antlr.v4.runtime.misc.Pair;
import org.antlr.v4.runtime.tree.ParseTree;
import org.junit.Test;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Optional;

import static org.junit.Assert.assertTrue;

public class ProgramTest {

    @Test
    public void testPrograms() {
        File[] testPrograms = new File("src/test/whilst").listFiles((f, n) -> n.endsWith(".w"));

        assertTrue(Arrays.stream(testPrograms)
                        .map(f -> new Pair<>(f, toANTLRFileStream(f)))
                        .map(p -> {
                            System.out.println("Parsing " + p.a.getName());
                            Optional<Boolean> isProgramParseable = p.b.flatMap(this::isProgramParseable);
                            System.out.println("Parseable? " + isProgramParseable.orElse(false));
                            System.out.println();
                            return new Pair<>(p.a, isProgramParseable);
                        })
                        .allMatch(p -> p.b.orElse(false))
        );
    }

    private Optional<ANTLRFileStream> toANTLRFileStream(File file) {
        try {
            return Optional.of(new ANTLRFileStream(file.getCanonicalPath()));
        } catch (IOException e) {
            System.err.println("Could not load file " + file.getName() + "\n" + e.getMessage());
        }
        return Optional.empty();
    }

    private Optional<Boolean> isProgramParseable(CharStream program) {
        SynLexer lexer = new SynLexer(program);
        CommonTokenStream tokens = new CommonTokenStream(lexer);
        SynParser parser = new SynParser(tokens);
        ErrorListener errorListener = new ErrorListener();
        parser.addErrorListener(errorListener);
        ParseTree tree = parser.program();
        System.out.println(tree.toStringTree(parser));

        if (errorListener.errors > 0) {
            System.err.println();
            return Optional.of(false);
        }
        return Optional.of(true);
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
