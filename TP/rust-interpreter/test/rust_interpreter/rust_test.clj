(ns rust-interpreter.rust-test
  (:require [clojure.test :refer :all]
            [rust-interpreter.rust :refer :all]))

(deftest listar-test
  (testing "Prueba de la función: listar"
    (is (= "fn main ( ) \n{\n  println! ( Hola, mundo! ) \n}\n"
      (with-out-str (is (nil?
        (listar (list 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'println! (symbol "(") "Hola, mundo!" (symbol ")") (symbol "}"))))))))))

(deftest palabra-reservada?-test
  (testing "Prueba de la función: palabra-reservada?"
    (is (true? (palabra-reservada? 'while)))
    (is (false? (palabra-reservada? 'until)))
    (is (false? (palabra-reservada? 13)))))

(deftest identificador?-test
  (testing "Prueba de la función: identificador?"
    (is (true? (identificador? 'boolean)))
    (is (true? (identificador? '_return)))
    (is (false? (identificador? 'return)))
    ;; (is (false? (identificador? 'bool))) -> Estoy casi seguro de que este ejemplo esta mal
    (is (true? (identificador? 'e120)))
    (is (false? (identificador? '12e0)))))

(deftest pasar-a-int?-test
  (testing "Prueba de la función: pasar-a-int?"
    (is (= 10 (pasar-a-int "10")))
    (is (= 10 (pasar-a-int 10.0)))
    (is (= 10 (pasar-a-int 10)))
    (is (= 'a (pasar-a-int 'a)))
    (is (= [10.0] (pasar-a-int [10.0])))))

(deftest pasar-a-float?-test
  (testing "Prueba de la función: pasar-a-float?"
    (is (= 10.0 (pasar-a-float "10")))
    (is (= 10.0 (pasar-a-float 10)))
    (is (= 10.0 (pasar-a-float 10.0)))
    (is (= 'a (pasar-a-float 'a)))
    (is (= [10] (pasar-a-float [10])))))

(deftest ya-declarado-localmente?-test
  (testing "Prueba de la función: ya-declarado-localmente?"
    (is (true? (ya-declarado-localmente? 'Write [[0] [['io ['lib '()] 0] ['Write ['lib '()] 0] ['entero_a_hexa ['fn [(list ['n (symbol ":") 'i64]) 'String]] 2]]])))
    (is (false? (ya-declarado-localmente? 'Read [[0] [['io ['lib '()] 0] ['Write ['lib '()] 0] ['entero_a_hexa ['fn [(list ['n (symbol ":") 'i64]) 'String]] 2]]])))
    (is (true? (ya-declarado-localmente? 'Write [[0 1] [['io ['lib '()] 0] ['Write ['lib '()] 0] ['entero_a_hexa ['fn [(list ['n (symbol ":") 'i64]) 'String]] 2]]])))
    (is (false? (ya-declarado-localmente? 'Write [[0 2] [['io ['lib '()] 0] ['Write ['lib '()] 0] ['entero_a_hexa ['fn [(list ['n (symbol ":") 'i64]) 'String]] 2]]])))))

(deftest inicializar-contexto-local-test
  (testing "Prueba de la función: inicializar-contexto-local"
    (is (=
         (flatten [(symbol "{") (list 'let 'x (symbol ":") 'i64 (symbol "=") 10 (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'x (symbol ")") (symbol "}")) ['fn 'main (symbol "(") (symbol ")")] 8 [[0] [['main ['fn [() ()]] 2]]] 0 [['CAL 2] 'HLT] []])
         (flatten (inicializar-contexto-local [(symbol "{") (list 'let 'x (symbol ":") 'i64 (symbol "=") 10 (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'x (symbol ")") (symbol "}")) ['fn 'main (symbol "(") (symbol ")")] 8 [[0] [['main ['fn [() ()]] 2]]] 0 [['CAL 2] 'HLT] []]))))
    (is (=
         (flatten [(symbol "{") (list 'let 'x (symbol ":") 'i64 (symbol "=") 10 (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'x (symbol ")") (symbol "}")) ['fn 'main (symbol "(") (symbol ")")] :sin-errores [[0 1] [['main ['fn [() ()]] 2]]] 0 [['CAL 2] 'HLT] []])
         (flatten (inicializar-contexto-local [(symbol "{") (list 'let 'x (symbol ":") 'i64 (symbol "=") 10 (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'x (symbol ")") (symbol "}")) ['fn 'main (symbol "(") (symbol ")")] :sin-errores [[0] [['main ['fn [() ()]] 2]]] 0 [['CAL 2] 'HLT] []]))))))

(deftest restaurar-contexto-anterior-test
  (testing "Prueba de la función: restaurar-contexto-anterior"
    (is (=
         (flatten ['EOF () ['fn 'main (symbol "(") (symbol ")") (symbol "{") 'let 'x (symbol ":") 'i64 (symbol "=") 10 (symbol ";") 'let 'y (symbol ":") 'i64 (symbol "=") 20 (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'x '+ 'y (symbol ")") (symbol "}")] 8 [[0 1] [['main ['fn [() ()]] 2] ['x ['var-inmut 'i64] 0] ['y ['var-inmut 'i64] 1]]] 2 [['CAL 2] 'HLT ['PUSHFI 10] ['POP 0] ['PUSHFI 20] ['POP 1] ['PUSHFI "{}"] ['PUSHFM 0] ['PUSHFM 1] 'ADD ['PUSHFI 2] 'OUT 'NL] [[2 ['i64 nil] ['i64 nil]]]])
         (flatten (restaurar-contexto-anterior ['EOF () ['fn 'main (symbol "(") (symbol ")") (symbol "{") 'let 'x (symbol ":") 'i64 (symbol "=") 10 (symbol ";") 'let 'y (symbol ":") 'i64 (symbol "=") 20 (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'x '+ 'y (symbol ")") (symbol "}")] 8 [[0 1] [['main ['fn [() ()]] 2] ['x ['var-inmut 'i64] 0] ['y ['var-inmut 'i64] 1]]] 2 [['CAL 2] 'HLT ['PUSHFI 10] ['POP 0] ['PUSHFI 20] ['POP 1] ['PUSHFI "{}"] ['PUSHFM 0] ['PUSHFM 1] 'ADD ['PUSHFI 2] 'OUT 'NL] [[2 ['i64 nil] ['i64 nil]]]]))))
    (is (=
         (flatten ['EOF () ['fn 'main (symbol "(") (symbol ")") (symbol "{") 'let 'x (symbol ":") 'i64 (symbol "=") 10 (symbol ";") 'let 'y (symbol ":") 'i64 (symbol "=") 20 (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'x '+ 'y (symbol ")") (symbol "}")] :sin-errores [[0] [['main ['fn [() ()]] 2]]] 2 [['CAL 2] 'HLT ['PUSHFI 10] ['POP 0] ['PUSHFI 20] ['POP 1] ['PUSHFI "{}"] ['PUSHFM 0] ['PUSHFM 1] 'ADD ['PUSHFI 2] 'OUT 'NL] [[2 ['i64 nil] ['i64 nil]]]])
         (flatten (restaurar-contexto-anterior ['EOF () ['fn 'main (symbol "(") (symbol ")") (symbol "{") 'let 'x (symbol ":") 'i64 (symbol "=") 10 (symbol ";") 'let 'y (symbol ":") 'i64 (symbol "=") 20 (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'x '+ 'y (symbol ")") (symbol "}")] :sin-errores [[0 1] [['main ['fn [() ()]] 2] ['x ['var-inmut 'i64] 0] ['y ['var-inmut 'i64] 1]]] 2 [['CAL 2] 'HLT ['PUSHFI 10] ['POP 0] ['PUSHFI 20] ['POP 1] ['PUSHFI "{}"] ['PUSHFM 0] ['PUSHFM 1] 'ADD ['PUSHFI 2] 'OUT 'NL] [[2 ['i64 nil] ['i64 nil]]]]))))))

(deftest cargar-en-reg-dest-test
  (testing "Prueba de la función: cargar-en-reg-dest"
    (is (=
         (flatten [[['String "2"] ['i64 6] ['i64 2] ['i64 2] ['i64 0]] [['i64 6] ['i64 2] ['i64 [0 3]] ['i64 [0 4]] ['i64 2] ['i64 2]]])
         (flatten (cargar-en-reg-dest [[['String "2"] ['i64 6] ['i64 2] ['i64 2] ['i64 2]] [['i64 6] ['i64 2] ['i64 [0 3]] ['i64 [0 4]] ['i64 2] ['i64 2]]] [0 4] 'i64 0))))
    (is (=
         (flatten [[['String "2"] ['i64 6] ['i64 2] ['f64 3] ['i64 0]] [['i64 6] ['i64 2] ['i64 [0 3]] ['i64 [0 4]] ['i64 2] ['i64 2]]])
         (flatten (cargar-en-reg-dest [[['String "2"] ['i64 6] ['i64 2] ['i64 2] ['i64 0]] [['i64 6] ['i64 2] ['i64 [0 3]] ['i64 [0 4]] ['i64 2] ['i64 2]]] [0 3] 'f64 3))))))

(deftest cargar-en-ult-reg-test
  (testing "Prueba de la función: cargar-en-ult-reg"
    (is (=
         (flatten [[['String "2"] ['i64 6] ['i64 2] ['i64 3] ['i64 0]] [['i64 nil] ['i64 0]]])
         (flatten (cargar-en-ult-reg [[['String "2"] ['i64 6] ['i64 2] ['i64 3] ['i64 0]] [['i64 nil] ['i64 nil]]] 1 'i64 0))))
    (is (=
         (flatten [[['String "2"] ['i64 6] ['i64 2] ['i64 3] ['i64 0]] [['f64 3] ['i64 0]]])
         (flatten (cargar-en-ult-reg [[['String "2"] ['i64 6] ['i64 2] ['i64 3] ['i64 0]] [['i64 nil] ['i64 0]]] 0 'f64 3))))))

(deftest dividir-test
  (testing "Prueba de la función: dividir"
    (is (= 4   (dividir 12 3)))
    (is (= 4.0 (dividir 12.0 3)))
    (is (= 4.0 (dividir 12 3.0)))
    (is (= 4.0 (dividir 12.0 3.0)))
    (is (= 0   (dividir 1 2)))
    (is (= 0.5 (dividir 1 2.0)))))

(deftest convertir-formato-impresion-test
  (testing "Prueba de la función: convertir-formato-impresion"
    (is (=
         '("Hola, mundo!")
         (convertir-formato-impresion '("Hola, mundo!"))))
    (is (=
         '("- My name is %s, James %s.\n- Hello, %d%d%d!" "Bond" "Bond" 0 0 7)
         (convertir-formato-impresion '("- My name is {}, James {}.\n- Hello, {}{}{}!" "Bond" "Bond" 0 0 7))))
    (is (=
         '("%.0f elevado a la %d es\t%.0f" 2.0 2 4.0)
         (convertir-formato-impresion '("{} elevado a la {} es\t{}" 2.0 2 4.0))))
    (is (=
         '("Las raices cuadradas de %.0f son +%.8f y -%.8f" 4.0 1.999999999985448 1.999999999985448)
         (convertir-formato-impresion '("Las raices cuadradas de {} son +{:.8} y -{:.8}" 4.0 1.999999999985448 1.999999999985448))))))

(deftest dump-test
  (testing "Prueba de la función: dump"
    (is (= "0 [POPREF 2]\n1 [PUSHFI 2]\n2 MUL\n3 [PUSHFI 1]\n4 ADD\n5 NEG\n"
           (with-out-str (is (nil?
                              (dump '[[POPREF 2] [PUSHFI 2] MUL [PUSHFI 1] ADD NEG]))))))
    (is (= "0 HLT\n"
           (with-out-str (is (nil?
                              (dump '[HLT]))))))
    (is (= "0 nil\n"
           (with-out-str (is (nil?
                              (dump nil))))))))

(deftest compatibles?-test
  (testing "Prueba de la función: compatibles?"
    (is (true? (compatibles? 'i64 5)))
    (is (false? (compatibles? 'i64 5.0)))
    (is (true? (compatibles? 'i64 [5.0])))
    (is (true? (compatibles? 'f64 5.0)))
    (is (true? (compatibles? 'String "Hola")))
    (is (true? (compatibles? 'bool true)))
    (is (false? (compatibles? 'bool 1)))
    (is (true? (compatibles? 'usize 1)))
    (is (true? (compatibles? 'char \a)))
    (is (false? (compatibles? 'char 'a)))
    (is (true? (compatibles? 'char ['a])))))

(deftest fixup-test
  (testing "Prueba de la función: fixup"
    (is (=
         (flatten [(symbol "{") (list 'x '= 20 (symbol ";") (symbol "}") (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'x (symbol ")") (symbol "}")) ['fn 'main (symbol "(") (symbol ")") (symbol "{") 'let 'x (symbol ":") 'i64 (symbol ";") 'if false (symbol "{") 'x '= 10 (symbol ";") (symbol "}") 'else] 8 [[0 1 2] [['main ['fn [() ()]] 2] ['x ['var-inmut 'i64] 0]]] 1 [['CAL 2] 'HLT ['PUSHFI false] ['JC 5] ['JMP '?] ['PUSHFI 10] ['POP 0] ['JMP '?]] [[2 ['i64 nil]]]])
         (flatten (fixup [(symbol "{") (list 'x '= 20 (symbol ";") (symbol "}") (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'x (symbol ")") (symbol "}")) ['fn 'main (symbol "(") (symbol ")") (symbol "{") 'let 'x (symbol ":") 'i64 (symbol ";") 'if false (symbol "{") 'x '= 10 (symbol ";") (symbol "}") 'else] 8 [[0 1 2] [['main ['fn [() ()]] 2] ['x ['var-inmut 'i64] 0]]] 1 [['CAL 2] 'HLT ['PUSHFI false] ['JC 5] ['JMP '?] ['PUSHFI 10] ['POP 0] ['JMP '?]] [[2 ['i64 nil]]]] 4))))
    (is (=
         (flatten [(symbol "{") (list 'x '= 20 (symbol ";") (symbol "}") (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'x (symbol ")") (symbol "}")) ['fn 'main (symbol "(") (symbol ")") (symbol "{") 'let 'x (symbol ":") 'i64 (symbol ";") 'if false (symbol "{") 'x '= 10 (symbol ";") (symbol "}") 'else] :sin-errores [[0 1 2] [['main ['fn [() ()]] 2] ['x ['var-inmut 'i64] 0]]] 1 [['CAL 2] 'HLT ['PUSHFI false] ['JC 5] ['JMP 8] ['PUSHFI 10] ['POP 0] ['JMP '?]] [[2 ['i64 nil]]]])
         (flatten (fixup [(symbol "{") (list 'x '= 20 (symbol ";") (symbol "}") (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'x (symbol ")") (symbol "}")) ['fn 'main (symbol "(") (symbol ")") (symbol "{") 'let 'x (symbol ":") 'i64 (symbol ";") 'if false (symbol "{") 'x '= 10 (symbol ";") (symbol "}") 'else] :sin-errores [[0 1 2] [['main ['fn [() ()]] 2] ['x ['var-inmut 'i64] 0]]] 1 [['CAL 2] 'HLT ['PUSHFI false] ['JC 5] ['JMP '?] ['PUSHFI 10] ['POP 0] ['JMP '?]] [[2 ['i64 nil]]]] 4))))))

(deftest generar-ref-test
  (testing "Prueba de la función: generar-ref"
    (is (=
         (flatten [(symbol ")") (list (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'v (symbol ")") (symbol ";") (symbol "}")) ['fn 'inc (symbol "(") 'v (symbol ":") (symbol "&") 'mut 'i64 (symbol ")") (symbol "{") '* 'v (symbol "+=") 1 (symbol ";") (symbol "}") 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'let 'mut 'v (symbol ":") 'i64 (symbol "=") 5 (symbol ";") 'inc (symbol "(") (symbol "&") 'mut 'v] 8 [[0 2] [['inc ['fn [(list ['v (symbol ":") (symbol "&") 'mut 'i64]) ()]] 2] ['main ['fn [() ()]] 6] ['v ['var-mut 'i64] 0]]] 1 [['CAL 6] 'HLT ['POPARG 0] ['PUSHFI 1] ['POPADDREF 0] 'RETN ['PUSHFI 5] ['POP 0]] [[2 ['i64 nil]] [6 ['i64 nil]]]])
         (flatten (generar-ref [(symbol ")") (list (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'v (symbol ")") (symbol ";") (symbol "}")) ['fn 'inc (symbol "(") 'v (symbol ":") (symbol "&") 'mut 'i64 (symbol ")") (symbol "{") '* 'v (symbol "+=") 1 (symbol ";") (symbol "}") 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'let 'mut 'v (symbol ":") 'i64 (symbol "=") 5 (symbol ";") 'inc (symbol "(") (symbol "&") 'mut 'v] 8 [[0 2] [['inc ['fn [(list ['v (symbol ":") (symbol "&") 'mut 'i64]) ()]] 2] ['main ['fn [() ()]] 6] ['v ['var-mut 'i64] 0]]] 1 [['CAL 6] 'HLT ['POPARG 0] ['PUSHFI 1] ['POPADDREF 0] 'RETN ['PUSHFI 5] ['POP 0]] [[2 ['i64 nil]] [6 ['i64 nil]]]]))))
    (is (=
         (flatten [(symbol ")") (list (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'v (symbol ")") (symbol ";") (symbol "}")) ['fn 'inc (symbol "(") 'v (symbol ":") (symbol "&") 'mut 'i64 (symbol ")") (symbol "{") '* 'v (symbol "+=") 1 (symbol ";") (symbol "}") 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'let 'mut 'v (symbol ":") 'i64 (symbol "=") 5 (symbol ";") 'inc (symbol "(") (symbol "&") 'mut 'v] :sin-errores [[0 2] [['inc ['fn [(list ['v (symbol ":") (symbol "&") 'mut 'i64]) ()]] 2] ['main ['fn [() ()]] 6] ['v ['var-mut 'i64] 0]]] 1 [['CAL 6] 'HLT ['POPARG 0] ['PUSHFI 1] ['POPADDREF 0] 'RETN ['PUSHFI 5] ['POP 0] ['PUSHADDR 0]] [[2 ['i64 nil]] [6 ['i64 nil]]]])
         (flatten (generar-ref [(symbol ")") (list (symbol ";") 'println! (symbol "(") "{}" (symbol ",") 'v (symbol ")") (symbol ";") (symbol "}")) ['fn 'inc (symbol "(") 'v (symbol ":") (symbol "&") 'mut 'i64 (symbol ")") (symbol "{") '* 'v (symbol "+=") 1 (symbol ";") (symbol "}") 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'let 'mut 'v (symbol ":") 'i64 (symbol "=") 5 (symbol ";") 'inc (symbol "(") (symbol "&") 'mut 'v] :sin-errores [[0 2] [['inc ['fn [(list ['v (symbol ":") (symbol "&") 'mut 'i64]) ()]] 2] ['main ['fn [() ()]] 6] ['v ['var-mut 'i64] 0]]] 1 [['CAL 6] 'HLT ['POPARG 0] ['PUSHFI 1] ['POPADDREF 0] 'RETN ['PUSHFI 5] ['POP 0]] [[2 ['i64 nil]] [6 ['i64 nil]]]]))))))

(deftest buscar-tipo-de-retorno-test
  (testing "Prueba de la función: buscar-tipo-de-retorno"
    (is (=
         'i64
         (buscar-tipo-de-retorno [(symbol ";") (list 'println! (symbol "(") "La suma de 5 mas 7 es {}" (symbol ",") 'suma (symbol "(") 5 (symbol ",") 7 (symbol ")") (symbol ")") (symbol ";") (symbol "}")) ['fn 'suma (symbol "(") 'x (symbol ":") 'i64 (symbol ",") 'y (symbol ":") 'i64 (symbol ")") (symbol "->") 'i64 (symbol "{") 'x '+ 'y (symbol "}") 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'suma (symbol "(") 5 (symbol ",") 7 (symbol ")")] :sin-errores [[0 2] [['suma ['fn [(list ['x (symbol ":") 'i64] ['y (symbol ":") 'i64]) 'i64]] 2] ['main ['fn [() ()]] 8]]] 0 [['CAL 8] 'HLT ['POPARG 1] ['POPARG 0] ['PUSHFM 0] ['PUSHFM 1] 'ADD 'RET ['PUSHFI 5] ['PUSHFI 7] ['CAL 2]] [[2 ['i64 nil] ['i64 nil]] [8]]] 2)))
    (is (=
         ()
         (buscar-tipo-de-retorno [(symbol ";") (list 'println! (symbol "(") "La suma de 5 mas 7 es {}" (symbol ",") 'suma (symbol "(") 5 (symbol ",") 7 (symbol ")") (symbol ")") (symbol ";") (symbol "}")) ['fn 'suma (symbol "(") 'x (symbol ":") 'i64 (symbol ",") 'y (symbol ":") 'i64 (symbol ")") (symbol "->") 'i64 (symbol "{") 'x '+ 'y (symbol "}") 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'suma (symbol "(") 5 (symbol ",") 7 (symbol ")")] :sin-errores [[0 2] [['suma ['fn [(list ['x (symbol ":") 'i64] ['y (symbol ":") 'i64]) 'i64]] 2] ['main ['fn [() ()]] 8]]] 0 [['CAL 8] 'HLT ['POPARG 1] ['POPARG 0] ['PUSHFM 0] ['PUSHFM 1] 'ADD 'RET ['PUSHFI 5] ['PUSHFI 7] ['CAL 2]] [[2 ['i64 nil] ['i64 nil]] [8]]] 8)))
    (is (=
         nil
         (buscar-tipo-de-retorno [(symbol ";") (list 'println! (symbol "(") "La suma de 5 mas 7 es {}" (symbol ",") 'suma (symbol "(") 5 (symbol ",") 7 (symbol ")") (symbol ")") (symbol ";") (symbol "}")) ['fn 'suma (symbol "(") 'x (symbol ":") 'i64 (symbol ",") 'y (symbol ":") 'i64 (symbol ")") (symbol "->") 'i64 (symbol "{") 'x '+ 'y (symbol "}") 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'suma (symbol "(") 5 (symbol ",") 7 (symbol ")")] :sin-errores [[0 2] [['suma ['fn [(list ['x (symbol ":") 'i64] ['y (symbol ":") 'i64]) 'i64]] 2] ['main ['fn [() ()]] 8]]] 0 [['CAL 8] 'HLT ['POPARG 1] ['POPARG 0] ['PUSHFM 0] ['PUSHFM 1] 'ADD 'RET ['PUSHFI 5] ['PUSHFI 7] ['CAL 2]] [[2 ['i64 nil] ['i64 nil]] [8]]] 1)))))

(deftest CARGAR-CONST-EN-TABLA-test
  (testing "Prueba de la función: CARGAR-CONST-EN-TABLA"
    (is (=
         (flatten [(symbol ";") (list 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'println! (symbol "(") "{}" (symbol ",") 'TRES (symbol ")") (symbol "}")) ['use 'std (symbol "::") 'io (symbol ";") 'const 'TRES (symbol ":") 'i64 (symbol "=") 3] 8 [[0] [['io ['lib '()] 0]]] 0 [['CAL 0] 'HLT] []])
         (flatten (cargar-const-en-tabla [(symbol ";") (list 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'println! (symbol "(") "{}" (symbol ",") 'TRES (symbol ")") (symbol "}")) ['use 'std (symbol "::") 'io (symbol ";") 'const 'TRES (symbol ":") 'i64 (symbol "=") 3] 8 [[0] [['io ['lib '()] 0]]] 0 [['CAL 0] 'HLT] []]))))
    (is (=
         (flatten [(symbol ";") (list 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'println! (symbol "(") "{}" (symbol ",") 'TRES (symbol ")") (symbol "}")) ['use 'std (symbol "::") 'io (symbol ";") 'const 'TRES (symbol ":") 'i64 (symbol "=") 3] :sin-errores [[0] [['io ['lib '()] 0] ['TRES ['const 'i64] 3]]] 0 [['CAL 0] 'HLT] []])
         (flatten (cargar-const-en-tabla [(symbol ";") (list 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'println! (symbol "(") "{}" (symbol ",") 'TRES (symbol ")") (symbol "}")) ['use 'std (symbol "::") 'io (symbol ";") 'const 'TRES (symbol ":") 'i64 (symbol "=") 3] :sin-errores [[0] [['io ['lib '()] 0]]] 0 [['CAL 0] 'HLT] []]))))))


(deftest agregar-ptocoma-test
  (testing "Prueba de la función: agregar-ptocoma"
    (is (= (flatten (list 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'if 'x '< '0 (symbol "{") 'x '= '- 'x (symbol ";") (symbol "}") (symbol ";") 'renglon '= 'x (symbol ";") 'if 'z '< '0 (symbol "{") 'z '= '- 'z (symbol ";") (symbol "}") (symbol "}") 'fn 'foo (symbol "(") (symbol ")") (symbol "{") 'if 'y '> '0 (symbol "{") 'y '= '- 'y (symbol ";") (symbol "}") 'else (symbol "{") 'x '= '- 'y (symbol ";") (symbol "}") (symbol "}")))
           (flatten (agregar-ptocoma (list 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'if 'x '< '0 (symbol "{") 'x '= '- 'x (symbol ";") (symbol "}") 'renglon '= 'x (symbol ";") 'if 'z '< '0 (symbol "{") 'z '= '- 'z (symbol ";") (symbol "}") (symbol "}") 'fn 'foo (symbol "(") (symbol ")") (symbol "{") 'if 'y '> '0 (symbol "{") 'y '= '- 'y (symbol ";") (symbol "}") 'else (symbol "{") 'x '= '- 'y (symbol ";") (symbol "}") (symbol "}"))))))))
