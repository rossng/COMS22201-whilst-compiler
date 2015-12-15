package eu.rossng.camle

import java.io.PrintStream
import java.net.URLEncoder
import java.util.*

public class Memory {
    val memory: MutableList<MemoryByte> = arrayListOf()
    val variables: HashMap<String, Int> = hashMapOf()

    /**
     * Allocate a string statically in the memory array
     * @property str The string to be allocated
     * @return The address of the string
     */
    fun allocateString(str: String): Int {
        padMemory()

        // Store the address at which we are allocating the string
        val allocatedAt = memory.size

        // Add all the string bytes to the memory
        memory.addAll(str.toByteArray().map { MemoryByte(str, it) })

        // Add a terminating null byte
        memory.add(MemoryByte("nul", 0))

        return allocatedAt
    }


    fun allocateOrGetVariable(name: String): Int {
        return variables.getOrElse(name, { allocateVariable(name) })
    }

    fun allocateVariable(name: String): Int {
        padMemory()
        val addr = memory.size;
        variables.put(name, addr)
        memory.add(MemoryByte(name, 0))
        return addr
    }

    /**
     * Insert null bytes into memory until at a word boundary
     */
    fun padMemory() {
        while (memory.size % 4 != 0) {
            memory.add(MemoryByte("padding", 0))
        }
    }

    fun dumpAssembly(out: PrintStream) {
        memory.forEach {
            out.println("DATA " + it.contents.toInt() + " ; " + URLEncoder.encode(it.name, "UTF-8"))
        }
    }

}