package eu.rossng.camle.haskell

fun show(s: Stm): String {
    return when (s) {
        is Stm.Ass -> "Ass \"" + s.variable + "\" (" + show(s.e) + ")"
        is Stm.Comp -> "Comp (" + show(s.s1) + ") (" + show(s.s2) + ")"
        is Stm.If -> "If (" + show(s.b) + ") (" + show(s.t) + ") (" + show(s.f) + ")"
        is Stm.Skip -> "Skip"
        is Stm.While -> "While (" + show(s.b) + ") (" + show(s.s) + ")"
        is Stm.Read -> "Read \"${s.variable}\""
        is Stm.WriteA -> "WriteA (" + show(s.e) + ")"
        is Stm.WriteB -> "WriteB (" + show(s.e) + ")"
        is Stm.WriteS -> "WriteS \"${s.s}\""
        is Stm.WriteLn -> "WriteLn"
    }
}

fun show(e: Aexp): String {
    return when (e) {
        is Aexp.N -> "N " + e.num.toString()
        is Aexp.V -> "V \"" + e.variable + "\""
        is Aexp.Add -> "Add (" + show(e.e1) + ") (" + show(e.e2) + ")"
        is Aexp.Mult -> "Mult (" + show(e.e1) + ") (" + show(e.e2) + ")"
        is Aexp.Sub -> "Sub (" + show(e.e1) + ") (" + show(e.e2) + ")"
    }
}

fun show(b: Bexp): String {
    return when (b) {
        is Bexp.And -> "And (" + show(b.e1) + ") (" + show(b.e2) + ")"
        is Bexp.Eq -> "Eq (" + show(b.e1) + ") (" + show(b.e2) + ")"
        is Bexp.False -> "FALSE"
        is Bexp.Le -> "Le (" + show(b.e1) + ") (" + show(b.e2) + ")"
        is Bexp.Neg -> "Neg (" + show(b.e) + ")"
        is Bexp.True -> "TRUE"
    }
}