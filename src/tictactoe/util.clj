(ns tictactoe.util
  (:use tictactoe.core))

(defn cell [board [i j]]
  (cond
    (contains? (:- board) [i j]) "   "
    (contains? (:x board) [i j]) " x "
    (contains? (:o board) [i j]) " o "))

(def all-cells
  (apply sorted-set
         (for [i (range 3) j (range 3)]
           [i j])))

(defn print-board [board]
  (doseq [row (interpose (apply str (repeat 11 "-"))
                         (map #(apply str (interpose "|" %)) (partition 3 (map #(cell board %) all-cells))))]
    (println row)))

(defmacro board [& cells]
  (let [marks (map vector cells all-cells)
        x (map second (filter #(= 'x (first %)) marks))
        o (map second (filter #(= 'o (first %)) marks))
        - (map second (filter #(= '- (first %)) marks))
        t (if (< (count o) (count x)) :o :x)]
    `{:x #{~@x} :o #{~@o} :- #{~@-} :turn ~t}))

(def initial-board
  (board - - -
         - - -
         - - -))

(defn play [position]
  (doto (best-play position)
    print-board))

(defn ask-player [position]
  (prn)
  (println "Su jugada: ")
  (mark position (read)))

(defn ended? [position]
  (empty? (:- position)))

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

(defn -main []
  (driver initial-board))

