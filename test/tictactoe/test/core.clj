(ns tictactoe.test.core
  (:use tictactoe.core tictactoe.ai tictactoe.board)
  (:use [clojure.test]))

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
  (is (= (best-tictactoe-move (board - x x
                                     - o -
                                     - - o))
         (board x x x
                - o -
                - - o)))

  (is (= (best-tictactoe-move (board - - o
                                     - x o
                                     x - -))
         (board - - o
                - x o
                x - x)))

  (is (= (best-tictactoe-move (board - - -
                                     - x o
                                     - - -))
         (board - - -
                - x o
                - - x)))

  (is (= (best-tictactoe-move (board - - o
                                     - x o
                                     x - -))
         (board - - o
                - x o
                x - x))))
