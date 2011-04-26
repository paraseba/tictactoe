(ns tictactoe.util
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

(defn ask-player [position]
  (prn)
  (println "Su jugada: ")
  (mark position (read)))

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

(def initial-board
  (board - - -
         - - -
         - - -))

(defn -main []
  (driver initial-board))

