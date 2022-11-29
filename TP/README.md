# rust-interpreter

```
λ cd rust-interpreter
λ lein test
Ran 19 tests containing 69 assertions.
0 failures, 0 errors.
λ lein run

Interprete de RUST en Clojure
Trabajo Practico de 75.14/95.48 - Lenguajes Formales - 2022

Inspirado en: rustc 1.64.0 (2022-09-22)

Lista de comandos posibles:
AYUDA: volver a este menu
SALIR: volver al REPL de Clojure
ESCAN <archivo>: mostrar los tokens de un programa escrito en Rust
VIRTU <archivo>: mostrar la RI de un programa escrito en Rust
INTER <archivo>: interpretar la RI de un programa escrito en Rust

Rust> VIRTU ../rust-examples/main01.rs
0 [CAL 2]
1 HLT
2 [PUSHFI - Hola, mundo!]
3 [PUSHFI 1]
4 OUT
5 NL
6 [PUSHFI - My name is {}, James {}.
- Hello, {}{}{}!]
7 [PUSHFI Bond]
8 [PUSHFI Bond]
9 [PUSHFI 2]
10 NEG
11 [PUSHFI 2]
12 ADD
13 [PUSHFI 0]
14 [PUSHFI 3]
15 [PUSHFI 2]
16 [PUSHFI 2]
17 MUL
18 ADD
19 [PUSHFI 6]
20 OUT
21 [PUSHFI 0]
22 OUT
23 NL
24 [PUSHFI - Hasta la vista, Baby!		I'll be back...]
25 [PUSHFI 1]
26 OUT
27 NL
28 [PUSHFI {}]
29 [PUSHFI true]
30 [JC 32]
31 [JMP 34]
32 [PUSHFI - Lo dudo!			Bye!]
33 [JMP 35]
34 [PUSHFI - Obviamente!]
35 [PUSHFI 2]
36 OUT
37 NL
38 RETN
Rust> INTER ../rust-examples/main01.rs
- Hola, mundo!
- My name is Bond, James Bond.
- Hello, 007!
- Hasta la vista, Baby!		I'll be back...
- Lo dudo!			Bye!
Rust> SALIR
```
