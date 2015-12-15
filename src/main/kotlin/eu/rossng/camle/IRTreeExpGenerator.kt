package eu.rossng.camle

import eu.rossng.camle.ir.*

internal class IRTreeExpGenerator(val memory: Memory) : WhilstBaseVisitor<ExpNode>() {

    override fun visitExpIntNum(ctx: WhilstParser.ExpIntNumContext): ExpNode {
        return ExpNode.Const(Integer.parseInt(ctx.INTNUM().text))
    }

    override fun visitExpTimes(ctx: WhilstParser.ExpTimesContext): ExpNode {
        return ExpNode.Binop(Binops.MULTIPLY, visit(ctx.exp(0)), visit(ctx.exp(1)))
    }

    override fun visitExpPlusMinus(ctx: WhilstParser.ExpPlusMinusContext): ExpNode {
        when(ctx.op.text) {
            "-" -> return ExpNode.Binop(Binops.MINUS, visit(ctx.exp(0)), visit(ctx.exp(1)))
            else -> return ExpNode.Binop(Binops.PLUS, visit(ctx.exp(0)), visit(ctx.exp(1)))
        }
    }

    override fun visitExpId(ctx: WhilstParser.ExpIdContext): ExpNode {
        return ExpNode.Mem(ExpNode.Const(memory.allocateOrGetVariable(ctx.text)))
    }

}
