;; sudo apt install clojure
;; clojure guia.clj
(println "Hello world!")

(println "---")
(defn tercer-angulo [a b]
  (- 180 (+ a b)))
(println (tercer-angulo 30 60)) ;; 90

(println "---")
(defn segundos [d h m s]
  (+ (* d (* 24 (* 60 60))) (* h (* 60 60)) (* m 60) s))
(println (segundos 1 0 0 0)) ;; 86400

(println "---")
(defn sig-mul-10 [n]
  (def m 0)
  (if (zero? n)
    (def m 1))
  (while (>= n m)
    (def m (+ m 10)))
  m)
(println (sig-mul-10 0)) ;; 1
(println (sig-mul-10 11)) ;; 20

(println "---")
(require '[clojure.string :as str])
(defn red [color]
  (nth (str/split color #",") 0))
(println (red "255,0,0")) ;; 255

(println "---")
(defn capicua? [num]
  (= (str num) (str/reverse (str num))))
(println (capicua? 12321)) ;; true
(println (capicua? 321)) ;; false

(println "---")
; iterate devuelve X, f(x), f(f(x)), ...
(defn infinite-seq [] (iterate inc 1))
(defn odd-seq [] (filter odd? (infinite-seq)))
(defn multiplier [] (cycle [1 -1]))
(defn alternate-sign [] (map * (multiplier) (odd-seq)))
(defn divide-by-one [] (map #(/ 1 %) (alternate-sign)))
(println (take 4 (infinite-seq))) ; (1 2 3 4)
(println (take 4 (odd-seq))) ; (1 3 5 7)
(println (take 4 (alternate-sign))) ; (1 -3 5 -7)
(println (take 4 (divide-by-one))) ; (1/1 -1/3 1/5 -1/7)
(defn aprox-pi [i] (* 4 (reduce + (take i (divide-by-one)))))
(println (aprox-pi 5)) ; 1052/315 === 3.33

(println "---")
(defn invertir [n] (bigint (str/reverse (str n))))
(println (invertir 123)) ;; 321

(println "---")
(defn nth-fibo [n]
  (cond
    (= n 0) 0
    (= n 1) 1
    :else (+ (nth-fibo (- n 1)) (nth-fibo (- n 2)))))
(println (map nth-fibo (range 7))) ; (0 1 1 2 3 5 8)

(println "---")
(defn cant-dig [n]
  (count (str n)))
(println (cant-dig 123)) ; 3

(println "---")
; log a b = ln b / ln a
(defn log-base [a b] (/ (Math/log b) (Math/log a)))
; = chequea si dos vars son lo mismo
; == chequea si dos __numeros__ son iguales (no le da bolilla al tipo)
(defn pot? [a b] (== (log-base a b) (int (log-base a b))))
(println (pot? 2 8)) ; true
(println (pot? 2 7)) ; false

(println "---")
(defn digs [n] (seq (str n)))
(println (digs 123)) ; (1 2 3)

(println "---")
(defn repartir
  ([] "Uno para vos, uno para mi")
  ([& rest] (for [x rest] (str "Uno para " x ", uno para mi"))))
(println (repartir)) ; "Uno para vos, uno para mi"
(println (repartir 1 2 3)) ; ("Uno para 1, uno para mi" "Uno para 2, uno para mi" "Uno para 3, uno para mi")

(println "---")
(defn indices-pares [lst] (for [x (range (count lst)) :when (even? x)] (nth lst x)))
(defn solo-pares [lst1 lst2] (interleave (indices-pares lst1) (indices-pares lst2)))
(println (solo-pares [0 1 2] [4 5 6])) ; 0 4 2 6

(println "---")
(defn char-adn2arn [c]
  (cond
    (= c \G) \C
    (= c \C) \G
    (= c \T) \A
    (= c \A) \U
    :else c))
(defn adn2arn [s] (apply str (map char-adn2arn (seq s))))
(println (adn2arn "ACGT")) ; "UGCA"

(println "---")
(defn remove-all [lst n]
  (mapcat (fn [el]
    (cond
      (sequential? el) [(remove-all el n)]
      (= el n) []
      :else [el])) lst))
(println (remove-all [[1 2 3] [1 2] 3] 3)) ; ((1 2) (1 2))

(println "---")
(defn last-last [lst]
  (cond
    (sequential? (last lst)) (last-last (last lst))
    :else (last lst)))
(println (last-last [1 2 3 4 5])) ; 5
(println (last-last [1 2 [3 4 5]])) ; 5

(println "---")
(defn center [lst]
  (nth lst (/ (count lst) 2)))
(println (center [1 2 3 4 5])) ; 3
(println (center [1 2 3 4 5 6])) ; 4

(println "---")
(defn remove-duplicates [lst] (distinct lst))
(println (remove-duplicates [1 2 3 4 5 1 2 3 4 5])) ; (1 2 3 4 5)

(println "---")
(defn remove-duplicates [lst] (distinct lst))
(println (remove-duplicates [1 2 3 4 5 1 2 3 4 5])) ; (1 2 3 4 5)

(println "---")
(defn longest-last [lstoflsts] (sort-by count lstoflsts))
(println (longest-last [[1 2 3] [1 2] [1 2 3 4 5]])) ; ([1 2] [1 2 3] [1 2 3 4 5])

(println "---")
(defn isbn-10? [s]
  (let [isbn
    (replace
      {\X 10}
      (seq (str/replace (str/replace s "-" "") " " "")))]
    (= 0
      (mod
        (reduce +
          (map *
            (range 10 0 -1)
            (map #(Integer/parseInt (str %)) isbn))) 11))))

(println (isbn-10? "3-598 21507-X")) ; true
(println (isbn-10? "4-598 21507-X")) ; false
