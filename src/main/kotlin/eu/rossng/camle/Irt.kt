package eu.rossng.camle

// COMS22201: IR tree construction

import org.antlr.runtime.Token
import org.antlr.runtime.tree.CommonTree

object Irt {
    // The code below is generated automatically from the ".tokens" file of the
    // ANTLR syntax analysis, using the TokenConv program.
    //
    // CAMLE TOKENS BEGIN
    val tokenNames = arrayOf("NONE", "NONE", "NONE", "NONE", "CLOSEPAREN", "COMMENT", "INTNUM", "OPENPAREN", "SEMICOLON", "STRING", "WRITE", "WRITELN", "WS")
    val CLOSEPAREN = 4
    val COMMENT = 5
    val INTNUM = 6
    val OPENPAREN = 7
    val SEMICOLON = 8
    val STRING = 9
    val WRITE = 10
    val WRITELN = 11
    val WS = 12
    // CAMLE TOKENS END

    fun convert(ast: CommonTree): IRTree {
        val irt = IRTree(null, arrayListOf())
        program(ast, irt)
        return irt
    }

    fun program(ast: CommonTree, irt: IRTree) {
        statements(ast, irt)
    }

    fun statements(ast: CommonTree, irt: IRTree) {
        val i: Int
        val t = ast.getToken()
        val tt = t.type

        if (tt == SEMICOLON) {
            val irt1 = IRTree(null, arrayListOf())
            val irt2 = IRTree(null, arrayListOf())
            val ast1 = ast.getChild(0) as CommonTree
            val ast2 = ast.getChild(1) as CommonTree
            statements(ast1, irt1)
            statements(ast2, irt2)
            irt.operation = "SEQ"
            irt.addSub(irt1)
            irt.addSub(irt2)
        } else {
            statement(ast, irt)
        }
    }

    fun statement(ast: CommonTree, irt: IRTree) {
        val ast1: CommonTree
        val ast2: CommonTree
        val ast3: CommonTree
        val irt1 = IRTree(null, arrayListOf())
        val irt2 = IRTree(null, arrayListOf())
        val irt3 = IRTree(null, arrayListOf())
        val t = ast.getToken()
        val tt = t.type
        if (tt == WRITE) {
            ast1 = ast.getChild(0) as CommonTree
            val type = arg(ast1, irt1)
            if (type == "int") {
                irt.operation = "WR"
                irt.addSub(irt1)
            } else {
                irt.operation = "WRS"
                irt.addSub(irt1)
            }
        } else if (tt == WRITELN) {
            irt.operation = "WRS"
            irt.addSub(
                    IRTree("MEM", arrayListOf(
                            IRTree("CONST", arrayListOf(
                                    IRTree("\n", arrayListOf())
                            ))
                    ))
            )
        } else {
            error(tt)
        }
    }

    fun arg(ast: CommonTree, irt: IRTree): String {
        val t = ast.getToken()
        val tt = t.type
        if (tt == STRING) {
            val tx = t.text
            irt.operation = "MEM"
            irt.addSub(IRTree("CONST", arrayListOf(IRTree(tx, arrayListOf()))))
            return "string"
        } else {
            expression(ast, irt)
            return "int"
        }
    }

    fun expression(ast: CommonTree, irt: IRTree) {
        val ast1: CommonTree
        val irt1 = IRTree(null, arrayListOf())
        val t = ast.getToken()
        val tt = t.type
        if (tt == INTNUM) {
            constant(ast, irt1)
            irt.operation = "CONST"
            irt.addSub(irt1)
        }
    }

    fun constant(ast: CommonTree, irt: IRTree) {
        val t = ast.getToken()
        val tt = t.type
        if (tt == INTNUM) {
            val tx = t.text
            irt.operation = tx
        } else {
            error(tt)
        }
    }

    private fun error(tt: Int) {
        println("IRT error: " + tokenNames[tt])
        System.exit(1)
    }
}
