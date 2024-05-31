package com.lostrucos.jicig.algorithms.crm;

import com.lostrucos.jicig.core.*;

import java.util.*;

/**
 * Implementation of the Counterfactual Regret Minimization (CFR) Algorithm.
 * This algorithm is used for finding approximate Nash equilibria in games of imperfect information.
 */
public class CFRMAlgorithm implements Algorithm {
    private Game game;
    private Agent agent;
    private final int numIterations;
    private final double regretMatchingWeight;
    private Map<InformationSet, Map<Action, Double>> regretTable;
    private Map<InformationSet, Map<Action, Double>> strategyTable;

    /**
     * Constructs a new CFRMAlgorithm.
     *
     * @param numIterations         the number of iterations for the training process.
     * @param regretMatchingWeight  the weight used in the regret matching process.
     */
    public CFRMAlgorithm(int numIterations, double regretMatchingWeight) {
        this.numIterations = numIterations;
        this.regretMatchingWeight = regretMatchingWeight;
        this.regretTable = new HashMap<>();
        this.strategyTable = new HashMap<>();
    }

    @Override
    public void initialize(Game game, Agent agent) {
        this.game = game;
        this.agent = agent;
    }

    /**
     * Returns the appropriate action for the given game state chosen by the algorithm.
     *
     * @param state the current game state.
     * @return the chosen action.
     */
    @Override
    public Action chooseAction(GameState state) {
        InformationSet infoSet = state.getInformationSet(state.getCurrentPlayer());
        Map<Action, Double> strategy = getStrategy(infoSet);
        return selectAction(strategy);
    }

    @Override
    public void updateAfterAction(GameState state, Action action) {
        // No operation needed
    }

    /**
     * Obtains the strategy for a given player.
     * If not already drafted, initializes a new strategy.
     *
     * @param infoSet the player's current information set.
     * @return the player's current strategy.
     */
    Map<Action, Double> getStrategy(InformationSet infoSet) {
        Map<Action, Double> strategy = strategyTable.get(infoSet);
        if (strategy == null) {
            strategy = initializeStrategy(infoSet);
            strategyTable.put(infoSet, strategy);
        }
        return strategy;
    }

    /**
     * Initializes the strategy for a given player.
     * The starting strategy consists of a list of actions initialized with equal values summing to 1.
     *
     * @param infoSet the player's current information set.
     * @return the initial strategy.
     */
    Map<Action, Double> initializeStrategy(InformationSet infoSet) {
        //Optional<GameState> firstKey = gameTree.keySet().stream().findFirst();
        //rootNode = firstKey.map(informationSet -> gameTree.get(informationSet)).orElse(null);

        Map<Action, Double> strategy = new HashMap<>();
        List<Action> actions = game.getPlayerActions(infoSet.getPlayerIndex(), infoSet.getPossibleStates().get(0));
        double weight = 1.0 / actions.size();
        for (Action action : actions) {
            strategy.put(action, weight);
        }
        return strategy;
    }

    /**
     * Chooses an action within the player's strategy based on the probabilities.
     *
     * @param strategy the player's current strategy.
     * @return the chosen action.
     */
    Action selectAction(Map<Action, Double> strategy) {
        Random random = new Random();
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

    /**
     * Starts training the algorithm by performing the specified number of iterations.
     */
    public void train() {
        for (int i = 0; i < numIterations; i++) {
            trainIteration();
        }
    }

    /**
     * Trains the algorithm starting from the initial state by performing a single iteration.
     */
    private void trainIteration() {
        GameState initialState = game.getInitialState();
        trainRecursive(initialState, 1.0);
        updateRegrets();
    }

    /**
     * Recursively performs the training on the game tree, computing utilities and updating regrets.
     *
     * @param state  the current game state.
     * @param weight the weight for the current path.
     * @return the computed utility.
     */
    double trainRecursive(GameState state, double weight) {
        if (game.isTerminal(state)) {
            return weight * game.getUtility(state, agent.getPlayerIndex());
        }

        int playerIndex = state.getCurrentPlayer();
        InformationSet infoSet = state.getInformationSet(playerIndex);
        Map<Action, Double> strategy = getStrategy(infoSet);
        double utility = 0.0;

        for (Map.Entry<Action, Double> entry : strategy.entrySet()) {
            Action action = entry.getKey();
            double probability = entry.getValue();
            GameState nextState = game.getNextState(state, List.of(action));
            double nextUtility = trainRecursive(nextState, weight * probability);
            double regret = nextUtility - utility;
            updateRegret(infoSet, action, regret);
            utility += probability * nextUtility;
        }

        return utility;
    }

    /**
     * Updates the regret for a specific action in an information set.
     *
     * @param infoSet the player's current information set.
     * @param action  the action taken by the player.
     * @param regret  the regret value to update.
     */
    void updateRegret(InformationSet infoSet, Action action, double regret) {
        Map<Action, Double> regretMap = regretTable.computeIfAbsent(infoSet, k -> new HashMap<>());
        regretMap.put(action, regretMap.getOrDefault(action, 0.0) + regret);
    }

    /**
     * Updates the strategies for all information sets based on regret and weight.
     */
    void updateRegrets() {
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

    /**
     * Returns the regret table used by the algorithm.
     *
     * @return the regret table.
     */
    public Map<InformationSet, Map<Action, Double>> getRegretTable() {
        return regretTable;
    }
}