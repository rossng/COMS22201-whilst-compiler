package eu.rossng.camle

import eu.rossng.camle.ir.*
import java.io.PrintStream
import java.util.*

class IRTreeVisitor(val out: PrintStream, val mem: Memory) {
    fun visit(node: IRNode) {
        node.accept(this)
    }

    fun visitProgram(ctx: ProgramNode) {
        out.println("XOR R0,R0,R0")
        visit(ctx.child)
        out.println("HALT")
        mem.dumpAssembly(out)
    }

    fun visitSeqStm(ctx: SeqStmNode) {
        for (stm in ctx.statements)
            visit(stm)
    }

    fun visitWrite(ctx: WriteNode) {
        visit(ctx.expression)
        out.println("WR R1")
    }

    fun visitConst(ctx: ConstNode) {
        out.println("ADDI R1 R0 " + ctx.value)
    }

}
