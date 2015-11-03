package eu.rossng.camle

import org.antlr.v4.runtime.*
import org.junit.Assert.assertTrue
import org.junit.Test
import java.io.File
import java.io.IOException

class ProgramTest {

    @Test
    fun testPrograms() {
        val testPrograms = File("src/test/whilst").listFiles { f, n -> n.endsWith(".w") }
        assertTrue(
            testPrograms
                    .map { Pair<File, ANTLRFileStream?>(it, toANTLRFileStream(it)) }
                    .map {
                        println("Parsing " + it.first.getName())
                        val isProgramParseable:Boolean = it.second?.let { isProgramParseable(it) } ?: false
                        println(if (isProgramParseable) "Parseable" else "Not parseable")
                        println()
                        isProgramParseable
                     }
                    .all { it }
        )
    }

    private fun toANTLRFileStream(file: File): ANTLRFileStream? {
        return try {
            ANTLRFileStream(file.canonicalPath)
        } catch (e: IOException) {
            System.err.println("Could not load file ${file.name} \n ${e.message}")
            null
        }
    }

    private fun isProgramParseable(program: CharStream): Boolean {
        val lexer = WhilstLexer(program)
        val tokens = CommonTokenStream(lexer)
        val parser = WhilstParser(tokens)
        val errorListener = ErrorListener()
        parser.addErrorListener(errorListener)
        val tree = parser.program()
        println(tree.toStringTree(parser))

        if (errorListener.errors > 0) {
            System.err.println()
            return false
        }
        return true
    }

    private inner class ErrorListener : BaseErrorListener() {
        var errors = 0

        override fun syntaxError(recognizer: Recognizer<*, *>?, offendingSymbol: Any?, line: Int, charPositionInLine: Int, msg: String?, e: RecognitionException?) {
            errors++
            super.syntaxError(recognizer, offendingSymbol, line, charPositionInLine, msg, e)
        }
    }

}
