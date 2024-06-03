package com.lostrucos.jabtbg.algorithms.mcts;

import com.lostrucos.jabtbg.core.Action;
import com.lostrucos.jabtbg.core.GameState;
import com.lostrucos.jabtbg.core.InformationSet;

import java.util.*;

/**
 * Represents a node in the Information Set Monte Carlo Tree Search (ISMCTS) algorithm.
 */
public class ISMCTSNode {
    private InformationSet infoSet;
    private ISMCTSNode parentNode;
    private List<GameState> stateList;
    private Map<Action, ISMCTSNode> childNodes;
    private double totalReward;
    private int visitCount;

    /**
     * Constructs a new ISMCTSNode.
     *
     * @param informationSet the information set represented by this node.
     * @param parentNode the parent node.
     */
    public ISMCTSNode(InformationSet informationSet, ISMCTSNode parentNode) {
        this.infoSet = informationSet;
        this.parentNode = parentNode;
        this.stateList = new ArrayList<>();
        this.childNodes = new HashMap<>();
        this.totalReward = 0.0;
        this.visitCount = 0;
    }

    /**
     * @return
     */
    public GameState getOrCreatePseudoState(int maxNumber) {
        GameState possibleNewState = infoSet.determinePseudoState();
        if(!stateList.contains(possibleNewState) && stateList.size() <= maxNumber) {
            stateList.add(possibleNewState);
            return possibleNewState;
        } else return getBestPseudoState();
    }

    /**
     * @return
     */
    public GameState getBestPseudoState() {
        return stateList.stream()
                .max(Comparator.comparing(state -> state.getUtility(state.getCurrentPlayer())))
                .orElseThrow(IllegalStateException::new);
    }

     /**
     * Gets or creates a child node for the given action.
     *
     * @param action the action to get or create the child node for.
     * @return the child node.
     */
     public ISMCTSNode getOrCreateChild(Action action) {
         return childNodes.computeIfAbsent(action, a -> new ISMCTSNode(this.getInformationSet().getNextInformationSet(a), this));
     }

    /**
     * Selects the best action from this node using UCB.
     *
     * @param explorationConstant the exploration constant.
     * @return the selected action.
     */
    public ISMCTSNode selectChild(double explorationConstant) {
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
     * Gets the information set represented by this node.
     *
     * @return the information set.
     */
    public InformationSet getInformationSet() {
        return infoSet;
    }

    /**
     * Gets the parent of this node.
     *
     * @return the parent node of this node.
     */
    public ISMCTSNode getParentNode() {
        return parentNode;
    }

    /**
     * Sets the given node as the parent of this node.
     *
     * @param parentNode the parent node of this node.
     */
    public void setParentNode(ISMCTSNode parentNode) {
        this.parentNode = parentNode;
    }

    /**
     * Gets the list of possible game states consistent with the information set of this node.
     *
     * @return the list of possible game states.
     */
    public List<GameState> getStateList() {
        return stateList;
    }

    /**
     * Gets the child nodes.
     *
     * @return a map of actions to child nodes.
     */
    public Map<Action, ISMCTSNode> getChildNodes() {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ISMCTSNode ismctsNode)) return false;
        return Objects.equals(infoSet, ismctsNode.infoSet) && Objects.equals(parentNode, ismctsNode.parentNode) && Objects.equals(childNodes, ismctsNode.childNodes);
    }

    @Override
    public int hashCode() {
        return Objects.hash(infoSet, parentNode, childNodes);
    }
}