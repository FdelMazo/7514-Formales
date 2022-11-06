(defproject rust-interpreter "0.1.0-SNAPSHOT"
  :description "Interprete de Rust -- Trabajo Práctico para [7514] Lenguajes Formales -- FIUBA"
  :dependencies [[org.clojure/clojure "1.10.0"]]
  :main ^:skip-aot rust-interpreter.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
