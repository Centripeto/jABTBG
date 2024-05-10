package com.lostrucos.jicig.algorithms.mcts;

import com.lostrucos.jicig.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class ISMCTSAlgorithm implements Algorithm {
    private Game game;
    private Agent agent;
    private int numSimulations;
    private double explorationConstant;
    private Random random;
    private Map<InformationSet, MCTSNode> treeRoot;

    public ISMCTSAlgorithm(int numSimulations, double explorationConstant) {
        this.numSimulations = numSimulations;
        this.explorationConstant = explorationConstant;
        this.random = new Random();
        this.treeRoot = new HashMap<>();
    }

    @Override
    public void initialize(Game game, Agent agent) {
        this.game = game;
        this.agent = agent;
    }

    @Override
    public Action getAction(GameState state) {
        int playerIndex = state.getPlayerToMove();
        InformationSet infoSet = state.getInformationSet(playerIndex);
        MCTSNode root = treeRoot.computeIfAbsent(infoSet, k -> new MCTSNode(infoSet));
        runSimulations(root);
        return root.selectBestAction();
    }

    @Override
    public void updateAfterAction(GameState state, Action action) {
        // Nessuna operazione necessaria
    }

    private void runSimulations(MCTSNode root) {
        for (int i = 0; i < numSimulations; i++) {
            GameState simulationState = root.getInfoSet().getPossibleStates().get(random.nextInt(root.getInfoSet().getPossibleStates().size()));
            runSimulation(root, simulationState);
        }
    }

    private void runSimulation(MCTSNode node, GameState state) {
        if (state.isTerminalNode()) {
            node.backpropagate(game.getUtility(state, agent.getPlayerIndex()));
            return;
        }

        int playerIndex = state.getPlayerToMove();
        InformationSet infoSet = state.getInformationSet(playerIndex);
        MCTSNode childNode = node.getOrCreateChild(infoSet);

        if (playerIndex == agent.getPlayerIndex()) {
            Action action = childNode.selectActionForExpansion();
            if (action == null) {
                runPlayout(childNode, state);
            } else {
                GameState nextState = action.applyAction(state);
                runSimulation(childNode, nextState);
            }
        } else {
            runPlayout(childNode, state);
        }
    }

    private void runPlayout(MCTSNode node, GameState state) {
        double utility = randomPlayout(state);
        node.backpropagate(utility);
    }

    private double randomPlayout(GameState state) {
        if (state.isTerminalNode()) {
            return game.getUtility(state, agent.getPlayerIndex());
        }

        int playerIndex = state.getPlayerToMove();
        List<Action> actions = game.getPlayerActions(playerIndex, state);
        Action randomAction = actions.get(random.nextInt(actions.size()));
        GameState nextState = randomAction.applyAction(state);

        if (playerIndex == agent.getPlayerIndex()) {
            return randomPlayout(nextState);
        } else {
            double utility = randomPlayout(nextState);
            return -utility; // Inversione dell'utilità per il giocatore avversario
        }
    }
}