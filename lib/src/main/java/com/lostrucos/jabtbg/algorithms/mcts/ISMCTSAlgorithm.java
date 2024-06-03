package com.lostrucos.jabtbg.algorithms.mcts;

import com.lostrucos.jabtbg.core.*;

import java.util.*;

/**
 * Implements the Information Set Monte Carlo Tree Search (ISMCTS) algorithm for games with imperfect information.
 */
public class ISMCTSAlgorithm implements Algorithm {
    private Game game;
    private Agent agent;
    private final int numIterations;
    private final int maxPseudoStatePerNode;
    private final double explorationConstant;
    private Map<InformationSet, ISMCTSNode> gameTree;
    private ISMCTSNode rootNode;

    /**
     * Constructs a new ISMCTSAlgorithm.
     *
     * @param numIterations the number of simulations to run.
     * @param explorationConstant the exploration constant used in UCB.
     */
    public ISMCTSAlgorithm(int numIterations, int maxPseudoStatePerNode, double explorationConstant) {
        this.numIterations = numIterations;
        this.maxPseudoStatePerNode = maxPseudoStatePerNode;
        this.explorationConstant = explorationConstant;
    }

    /**
     * Initializes the algorithm with the given game and agent.
     * Initializes also its root node with the initial information set.
     *
     * @param game the game to be played.
     * @param agent the agent playing the game.
     */
    @Override
    public void initialize(Game game, Agent agent) {
        this.game = game;
        this.agent = agent;
        InformationSet initialInfoSet = game.getInitialState().getInformationSet(agent.getPlayerIndex());
        this.rootNode = new ISMCTSNode(initialInfoSet, null);
    }

    /**
     * It chooses the best action to take from the considered state using the ISMCTS algorithm
     * iterated as many times as was indicated during the instantiation of the ISMCTS algorithm.
     *
     * @param state the current game state.
     * @return the selected action.
     */
    @Override
    public Action chooseAction(GameState state) {
        InformationSet rootInfoSet = state.getInformationSet(agent.getPlayerIndex());
        ISMCTSNode node = gameTree.get(rootInfoSet);
        for (int i = 0; i < numIterations; i++) {
            GameState pseudoState = node.getOrCreatePseudoState(maxPseudoStatePerNode);
            runIteration(pseudoState);
        }
        return node.selectBestAction();
    }

    /**
     * Updates the tree after an action is taken.
     *
     * @param state the new game state.
     * @param action the action taken.
     */
    @Override
    public void updateAfterAction(GameState state, Action action) {
        InformationSet newInfoSet = state.getInformationSet(agent.getPlayerIndex());
        ISMCTSNode childNode = rootNode.getChildNodes().get(action);
        if (childNode != null && childNode.getInformationSet().equals(newInfoSet)) {
            rootNode = childNode;
            rootNode.setParentNode(null);
        } else {
            rootNode = new ISMCTSNode(newInfoSet, null);
        }
    }

    /**
     * Runs one complete iteration starting from the given pseudo-state (selection, expansion, simulation, and back-propagation).
     *
     * @param pseudoState the starting pseudo-state for the iteration.
     */
    void runIteration(GameState pseudoState) {
        ISMCTSNode selectedNode = selectNode(rootNode, pseudoState);
        ISMCTSNode expandedNode = expandNode(selectedNode);
        double reward = defaultPolicy(expandedNode.getInformationSet().determinePseudoState());
        backpropagation(expandedNode, reward);
    }

    /**
     * Selection phase.
     *
     * @param node the current node.
     * @param state the current state.
     * @return the node selected for simulation.
     */
    ISMCTSNode selectNode(ISMCTSNode node, GameState state) {
        while (!state.isTerminalNode() && node.getChildNodes().size() == node.getInformationSet().getPlayerActions().size()) {
            node = node.selectChild(explorationConstant);
            state = node.getInformationSet().determinePseudoState();
        }
        return node;
    }

    /**
     * Expands the game tree from the given node for every action available.
     *
     * @param node the node to expand.
     * @return the expanded node.
     */
    ISMCTSNode expandNode(ISMCTSNode node) {
        for (Action action : node.getInformationSet().getPlayerActions()) {
            if (!node.getChildNodes().containsKey(action)) {
                return node.getOrCreateChild(action);
            }
        }
        return node;  // Return the same node if all actions have been tried
    }

    /**
     * Simulation phase.
     *
     * @param state the starting state for the playout.
     * @return the reward from the playout.
     */
    double defaultPolicy(GameState state) {
        while (!state.isTerminalNode()) {
            state = randomNextState(state);
        }
        return state.getUtility(state.getCurrentPlayer());
    }

    /**
     * Selects a random next state from the given state.
     *
     * @param state the current state.
     * @return the next state.
     */
    GameState randomNextState(GameState state) {
        List<Action> actions = state.getInformationSet(state.getCurrentPlayer()).getPlayerActions();
        Action action = actions.get(new Random().nextInt(actions.size()));
        return action.applyAction(state);
    }

    /**
     * Back-propagation phase.
     *
     * @param node the starting node for the back-propagation step.
     * @param reward the reward to propagate.
     */
    void backpropagation(ISMCTSNode node, double reward) {
        while (node != null) {
            node.updateNodeStats(reward);
            node = node.getParentNode();
        }
    }

    /**
     * Gets the root node of the game tree.
     *
     * @return the root node.
     */
    public ISMCTSNode getRootNode() {
        return rootNode;
    }
}