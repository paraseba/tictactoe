(ns tictactoe.test.core
  (:use [tictactoe.core]
        [tictactoe.util])
  (:use [clojure.test]))

(deftest mark-test
  (is (= initial-board (board - - -
                              - - -
                              - - -)))
  (is (= (board - x -
                - - -
                - - -)
         (mark# initial-board n)))

  (is (= (board - x -
                - o -
                - - -)
         (-> initial-board
           (mark# n)
           (mark# c))))

  (is (= (board - x -
                - o x
                o - -)
         (-> initial-board
           (mark# n)
           (mark# c)
           (mark# e)
           (mark# sw)))))

(deftest winner-test
  (is (nil? (winner (board - - -
                           - - -
                           - - -))))

  (is (nil? (winner (board - - o
                           x x o
                           - - -))))

  (is (nil? (winner (board x o o
                           x x o
                           o x -))))

  (is (= :x (winner (board x o o
                           x x o
                           o x x))))

  (is (= :o (winner (board x o o
                           x x o
                           o x o)))))

(deftest play-test
  (is (= (best-play (board - x x
                           - o -
                           - - o))
         (board x x x
                - o -
                - - o)))

  (is (= (best-play (board - - o
                           - x o
                           x - -))
         (board - - o
                - x o
                x - x)))

  (is (= (best-play (board - - -
                           - x o
                           - - -))
         (board - - -
                - x o
                - - x)))

  (is (= (best-play (board - - o
                           - x o
                           x - -))
         (board - - o
                - x o
                x - x))))
