package com.lostrucos.jabtbg.algorithms.minimax;

import com.lostrucos.jabtbg.core.*;

import java.util.List;

public class Minimax implements Algorithm {
    private Game game;
    private Player player;
    private int playerIndex;
    private int maxDepth;

    public Minimax(int maxDepth) {
        this.maxDepth = maxDepth;
    }

    @Override
    public void initialize(Game game, Player player) {
        this.game = game;
        this.player = player;
        this.playerIndex = player.getPlayerIndex();
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

    //
    @Override
    public Action chooseAction(GameState gameState) {
        double bestValue = Double.NEGATIVE_INFINITY;
        Action bestAction = null;

        InformationSet infoSet = gameState.getInformationSet(playerIndex);
        List<Action> actions = infoSet.getPlayerActions();

        for (Action action : actions) {
            InformationSet nextInfoSet = infoSet.getNextInformationSet(action);
            double value = minimaxRecursive(nextInfoSet, 0);
            if (value > bestValue) {
                bestValue = value;
                bestAction = action;
            }
        }

        return bestAction;
    }

    @Override
    public void updateAfterAction(GameState gameState, Action action) {
        // Non è necessario aggiornare lo stato interno in questo caso
    }

    @Override
    public void applyPseudoAction(GameState state, Action action) {
    }


    private double minimaxRecursive(InformationSet infoSet, int depth) {
        if (infoSet.isTerminal() || depth == maxDepth) {
            return infoSet.getUtility(playerIndex);
        }

        if (infoSet.getPlayerIndex() == playerIndex) {
            double bestValue = Double.NEGATIVE_INFINITY;
            List<Action> actions = infoSet.getPlayerActions();
            for (Action action : actions) {
                InformationSet nextInfoSet = infoSet.getNextInformationSet(action);
                double value = minimaxRecursive(nextInfoSet, depth + 1);
                bestValue = Math.max(bestValue, value);
            }
            return bestValue;
        } else {
            double bestValue = Double.POSITIVE_INFINITY;
            List<GameState> possibleStates = infoSet.getPossibleStates();
            for (GameState state : possibleStates) {
                InformationSet nextInfoSet = state.getInformationSet(infoSet.getPlayerIndex());
                double value = minimaxRecursive(nextInfoSet, depth + 1);
                bestValue = Math.min(bestValue, value);
            }
            return bestValue;
        }
    }

    public int getMaxDepth() {
        return maxDepth;
    }

    public void setMaxDepth(int maxDepth) {
        this.maxDepth = maxDepth;
    }
}