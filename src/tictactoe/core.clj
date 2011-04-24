(ns tictactoe.core)

(defn mark [board cell]
  (-> board
    (update-in [(:turn board)] conj cell)
    (update-in [:-] disj cell)
    (update-in [:turn] #(if (= :x %) :o :x))))

(defn plays [board]
  (map (partial mark board) (:- board)))

(defn game-tree [position]
  {:node position :children (map game-tree (plays position))})

(defn map-tree [f t]
  {:node (f (:node t))
   :children (map (partial map-tree f) (:children t))})

(defn line? [cells]
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
    (line? (:x position)) :x
    (line? (:o position)) :o))

(defn evaluate-static-position [position]
  (case (winner position)
    :x 1
    :o -1
    0))

(defn evaluate-static [gametree]
  (map-tree evaluate-static-position gametree))

(declare minimize)

(defn maximize [{:keys [node children]}]
  (if (seq children)
    (apply max (map minimize children))
    node))

(defn minimize [{:keys [node children]}]
  (if (seq children)
    (apply min (map maximize children))
    node))

(defn prune [n {:keys [node children]}]
  (if (= n 0)
    {:node node :children []}
    {:node node :children (map (partial prune (dec n)) children)}))

(defn evaluate [tree]
  (minimize (evaluate-static (prune 20 tree))))

(defn best-play [position]
  (let [children (:children (game-tree position))]
    (:node (apply max-key evaluate children))))

