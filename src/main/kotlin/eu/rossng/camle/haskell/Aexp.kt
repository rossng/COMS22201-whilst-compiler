package eu.rossng.camle.haskell

sealed class Aexp {
    class N(val num: Int): Aexp()
    class V(val variable: String): Aexp()
    class Add(val e1: Aexp, val e2: Aexp): Aexp()
    class Mult(val e1: Aexp, val e2: Aexp): Aexp()
    class Sub(val e1: Aexp, val e2: Aexp): Aexp()
}