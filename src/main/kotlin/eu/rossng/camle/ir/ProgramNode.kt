package eu.rossng.camle.ir

import eu.rossng.camle.IRTreeVisitor
import eu.rossng.camle.jouette.JouetteNode

class ProgramNode(val child: IRNode) : IRNode {
    override fun accept(visitor: IRTreeVisitor) : JouetteNode {
        visitor.visitProgram(this)
        return JouetteNode()
    }
}