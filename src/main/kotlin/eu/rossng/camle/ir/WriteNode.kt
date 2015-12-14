package eu.rossng.camle.ir

import eu.rossng.camle.IRTreeVisitor
import eu.rossng.camle.jouette.JouetteNode

class WriteNode(val expression: IRNode) : IRNode {
    override fun accept(visitor: IRTreeVisitor) : JouetteNode {
        visitor.visitWrite(this)
        return JouetteNode()
    }
}