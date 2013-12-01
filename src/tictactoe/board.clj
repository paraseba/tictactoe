(ns tictactoe.board)

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

(def ^{:doc "Sorted set of all cell identifiers, ordered from top left to bottom right"}
  all-cells
  (apply sorted-set (range 9)))

(def ^{:doc "Get board cells marked with x"} x-cells :x)
(def ^{:doc "Get board cells marked with o"} o-cells :o)

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
