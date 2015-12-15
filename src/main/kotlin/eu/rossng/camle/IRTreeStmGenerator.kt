package eu.rossng.camle

import eu.rossng.camle.ir.*

internal class IRTreeStmGenerator(val expGenerator: IRTreeExpGenerator, val memory: Memory) : WhilstBaseVisitor<StmNode>() {

    override fun visitProgram(ctx: WhilstParser.ProgramContext): StmNode {
        return visit(ctx.statements())
    }

    override fun visitStatementsSequence(ctx: WhilstParser.StatementsSequenceContext): StmNode {
        return StmNode.Seq(visit(ctx.statement()), visit(ctx.statements()))
    }

    override fun visitStatementsSingle(ctx: WhilstParser.StatementsSingleContext): StmNode {
        return visit(ctx.statement())
    }

    override fun visitStatementWriteExp(ctx: WhilstParser.StatementWriteExpContext): StmNode {
        return StmNode.Write(expGenerator.visit(ctx.exp()))
    }

    override fun visitStatementWriteString(ctx: WhilstParser.StatementWriteStringContext): StmNode {
        return StmNode.WriteStr(ExpNode.Const(memory.allocateString(ctx.STRING().text.substring(1, ctx.STRING().text.lastIndex))))
    }

    override fun visitStatementWriteLn(ctx: WhilstParser.StatementWriteLnContext): StmNode {
        return StmNode.WriteStr(ExpNode.Const(memory.allocateString("\n")))
    }

    override fun visitStatementAssign(ctx: WhilstParser.StatementAssignContext): StmNode {
        return StmNode.Move(ExpNode.Mem(ExpNode.Const(memory.allocateOrGetVariable(ctx.ID().text))), expGenerator.visit(ctx.exp()))
    }
}
