package eu.rossng.camle.ir

public abstract class ExpNode private constructor() : IRNode {

    abstract fun <T> match(
            a: (Const) -> T,
            b: (Name) -> T,
            c: (Temp) -> T,
            d: (Binop) -> T,
            e: (Mem) -> T,
            f: (Eseq) -> T): T

    class Const(val value: Int) : ExpNode() {
        override fun <T> match(a: (Const) -> T, b: (Name) -> T, c: (Temp) -> T,
                               d: (Binop) -> T, e: (Mem) -> T, f: (Eseq) -> T): T {
            return a.invoke(this)
        }
    }

    class Name(val label: Label) : ExpNode() {
        override fun <T> match(a: (Const) -> T, b: (Name) -> T, c: (Temp) -> T,
                               d: (Binop) -> T, e: (Mem) -> T, f: (Eseq) -> T): T {
            return b.invoke(this)
        }
    }

    class Temp(val temp: Temp) : ExpNode() {
        override fun <T> match(a: (Const) -> T, b: (Name) -> T, c: (Temp) -> T,
                               d: (Binop) -> T, e: (Mem) -> T, f: (Eseq) -> T): T {
            return c.invoke(this)
        }
    }

    class Binop(val op: Binops, val left: ExpNode, val right: ExpNode) : ExpNode() {
        override fun <T> match(a: (Const) -> T, b: (Name) -> T, c: (Temp) -> T,
                               d: (Binop) -> T, e: (Mem) -> T, f: (Eseq) -> T): T {
            return d.invoke(this)
        }
    }

    class Mem(val addr: ExpNode) : ExpNode() {
        override fun <T> match(a: (Const) -> T, b: (Name) -> T, c: (Temp) -> T,
                               d: (Binop) -> T, e: (Mem) -> T, f: (Eseq) -> T): T {
            return e.invoke(this)
        }
    }

    class Eseq(val statement: StmNode, val result: ExpNode) : ExpNode() {
        override fun <T> match(a: (Const) -> T, b: (Name) -> T, c: (Temp) -> T,
                               d: (Binop) -> T, e: (Mem) -> T, f: (Eseq) -> T): T {
            return f.invoke(this)
        }
    }
}