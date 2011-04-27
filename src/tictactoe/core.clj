(ns tictactoe.core)

(defn make-board [x-cells o-cells]
  (let [x-cells (set x-cells)
        o-cells (set o-cells)]
    {:x (set x-cells) :o (set o-cells)}))

(defn turn [board]
  (let [xs (:x board)
        os (:o board)]
    (if (> (count xs) (count os))
      :o
      :x)))

(def all-cells (apply sorted-set (range 9)))

(defn empty-cells [board]
  (clojure.set/difference all-cells (:x board) (:o board)))

(defn mark [board cell]
  (assert (contains? (empty-cells board) cell))
  (let [t (turn board)]
    (assoc board
           t
           (conj (t board) cell))))

(def win-cells
  (let [row1 #{0 1 2}
        row2 #{3 4 5}
        row3 #{6 7 8}
        col1 #{0 3 6}
        col2 #{1 4 7}
        col3 #{2 5 8}
        dia1 #{0 4 8}
        dia2 #{2 4 6}]
    [row1 row2 row3 col1 col2 col3 dia1 dia2]))

; todo memoize? parece que baja de 14 a 11
(defn won? [cells]
  (some #(every? cells %) win-cells))

(defn winner [position]
  (cond
    (won? (:x position)) :x
    (won? (:o position)) :o))

(defn plays [board]
  (if (winner board)
    []
    (map (partial mark board) (empty-cells board))))

(defn prune [n {:keys [node children]}]
  (if (= n 0)
    {:node node :children []}
    {:node node :children (map (partial prune (dec n)) children)}))

(defn game-tree [position]
  {:node position :children (map game-tree (plays position))})

(defn evaluate-static-position [position]
  (case (winner position)
    :x 1
    :o -1
    0))

(declare minimize)

(defn maximize [{:keys [node children]}]
  (if (seq children)
    (apply max (map minimize children))
    (evaluate-static-position node)))

(defn minimize [{:keys [node children]}]
  (if (seq children)
    (apply min (map maximize children))
    (evaluate-static-position node)))

(defn evaluate [tree]
  (minimize tree))

(defn best-play [position]
  (let [tree (game-tree position)
        children (:children tree)]
    (:node (apply max-key evaluate children))))

