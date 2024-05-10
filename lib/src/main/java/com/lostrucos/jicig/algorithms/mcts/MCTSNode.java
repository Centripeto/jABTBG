package com.lostrucos.jicig.algorithms.mcts;

import com.lostrucos.jicig.core.Action;
import com.lostrucos.jicig.core.InformationSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MCTSNode {

    private final InformationSet infoSet;
    private final Map<InformationSet, MCTSNode> children;
    private double totalValue;
    private int visitCount;

    public MCTSNode(InformationSet infoSet) {
        this.infoSet = infoSet;
        this.children = new HashMap<>();
        this.totalValue = 0.0;
        this.visitCount = 0;
    }

    public MCTSNode getOrCreateChild(InformationSet childInfoSet) {
        return children.computeIfAbsent(childInfoSet, k -> new MCTSNode(childInfoSet));
    }

    public Action selectActionForExpansion() {
        List<Action> actions = infoSet.getPlayerActions();
        if (actions.isEmpty()) {
            return null;
        }

        double bestValue = Double.NEGATIVE_INFINITY;
        Action bestAction = null;

        for (Action action : actions) {
            MCTSNode child = getOrCreateChild(infoSet.getNextInformationSet(action));
            double value = child.totalValue / child.visitCount;
            if (value > bestValue) {
                bestValue = value;
                bestAction = action;
            }
        }

        return bestAction;
    }

    public Action selectBestAction() {
        List<Action> actions = infoSet.getPlayerActions();
        if (actions.isEmpty()) {
            return null;
        }

        double bestValue = Double.NEGATIVE_INFINITY;
        Action bestAction = null;

        for (Action action : actions) {
            MCTSNode child = getOrCreateChild(infoSet.getNextInformationSet(action));
            double value = child.totalValue / child.visitCount;
            if (value > bestValue) {
                bestValue = value;
                bestAction = action;
            }
        }

        return bestAction;
    }

    public void backpropagate(double value) {
        totalValue += value;
        visitCount++;
    }

    public InformationSet getInfoSet() {
        return infoSet;
    }

    public Map<InformationSet, MCTSNode> getChildren() {
        return children;
    }

    public double getTotalValue() {
        return totalValue;
    }

    public int getVisitCount() {
        return visitCount;
    }

}