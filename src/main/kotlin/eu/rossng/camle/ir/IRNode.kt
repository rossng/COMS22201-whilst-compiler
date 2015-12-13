package eu.rossng.camle.ir

interface IRNode {
    fun getSub(i: Int): IRNode;
}