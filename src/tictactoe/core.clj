(ns tictactoe.core
  (:require [tictactoe.ai :as ai]
            [clojure.set]))

; -----------------
; Board abstraction
; -----------------
(defn make-board
  "Main game board abstraction. Creates a board given x marked cells
  and o marked cells. Cells will be identified by integers starting
  with 0 at top lef and ending with 8 at bottom right.
  The board is immutable"
  [x-cells o-cells]
  {:x (set x-cells) :o (set o-cells)})

(def ^{:doc "Get board cells marked with x"} x-cells :x)
(def ^{:doc "Get board cells marked with o"} o-cells :o)

(def ^{:doc "Sorted set of all cell identifiers, ordered from top left to bottom right"}
  all-cells
  (apply sorted-set (range 9)))

(defn empty-cells
  "Given a board, return a set of all empty cells"
  [board]
  (clojure.set/difference all-cells (x-cells board) (o-cells board)))

(defn mark
  "Mark a cell in the board. Cell is marked according to the corresponding player turn.
  Cell should be an integer 0 <= cell < 9. Returns a new board with the given cell marked"
  [board cell]
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

; ----------
; Find winner
; ----------

(def ^{:doc "All sets of winning cells"}
  win-cells
  (let [row1 #{0 1 2} row2 #{3 4 5} row3 #{6 7 8}
        col1 #{0 3 6} col2 #{1 4 7} col3 #{2 5 8}
        dia1 #{0 4 8} dia2 #{2 4 6}]
    [row1 row2 row3 col1 col2 col3 dia1 dia2]))

(defn won?
  "Return not nil if the sequence of marked cells represent a winner board"
  [cells]
  (some #(every? cells %) win-cells))

(defn winner
  "Return a keyword (:x or :o) representing the winner or nil if nobody wins in the board"
  [board]
  (cond
    (won? (:x board)) :x
    (won? (:o board)) :o))

; ---------
; Tic-tac-toe
; ---------

(defn evaluate-static-position
  "Trivial static evaluation of a board. Not really evaluating anything, it just
  detects winners"
  [board]
  (case (winner board)
    :x 1
    :o -1
    0))

(defn plays
  "Return a lazy seq of all posible boards obtained by making one move from the given
  board. The move will be done by the player with the current turn"
  [board]
  (if (winner board)
    []
    (map (partial mark board) (empty-cells board))))

; --------------
; Board printing
; --------------

(defn cell-string
  "Print a given cell of the board, using the right player symbol"
  [board cell]
  (cond
    (contains? (:x board) cell) " x "
    (contains? (:o board) cell) " o "
    :else "   "))

(defn print-board
  "Print the given board to stdout"
  [board]
  (let [cells (map #(cell-string board %) all-cells)
        rows (partition 3 cells)
        str-rows (map #(apply str (interpose "|" %)) rows)
        row-sep "-----------"]
  (doseq [row (interpose row-sep str-rows)]
    (println row))))

; -------
; Helpers
; -------

(defmacro board
  "Create a board by drawing it. Pass to the macro 9 arguments, each one being
  one of the symbols x, o -

  (board - x -
         o - x
         o - -)

  It doesn't do any checks"
  [& cells]
  (let [marks (map vector cells all-cells)
        x (map second (filter #(= 'x (first %)) marks))
        o (map second (filter #(= 'o (first %)) marks))]
    `(make-board (vector ~@x) (vector ~@o))))

(def symbol->cell
  {'n 1 's 7 'e 5
   'w 3 'c 4 'o 4
   'ne 2 'nw 0 'se 8 'sw 6})

(defmacro mark#
  "Mark a cell in a board with the current player turn.
  sym could be any of
  n s e w c o ne nw se sw

  c and o represent the center cell, the rest are the corresponding cardinal positions"
  [board sym]
  `(mark ~board ~(symbol->cell sym)))

(def initial-board
  (board - - -
         - - -
         - - -))

(defn best-tictactoe-move
  "Find the best computer move for the given board position"
  [board]
  (ai/best-move (ai/game-tree board plays) evaluate-static-position))

; -------
; Game UI
; -------

(defn play
  "Find best computer move and print it. Return new board"
  [board]
  (doto (best-tictactoe-move board)
    print-board))

(defn player-move
  "Ask the player to move and return the new board"
  [board]
  (prn)
  (println "Your move: ")
  (mark board (symbol->cell (read))))

(defn ended?
  "True if there are no more empty cells in the board"
  [board]
  (empty? (empty-cells board)))

(defn driver
  "Drive the UI asking the player and making the computer make moves"
  [board]
  (let [my-play (play board)]
    (cond
      (winner my-play) (println "I win")
      (ended? my-play) (println "Draw")
      :else (let [your-play (player-move my-play)]
              (cond
                (winner your-play) (println "You win")
                (ended? your-play) (println "Draw")
                :else (driver your-play))))))

(defn -main []
  (driver initial-board))

