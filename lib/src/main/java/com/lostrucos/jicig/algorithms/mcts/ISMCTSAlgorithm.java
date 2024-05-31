package com.lostrucos.jicig.algorithms.mcts;

import com.lostrucos.jicig.core.*;

import java.util.*;

/**
 * Implements the Information Set Monte Carlo Tree Search (ISMCTS) algorithm for games with imperfect information.
 */
public class ISMCTSAlgorithm implements Algorithm {
    private Game game;
    private Agent agent;
    private int numSimulations;
    private double explorationConstant;
    private Map<InformationSet, MCTSNode> gameTree;
    private MCTSNode rootNode;
    private List<MCTSNode> selectedPath;

    /**
     * Constructs a new ISMCTSAlgorithm.
     *
     * @param numSimulations     the number of simulations to run.
     * @param explorationConstant the exploration constant used in UCB.
     */
    public ISMCTSAlgorithm(int numSimulations, double explorationConstant) {
        this.numSimulations = numSimulations;
        this.explorationConstant = explorationConstant;
        this.gameTree = new HashMap<>();
        this.selectedPath = new ArrayList<>();
    }

    /**
     * Initializes the algorithm with the given game and agent.
     *
     * @param game  the game to be played.
     * @param agent the agent playing the game.
     */
    @Override
    public void initialize(Game game, Agent agent) {
        this.game = game;
        this.agent = agent;
        InformationSet initialInfoSet = game.getInitialState().getInformationSet(agent.getPlayerIndex());
        gameTree.put(initialInfoSet, new MCTSNode(game.getInitialState(), null));
        Optional<InformationSet> firstKey = gameTree.keySet().stream().findFirst();
        rootNode = firstKey.map(gameTree::get).orElse(null);
    }

    /**
     * Chooses an action for the given state using the ISMCTS algorithm.
     *
     * @param state the current game state.
     * @return the selected action.
     */
    @Override
    public Action chooseAction(GameState state) {
        InformationSet infoSet = state.getInformationSet(agent.getPlayerIndex());
        MCTSNode node = gameTree.get(infoSet);
        runIteration(node);
        return node.selectBestAction();
    }

    /**
     * Updates the tree after an action is taken.
     *
     * @param state  the new game state.
     * @param action the action taken.
     */
    @Override
    public void updateAfterAction(GameState state, Action action) {
        InformationSet infoSet = state.getInformationSet(state.getCurrentPlayer());
        gameTree.keySet().removeIf(key -> !key.equals(infoSet));
    }

    /**
     * Runs a simulation from the given starting node.
     *
     * @param startingNode the starting node for the simulation.
     */
    void runIteration(MCTSNode startingNode) {
        expandGameTree(selectLeafNode(startingNode));
        randomPlayout(selectedPath.get(selectedPath.size() -1));
        backpropagation();
    }

    /**
     * Selects a leaf node from the given starting node.
     *
     * @param startingNode the starting node.
     * @return the selected leaf node.
     */
    MCTSNode selectLeafNode(MCTSNode startingNode) {
        if (startingNode.isTerminal()) {
            return startingNode;
        }

        MCTSNode selectedNode = startingNode;
        selectedPath.add(selectedNode);

        while (!selectedNode.isLeaf()) {
            selectedNode = selectedNode.selectChild(explorationConstant);
            selectedPath.add(selectedNode);
        }

        return selectedNode;
    }

    /**
     * Expands the game tree from the given leaf node.
     *
     * @param leafNode the leaf node to expand.
     */
    void expandGameTree(MCTSNode leafNode) {
        if (!leafNode.isTerminal()) {
            InformationSet infoSet = leafNode.getState().getInformationSet(agent.getPlayerIndex());
            List<Action> actionList = infoSet.getPlayerActions();
            for (Action action : actionList) {
                leafNode.getOrCreateChild(action);
            }
        }
    }

    /**
     * Performs a random playout from the given node.
     *
     * @param node the node to start the playout from.
     */
    void randomPlayout(MCTSNode node) {
        List<Action> keys = new ArrayList<>(node.getChildNodes().keySet());
        Random random = new Random();
        int randomIndex = random.nextInt(keys.size());
        Action randomKey = keys.get(randomIndex);
        MCTSNode selectedNode = node.getChildNodes().get(randomKey);
        selectedPath.add(selectedNode);
    }

    /**
     * Performs backpropagation to update the tree with the results of the simulation.
     */
    void backpropagation() {
        for (MCTSNode node : selectedPath) {
            node.updateNodeStats(game.getUtility(node.getState(), agent.getPlayerIndex()));
        }
        selectedPath.clear();
    }

    /**
     * Gets the game tree.
     *
     * @return the game tree.
     */
    public Map<InformationSet, MCTSNode> getGameTree() {
        return gameTree;
    }

    /**
     * Gets the root node of the game tree.
     *
     * @return the root node.
     */
    public MCTSNode getRootNode() {
        return rootNode;
    }

    /**
     * Gets the selected path from the root to the current node.
     *
     * @return the selected path.
     */
    public List<MCTSNode> getSelectedPath() {
        return selectedPath;
    }
}