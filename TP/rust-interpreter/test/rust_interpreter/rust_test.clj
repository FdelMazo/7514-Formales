(ns rust-interpreter.rust-test
  (:require [clojure.test :refer :all]
            [rust-interpreter.rust :refer :all]))

(deftest listar-test
  (testing "Prueba de la funcion: listar"
    ;; Chequear printeado
    (is (nil? (listar (list 'fn 'main (symbol "(") (symbol ")") (symbol "{") 'println! (symbol "(") "Hola, mundo!" (symbol ")") (symbol "}")))))))
