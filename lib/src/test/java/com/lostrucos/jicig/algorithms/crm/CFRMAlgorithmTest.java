package com.lostrucos.jicig.algorithms.crm;

import com.lostrucos.jicig.core.*;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class CFRMAlgorithmTest {
    private static Game game;
    private static Agent agent;
    private static CFRMAlgorithm algorithm;

    @BeforeAll
    public static void setUp() {
        game = Mockito.mock(Game.class);
        agent = Mockito.mock(Agent.class);
        algorithm = new CFRMAlgorithm(10000, 0.5);
        algorithm.initialize(game, agent);
    }

    @Test
    public void testInitializeStrategy() {
        InformationSet infoSet = Mockito.mock(InformationSet.class);
        when(infoSet.getPlayerIndex()).thenReturn(0);
        when(infoSet.getPossibleStates()).thenReturn(Collections.singletonList(Mockito.mock(GameState.class)));
        List<Action> actions = Arrays.asList(Mockito.mock(Action.class), Mockito.mock(Action.class));

        // Cast esplicito per risolvere il problema del tipo generico
        when(game.getPlayerActions(eq(0), any(GameState.class))).thenReturn((List) actions);

        Map<Action, Double> strategy = algorithm.initializeStrategy(infoSet);

        assertEquals(2, strategy.size());
        double weight = 1.0 / actions.size();
        for (Double probability : strategy.values()) {
            assertEquals(weight, probability, 0.0001);
        }
    }

    @Test
    public void testChooseAction() {
        Map<Action, Double> strategy = Map.of(
                Mockito.mock(Action.class), 0.3,
                Mockito.mock(Action.class), 0.5,
                Mockito.mock(Action.class), 0.2
        );

        Action chosenAction = algorithm.chooseAction(strategy);
        assertTrue(strategy.containsKey(chosenAction));
    }

    @Test
    public void testTrainIteration() throws Exception {
        GameState initialState = Mockito.mock(GameState.class);
        when(game.getInitialState()).thenReturn(initialState);
        when(agent.getPlayerIndex()).thenReturn(0);

        // Create an InformationSet mock
        InformationSet infoSet = Mockito.mock(InformationSet.class);
        when(infoSet.getPlayerIndex()).thenReturn(0);
        when(infoSet.getPossibleStates()).thenReturn(Collections.singletonList(initialState));

        // Configures the initial state mock to return the infoSet
        when(initialState.getCurrentPlayer()).thenReturn(0);
        when(initialState.getInformationSet(0)).thenReturn(infoSet);

        // Creates a spy of CFRAlgorithm
        CFRMAlgorithm algorithmSpy = Mockito.spy(new CFRMAlgorithm(10000, 0.5));
        algorithmSpy.initialize(game, agent);

        // Calls the trainIteration method on the spy
        algorithmSpy.trainIteration();

        verify(algorithmSpy, times(1)).trainRecursive(initialState, 1.0);
        verify(algorithmSpy, times(1)).updateRegrets();
    }

    @Test
    public void testUpdateRegret() {
        InformationSet infoSet = Mockito.mock(InformationSet.class);
        Action action = Mockito.mock(Action.class);
        double regret = 0.5;

        algorithm.updateRegret(infoSet, action, regret);

        Map<Action, Double> regretMap = algorithm.getRegretTable().get(infoSet);
        assertNotNull(regretMap);
        assertEquals(regret, regretMap.get(action), 0.0001);
    }
}