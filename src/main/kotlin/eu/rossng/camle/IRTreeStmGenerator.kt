package eu.rossng.camle

import eu.rossng.camle.ir.*
import java.util.*

internal class IRTreeStmGenerator(val expGenerator: IRTreeExpGenerator, val memory: Memory) : WhilstBaseVisitor<StmNode>() {
    // Internal state to hack around the fact that ANTLR doesn't support parameterised visitors
    // Wouldn't it be nice if it were all functional...
    var conditionalBlockIdProducer = IdProducer();
    var condLabelIdProducer = IdProducer();
    var conditionalBlockStack = Stack<IfElse>()

    class IdProducer {
        private var id = 0;
        fun getNewId(): Int {
            id++
            return id
        }
    }

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
        return StmNode.Move(
                ExpNode.Mem(ExpNode.Const(memory.allocateOrGetVariable(ctx.ID().text))),
                expGenerator.visit(ctx.exp())
        )
    }

    override fun visitStatementRead(ctx: WhilstParser.StatementReadContext): StmNode {
        return StmNode.Move(
                ExpNode.Mem(ExpNode.Const(memory.allocateOrGetVariable(ctx.ID().text))),
                ExpNode.Read()
        )
    }

    override fun visitStatementStatements(ctx: WhilstParser.StatementStatementsContext): StmNode {
        return visit(ctx.statements())
    }

    override fun visitStatementSkip(ctx: WhilstParser.StatementSkipContext): StmNode {
        return StmNode.Nop()
    }

    override fun visitStatementIfThenElse(ctx: WhilstParser.StatementIfThenElseContext): StmNode {

        // Save the information about this IfElse block to the stack, so that its immediate children can find the labels
        // they need to refer to etc.
        val id = conditionalBlockIdProducer.getNewId()
        val trueBranchLabel = "TB$id"
        val falseBranchLabel = "FB$id"
        val joinBranchLabel = "J$id"

        val labelStack = Stack<LabelPair>()
        labelStack.push(LabelPair(trueBranchLabel, falseBranchLabel))
        conditionalBlockStack.push(IfElse(id, labelStack))

        // Produce the IfElse block
        val result = StmNode.Seq(
                visit(ctx.boolexp()),
                StmNode.Seq(
                        StmNode.SetLabel(trueBranchLabel),
                        StmNode.Seq(
                                visit(ctx.statement(0)),                            // Another IfElse may be nested here
                                StmNode.Seq(
                                        StmNode.Jump(ExpNode.Const(0), arrayListOf(joinBranchLabel)),
                                        StmNode.Seq(
                                                StmNode.SetLabel(falseBranchLabel),
                                                StmNode.Seq(
                                                        visit(ctx.statement(1)),    // Another IfElse may be nested here
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

        // Now we're finished producing this IfElse block, pop it off the stack
        conditionalBlockStack.pop()

        return result
    }

    override fun visitStatementWhile(ctx: WhilstParser.StatementWhileContext): StmNode {

        // Save the information about this IfElse block to the stack, so that its immediate children can find the labels
        // they need to refer to etc.
        val id = conditionalBlockIdProducer.getNewId()
        val trueBranchLabel = "TB$id"
        val falseBranchLabel = "FB$id"
        val conditionLabel = "WC$id"

        val labelStack = Stack<LabelPair>()
        labelStack.push(LabelPair(trueBranchLabel, falseBranchLabel))
        conditionalBlockStack.push(IfElse(id, labelStack))

        // Produce the While block
        // condition label, condition test, true label, statement, jump to condition label, false label
        val result = StmNode.Seq(
                StmNode.SetLabel(conditionLabel),
                StmNode.Seq(
                    visit(ctx.boolexp()),
                    StmNode.Seq(
                            StmNode.SetLabel(trueBranchLabel),
                            StmNode.Seq(
                                    visit(ctx.statement()),
                                    StmNode.Seq(
                                            StmNode.Jump(ExpNode.Const(0), arrayListOf(conditionLabel)),
                                            StmNode.SetLabel(falseBranchLabel)
                                    )
                            )
                    )
                )
        )

        // Now we're finished producing this While block, pop it off the stack
        conditionalBlockStack.pop()

        return result
    }

    override fun visitBoolExpAnd(ctx: WhilstParser.BoolExpAndContext): StmNode {
        // Retrieve the IfElse statement in which this boolean expression is being used
        val currentIfElse = conditionalBlockStack.peek()

        // As this is a (boolterm AND boolexp), generate a label for jumping to the boolexp if the boolterm is true
        val id = condLabelIdProducer.getNewId()
        val nextConditionLabel = "IF${currentIfElse.id}CND${id}"

        // Store the nextConditionLabel (jump here if true) and existing false label (jump here if false) on the stack
        // so that the boolterm can refer to them
        currentIfElse.labelStack.push(LabelPair(nextConditionLabel, currentIfElse.labelStack.peek().falseLabel))
        val term = visit(ctx.boolterm())

        // Remove those labels from the stack
        currentIfElse.labelStack.pop()

        // Continue processing the rest of the boolexps
        return StmNode.Seq(
                term,
                StmNode.Seq(
                        StmNode.SetLabel(nextConditionLabel),
                        visit(ctx.boolexp())
                )
        )
    }

    override fun visitBoolExpPlain(ctx: WhilstParser.BoolExpPlainContext): StmNode {
        val currentIfElse = conditionalBlockStack.peek()
        currentIfElse.labelStack.push(
                LabelPair(currentIfElse.labelStack.peek().trueLabel, currentIfElse.labelStack.peek().falseLabel)
        )
        val result = visit(ctx.boolterm())
        currentIfElse.labelStack.pop()
        return result
    }

    override fun visitBoolTermNot(ctx: WhilstParser.BoolTermNotContext): StmNode {
        // Invert the true and false branch labels
        val currentLabelStack = conditionalBlockStack.peek().labelStack
        val oldLabels = currentLabelStack.pop()
        currentLabelStack.push(LabelPair(oldLabels.falseLabel, oldLabels.trueLabel))

        // Process the bool
        return visit(ctx.bool())
    }

    override fun visitBoolTermPlain(ctx: WhilstParser.BoolTermPlainContext): StmNode {
        return visit(ctx.bool())
    }

    override fun visitBoolTrue(ctx: WhilstParser.BoolTrueContext): StmNode {
        return StmNode.Jump(ExpNode.Const(0), arrayListOf(conditionalBlockStack.peek().labelStack.peek().trueLabel))
    }

    override fun visitBoolFalse(ctx: WhilstParser.BoolFalseContext): StmNode {
        return StmNode.Jump(ExpNode.Const(0), arrayListOf(conditionalBlockStack.peek().labelStack.peek().falseLabel))
    }

    override fun visitBoolEq(ctx: WhilstParser.BoolEqContext): StmNode {
        val labels = conditionalBlockStack.peek().labelStack.peek()
        return StmNode.Cjump(
                BoolOp.EQ, expGenerator.visit(ctx.exp(0)), expGenerator.visit(ctx.exp(1)),
                labels.trueLabel, labels.falseLabel)
    }

    override fun visitBoolLeq(ctx: WhilstParser.BoolLeqContext): StmNode {
        val labels = conditionalBlockStack.peek().labelStack.peek()
        return StmNode.Cjump(
                BoolOp.LE, expGenerator.visit(ctx.exp(0)), expGenerator.visit(ctx.exp(1)),
                labels.trueLabel, labels.falseLabel
        )
    }

    override fun visitBoolBracketed(ctx: WhilstParser.BoolBracketedContext): StmNode {
        return visit(ctx.boolexp())
    }
}
