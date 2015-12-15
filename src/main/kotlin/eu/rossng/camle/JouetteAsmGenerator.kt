package eu.rossng.camle

import eu.rossng.camle.ir.*
import java.io.PrintStream

class JouetteAsmGenerator(val out: PrintStream) {
    var registersAllocated = 0

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

    fun generateExp(expNode: ExpNode): Int {
        var reg: Int = -1
        expNode.match(
            { reg = generateConst(it) },
            { reg = generateName(it) },
            { reg = generateTemp(it) },
            { reg = generateBinop(it) },
            { reg = generateMem(it) },
            { reg = generateEseq(it) }
        )
        return reg
    }

    fun evaluateExp(expNode: ExpNode): Int {
        return expNode.match(
                { evaluateConst(it) },
                { -1 },
                { -1 },
                { -1 },
                { evaluateMem(it) },
                { -1 }
        )
    }

    fun generateMove(move: StmNode.Move) {
        val destAddr = evaluateExp(move.destination)
        val srcReg = generateExp(move.source)
        out.println("STORE R" + srcReg + ",R0," + destAddr)
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
        val reg = generateExp(write.value)
        out.println("WR R" + reg)
    }

    fun generateWriteStr(writeStr: StmNode.WriteStr) {
        out.println("WRS " + evaluateExp(writeStr.addr))
    }

    fun generateConst(const: ExpNode.Const): Int {
        val reg = getNewRegister()
        out.println("ADDI R" + reg + ",R0," + const.value)
        return reg
    }

    fun generateName(name: ExpNode.Name): Int {
        return -1
    }

    fun generateTemp(temp: ExpNode.Temp): Int {
        return -1
    }

    fun generateBinop(binop: ExpNode.Binop): Int {
        when (binop.op) {
            Binops.MULTIPLY -> {
                val leftReg = generateExp(binop.left)
                val rightReg = generateExp(binop.right)
                val resultReg = getNewRegister()
                out.println("MUL R" + resultReg + ",R" + leftReg + ",R" + rightReg)
                return resultReg
            }
            Binops.PLUS -> {
                val leftReg = generateExp(binop.left)
                val rightReg = generateExp(binop.right)
                val resultReg = getNewRegister()
                out.println("ADD R" + resultReg + ",R" + leftReg + ",R" + rightReg)
                return resultReg
            }
            Binops.MINUS -> {
                val leftReg = generateExp(binop.left)
                val rightReg = generateExp(binop.right)
                val resultReg = getNewRegister()
                out.println("SUB R" + resultReg + ",R" + leftReg + ",R" + rightReg)
                return resultReg
            }
            else -> {
                return -1
            }
        }
    }

    fun generateMem(mem: ExpNode.Mem): Int {
        val addr = evaluateExp(mem.addr)
        val resultReg = getNewRegister()
        out.println("LOAD R" + resultReg + ",R0," + addr)
        return resultReg
    }

    fun generateEseq(eseq: ExpNode.Eseq): Int {
        return -1
    }

    fun evaluateConst(const: ExpNode.Const): Int {
        return const.value
    }

    fun evaluateMem(mem: ExpNode.Mem): Int {
        return evaluateExp(mem.addr)
    }

    fun getNewRegister(): Int {
        registersAllocated++
        return registersAllocated
    }

}
