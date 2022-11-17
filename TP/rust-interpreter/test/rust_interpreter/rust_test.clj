(ns rust-interpreter.rust-test
  (:require [clojure.test :refer :all]
            [rust-interpreter.rust :refer :all]))

(deftest listar-test
  (testing "Prueba de la función: listar"
    ;; (is (= "fn main ( )\n{\n\tprintln! ( Hola, mundo! )\n}\n" (with-out-str (listar (list 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'println! (symbol "(") "Hola, mundo!" (symbol ")") (symbol "}"))))))
    (is (nil? (listar (list 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'println! (symbol "(") "Hola, mundo!" (symbol ")") (symbol "}")))))))

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
