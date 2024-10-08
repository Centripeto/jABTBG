package com.lostrucos.jabtbg.algorithms.mcts;

import com.lostrucos.jabtbg.core.*;

import java.util.*;

/**
 * Implements the Information Set Monte Carlo Tree Search (ISMCTS) algorithm for games with imperfect information.
 */
public class ISMCTSAlgorithm implements Algorithm {
    private Game game;
    private Player player;
    private int numSimulations;
    private double explorationConstant;
    private Map<InformationSet, MCTSNode> gameTree;
    private MCTSNode rootNode;

    /**
     * Constructs a new ISMCTSAlgorithm.
     *
     * @param numSimulations the number of simulations to run.
     * @param explorationConstant the exploration constant used in UCB.
     */
    public ISMCTSAlgorithm(int numSimulations, double explorationConstant) {
        this.numSimulations = numSimulations;
        this.explorationConstant = explorationConstant;
        this.gameTree = new HashMap<>();
    }

    /**
     * Initializes the algorithm with the given game and agent.
     * Initializes also the game tree and its root node.
     *
     * @param game the game to be played.
     * @param player the agent playing the game.
     */
    @Override
    public void initialize(Game game, Player player) {
        this.game = game;
        this.player = player;
        InformationSet initialInfoSet = game.getInitialState().getInformationSet(player.getPlayerIndex());
        gameTree.put(initialInfoSet, new MCTSNode(game.getInitialState(), null));
        Optional<InformationSet> firstKey = gameTree.keySet().stream().findFirst();
        rootNode = firstKey.map(gameTree::get).orElse(null);
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

    /**
     * It chooses the best action to take from the considered state using the MCTS algorithm
     * iterated as many times as was indicated during the instantiation of the MCTS algorithm.
     *
     * @param state the current game state.
     * @return the selected action.
     */
    @Override
    public Action chooseAction(GameState state) {
        InformationSet infoSet = state.getInformationSet(player.getPlayerIndex());
        MCTSNode node = gameTree.get(infoSet);
        for (int i = 0; i < numSimulations; i++) {
            runIteration(node);
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
        InformationSet infoSet = state.getInformationSet(state.getCurrentPlayer());
        gameTree.keySet().removeIf(key -> !key.equals(infoSet));
    }

    @Override
    public void applyPseudoAction(GameState state, Action action) {
    }


    /**
     * Runs one complete iteration starting from the given starting node (selection, expansion, simulation and backpropagation).
     *
     * @param startingNode the starting node for the iteration.
     */
    void runIteration(MCTSNode startingNode) {
        MCTSNode leafNode = expandGameTree(selectLeafNode(startingNode));
        simulation(leafNode);
    }

    /**
     * Selects a leaf node from the given starting node.
     *
     * @param startingNode the starting node.
     * @return the selected leaf node.
     */
    MCTSNode selectLeafNode(MCTSNode startingNode) {
        if(startingNode.isTerminal()) {
            return startingNode;
        }

        MCTSNode selectedNode = startingNode;

        while(!selectedNode.isLeaf()) {
            selectedNode = selectedNode.selectChild(explorationConstant);
        }

        return selectedNode;
    }

    /**
     * If the leaf node isn't a terminal node, expands the game tree one time from the given leaf node.
     *
     * @param leafNode the leaf node to expand.
     * @return the expanded node.
     */
    MCTSNode expandGameTree(MCTSNode leafNode) {
        if(leafNode.isTerminal()) return leafNode;

        // 3 metodi diversi per ricavare la lista delle azioni disponibili per il nodo foglia selezionato per l'espansione

        //List<Action> actionList = game.getPlayerActions(agent.getPlayerIndex(),leafNode.getState());
        //List<Action> actionList = leafNode.getState().getAvailableActions(agent.getPlayerIndex());
        InformationSet infoSet = leafNode.getState().getInformationSet(player.getPlayerIndex());
        List<Action> actionList = infoSet.getPlayerActions();

        // nel caso in cui il nodo venga creato grazie ad una azione presa a random dalla lista
        //Random random = new Random();
        //int randomIndex = random.nextInt(actionList.size());
        //Action action = actionList.get(randomIndex);

        // nel caso in cui il nodo venga creato grazie alla prima azione disponibile della lista
        Action action = actionList.get(0);

        MCTSNode expandedNode = leafNode.getOrCreateChild(action);
        gameTree.put(expandedNode.getState().getInformationSet(player.getPlayerIndex()), expandedNode);
        return expandedNode;
    }

    /**
     * Performs a simulation from the given starting node then calls a backpropagation for every simulation node created.
     *
     * @param startingNode the startingNode to start the playout from.
     */
    void simulation(MCTSNode startingNode) {
        MCTSNode terminalNode = startingNode;
        while(!terminalNode.isTerminal()) {
            List<Action> actionList = new ArrayList<>(startingNode.getChildNodes().keySet());
            Random random = new Random();
            int randomIndex = random.nextInt(actionList.size());
            Action randomAction = actionList.get(randomIndex);
            terminalNode = terminalNode.getOrCreateChild(randomAction);
        }
        do {
            terminalNode = backpropagationStep(terminalNode);
        } while(!terminalNode.equals(rootNode));
    }

    /**
     * Performs one step of backpropagation to update the node's visits and score with the results of the simulation.
     *
     * @param startingNode the starting node to perform the backpropagation step.
     * @return the parent node of the backpropagated node.
     */
    MCTSNode backpropagationStep(MCTSNode startingNode) {
        startingNode.updateNodeStats(game.getUtility(startingNode.getState(), player.getPlayerIndex()));
        return startingNode.getParentNode();
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
}