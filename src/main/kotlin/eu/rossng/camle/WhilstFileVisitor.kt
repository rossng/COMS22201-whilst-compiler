package eu.rossng.camle

import eu.rossng.camle.ir.*
import java.util.*

internal class WhilstFileVisitor : WhilstBaseVisitor<IRNode>() {
    override fun visitProgram(ctx: WhilstParser.ProgramContext): IRNode {
        return ProgramNode(visit(ctx.statements()))
    }

    override fun visitStatements(ctx: WhilstParser.StatementsContext): IRNode {
        val statements = arrayListOf<IRNode>();
        ctx.statement().forEach {
            statements.add(visit(it))
        }

        return SeqStmNode(statements);
    }

    override fun visitStatementWriteExp(ctx: WhilstParser.StatementWriteExpContext): IRNode {
        return WriteNode(visit(ctx.exp()))
    }

    override fun visitExpIntNum(ctx: WhilstParser.ExpIntNumContext): IRNode {
        return ConstNode(Integer.parseInt(ctx.INTNUM().text))
    }

}
