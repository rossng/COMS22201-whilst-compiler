package eu.rossng.camle

import eu.rossng.camle.ir.*
import java.io.PrintStream

class JouetteAsmGenerator(val out: PrintStream) {

    fun invoke(stmNode: StmNode, memory: Memory) {
        out.println("XOR R0,R0,R0")
        generateStm(stmNode)
        out.println("HALT")
        memory.dumpAssembly(out)
    }

    fun generateStm(stmNode: StmNode)  {
        stmNode.match(
            { generateMove(it) },
            { generateExp(it) },
            { generateJump(it) },
            { generateCjump(it) },
            { generateSeq(it) },
            { generateSetLabel(it) },
            { generateNop(it) },
            { generateWrite(it) },
            { generateWriteStr(it) }
        )
    }

    fun generateExp(expNode: ExpNode) {
        expNode.match(
            { generateConst(it) },
            { generateName(it) },
            { generateTemp(it) },
            { generateBinop(it) },
            { generateMem(it) },
            { generateEseq(it) }
        )
    }

    fun evaluateExp(expNode: ExpNode): Int {
        return expNode.match(
                { evaluateConst(it) },
                { -1 },
                { -1 },
                { -1 },
                { -1 },
                { -1 }
        )
    }

    fun generateMove(move: StmNode.Move) {
        // TODO
    }

    fun generateExp(exp: StmNode.Exp) {
        // TODO
    }

    fun generateJump(jump: StmNode.Jump) {}

    fun generateCjump(cjump: StmNode.Cjump) {}

    fun generateSeq(seq: StmNode.Seq) {
        generateStm(seq.left)
        generateStm(seq.right)
    }

    fun generateSetLabel(setLabel: StmNode.SetLabel) {}

    fun generateNop(nop: StmNode.Nop) {}

    fun generateWrite(write: StmNode.Write) {
        generateExp(write.value)
        out.println("WR R1")
    }

    fun generateWriteStr(writeStr: StmNode.WriteStr) {
        out.println("WRS " + evaluateExp(writeStr.addr))
    }

    fun generateConst(const: ExpNode.Const) {
        out.println("ADDI R1,R0," + const.value)
    }

    fun generateName(name: ExpNode.Name) {}

    fun generateTemp(temp: ExpNode.Temp) {}

    fun generateBinop(binop: ExpNode.Binop) {}

    fun generateMem(mem: ExpNode.Mem) {}

    fun generateEseq(eseq: ExpNode.Eseq) {}

    fun evaluateConst(const: ExpNode.Const): Int {
        return const.value;
    }

}
