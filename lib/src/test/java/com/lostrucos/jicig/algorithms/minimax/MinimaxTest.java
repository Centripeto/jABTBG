/*
package com.lostrucos.jicig.algorithms.minimax;

import com.lostrucos.jicig.core.*;
import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;
import static org.mockito.Mockito.*;

public class MinimaxTest {
    private Minimax minimaxAlgorithm;
    private Game game;
    private Agent agent;
    private GameState state;

    @Before
    public void setup() {
        minimaxAlgorithm = new Minimax(3);
        game = mock(Game.class);
        agent = mock(Agent.class);
        state = mock(GameState.class);
        when(agent.getPlayerIndex()).thenReturn(0);
    }

    @Test
    public void testInitialize() {
        minimaxAlgorithm.initialize(game, agent);
        // Verifica che l'inizializzazione sia avvenuta correttamente
    }

    @Test
    public void testGetActionTerminalState() {
        when(state.getInformationSet(anyInt())).thenReturn(mock(InformationSet.class));
        when(state.getInformationSet(anyInt()).isTerminal()).thenReturn(true);
        when(state.getInformationSet(anyInt()).getUtility(anyInt())).thenReturn(10.0);

        minimaxAlgorithm.initialize(game, agent);
        Action action = minimaxAlgorithm.getAction(state);

        // Verifica che l'azione restituita sia null in uno stato terminale
        assertNull(action);
    }

    @Test
    public void testGetActionNonTerminalState() {
        Action action1 = mock(Action.class);
        Action action2 = mock(Action.class);
        List<Action> actions = new ArrayList<>();
        actions.add(action1);
        actions.add(action2);

        InformationSet infoSet = mock(InformationSet.class);
        when(state.getInformationSet(anyInt())).thenReturn(infoSet);
        when(infoSet.isTerminal()).thenReturn(false);
        when(infoSet.getPlayerIndex()).thenReturn(0);
        when(infoSet.getPlayerActions()).thenReturn(actions);

        GameState nextState1 = mock(GameState.class);
        GameState nextState2 = mock(GameState.class);
        InformationSet nextInfoSet1 = mock(InformationSet.class);
        InformationSet nextInfoSet2 = mock(InformationSet.class);

        when(action1.applyAction(state)).thenReturn(nextState1);
        when(action2.applyAction(state)).thenReturn(nextState2);
        when(nextState1.getInformationSet(anyInt())).thenReturn(nextInfoSet1);
        when(nextState2.getInformationSet(anyInt())).thenReturn(nextInfoSet2);
        when(nextInfoSet1.isTerminal()).thenReturn(false);
        when(nextInfoSet2.isTerminal()).thenReturn(false);
        when(nextInfoSet1.getUtility(anyInt())).thenReturn(5.0);
        when(nextInfoSet2.getUtility(anyInt())).thenReturn(10.0);
        when(nextInfoSet1.getPlayerIndex()).thenReturn(0);
        when(nextInfoSet2.getPlayerIndex()).thenReturn(0);

        minimaxAlgorithm.initialize(game, agent);
        Action chosenAction = minimaxAlgorithm.getAction(state);

        // Verifica che l'azione con il valore di utilità più alto venga scelta
        assertEquals(action2, chosenAction);
    }

    @Test
    public void testMaxDepthReached() {
        InformationSet infoSet = mock(InformationSet.class);
        when(state.getInformationSet(anyInt())).thenReturn(infoSet);
        when(infoSet.isTerminal()).thenReturn(false);
        when(infoSet.getPlayerIndex()).thenReturn(0);
        when(infoSet.getUtility(anyInt())).thenReturn(5.0);

        minimaxAlgorithm.initialize(game, agent);
        minimaxAlgorithm.setMaxDepth(0); // Imposta la profondità massima a 0
        Action chosenAction = minimaxAlgorithm.getAction(state);

        // Verifica che l'azione restituita sia null quando la profondità massima è raggiunta
        assertNull(chosenAction);
    }
}*/
