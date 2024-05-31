# jABTBG
## Java Agent Behaviour for Turn Based Games

A Java library that helps implement intelligent agents that can compete within **turn based games** using **decision tree algorithms** to choose the best action.

Inside the library, already implemented for you, there are algorithms best suited in **perfect information** contexts and others in **imperfect information** contexts.
Be careful which algorithm you decide to use!

Broken down by context of information provided by the game, you will find the following algorithms:

  1) Perfect information algorithms:
       - Monte Carlo Tree Search (MCTS)
       - standard Minimax
       - Alfa-Beta pruning Minimax

  2) Imperfect information algorithms:
       - Perfect Information Monte Carlo Tree Search (PIMCTS)
       - Information Set Monte Carlo Tree Search (ISMCTS)
       - Counterfactual Regret Minimization (CFRM)
    
***The library will take care of everything, you will only have to implement the game!***
