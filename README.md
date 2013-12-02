# tictactoe

[![Build Status](https://travis-ci.org/paraseba/tictactoe.png)](https://travis-ci.org/paraseba/tictactoe)

This is a basic tic-tac-toe player program.

It was created for a Clojure presentation, where the public knew nothing about
Clojure. So, I'm using only the most basic parts of Clojure. It has no optimizations
at all. It's only a starting point to play by improving the behaviour and performance.

But, all that said, it was a lot of fun to write it, and it's a lot of fun to play with it.
I think it showcases several great features of the language.
For instance, you could notice that:

* The code is really well modularized. The AI could play any game, the board
abstraction is simple and isolated from the details of finding the best move.
* tic-tac-toe rules are also isolated to a small number of functions
* Lazy sequences glue everything in a neat way
* It would be extremely easy to (lazily) prune the game tree to a certain level
* It would be easy to implement alpha-beta pruning optimization
* It would be easy to sort moves evaluation
* It would be very easy to improve the static evaluation function

You can also take a look at tests, they are beautiful thanks to Clojure macros:

      (is (= (best-tictactoe-move (board - x x
                                         - o -
                                         - - o))
             (board x x x
                    - o -
                    - - o)))

## I'm not a genius

It doesn't matter how bad you find this implementation, you have to love the ideas
behind it. Unfortunately the bad implementation is my fault, the beautiful ideas
are someone else's. You can read about it in a fantastic paper:
"Why functional programming matters" by John Hughes.
If you never read it before, you should go read it right now.
Probably you should go read it even if you have already read it.

## But I'm not an idiot either

If you find ways to improve this code, go ahead and do it, in fact, show me what
you did. But remember, it's just a showcase for people without any Clojure knowledge.
This is not professional code.

## License

Copyright (C) 2011 Sebastián Galkin

Distributed under the Eclipse Public License, the same as Clojure.
