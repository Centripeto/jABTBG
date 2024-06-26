package com.lostrucos.jabtbg.algorithms.minimax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

import com.lostrucos.jabtbg.core.Action;
import com.lostrucos.jabtbg.core.Agent;
import com.lostrucos.jabtbg.core.Game;
import com.lostrucos.jabtbg.core.GameState;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mockito.*;

public class AlphaBetaPruningMinimaxAlgorithmTest {

    @Mock
    private static Game game;

    @Mock
    private static Agent agent;

    @Mock
    private GameState initialGameState;

    @Mock
    private GameState nextGameState;

    @Mock
    private Action action1;

    @Mock
    private Action action2;

    private static AlphaBetaPruningMinimaxAlgorithm alphaBetaPruningMinimaxAlgorithm;

    @BeforeAll
    public static void setUp() {
        alphaBetaPruningMinimaxAlgorithm = new AlphaBetaPruningMinimaxAlgorithm();
        alphaBetaPruningMinimaxAlgorithm.initialize(game, agent);
    }

    @Test
    @SuppressWarnings("unchecked")
    public void testChooseAction() {
        // Setup
        List<Action> possibleActions = Arrays.asList(action1, action2);

        // Cast esplicito per risolvere il problema del tipo generico
        when(game.getPlayerActions(eq(agent.getPlayerIndex()), any(GameState.class))).thenReturn((List) possibleActions);
        when(action1.applyAction(initialGameState)).thenReturn(nextGameState);
        when(action2.applyAction(initialGameState)).thenReturn(nextGameState);
        when(initialGameState.isTerminalNode()).thenReturn(false);
        when(nextGameState.isTerminalNode()).thenReturn(true);
        when(nextGameState.getUtility(agent.getPlayerIndex())).thenReturn(10.0, -10.0); // First call returns 10, second returns -10

        // Execute
        Action chosenAction = alphaBetaPruningMinimaxAlgorithm.chooseAction(initialGameState);

        // Verify
        assertEquals(action1, chosenAction);

        verify(game).getPlayerActions(agent.getPlayerIndex(), initialGameState);
        verify(action1).applyAction(initialGameState);
        verify(action2).applyAction(initialGameState);
        verify(nextGameState, times(2)).isTerminalNode();
        verify(nextGameState, times(2)).getUtility(agent.getPlayerIndex());
    }

    @Test
    public void testAlphaBetaMinimaxMaxValue() {
        // Setup
        when(initialGameState.isTerminalNode()).thenReturn(true);
        when(initialGameState.getUtility(agent.getPlayerIndex())).thenReturn(42.0);

        // Execute
        double value = alphaBetaPruningMinimaxAlgorithm.alphaBetaMinimax(initialGameState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, true);

        // Verify
        assertEquals(42, value, 0.01);

        verify(initialGameState).isTerminalNode();
        verify(initialGameState).getUtility(agent.getPlayerIndex());
    }

    @Test
    public void testAlphaBetaMinimaxMinValue() {
        // Setup
        when(initialGameState.isTerminalNode()).thenReturn(true);
        when(initialGameState.getUtility(agent.getPlayerIndex())).thenReturn(-42.0);

        // Execute
        double value = alphaBetaPruningMinimaxAlgorithm.alphaBetaMinimax(initialGameState, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY, false);

        // Verify
        assertEquals(-42, value, 0.01);

        verify(initialGameState).isTerminalNode();
        verify(initialGameState).getUtility(agent.getPlayerIndex());
    }
}