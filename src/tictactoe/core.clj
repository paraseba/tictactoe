(ns tictactoe.core)

(defn make-board [x-cells o-cells]
  {:x (set x-cells) :o (set o-cells)})

(def x-cells :x)
(def o-cells :o)

(def all-cells (apply sorted-set (range 9)))

(defn empty-cells [board]
  (clojure.set/difference all-cells (x-cells board) (o-cells board)))

(defn mark [board cell]
  (assert (contains? (empty-cells board) cell))
  (let [turn (fn [board]
               (if (> (count (x-cells board))
                      (count (o-cells board)))
                 :o
                 :x))

        t (turn board)]
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

(defn won? [cells]
  (some #(every? cells %) win-cells))

(defn winner [board]
  (cond
    (won? (:x board)) :x
    (won? (:o board)) :o))

(defn plays [board]
  (if (winner board)
    []
    (map (partial mark board) (empty-cells board))))

(defn prune [n {:keys [node children]}]
  (if (= n 0)
    {:node node :children []}
    {:node node :children (map (partial prune (dec n)) children)}))

(defn game-tree [board generator]
  {:node board
   :children (map #(game-tree % generator) (generator board))})

(defn evaluate-static-position [position]
  (case (winner position)
    :x 1
    :o -1
    0))

(declare minimize)

(defn maximize [tree]
  (if (seq (:children tree))
    (apply max (map minimize (:children tree)))
    (evaluate-static-position (:node tree))))

(defn minimize [tree]
  (if (seq (:children tree))
    (apply min (map maximize (:children tree)))
    (evaluate-static-position (:node tree))))

(defn evaluate [tree]
  (minimize tree))

(defn best-play [position]
  (let [tree (game-tree position plays)
        children (:children tree)]
    (:node (apply max-key evaluate children))))

