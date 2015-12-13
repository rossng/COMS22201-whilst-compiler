package eu.rossng.camle.ir

class SeqStmNode(val statements: List<IRNode>) : IRNode {
    override fun getSub(i: Int): IRNode {
        return statements[i];
    }
}