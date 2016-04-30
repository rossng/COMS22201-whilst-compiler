package eu.rossng.camle.haskell

import eu.rossng.camle.WhilstBaseVisitor
import eu.rossng.camle.WhilstParser

class HaskellStmGenerator(val aexpGenerator: HaskellAexpGenerator, val bexpGenerator: HaskellBexpGenerator) : WhilstBaseVisitor<Stm>() {
    override fun visitProgram(ctx: WhilstParser.ProgramContext?): Stm? {
        return visit(ctx!!.statements())
    }

    override fun visitStatementsSequence(ctx: WhilstParser.StatementsSequenceContext?): Stm? {
        return Stm.Comp(visit(ctx!!.statement()), visit(ctx.statements()))
    }

    override fun visitStatementsSingle(ctx: WhilstParser.StatementsSingleContext?): Stm? {
        return visit(ctx!!.statement())
    }

    override fun visitStatementAssign(ctx: WhilstParser.StatementAssignContext?): Stm? {
        return Stm.Ass(ctx!!.ID().text, aexpGenerator.visit(ctx.exp()))
    }

    override fun visitStatementSkip(ctx: WhilstParser.StatementSkipContext?): Stm? {
        return Stm.Skip()
    }

    override fun visitStatementIfThenElse(ctx: WhilstParser.StatementIfThenElseContext?): Stm? {
        return Stm.If(bexpGenerator.visit(ctx!!.boolexp()), visit(ctx.statement(0)), visit(ctx.statement(1)))
    }

    override fun visitStatementWhile(ctx: WhilstParser.StatementWhileContext?): Stm? {
        return Stm.While(bexpGenerator.visit(ctx!!.boolexp()), visit(ctx.statement()))
    }

    override fun visitStatementRead(ctx: WhilstParser.StatementReadContext?): Stm? {
        return Stm.Read(ctx!!.ID().text)
    }

    override fun visitStatementWriteExp(ctx: WhilstParser.StatementWriteExpContext?): Stm? {
        return Stm.WriteA(aexpGenerator.visit(ctx!!.exp()))
    }

    override fun visitStatementWriteBoolExp(ctx: WhilstParser.StatementWriteBoolExpContext?): Stm? {
        return Stm.WriteB(bexpGenerator.visit(ctx!!.boolexp()))
    }

    override fun visitStatementWriteString(ctx: WhilstParser.StatementWriteStringContext?): Stm? {
        return Stm.WriteS(ctx!!.STRING().text.slice(1..(ctx.STRING().text.length - 2)))
    }

    override fun visitStatementWriteLn(ctx: WhilstParser.StatementWriteLnContext?): Stm? {
        return Stm.WriteLn()
    }

    override fun visitStatementStatements(ctx: WhilstParser.StatementStatementsContext?): Stm? {
        return visit(ctx!!.statements())
    }
}

class HaskellAexpGenerator() : WhilstBaseVisitor<Aexp>() {
    override fun visitExpId(ctx: WhilstParser.ExpIdContext?): Aexp? {
        return Aexp.V(ctx!!.ID().text)
    }

    override fun visitExpPlusMinus(ctx: WhilstParser.ExpPlusMinusContext?): Aexp? {
        return when (ctx!!.op.text) {
            "+" -> Aexp.Add(visit(ctx.exp(0)), visit(ctx.exp(1)))
            "-" -> Aexp.Sub(visit(ctx.exp(0)), visit(ctx.exp(1)))
            else -> null
        }
    }

    override fun visitExpTimes(ctx: WhilstParser.ExpTimesContext?): Aexp? {
        return Aexp.Mult(visit(ctx!!.exp(0)), visit(ctx.exp(1)))
    }

    override fun visitExpIntNum(ctx: WhilstParser.ExpIntNumContext?): Aexp? {
        return Aexp.N(ctx!!.INTNUM().text.toInt())
    }

    override fun visitExpBracketed(ctx: WhilstParser.ExpBracketedContext?): Aexp? {
        return visit(ctx!!.exp())
    }

}

class HaskellBexpGenerator(val aexpGenerator: HaskellAexpGenerator) : WhilstBaseVisitor<Bexp>() {
    override fun visitBoolExpAnd(ctx: WhilstParser.BoolExpAndContext?): Bexp? {
        return Bexp.And(visit(ctx!!.boolterm()), visit(ctx.boolexp()))
    }

    override fun visitBoolExpPlain(ctx: WhilstParser.BoolExpPlainContext?): Bexp? {
        return visit(ctx!!.boolterm())
    }

    override fun visitBoolTermNot(ctx: WhilstParser.BoolTermNotContext?): Bexp? {
        return Bexp.Neg(visit(ctx!!.bool()))
    }

    override fun visitBoolTermPlain(ctx: WhilstParser.BoolTermPlainContext?): Bexp? {
        return visit(ctx!!.bool())
    }

    override fun visitBoolTrue(ctx: WhilstParser.BoolTrueContext?): Bexp? {
        return Bexp.True()
    }

    override fun visitBoolFalse(ctx: WhilstParser.BoolFalseContext?): Bexp? {
        return Bexp.False()
    }

    override fun visitBoolEq(ctx: WhilstParser.BoolEqContext?): Bexp? {
        return Bexp.Eq(aexpGenerator.visit(ctx!!.exp(0)), aexpGenerator.visit(ctx.exp(1)))
    }

    override fun visitBoolLeq(ctx: WhilstParser.BoolLeqContext?): Bexp? {
        return Bexp.Le(aexpGenerator.visit(ctx!!.exp(0)), aexpGenerator.visit(ctx.exp(1)))
    }

    override fun visitBoolBracketed(ctx: WhilstParser.BoolBracketedContext?): Bexp? {
        return visit(ctx!!.boolexp())
    }

}