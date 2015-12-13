package eu.rossng.camle

import java.io.PrintStream

public class Memory {
    val memory: MutableList<MemoryByte> = arrayListOf()

    /**
     * Allocate a string statically in the memory array
     * @property str The string to
     */
    fun allocateString(str: String): Int {
        // Store the address at which we are allocating the string
        val allocatedAt = memory.size

        // Add all the string bytes to the memory
        memory.addAll(str.toByteArray().map { MemoryByte(str, it) })

        // Add a terminating null byte
        memory.add(MemoryByte("nul", 0))

        return allocatedAt
    }

    fun dumpAssembly(out: PrintStream) {
        memory.forEach {
            out.println("DATA " + it.contents.toInt() + " ; " + it.name)
        }
    }

}