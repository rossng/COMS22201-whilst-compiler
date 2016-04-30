package eu.rossng.camle.haskell

sealed class Bexp {
    class True() : Bexp()
    class False() : Bexp()
    class Eq(val e1: Aexp, val e2: Aexp) : Bexp()
    class Le(val e1: Aexp, val e2: Aexp) : Bexp()
    class Neg(val e: Bexp) : Bexp()
    class And(val e1: Bexp, val e2: Bexp) : Bexp()
}