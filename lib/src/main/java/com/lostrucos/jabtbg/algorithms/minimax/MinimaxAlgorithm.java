package com.lostrucos.jabtbg.algorithms.minimax;

import com.lostrucos.jabtbg.core.*;

import java.util.List;

public class MinimaxAlgorithm implements Algorithm {
    private Game game;
    private Agent agent;

    @Override
    public void initialize(Game game, Agent agent) {
        this.game = game;
        this.agent = agent;
    }

    @Override
    public void initialize(GameState state) {

    }

    @Override
    public void setUtilityStrategy(UtilityStrategy strategy) {

    }

    @Override
    public void reset() {

    }

    @Override
    public Action chooseAction(GameState gameState) {
        return minimaxDecision(gameState, agent.getPlayerIndex() % 2 == 0);
    }

    @Override
    public void updateAfterAction(GameState gameState, Action action) {
        // Potrebbe essere implementato per aggiornare lo stato interno, se necessario.
    }

    @Override
    public GameState applyPseudoAction(GameState state, Action action) {
        return null;
    }

    private Action minimaxDecision(GameState gameState, boolean isMaximizing) {
        List<? extends Action> actions = game.getPlayerActions(agent.getPlayerIndex(), gameState);
        Action bestAction = null;
        double bestValue = isMaximizing ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;

        for (Action action : actions) {
            GameState newState = action.applyAction(gameState);
            double value = minMaxValue(newState, !isMaximizing);

            if (isMaximizing) {
                if (value > bestValue) {
                    bestValue = value;
                    bestAction = action;
                }
            } else {
                if (value < bestValue) {
                    bestValue = value;
                    bestAction = action;
                }
            }
        }

        return bestAction;
    }

    double minMaxValue(GameState gameState, boolean isMaximizing) {
        if (gameState.isTerminalNode()) {
            return gameState.getUtility(agent.getPlayerIndex());
        }

        double value = isMaximizing ? Double.NEGATIVE_INFINITY : Double.POSITIVE_INFINITY;
        List<? extends Action> actions = game.getPlayerActions(agent.getPlayerIndex(), gameState);

        for (Action action : actions) {
            GameState newState = action.applyAction(gameState);
            value = isMaximizing ? Math.max(value, minMaxValue(newState, false)) : Math.min(value, minMaxValue(newState, true));
        }

        return value;
    }
}
