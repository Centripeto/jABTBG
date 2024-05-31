package com.lostrucos.jabtbg.algorithms.mcts;

import com.lostrucos.jabtbg.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MCTSAlgorithmTest {

    private Game mockGame;
    private Agent mockAgent;
    private GameState mockInitialState;
    private GameState mockMiddleState;
    private GameState mockTerminalState;
    private Action mockAction;
    private Action mockAction2;
    private InformationSet mockInfoSet;
    private InformationSet mockInfoSet2;
    private MCTSAlgorithm mctsAlgorithm;

    @BeforeEach
    void setUp() {
        mockGame = mock(Game.class);
        mockAgent = mock(Agent.class);
        mockInitialState = mock(GameState.class);
        mockMiddleState = mock(GameState.class);
        mockTerminalState = mock(GameState.class);
        mockAction = mock(Action.class);
        mockAction2 = mock(Action.class);
        mockInfoSet = mock(InformationSet.class);
        mockInfoSet2 = mock(InformationSet.class);

        when(mockGame.getInitialState()).thenReturn(mockInitialState);
        when(mockInitialState.getInformationSet(anyInt())).thenReturn(mockInfoSet);
        when(mockInfoSet.getPlayerActions()).thenReturn(Collections.singletonList(mockAction));
        when(mockAction.applyAction(any(GameState.class))).thenReturn(mockMiddleState);

        when(mockMiddleState.getInformationSet(anyInt())).thenReturn(mockInfoSet2);
        when(mockInfoSet2.getPlayerActions()).thenReturn(Collections.singletonList(mockAction2));
        when(mockAction.applyAction(any(GameState.class))).thenReturn(mockTerminalState);

        when(mockGame.getUtility(any(GameState.class), anyInt())).thenReturn(1.0);
        when(mockInitialState.isTerminalNode()).thenReturn(false);
        when(mockMiddleState.isTerminalNode()).thenReturn(false);
        when(mockTerminalState.isTerminalNode()).thenReturn(true);

        mctsAlgorithm = new MCTSAlgorithm(100, Math.sqrt(2));
        mctsAlgorithm.initialize(mockGame, mockAgent);
    }

    @Test
    void testInitialize() {
        assertEquals(1, mctsAlgorithm.getGameTree().size());
        assertNotNull(mctsAlgorithm.getRootNode());
        assertEquals(mockInitialState, mockGame.getInitialState());
        assertEquals(mockInitialState, mctsAlgorithm.getRootNode().getState());
    }

    @Test
    void testChooseAction() {
        when(mockAgent.getPlayerIndex()).thenReturn(0);
        Action selectedAction = mctsAlgorithm.chooseAction(mockInitialState);

        assertNotNull(selectedAction);

        verify(mockInitialState, atLeastOnce()).getInformationSet(0);
        verify(mockInfoSet, atLeastOnce()).getPlayerActions();
    }

    @Test
    void testUpdateAfterAction() {
        InformationSet mockInfoSet = mock(InformationSet.class);
        when(mockTerminalState.getInformationSet(anyInt())).thenReturn(mockInfoSet);

        mctsAlgorithm.updateAfterAction(mockTerminalState, mockAction);
        ArgumentCaptor<GameState> stateCaptor = ArgumentCaptor.forClass(GameState.class);
        verify(mockTerminalState, times(1)).getInformationSet(anyInt());
    }

    @Test
    void testRunIteration() {
        MCTSNode rootNode = mctsAlgorithm.getRootNode();
        assertEquals(1, mctsAlgorithm.getGameTree().size());
        mctsAlgorithm.runIteration(rootNode);
        assertEquals(2, mctsAlgorithm.getGameTree().size());
    }

    @Test
    void testSelectLeafNode() {
        MCTSNode rootNode = mctsAlgorithm.getRootNode();
        MCTSNode middleNode = rootNode.getOrCreateChild(mockAction);
        MCTSNode leafNode = middleNode.getOrCreateChild(mockAction2);

        MCTSNode selectedNode = mctsAlgorithm.selectLeafNode(rootNode);

        assertNotNull(leafNode);
        assertTrue(leafNode.equals(selectedNode));
    }

    @Test
    void testExpandGameTree() {
        MCTSNode rootNode = mctsAlgorithm.getRootNode();
        mctsAlgorithm.expandGameTree(rootNode);
        assertFalse(rootNode.isLeaf());
        assertFalse(rootNode.getChildNodes().isEmpty());
    }

    @Test
    void testSimulation() {
        MCTSNode rootNode = mctsAlgorithm.getRootNode();
        assertEquals(1, mctsAlgorithm.getGameTree().size());
        mctsAlgorithm.expandGameTree(rootNode);
        assertEquals(2, mctsAlgorithm.getGameTree().size());
        mctsAlgorithm.simulation(rootNode);
        assertEquals(2, mctsAlgorithm.getGameTree().size());
    }

    @Test
    void testBackpropagationStep() {
        MCTSNode rootNode = mctsAlgorithm.getRootNode();
        MCTSNode leafNode = mctsAlgorithm.expandGameTree(rootNode);
        MCTSNode terminalNode = new MCTSNode(mockTerminalState, leafNode);

        assertEquals(0, rootNode.getVisitCount());
        assertEquals(0, leafNode.getVisitCount());
        assertEquals(0, terminalNode.getVisitCount());

        mctsAlgorithm.simulation(leafNode);
        mctsAlgorithm.backpropagationStep(terminalNode);

        assertEquals(1, terminalNode.getVisitCount());
        assertEquals(1, leafNode.getVisitCount());
        assertEquals(0, rootNode.getVisitCount());
    }
}