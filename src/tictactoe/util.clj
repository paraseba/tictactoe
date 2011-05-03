(ns tictactoe.util
  (:gen-class)
  (:use tictactoe.core))

(defn cell-char [board cell]
  (cond
    (contains? (:x board) cell) " x "
    (contains? (:o board) cell) " o "
    :else "   "))

(defn print-board [board]
  (let [cells (map #(cell-char board %) all-cells)
        rows (partition 3 cells)
        str-rows (map #(apply str (interpose "|" %)) rows)
        row-sep "-----------"]
  (doseq [row (interpose row-sep str-rows)]
    (println row))))

(defn play [position]
  (doto (best-play position)
    print-board))

(def symbol->cell
  {'n 1 's 7 'e 5
   'w 3 'c 4 'o 4
   'ne 2 'nw 0 'se 8 'sw 6})

(defn ask-player [position]
  (prn)
  (println "Su jugada: ")
  (mark position (symbol->cell (read))))

(defn ended? [position]
  (empty? (empty-cells position)))

(defn driver [position]
  (let [my-play (play position)]
    (cond
      (winner my-play) (println "Yo gano")
      (ended? my-play) (println "Empate")
      :else (let [your-play (ask-player my-play)]
              (cond
                (winner your-play) (println "Usted gana")
                (ended? your-play) (println "Empate")
                :else (driver your-play))))))

(defmacro board [& cells]
  (let [marks (map vector cells all-cells)
        x (map second (filter #(= 'x (first %)) marks))
        o (map second (filter #(= 'o (first %)) marks))]
    `(make-board (vector ~@x) (vector ~@o))))

(defmacro mark# [board sym]
  `(mark ~board ~(symbol->cell sym)))

(def initial-board
  (board - - -
         - - -
         - - -))

(defn count-tree [t]
  (apply + 1 (map count-tree (:children t))))

(defn -main []
  (driver initial-board))

