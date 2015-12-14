package eu.rossng.camle

import eu.rossng.camle.ir.ConstNode
import eu.rossng.camle.ir.ProgramNode
import eu.rossng.camle.ir.SeqStmNode
import eu.rossng.camle.ir.WriteNode
import java.io.PrintStream

class JouetteAsmGenerator(val out: PrintStream, val mem: Memory) {

    fun generateAsm(programNode: ProgramNode) {
        out.println("XOR R0,R0,R0")
        generateAsm(programNode.statements)
        out.println("HALT")
        mem.dumpAssembly(out)
    }

    fun generateAsm(seqStmNode: SeqStmNode) {
        for (stm in seqStmNode.statements)
            generateAsm(stm)
    }

    fun generateAsm(ctx: WriteNode) {
        visit(ctx.expression)
        out.println("WR R1")
    }

    fun visitConst(ctx: ConstNode) {
        out.println("ADDI R1 R0 " + ctx.value)
    }

}
