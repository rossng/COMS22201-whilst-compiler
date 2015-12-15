package eu.rossng.camle

import eu.rossng.camle.ir.*
import java.util.*

internal class IRTreeStmGenerator(val expGenerator: IRTreeExpGenerator, val memory: Memory) : WhilstBaseVisitor<StmNode>() {
    var numIfElse = 0;
    var numCondLabels = 0;
    var ifElseStack = Stack<IfElse>()

    data class IfElse(val id: Int, val labelStack: Stack<LabelPair>)
    data class LabelPair(val trueLabel: String, val falseLabel: String)

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

    override fun visitStatementIfThenElse(ctx: WhilstParser.StatementIfThenElseContext): StmNode {
        numIfElse++
        val trueBranchLabel = "TB$numIfElse"
        val falseBranchLabel = "FB$numIfElse"
        val joinBranchLabel = "J$numIfElse"

        val labelStack = Stack<LabelPair>()
        labelStack.push(LabelPair(trueBranchLabel, falseBranchLabel))
        ifElseStack.push(IfElse(numIfElse, labelStack))

        val result = StmNode.Seq(
                visit(ctx.boolexp()),
                StmNode.Seq(
                        StmNode.SetLabel(trueBranchLabel),
                        StmNode.Seq(
                                visit(ctx.statement(0)),
                                StmNode.Seq(
                                        StmNode.Jump(ExpNode.Const(0), arrayListOf(joinBranchLabel)),
                                        StmNode.Seq(
                                                StmNode.SetLabel(falseBranchLabel),
                                                StmNode.Seq(
                                                        visit(ctx.statement(1)),
                                                        StmNode.Seq(
                                                                StmNode.Jump(ExpNode.Const(0), arrayListOf(joinBranchLabel)),
                                                                StmNode.SetLabel(joinBranchLabel)
                                                        )
                                                )
                                        )
                                )
                        )
                )
        )

        ifElseStack.pop()

        return result
    }

    override fun visitBoolExpAnd(ctx: WhilstParser.BoolExpAndContext): StmNode {
        val currentIfElse = ifElseStack.peek()
        numCondLabels++
        val nextConditionLabel = "IF${currentIfElse.id}CND${numCondLabels}" // Can nest inside each if-else block
        currentIfElse.labelStack.push(LabelPair(nextConditionLabel, currentIfElse.labelStack.peek().falseLabel))
        val term = visit(ctx.boolterm())
        currentIfElse.labelStack.pop()
        val result = StmNode.Seq(
                term,
                StmNode.Seq(
                        StmNode.SetLabel(nextConditionLabel),
                        visit(ctx.boolexp())
                )
        )
        return result
    }

    override fun visitBoolExpPlain(ctx: WhilstParser.BoolExpPlainContext): StmNode {
        val currentIfElse = ifElseStack.peek()
        currentIfElse.labelStack.push(
                LabelPair(currentIfElse.labelStack.peek().trueLabel, currentIfElse.labelStack.peek().falseLabel)
        )
        val result = visit(ctx.boolterm())
        currentIfElse.labelStack.pop()
        return result
    }

    override fun visitBoolTermPlain(ctx: WhilstParser.BoolTermPlainContext): StmNode {
        return visit(ctx.bool())
    }

    override fun visitBoolTrue(ctx: WhilstParser.BoolTrueContext): StmNode {
        return StmNode.Jump(ExpNode.Const(0), arrayListOf(ifElseStack.peek().labelStack.peek().trueLabel))
    }

    override fun visitBoolFalse(ctx: WhilstParser.BoolFalseContext): StmNode {
        return StmNode.Jump(ExpNode.Const(0), arrayListOf(ifElseStack.peek().labelStack.peek().falseLabel))
    }

    override fun visitBoolEq(ctx: WhilstParser.BoolEqContext): StmNode {
        val labels = ifElseStack.peek().labelStack.peek()
        return StmNode.Cjump(
                BoolOp.EQ, expGenerator.visit(ctx.exp(0)), expGenerator.visit(ctx.exp(1)),
                labels.trueLabel, labels.falseLabel)
    }

    override fun visitBoolLeq(ctx: WhilstParser.BoolLeqContext): StmNode {
        val labels = ifElseStack.peek().labelStack.peek()
        return StmNode.Cjump(
                BoolOp.LE, expGenerator.visit(ctx.exp(0)), expGenerator.visit(ctx.exp(1)),
                labels.trueLabel, labels.falseLabel
        )
    }

    override fun visitBoolBracketed(ctx: WhilstParser.BoolBracketedContext): StmNode {
        return visit(ctx.boolexp())
    }
}
