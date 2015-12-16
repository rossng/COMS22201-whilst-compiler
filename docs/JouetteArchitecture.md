# The Jouette architecture

For reference, this was the provided definition of the Jouette architecture (implemented by `assmule`).

## Assembly language grammar

There is no distinction between upper and lower case letters.

An assembly program is a sequence of lines, defined as:

```
<line> ::= ( <label> ':' )? <instruction>? ( ';' <comment> )?
     
<instruction> ::= <opcode> ( <operand> ( ','? <operand> )? ( ','? <operand> )? )?

<operand> ::= <constant> | <register> | <label>
```

Operands and labels may not be more than 10 characters long.

### Opcodes

| Instruction   | Effect           | Comments                                                                                                                      |
|---------------|------------------|-------------------------------------------------------------------------------------------------------------------------------|
| ADD Ri,Rj,Rk  | Ri <- Rj + Rk    | Integer addition                                                                                                              |
| SUB Ri,Rj,Rk  | Ri <- Rj - Rk    | Integer subtraction                                                                                                           |
| MUL Ri,Rj,Rk  | Ri <- Rj * Rk    | Integer multiplication                                                                                                        |
| DIV Ri,Rj,Rk  | Ri <- Rj / Rk    | Integer division                                                                                                              |
| XOR Ri,Rj,Rk  | Ri <- Rj ^ Rk    | Bitwise XOR                                                                                                                   |
| ADDR Ri,Rj,Rk | Ri <- Rj + Rk    | Real addition                                                                                                                 |
| SUBR Ri,Rj,Rk | Ri <- Rj - Rk    | Real subtraction                                                                                                              |
| MULR Ri,Rj,Rk | Ri <- Rj * Rk    | Real multiplication                                                                                                           |
| DIVR Ri,Rj,Rk | Ri <- Rj / Rk    | Real division                                                                                                                 |
| ADDI Ri,Rj,I  | Ri <- Rj + I     | Integer addition: register and constant                                                                                       |
| SUBI Ri,Rj,I  | Ri <- Rj - I     | Integer subtraction: register and constant                                                                                    |
| MULI Ri,Rj,I  | Ri <- Rj * I     | Integer multiplication: register and constant                                                                                 |
| DIVI Ri,Rj,I  | Ri <- Rj / I     | Integer division: register and constant                                                                                       |
| XORI Ri,Rj,I  | Ri <- Rj ^ I     | Bitwise XOR: register and constant                                                                                            |
| MOVIR Ri,F    | Ri <- F          | Real constant moved to register                                                                                               |
| ITOR Ri,Rj    | Ri <- Rj         | Integer to real conversion (Rj is integer; Ri is real)                                                                        |
| RTOI Ri,Rj    | Ri <- Rj         | Real to integer conversion (Rj is real; Ri is integer)                                                                        |
| RD Ri         | Read Ri          | Reads integer from stdin                                                                                                      |
| RDR Ri        | Read Ri          | Reads real from stdin                                                                                                         |
| WR Ri         | Write Ri         | Writes integer to stdout                                                                                                      |
| WRR Ri        | Write Ri         | Writes real to stdout                                                                                                         |
| WRS I         | Write M[I]...    | Writes string (from address I to next 0 byte) to stdout                                                                       |
| LOAD Ri,Rj,I  | Ri <- M[Rj + I]  | Loads memory contents to register                                                                                             |
| STORE Ri,Rj,I | M[Rj + I] <- Ri  | Stores register contents in memory                                                                                            |
| JMP L         | goto L           | Jumps to label L                                                                                                              |
| JUMP Ri       | goto Ri          | Jumps to the instruction whose address is stored in the register                                                              |
| IADDR Ri,L    | Ri <- L          | Store address L in the register                                                                                               |
| BGEZ Ri,L     | if Ri ≥ 0 goto L | If register's contents (integer) non-negative jump to L                                                                       |
| BGEZR Ri,L    | if Ri ≥ 0 goto L | If register's contents (real) non-negative jump to L                                                                          |
| BLTZ Ri,L     | if Ri < 0 goto L | If register's contents (integer) negative jump to L                                                                           |
| BLTZR Ri,L    | if Ri < 0 goto L | If register's contents (real) negative jump to L                                                                              |
| BEQZ Ri,L     | if Ri = 0 goto L | If register's contents (integer) zero jump to L                                                                               |
| BEQZR Ri,L    | if Ri = 0 goto L | If register's contents (real) zero jump to L                                                                                  |
| BNEZ Ri,L     | if Ri ≠ 0 goto L | If register's contents (integer) non-zero jump to L                                                                           |
| BNEZR Ri,L    | if Ri ≠ 0 goto L | If register's contents (real) non-zero jump to L                                                                              |
| NOP           |                  | No operation                                                                                                                  |
| HALT          |                  | Stop execution                                                                                                                |
| DATA I        |                  | A pseudo-instruction. Used by the assembler to allocate one byte in data memory initialized to the value I (in range 0..255). |

### Notes

* The machine has registers `R0, R1 .. R999999999`
* `R0` must be manually initialised to 0 (by `XOR R0,R0,R0`)
* All integers and reals are 32-bit
* The word size is 4 bytes
* The addresses supplied to `LOAD` and `STORE` are byte addresses, but the address supplied must be at a word boundary (i.e. addr%4 = 0)
* `WRS` reads from the supplied address to the first 0 byte. That is, strings are null-terminated.
* Memory is initialised by the pseudo-instruction `DATA`. 
* `DATA` instructions appear after the `HALT` instruction. The first `DATA` instruction initialises byte 0, the second byte 1, the third byte 2, etc.