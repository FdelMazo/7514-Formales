;; Dada una matriz cuadrada m,
;; devolver solamente la matriz triangular superior
(defn upper-matrix [m]
  (for [row (range (count m))]
    (concat (repeat row 0) (take-last (- (count m) row) (nth m row)))))
(println (upper-matrix [[1 2 3]    ; (1 2 3)
                        [4 5 6]    ; (0 5 6)
                        [7 8 9]])) ; (0 0 9)

;; Dada una matriz cuadrada m,
;; devolver solamente la diagonal principal
(defn diagonal [m]
  (for [row (range (count m)) col (range (count m)) :when (= row col)]
    (nth (nth m row) col)))
(println (diagonal [[1 2 3]
                    [4 5 6]
                    [7 8 9]])) ; (1 5 9)
