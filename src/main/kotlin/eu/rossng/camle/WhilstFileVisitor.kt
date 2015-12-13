package eu.rossng.camle

import eu.rossng.camle.ir.IRNode
import eu.rossng.camle.ir.SeqStmNode
import java.util.*

internal class WhilstFileVisitor : WhilstBaseVisitor<IRNode?>() {
    override fun visitProgram(ctx: WhilstParser.ProgramContext): IRNode? {
        return visit(ctx.statements())
    }

    override fun visitStatements(ctx: WhilstParser.StatementsContext): IRNode? {
        return SeqStmNode(ArrayList<IRNode>());
        ctx.statement().map { visit(it) }
    }



}
