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

    /**
     * The method allows to return the appropriate player's Action for the given GameState
     * chosen by its algorithm by calling the chooseAction method.
     *
     * @param state the current player's GameState.
     * @return      the chosen Action to take.
     */
    @Override
    public Action getAction(GameState state) {
        int playerIndex = state.getPlayerToMove();
        InformationSet infoSet = state.getInformationSet(playerIndex);
        Map<Action, Double> strategy = getStrategy(infoSet);
        return chooseAction(strategy);
    }

    @Override
    public void updateAfterAction(GameState state, Action action) {
        // Nessuna operazione necessaria
    }

    /**
     * The method allows obtaining the strategy of a given player.
     * If not already drafted, the method will automatically initialize one.
     *
     * @param   infoSet the player's current InformationSet.
     * @return          the player's current strategy.
     */
    private Map<Action, Double> getStrategy(InformationSet infoSet) {
        Map<Action, Double> strategy = strategyTable.get(infoSet);
        if (strategy == null) {
            strategy = initializeStrategy(infoSet);
            strategyTable.put(infoSet, strategy);
        }
        return strategy;
    }

    /**
     * The method allows to initialize the strategy of a given player.
     * The starting strategy consists of a list of Actions initialized with an equal value between them
     * where the sum of their total weight is equal to 1.
     *
     * @param   infoSet the player's current InformationSet.
     * @return          the player's first strategy.
     */
    public Map<Action, Double> initializeStrategy(InformationSet infoSet) {
        Map<Action, Double> strategy = new HashMap<>();
        List<Action> actions = game.getPlayerActions(infoSet.getPlayerIndex(), infoSet.getPossibleStates().get(0));
        double weight = 1.0 / actions.size();
        for (Action action : actions) {
            strategy.put(action, weight);
        }
        return strategy;
    }

    /**
     * The method allows to choose the best Action within the player's strategy.
     * An Action may be present more than once within the player's strategy.
     * This will help the algorithm to choose that Action with greater probability.
     * If no action is chosen, the method throws an IllegalStateException exception.
     *
     * @param strategy the player's current strategy.
     * @return         the best Action chosen from the strategy.
     */
    public Action chooseAction(Map<Action, Double> strategy) {
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
     * This method allows to start training the algorithm by performing a number of iterations given by the amount stored in the variable iterations.
     */
    public void train() {
        for (int i = 0; i < iterations; i++) {
            trainIteration();
        }
    }

    /**
     * This method allows to train the algorithm starting from the beginning of the game by performing a singular iteration.
     * The method trains the algorithm calling two other methods: trainRecursive and updateRegrets.
     */
    public void trainIteration() {
        GameState initialState = game.getInitialState();
        trainRecursive(initialState, 1.0);
        updateRegrets();
    }

    /**
     * The method allows to perform recursion on the game tree, computes utilities, and updates the regret for each InformationSet and Action.
     *
     * @param state  the current player's GameState.
     * @param weight the given value for the weight.
     * @return       the utility value needed to calculate the new regret.
     */
    public double trainRecursive(GameState state, double weight) {
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

    /**
     * The method allows to update the regret for a specific action in an InformationSet.
     *
     * @param infoSet the player's current InformationSet.
     * @param action  the Action that will be taken by the player.
     * @param regret  the new amount of regret used to update the current one.
     */
    public void updateRegret(InformationSet infoSet, Action action, double regret) {
        Map<Action, Double> regretMap = regretTable.computeIfAbsent(infoSet, k -> new HashMap<>());
        regretMap.put(action, regretMap.getOrDefault(action, 0.0) + regret);
    }

    /**
     * Updates the strategies for all information sets by calculating the sum of regrets for each information set,
     * then updates the probabilities of actions based on regret and weight.
     */
    public void updateRegrets() {
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

    public Map<InformationSet, Map<Action, Double>> getRegretTable() {
        return regretTable;
    }
}