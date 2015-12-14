package eu.rossng.camle.ir

import eu.rossng.camle.IRTreeVisitor
import eu.rossng.camle.jouette.JouetteNode

interface IRNode {
    abstract fun accept(visitor: IRTreeVisitor) : JouetteNode
}