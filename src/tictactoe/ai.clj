(ns tictactoe.ai
  "Implement AI to play a game. Generate a tree of candidate moves and decide
  which one is the best for the computer to play")

; ---------------
; Tree Generation
; ---------------

(defn game-tree
  "Generate a tree of posible next boards starting with a given board position.
  Each node in the tree will have a board and a set of children representing
  possible positions for the next move. The tree is genarated lazily.
  make-move is a function that given a board returns all posible next boards"
  [board make-move]
  {:node board
   :children (map #(game-tree % make-move)
                  (make-move board))})

; ---------------
; Tree Evaluation
; ---------------

(declare minimize)

(defn maximize [evaluator tree]
  (if (seq (:children tree))
    (apply max
           (map #(minimize evaluator %)
                (:children tree)))
    (evaluator (:node tree))))

(defn minimize [evaluator tree]
  (if (seq (:children tree))
    (apply min
           (map #(maximize evaluator %)
                (:children tree)))
    (evaluator (:node tree))))

(defn evaluator
  "Dynamic evaluation of a game tree. Returns a number representing how good
  the root position is. Uses minimax algorithm"
  [static-evaluator]
  (fn [tree]
    (minimize static-evaluator tree)))

(defn best-move
  "Get the best computer move for the given game tree.
  static-evaluator evaluates single positions, without looking at the tree, and
  returning a number"
  [tree static-evaluator]
  (:node (apply max-key
                (evaluator static-evaluator)
                (:children tree))))

