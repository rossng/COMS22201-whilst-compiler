package eu.rossng.camle.ir
import java.util.function.Function

public abstract class StmNode private constructor() : IRNode {

    abstract fun <T> match(
            a: (Move) -> T,
            b: (Exp) -> T,
            c: (Jump) -> T,
            d: (Cjump) -> T,
            e: (Seq) -> T,
            f: (SetLabel) -> T,
            g: (Nop) -> T,
            h: (Write) -> T,
            i: (WriteStr) -> T
    ): T

    class Move(val destination: ExpNode, val source: ExpNode) : StmNode() {

        override fun <T> match(a: (Move) -> T, b: (Exp) -> T, c: (Jump) -> T, d: (Cjump) -> T,
                               e: (Seq) -> T, f: (SetLabel) -> T, g: (Nop) -> T,
                               h: (Write) -> T, i: (WriteStr) -> T): T {
            return a.invoke(this)
        }
    }

    class Exp(val exp: ExpNode) : StmNode() {

        override fun <T> match(a: (Move) -> T, b: (Exp) -> T, c: (Jump) -> T, d: (Cjump) -> T,
                               e: (Seq) -> T, f: (SetLabel) -> T, g: (Nop) -> T,
                               h: (Write) -> T, i: (WriteStr) -> T): T {
            return b.invoke(this)
        }
    }

    class Jump(val exp: ExpNode, val targets: List<String>) : StmNode() {

        override fun <T> match(a: (Move) -> T, b: (Exp) -> T, c: (Jump) -> T, d: (Cjump) -> T,
                               e: (Seq) -> T, f: (SetLabel) -> T, g: (Nop) -> T,
                               h: (Write) -> T, i: (WriteStr) -> T): T {
            return c.invoke(this)
        }
    }

    class Cjump(val op: BoolOp, val left: ExpNode, val right: ExpNode,
                val trueTarget: String, val falseTarget: String ) : StmNode() {

        override fun <T> match(a: (Move) -> T, b: (Exp) -> T, c: (Jump) -> T, d: (Cjump) -> T,
                               e: (Seq) -> T, f: (SetLabel) -> T, g: (Nop) -> T,
                               h: (Write) -> T, i: (WriteStr) -> T): T {
            return d.invoke(this)
        }
    }

    class Seq(val left: StmNode, val right: StmNode) : StmNode() {

        override fun <T> match(a: (Move) -> T, b: (Exp) -> T, c: (Jump) -> T, d: (Cjump) -> T,
                               e: (Seq) -> T, f: (SetLabel) -> T, g: (Nop) -> T,
                               h: (Write) -> T, i: (WriteStr) -> T): T {
            return e.invoke(this)
        }
    }

    class SetLabel(val label: String) : StmNode() {

        override fun <T> match(a: (Move) -> T, b: (Exp) -> T, c: (Jump) -> T, d: (Cjump) -> T,
                               e: (Seq) -> T, f: (SetLabel) -> T, g: (Nop) -> T,
                               h: (Write) -> T, i: (WriteStr) -> T): T {
            return f.invoke(this)
        }
    }

    class Nop() : StmNode() {

        override fun <T> match(a: (Move) -> T, b: (Exp) -> T, c: (Jump) -> T, d: (Cjump) -> T,
                               e: (Seq) -> T, f: (SetLabel) -> T, g: (Nop) -> T,
                               h: (Write) -> T, i: (WriteStr) -> T): T {
            return g.invoke(this)
        }
    }

    class Write(val value: ExpNode) : StmNode() {

        override fun <T> match(a: (Move) -> T, b: (Exp) -> T, c: (Jump) -> T, d: (Cjump) -> T,
                               e: (Seq) -> T, f: (SetLabel) -> T, g: (Nop) -> T,
                               h: (Write) -> T, i: (WriteStr) -> T): T {
            return h.invoke(this)
        }
    }

    class WriteStr(val addr: ExpNode) : StmNode() {

        override fun <T> match(a: (Move) -> T, b: (Exp) -> T, c: (Jump) -> T, d: (Cjump) -> T,
                               e: (Seq) -> T, f: (SetLabel) -> T, g: (Nop) -> T,
                               h: (Write) -> T, i: (WriteStr) -> T): T {
            return i.invoke(this)
        }
    }
}