package com.lostrucos.jicig.algorithms.crm;

import com.lostrucos.jicig.core.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

public class CFRAlgorithm implements Algorithm {
    private Game game;
    private Agent agent;
    private int iterations;
    private double regretMatchingWeight;
    private Random random;
    private Map<InformationSet, Map<Action, Double>> regretTable;
    private Map<InformationSet, Map<Action, Double>> strategyTable;

    public CFRAlgorithm(int iterations, double regretMatchingWeight) {
        this.iterations = iterations;
        this.regretMatchingWeight = regretMatchingWeight;
        this.random = new Random();
        this.regretTable = new HashMap<>();
        this.strategyTable = new HashMap<>();
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
        Map<Action, Double> strategy = getStrategy(infoSet);
        return sampleAction(strategy);
    }

    @Override
    public void updateAfterAction(GameState state, Action action) {
        // Nessuna operazione necessaria
    }

    private Map<Action, Double> getStrategy(InformationSet infoSet) {
        Map<Action, Double> strategy = strategyTable.get(infoSet);
        if (strategy == null) {
            strategy = initializeStrategy(infoSet);
            strategyTable.put(infoSet, strategy);
        }
        return strategy;
    }

    private Map<Action, Double> initializeStrategy(InformationSet infoSet) {
        Map<Action, Double> strategy = new HashMap<>();
        List<Action> actions = game.getPlayerActions(infoSet.getPlayerIndex(), infoSet.getPossibleStates().get(0));
        double weight = 1.0 / actions.size();
        for (Action action : actions) {
            strategy.put(action, weight);
        }
        return strategy;
    }

    private Action sampleAction(Map<Action, Double> strategy) {
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (Map.Entry<Action, Double> entry : strategy.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (randomValue < cumulativeProbability) {
                return entry.getKey();
            }
        }
        throw new IllegalStateException("Invalid strategy distribution");
    }

    public void train() {
        for (int i = 0; i < iterations; i++) {
            trainIteration();
        }
    }

    private void trainIteration() {
        GameState initialState = game.getInitialState();
        trainRecursive(initialState, 1.0);
        updateRegrets();
    }

    private double trainRecursive(GameState state, double weight) {
        if (state.isTerminalNode()) {
            return weight * game.getUtility(state, agent.getPlayerIndex());
        }

        int playerIndex = state.getPlayerToMove();
        InformationSet infoSet = state.getInformationSet(playerIndex);
        Map<Action, Double> strategy = getStrategy(infoSet);
        double utility = 0.0;

        for (Map.Entry<Action, Double> entry : strategy.entrySet()) {
            Action action = entry.getKey();
            double probability = entry.getValue();
            GameState nextState = action.applyAction(state);
            double nextUtility = trainRecursive(nextState, weight * probability);
            double regret = nextUtility - utility;
            updateRegret(infoSet, action, regret);
            utility += probability * nextUtility;
        }

        return utility;
    }

    private void updateRegret(InformationSet infoSet, Action action, double regret) {
        Map<Action, Double> regretMap = regretTable.computeIfAbsent(infoSet, k -> new HashMap<>());
        regretMap.put(action, regretMap.getOrDefault(action, 0.0) + regret);
    }

    private void updateRegrets() {
        for (Map.Entry<InformationSet, Map<Action, Double>> entry : regretTable.entrySet()) {
            InformationSet infoSet = entry.getKey();
            Map<Action, Double> regretMap = entry.getValue();
            Map<Action, Double> strategy = getStrategy(infoSet);
            double sumRegrets = regretMap.values().stream().mapToDouble(Double::doubleValue).sum();
            double normalizationFactor = Math.max(sumRegrets, 1.0);

            for (Map.Entry<Action, Double> actionEntry : regretMap.entrySet()) {
                Action action = actionEntry.getKey();
                double regret = actionEntry.getValue();
                double newWeight = (1 - regretMatchingWeight) * strategy.get(action) + regretMatchingWeight * regret / normalizationFactor;
                strategy.put(action, newWeight);
            }
        }
    }
}