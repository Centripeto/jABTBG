package com.lostrucos.jabtbg.algorithms.crm;

import com.lostrucos.jabtbg.core.*;

import java.util.*;

/**
 * Implementation of the Counterfactual Regret Minimization (CFR) Algorithm.
 * This algorithm is used for finding approximate Nash equilibrium in games of imperfect-but-complete-information.
 */
public class CFRMAlgorithm<T extends GameState<E>, E extends Action> implements Algorithm<T, E> {
    private Game<T, E> game;
    private final int numIterations;
    private final double regretMatchingWeight;
    private Map<InformationSet<T, E>, Map<E, Double>> regretTable;
    private Map<InformationSet<T, E>, Map<E, Double>> strategyTable;
    private Strategy<T, E> strategy;

    private static final long TIME_LIMIT_MS = 10000; // 10 secondi

    /**
     * Constructs a new CFRMAlgorithm.
     *
     * @param numIterations the number of iterations for the training process.
     * @param regretMatchingWeight the weight used in the regret matching process.
     */
    public CFRMAlgorithm(int numIterations, double regretMatchingWeight) {
        this.numIterations = numIterations;
        this.regretMatchingWeight = regretMatchingWeight;
        this.regretTable = new HashMap<>();
        this.strategyTable = new HashMap<>();
    }

    @Override
    public void initialize(T state) {}

    @Override
    public void setStrategy(Strategy<T, E> strategy) {
        this.strategy = strategy;
    }

    @Override
    public void reset() {
        regretTable.clear();
        strategyTable.clear();
    }

    /**
     * Returns the appropriate action for the given game state chosen by the algorithm.
     *
     * @param state the current game state.
     * @return the chosen action.
     */
    @Override
    public E chooseAction(T state) {
        long startTime = System.currentTimeMillis();

        InformationSet<T, E> infoSet = game.getInformationSet(state.getCurrentPlayer(), state);
        Map<E, Double> strategy = getStrategy(infoSet);
        return selectAction(strategy);
    }

    @Override
    public void updateAfterAction(T state, E action) {train(state);}

    @Override
    public void applyPseudoAction(T state, E action) {game.getNextState(state, action);}

    /**
     * Chooses an action within the player's strategy based on the probabilities.
     *
     * @param strategy the player's current strategy.
     * @return the chosen action.
     */
    private E selectAction(Map<E, Double> strategy) {
        Random random = new Random();
        double randomValue = random.nextDouble();
        double cumulativeProbability = 0.0;
        for (Map.Entry<E, Double> entry : strategy.entrySet()) {
            cumulativeProbability += entry.getValue();
            if (randomValue < cumulativeProbability) {
                return entry.getKey();
            }
        }
        return strategy.keySet().iterator().next(); // Fallback to first action
    }

    /**
     * Starts training the algorithm by performing the specified number of iterations.
     */
    private void train(T initialState) {
        int iterations = 0;

        for (int i = 0; i < numIterations; i++) {
            //while(System.currentTimeMillis() - startTime < TIME_LIMIT_MS && iterations < numIterations) {
            //iterations++;
            for (int player = 0; player < 2; player++) {
                cfrm(initialState, player, 1.0, 1.0);
            }
        }
        //System.out.println("CFRMAlgorithm completed " + iterations + " iterations in " + (System.currentTimeMillis() - startTime) + "ms");
    }

    /**
     * Performs recursively all the operation of the cfrm algorithm and returns the utility.
     *
     * @return the utility value.
     */
    private double cfrm(T state, int player, double reachProbability, double opponentProbability) {
        if (state.isTerminalNode()) {
            return state.getUtility(player);
        }

        int currentPlayer = state.getCurrentPlayer();
        InformationSet<T, E> infoSet = game.getInformationSet(currentPlayer, state);

        Map<E, Double> strategy = getStrategy(infoSet);
        Map<E, Double> utilities = new HashMap<>();
        double expectedUtility = 0;

        for (E action : infoSet.getPlayerActions(currentPlayer)) {
            double newReachProb = (currentPlayer == player) ? reachProbability : opponentProbability;
            double actionProbability = strategy.get(action);
            T nextState = game.getNextState(state, action);
            double utility = cfrm(nextState, player, reachProbability * actionProbability, opponentProbability * actionProbability);
            utilities.put(action, utility);
            expectedUtility += actionProbability * utility;
        }

        if (currentPlayer == player) {
            for (E action : infoSet.getPlayerActions(currentPlayer)) {
                double regret = utilities.get(action) - expectedUtility;
                updateRegretSum(infoSet, action, regret * opponentProbability);
            }
        }

        return expectedUtility;
    }
    
    /**
     * Obtains the strategy for a given player.
     * If not already drafted, initializes a new strategy.
     *
     * @param infoSet the player's current information set.
     * @return the player's current strategy.
     */
    private Map<E, Double> getStrategy(InformationSet<T, E> infoSet) {
        Map<E, Double> strategy = strategyTable.get(infoSet);
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
    private Map<E, Double> initializeStrategy(InformationSet<T, E> infoSet) {
        Map<E, Double> strategy = new HashMap<>();
        double normalizingSum = 0;

        for (E action : infoSet.getPlayerActions(infoSet.getPlayerIndex())) {
            strategy.put(action, Math.max(regretTable.getOrDefault(infoSet, new HashMap<>()).getOrDefault(action, 0.0), 0.0));
            normalizingSum += strategy.get(action);
        }

        if (normalizingSum > 0) {
            for (E action : strategy.keySet()) {
                strategy.put(action, strategy.get(action) / normalizingSum);
            }
        } else {
            double uniformProbability = 1.0 / infoSet.getPlayerActions(infoSet.getPlayerIndex()).size();
            for (E action : infoSet.getPlayerActions(infoSet.getPlayerIndex())) {
                strategy.put(action, uniformProbability);
            }
        }

        for (E action : strategy.keySet()) {
            updateStrategySum(infoSet, action, strategy.get(action));
        }

        return strategy;
    }

    /**
     * Updates the regret for a specific action in an information set.
     *
     * @param infoSet the player's current information set.
     * @param action the action taken by the player.
     * @param regret the regret value to update.
     */
    private void updateRegretSum(InformationSet<T, E> infoSet, E action, double regret) {
        regretTable.computeIfAbsent(infoSet, k -> new HashMap<>()).merge(action, regret, Double::sum);
    }

    /**
     * Updates the strategy for a specific action in an information set.
     *
     * @param infoSet the player's current information set.
     * @param action the action taken by the player.
     * @param probability the regret value to update.
     */
    private void updateStrategySum(InformationSet<T, E> infoSet, E action, double probability) {
        strategyTable.computeIfAbsent(infoSet, k -> new HashMap<>()).merge(action, probability, Double::sum);
    }

    /**
     * Returns the regret table used by the algorithm.
     *
     * @return the regret table.
     */
    public Map<InformationSet<T, E>, Map<E, Double>> getRegretTable() {
        return regretTable;
    }
}