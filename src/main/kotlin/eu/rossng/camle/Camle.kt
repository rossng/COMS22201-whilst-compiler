package eu.rossng.camle

import org.antlr.v4.runtime.ANTLRFileStream
import org.antlr.v4.runtime.CommonTokenStream
import org.antlr.v4.runtime.Token
import java.io.FileOutputStream
import java.io.OutputStream
import java.io.PrintStream

/**
 * A compiler for the Whilst language, built for COMS22201 at the University of Bristol.
 */
fun main(args: Array<String>) {
    println("\n=============================================================")
    println("CAMLE - Compiler to Abstract Machine for Language Engineering")
    println("=============================================================\n")
    var opt = ""
    var inFile = ""
    val outFile = ""
    val pos: Int
    if (args.size == 1 && args[0].get(0) != '-') {
        opt = ""
        inFile = args[0]
    } else if (args.size == 2 && args[0].get(0) == '-' && args[1].get(0) != '-') {
        opt = args[0]
        inFile = args[1]
    } else {
        println("Usage: <jar> <option> filename")
        println()
        println("Where <jar> executes the CAMLE jar, for example:")
        println(" java -jar target/antlr4-camle-jar-with-dependencies.jar")
        println()
        println("And where <option> is one of:\n -lex, -syn, -irt, -cg")
        println()
        println("And where filename is the name of a file containing Whilst source code")
        System.exit(1)
    }

    try {
        val input = ANTLRFileStream(inFile)
        val lexer = WhilstLexer(input)
        val tokens = CommonTokenStream(lexer)
        val parser = WhilstParser(tokens)

        when (opt) {
            "-lex" -> {
                var T: Token
                T = lexer.nextToken()
                while (T.type != -1) {
                    println("${T.type} \"${T.text}\"")
                    T = lexer.nextToken()
                }
                System.exit(0)
            }
            "-syn" -> {
                val tree = parser.program()
                println(tree.toStringTree(parser))
                System.exit(0)
            }
            "-irt" -> {
                val tree = parser.program()
                val whilstVisitor = WhilstFileVisitor()
                val irTree = whilstVisitor.visit(tree)
                System.out.println(irTree)
                System.exit(0)
            }
            "-cg" -> {
                val tree = parser.program()
                val whilstVisitor = WhilstFileVisitor()
                val irTree = whilstVisitor.visit(tree)
                val memory = Memory()
                val outputStream = PrintStream(FileOutputStream("out.ass"))
                val irVisitor = JouetteAsmGenerator(outputStream, memory)
                irVisitor.visit(irTree)
                outputStream.close()
            }
            else -> {
                println("Please provide valid arguments: -lex, -syn or -irt.")
                System.exit(1)
            }
        }
    } catch (e: Exception) {
        print(e.message)
        System.exit(1)
    }

}
