package eu.rossng.camle

internal class WhilstFileVisitor : WhilstBaseVisitor<IRTree?>() {
    override fun visitProgram(ctx: WhilstParser.ProgramContext): IRTree? {
        return visit(ctx.statements())
    }

    override fun visitStatements(ctx: WhilstParser.StatementsContext): IRTree? {
        if (ctx.statement().size > 1) {
            return IRTree("SEQ", ctx.statement().map { this.visit(it) }.filterNotNull().toArrayList())
        } else {
            return visit(ctx.statement(0))
        }
    }

    override fun visitStatementWriteExp(ctx: WhilstParser.StatementWriteExpContext): IRTree? {
        return IRTree("WR", arrayListOf(visit(ctx.exp())).filterNotNull().toArrayList())
    }

    override fun visitStatementWriteString(ctx: WhilstParser.StatementWriteStringContext): IRTree? {
        return IRTree("WRS", arrayListOf()) // TODO
    }

    override fun visitExpTimes(ctx: WhilstParser.ExpTimesContext): IRTree? {
        return IRTree("MULT", ctx.exp().map { this.visit(it) }.filterNotNull().toArrayList())
    }

    override fun visitExpPlusMinus(ctx: WhilstParser.ExpPlusMinusContext): IRTree? {
        if (ctx.op.text == "-") {
            return IRTree("SUB", ctx.exp().map { this.visit(it) }.filterNotNull().toArrayList())
        } else {
            return IRTree("ADD", ctx.exp().map { this.visit(it) }.filterNotNull().toArrayList())
        }
    }

    override fun visitExpId(ctx: WhilstParser.ExpIdContext): IRTree? {
        return visit(ctx.ID())
    }

    override fun visitExpIntNum(ctx: WhilstParser.ExpIntNumContext): IRTree? {
        return IRTree("CONST", arrayListOf(IRTree(ctx.INTNUM().text, arrayListOf())))
    }

    override fun visitExpBracketed(ctx: WhilstParser.ExpBracketedContext): IRTree? {
        return visit(ctx.exp())
    }

}
