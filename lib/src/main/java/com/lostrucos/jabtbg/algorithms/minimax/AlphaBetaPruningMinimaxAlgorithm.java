package com.lostrucos.jabtbg.algorithms.minimax;

import com.lostrucos.jabtbg.core.*;

import java.util.List;

public class AlphaBetaPruningMinimaxAlgorithm implements Algorithm {
    private Game game;
    private Player player;

    @Override
    public void initialize(Game game, Player player) {
        this.game = game;
        this.player = player;
    }

    @Override
    public void initialize(GameState state) {

    }

    @Override
    public void setStrategy(Strategy strategy) {

    }

    @Override
    public void reset() {

    }

    @Override
    public Action chooseAction(GameState gameState) {
        return alphaBetaMinimaxDecision(gameState, player.getPlayerIndex() % 2 == 0);
    }

    @Override
    public void updateAfterAction(GameState gameState, Action action) {
        // Potrebbe essere implementato per aggiornare lo stato interno, se necessario.
    }

    @Override
    public void applyPseudoAction(GameState state, Action action) {
    }


    private Action alphaBetaMinimaxDecision(GameState gameState, boolean isMaximizing) {
        List<? extends Action> actions = game.getPlayerActions(player.getPlayerIndex(), gameState);
        Action bestAction = null;
        double bestValue = isMaximizing ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        double alpha = Double.NEGATIVE_INFINITY;
        double beta = Double.POSITIVE_INFINITY;

        for (Action action : actions) {
            GameState newState = action.applyAction(gameState);
            double value = alphaBetaMinimax(newState, alpha, beta, !isMaximizing);

            if (isMaximizing) {
                if (value > bestValue) {
                    bestValue = value;
                    bestAction = action;
                }
                alpha = Math.max(alpha, bestValue);
            } else {
                if (value < bestValue) {
                    bestValue = value;
                    bestAction = action;
                }
                beta = Math.min(beta, bestValue);
            }

            if (beta <= alpha) {
                break; // Potatura alfa-beta
            }
        }

        return bestAction;
    }

    double alphaBetaMinimax(GameState gameState, double alpha, double beta, boolean isMaximizing) {
        if (gameState.isTerminalNode()) {
            return gameState.getUtility(player.getPlayerIndex());
        }

        double value = isMaximizing ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        List<? extends Action> actions = game.getPlayerActions(player.getPlayerIndex(), gameState);

        for (Action action : actions) {
            GameState newState = action.applyAction(gameState);
            if (isMaximizing) {
                value = Math.max(value, alphaBetaMinimax(newState, alpha, beta, false));
                alpha = Math.max(alpha, value);
            } else {
                value = Math.min(value, alphaBetaMinimax(newState, alpha, beta, true));
                beta = Math.min(beta, value);
            }

            if (beta <= alpha) {
                break; // Potatura alfa-beta
            }
        }

        return value;
    }
}