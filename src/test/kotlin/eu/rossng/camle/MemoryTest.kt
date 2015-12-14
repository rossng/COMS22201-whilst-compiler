package eu.rossng.camle

import org.junit.Assert
import org.junit.Test
import java.io.ByteArrayOutputStream
import java.io.PrintStream

class MemoryTest {
    @Test
    fun testMemory() {
        val m = Memory()
        m.allocateString("hi")

        val o = ByteArrayOutputStream()
        val p = PrintStream(o)

        m.dumpAssembly(p)

        p.flush()
        o.flush()

        Assert.assertEquals(
                "DATA 104 ; hi" + System.lineSeparator() +
                        "DATA 105 ; hi" + System.lineSeparator() +
                        "DATA 0 ; nul" + System.lineSeparator(),
                o.toString()
        );
    }
}