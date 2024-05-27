package com.lostrucos.jicig.algorithms.mcts;

import com.lostrucos.jicig.core.Action;
import com.lostrucos.jicig.core.GameState;

import java.util.*;

/**
 * Represents a node in the Monte Carlo Tree Search (MCTS) algorithm.
 */
public class MCTSNode {
    private GameState state;
    private MCTSNode parentNode;
    private Map<Action, MCTSNode> childNodes;
    private double totalReward;
    private int visitCount;
    private boolean isLeaf;

    /**
     * Constructs a new MCTSNode.
     *
     * @param state  the game state represented by this node.
     * @param parentNode the parent node.
     */
    public MCTSNode(GameState state, MCTSNode parentNode) {
        this.state = state;
        this.parentNode = parentNode;
        this.childNodes = new HashMap<>();
        this.totalReward = 0.0;
        this.visitCount = 0;
        this.isLeaf = true;
    }

    /**
     * Checks if this node is a terminal node.
     *
     * @return true if this node is terminal, false otherwise.
     */
    public boolean isTerminal() {
        return isLeaf && state.isTerminalNode();
    }

    /**
     * Gets or creates a child node for the given action.
     *
     * @param action the action to get or create the child node for.
     * @return the child node.
     */
    public MCTSNode getOrCreateChild(Action action) {
        MCTSNode childNode = childNodes.computeIfAbsent(action, a -> new MCTSNode(action.applyAction(this.state), this));
        isLeaf = false;
        return childNode;
    }

    /**
     * Selects the best action from this node using UCB.
     *
     * @param explorationConstant the exploration constant.
     * @return the selected action.
     */
    public MCTSNode selectChild(double explorationConstant) {
        return childNodes.values().stream()
                .max(Comparator.comparing(node -> node.getUCBValue(explorationConstant)))
                .orElseThrow(IllegalStateException::new);
    }

    /**
     * Gets the UCB value for this node.
     *
     * @param explorationConstant the exploration constant.
     * @return the UCB value.
     */
    public double getUCBValue(double explorationConstant) {
        if (visitCount == 0) {
            return Double.POSITIVE_INFINITY;
        }
        return totalReward / visitCount + explorationConstant * Math.sqrt(Math.log(parentNode.visitCount) / visitCount);
    }

    /**
     * Select the best action of this node based on the ratio of reward to visits.
     *
     * @return the best action.
     */
    public Action selectBestAction() {
        return childNodes.entrySet().stream()
                .max(Comparator.comparingDouble(entry -> entry.getValue().totalReward / entry.getValue().visitCount))
                .map(Map.Entry::getKey)
                .orElseThrow(IllegalStateException::new);
    }

    /**
     * Updates the statistics of this node.
     *
     * @param reward the value to update with.
     */
    public void updateNodeStats(double reward) {
        this.visitCount++;
        this.totalReward += reward;
    }

    /**
     * Gets the game state represented by this node.
     *
     * @return the game state.
     */
    public GameState getState() {
        return state;
    }

    /**
     * Gets the parent node.
     *
     * @return the parent node.
     */
    public MCTSNode getParentNode() {
        return parentNode;
    }

    /**
     * Gets the child nodes.
     *
     * @return a map of actions to child nodes.
     */
    public Map<Action, MCTSNode> getChildNodes() {
        return childNodes;
    }

    /**
     * Gets the total reward of the node
     *
     * @return the reward score of the node
     */
    public double getTotalReward() {
        return totalReward;
    }

    /**
     * Gets the number of times the node has been visited
     *
     * @return the number of times the node has been visited
     */
    public int getVisitCount() {
        return visitCount;
    }

    /**
     * Checks if this node is a leaf node.
     *
     * @return true if this node is a leaf, false otherwise.
     */
    public boolean isLeaf() {
        return isLeaf;
    }

    /**
     * Sets the new value for the variable isLeaf
     *
     * @param leaf the new boolean value
     */
    public void setLeaf(boolean leaf) {
        isLeaf = leaf;
    }
}