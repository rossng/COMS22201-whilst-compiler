package eu.rossng.camle.ir

import eu.rossng.camle.IRTreeVisitor
import eu.rossng.camle.jouette.JouetteNode

class SeqStmNode(val statements: List<IRNode>) : IRNode {
    override fun accept(visitor: IRTreeVisitor) : JouetteNode {
        visitor.visitSeqStm(this)
        return JouetteNode()
    }
}