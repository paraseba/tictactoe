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

(def all-cells
  (apply sorted-set
         (for [i (range 3) j (range 3)]
           [i j])))

(defn empty-cells [board]
  (clojure.set/difference all-cells (:x board) (:o board)))

(defn mark [board cell]
  (assert (contains? (empty-cells board) cell))
  (assoc board
         (turn board)
         (conj ((turn board) board) cell)))

(defn won? [cells]
  (or
    (some #(= (count %) 3) (vals (group-by first cells)))
    (some #(= (count %) 3) (vals (group-by second cells)))
    (and (contains? cells [0 0])
         (contains? cells [1 1])
         (contains? cells [2 2]))
    (and (contains? cells [0 2])
         (contains? cells [1 1])
         (contains? cells [2 0]))))

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

