package eu.rossng.camle

import eu.rossng.camle.ir.*
import java.io.PrintStream

class JouetteAsmGenerator(val out: PrintStream) {
    fun invoke(stmNode: StmNode, memory: Memory) {
        out.println("XOR R0,R0,R0")
        generateStm(stmNode, 1)
        out.println("HALT")
        memory.dumpAssembly(out)
    }

    fun generateStm(stmNode: StmNode, reg: Int)  {
        stmNode.match(
            { generateMove(it, reg) },
            { generateStmExp(it, reg) },
            { generateJump(it, reg) },
            { generateCjump(it, reg) },
            { generateSeq(it, reg) },
            { generateSetLabel(it, reg) },
            { generateNop(it, reg) },
            { generateWrite(it, reg) },
            { generateWriteStr(it, reg) }
        )
    }

    fun generateExp(expNode: ExpNode, reg: Int): Int {
        var nextReg: Int = reg
        expNode.match(
                { nextReg = generateConst(it, reg) },
                { nextReg = generateName(it, reg) },
                { nextReg = generateTemp(it, reg) },
                { nextReg = generateBinop(it, reg) },
                { nextReg = generateMem(it, reg) },
                { nextReg = generateEseq(it, reg) },
                { nextReg = generateRead(it, reg) }
        )
        return nextReg
    }

    fun evaluateExp(expNode: ExpNode): Int {
        return expNode.match(
                { evaluateConst(it) },
                { -1 },
                { -1 },
                { -1 },
                { evaluateMem(it) },
                { -1 },
                { -1 }
        )
    }

    fun generateMove(move: StmNode.Move, nextReg: Int) {
        val destAddr = evaluateExp(move.destination)
        generateExp(move.source, nextReg)
        out.println("STORE R$nextReg,R0,$destAddr")
    }

    fun generateStmExp(exp: StmNode.Exp, reg: Int) {
        // TODO
    }

    fun generateJump(jump: StmNode.Jump, reg: Int) {
        out.println("JMP ${jump.targets[evaluateExp(jump.exp)]}")
    }

    fun generateCjump(cjump: StmNode.Cjump, nextReg: Int) {
        when (cjump.op) {
            BoolOp.EQ -> {
                val rightReg = generateExp(cjump.left, nextReg)
                generateExp(cjump.right, rightReg)
                out.println("SUB R$nextReg,R$nextReg,R$rightReg")
                out.println("BEQZ R$nextReg,${cjump.trueTarget}")
                out.println("JMP ${cjump.falseTarget}")
            }
            BoolOp.LE -> {
                val rightReg = generateExp(cjump.left, nextReg)
                generateExp(cjump.right, rightReg)
                out.println("SUB R$nextReg,R$rightReg,R$nextReg")
                out.println("BGEZ R$nextReg,${cjump.trueTarget}")
                out.println("JMP ${cjump.falseTarget}")
            }
            else -> {
                out.println("UNRECOGNISED BOOLOP")
            }
        }
    }

    fun generateSeq(seq: StmNode.Seq, reg: Int) {
        generateStm(seq.left, reg)
        generateStm(seq.right, reg)
    }

    fun generateSetLabel(setLabel: StmNode.SetLabel, reg: Int) {
        out.println("${setLabel.label}:")
    }

    fun generateNop(nop: StmNode.Nop, reg: Int) {}

    fun generateWrite(write: StmNode.Write, nextReg: Int) {
        generateExp(write.value, nextReg)
        out.println("WR R$nextReg")
    }

    fun generateWriteStr(writeStr: StmNode.WriteStr, nextReg: Int) {
        out.println("WRS " + evaluateExp(writeStr.addr))
    }

    ///////////

    fun generateConst(const: ExpNode.Const, nextReg: Int): Int {
        out.println("ADDI R$nextReg,R0,${const.value}; nextReg $nextReg")
        return nextReg + 1
    }

    fun generateName(name: ExpNode.Name, reg: Int): Int {
        return -1
    }

    fun generateTemp(temp: ExpNode.Temp, reg: Int): Int {
        return -1
    }

    fun generateBinop(binop: ExpNode.Binop, nextReg: Int): Int {
        when (binop.op) {
            Binops.MULTIPLY -> {
                val rightReg = generateExp(binop.left, nextReg)
                generateExp(binop.right, rightReg)
                out.println("MUL R$nextReg,R$nextReg,R$rightReg; nextReg $nextReg")
                return nextReg + 1
            }
            Binops.PLUS -> {
                val rightReg = generateExp(binop.left, nextReg)
                generateExp(binop.right, rightReg)
                out.println("ADD R$nextReg,R$nextReg,R$rightReg; nextReg $nextReg")
                return nextReg + 1
            }
            Binops.MINUS -> {
                val rightReg = generateExp(binop.left, nextReg)
                generateExp(binop.right, rightReg)
                out.println("SUB R$nextReg,R$nextReg,R$rightReg; nextReg $nextReg")
                return nextReg + 1
            }
            else -> {
                return -1
            }
        }
    }

    fun generateMem(mem: ExpNode.Mem, nextReg: Int): Int {
        val addr = evaluateExp(mem.addr)
        out.println("LOAD R$nextReg,R0,$addr; nextReg $nextReg")
        return nextReg + 1
    }

    fun generateEseq(eseq: ExpNode.Eseq, reg: Int): Int {
        return -1
    }

    fun generateRead(read: ExpNode.Read, nextReg: Int): Int {
        out.println("RD R$nextReg")
        return nextReg + 1
    }

    ///////////

    fun evaluateConst(const: ExpNode.Const): Int {
        return const.value
    }

    fun evaluateMem(mem: ExpNode.Mem): Int {
        return evaluateExp(mem.addr)
    }
}
