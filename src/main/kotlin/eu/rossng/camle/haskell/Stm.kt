package eu.rossng.camle.haskell

sealed class Stm {
    class Ass(val variable: String, val e: Aexp): Stm()
    class Skip(): Stm()
    class Comp(val s1: Stm, val s2: Stm): Stm()
    class If(val b: Bexp, val t: Stm, val f: Stm): Stm()
    class While(val b: Bexp, val s: Stm): Stm()
    class Read(val variable: String): Stm()
    class WriteA(val e: Aexp): Stm()
    class WriteB(val e: Bexp): Stm()
    class WriteS(val s: String): Stm()
    class WriteLn(): Stm()
}