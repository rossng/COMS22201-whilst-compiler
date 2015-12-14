package eu.rossng.camle

import eu.rossng.camle.ir.*

internal class IRTreeExpGenerator : WhilstBaseVisitor<ExpNode>() {

    override fun visitExpIntNum(ctx: WhilstParser.ExpIntNumContext): ExpNode {
        return ExpNode.Const(Integer.parseInt(ctx.INTNUM().text))
    }

}
