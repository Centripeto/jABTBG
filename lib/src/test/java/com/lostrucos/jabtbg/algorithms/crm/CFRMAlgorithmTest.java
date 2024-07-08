package com.lostrucos.jabtbg.algorithms.crm;

import com.lostrucos.jabtbg.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CFRMAlgorithmTest {
    private Game mockGame;
    private Player mockPlayer;
    private GameState mockInitialState;
    private GameState mockNextState;
    private Action mockAction;
    private InformationSet mockInfoSet;
    private CFRMAlgorithm cfrmAlgorithm;

    @BeforeEach
    void setUp() {
        mockGame = mock(Game.class);
        mockPlayer = mock(Player.class);
        mockInitialState = mock(GameState.class);
        mockNextState = mock(GameState.class);
        mockAction = mock(Action.class);
        mockInfoSet = mock(InformationSet.class);

        when(mockGame.getInitialState()).thenReturn(mockInitialState);
        when(mockGame.getPlayerActions(anyInt(), any(GameState.class))).thenReturn(Collections.singletonList(mockAction));
        when(mockGame.getNextState(any(GameState.class), anyList())).thenReturn(mockNextState);
        when(mockGame.getUtility(any(GameState.class), anyInt())).thenReturn(1.0);
        when(mockInitialState.getInformationSet(anyInt())).thenReturn(mockInfoSet);
        when(mockNextState.getInformationSet(anyInt())).thenReturn(mockInfoSet);
        when(mockInfoSet.getPlayerActions()).thenReturn(Collections.singletonList(mockAction));
        when(mockInitialState.isTerminalNode()).thenReturn(false);
        when(mockNextState.isTerminalNode()).thenReturn(true);

        cfrmAlgorithm = new CFRMAlgorithm(1000, 0.1);
        cfrmAlgorithm.initialize(mockGame, mockPlayer);
    }

    @Test
    void testInitialize() {
        assertNotNull(cfrmAlgorithm);
    }

    @Test
    void testChooseAction() {
        Action selectedAction = cfrmAlgorithm.chooseAction(mockInitialState);
        assertNotNull(selectedAction);
        verify(mockGame, atLeastOnce()).getPlayerActions(anyInt(), any(GameState.class));
    }

    @Test
    void testTrain() {
        cfrmAlgorithm.train();
        assertFalse(cfrmAlgorithm.getRegretTable().isEmpty());
    }

    @Test
    void testUpdateAfterAction() {
        // CFR does not require incremental updates after each action, so no operations are needed here.
    }

    @Test
    void testGetStrategy() {
        Map<Action, Double> strategy = cfrmAlgorithm.getStrategy(mockInfoSet);
        assertNotNull(strategy);
        assertEquals(1, strategy.size());
    }

    @Test
    void testInitializeStrategy() {
        Map<Action, Double> strategy = cfrmAlgorithm.initializeStrategy(mockInfoSet);
        assertNotNull(strategy);
        assertEquals(1.0, strategy.values().stream().mapToDouble(Double::doubleValue).sum());
    }

    @Test
    void testSelectActionFromStrategy() {
        Map<Action, Double> strategy = new HashMap<>();
        strategy.put(mockAction, 1.0);
        Action selectedAction = cfrmAlgorithm.selectAction(strategy);
        assertEquals(mockAction, selectedAction);
    }

    @Test
    void testTrainRecursive() {
        double utility = cfrmAlgorithm.trainRecursive(mockInitialState, 1.0);
        assertEquals(1.0, utility, 0.01);
    }

    @Test
    void testUpdateRegret() {
        cfrmAlgorithm.updateRegret(mockInfoSet, mockAction, 1.0);
        Map<Action, Double> regretMap = cfrmAlgorithm.getRegretTable().get(mockInfoSet);
        assertNotNull(regretMap);
        assertEquals(1.0, regretMap.get(mockAction));
    }

    @Test
    void testUpdateRegrets() {
        cfrmAlgorithm.trainRecursive(mockInitialState, 1.0);
        cfrmAlgorithm.updateRegrets();
        Map<InformationSet, Map<Action, Double>> regretTable = cfrmAlgorithm.getRegretTable();
        assertFalse(regretTable.isEmpty());
    }
}