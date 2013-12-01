(ns tictactoe.test.board
  (:use tictactoe.board)
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

