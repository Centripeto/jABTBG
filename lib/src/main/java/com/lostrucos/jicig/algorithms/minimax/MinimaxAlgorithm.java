package com.lostrucos.jicig.algorithms.minimax;

import com.lostrucos.jicig.core.*;

import java.util.List;

public class MinimaxAlgorithm implements Algorithm {
    private Game game;
    private Agent agent;
    private int maxDepth;

    public MinimaxAlgorithm(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public void initialize(Game game, Agent agent) {
        this.game = game;
        this.agent = agent;
    }

    @Override
    public Action getAction(GameState state) {
        int playerIndex = state.getPlayerToMove();
        return maximizeValue(state, maxDepth, playerIndex);
    }

    @Override
    public void updateAfterAction(GameState state, Action action) {
        // Nessuna operazione necessaria per l'algoritmo Minimax
    }

    private Action maximizeValue(GameState state, int depth, int playerIndex) {
        if (state.isTerminalNode() || depth == 0) {
            return null; // Nessuna azione da eseguire in uno stato terminale o alla massima profondità
        }

        List<Action> actions = game.getPlayerActions(playerIndex, state);
        double bestValue = Double.NEGATIVE_INFINITY;
        Action bestAction = null;

        for (Action action : actions) {
            GameState nextState = action.applyAction(state);
            double value = minimizeValue(nextState, depth - 1, (playerIndex + 1) % game.getPlayerCount());

            if (value > bestValue) {
                bestValue = value;
                bestAction = action;
            }
        }

        return bestAction;
    }

    private double minimizeValue(GameState state, int depth, int playerIndex) {
        if (state.isTerminalNode() || depth == 0) {
            return game.getUtility(state, agent.getPlayerIndex());
        }

        double bestValue = Double.POSITIVE_INFINITY;
        List<Action> actions = game.getPlayerActions(playerIndex, state);

        for (Action action : actions) {
            GameState nextState = action.applyAction(state);
            Action nextAction = maximizeValue(nextState, depth - 1, (playerIndex + 1) % game.getPlayerCount());
            if (nextAction != null) {
                GameState nextNextState = nextAction.applyAction(nextState);
                double value = game.getUtility(nextNextState, agent.getPlayerIndex());
                bestValue = Math.min(bestValue, value);
            }
        }

        return bestValue;
    }
}
