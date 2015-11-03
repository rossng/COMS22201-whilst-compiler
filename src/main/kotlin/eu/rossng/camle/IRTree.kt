package eu.rossng.camle

internal class IRTree(var operation: String?, val subTrees: MutableList<IRTree>) {

    fun addSub(subTree: IRTree) { subTrees.add(subTree) }

    fun getSub(i: Int): IRTree {
        return subTrees[i]
    }

    override fun toString(): String {
        var operationString = operation ?: "nop"
        var subTreeStrings = subTrees.map { " ${it.toString()}" }.fold ("", {s, t -> s + t})
        return "($operationString$subTreeStrings)"
    }
}
