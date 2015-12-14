package eu.rossng.camle.ir

import eu.rossng.camle.IRTreeVisitor
import eu.rossng.camle.jouette.JouetteNode

class ConstNode(val value: Int) : IRNode {
    override fun accept(visitor: IRTreeVisitor) : JouetteNode {
        visitor.visitConst(this)
        return JouetteNode()
    }
}