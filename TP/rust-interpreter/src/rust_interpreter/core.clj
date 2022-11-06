(ns rust-interpreter.core
  (:require [rust-interpreter.rust :refer :all]))
(defn -main
  [& args]
  (driver-loop))
