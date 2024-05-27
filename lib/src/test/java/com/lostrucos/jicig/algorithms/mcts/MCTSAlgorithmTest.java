package com.lostrucos.jicig.algorithms.mcts;

import com.lostrucos.jicig.core.*;
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
    private GameState mockNextState;
    private Action mockAction;
    private InformationSet mockInfoSet;
    private MCTSAlgorithm mctsAlgorithm;

    @BeforeEach
    void setUp() {
        mockGame = mock(Game.class);
        mockAgent = mock(Agent.class);
        mockInitialState = mock(GameState.class);
        mockNextState = mock(GameState.class);
        mockAction = mock(Action.class);
        mockInfoSet = mock(InformationSet.class);

        when(mockGame.getInitialState()).thenReturn(mockInitialState);
        when(mockInitialState.getInformationSet(anyInt())).thenReturn(mockInfoSet);
        when(mockInfoSet.getPlayerActions()).thenReturn(Collections.singletonList(mockAction));
        when(mockNextState.getInformationSet(anyInt())).thenReturn(mockInfoSet);
        when(mockAction.applyAction(any(GameState.class))).thenReturn(mockNextState);
        when(mockGame.getUtility(any(GameState.class), anyInt())).thenReturn(1.0);
        when(mockInitialState.isTerminalNode()).thenReturn(false);
        when(mockNextState.isTerminalNode()).thenReturn(true);

        mctsAlgorithm = new MCTSAlgorithm(100, Math.sqrt(2));
        mctsAlgorithm.initialize(mockGame, mockAgent);
    }

    @Test
    void testInitialize() {
        assertNotNull(mctsAlgorithm.getRootNode());
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
        when(mockNextState.getInformationSet(anyInt())).thenReturn(mockInfoSet);

        mctsAlgorithm.updateAfterAction(mockNextState, mockAction);
        ArgumentCaptor<GameState> stateCaptor = ArgumentCaptor.forClass(GameState.class);
        verify(mockNextState, times(1)).getInformationSet(anyInt());
    }

    @Test
    void testRunSimulation() {
        MCTSNode rootNode = mctsAlgorithm.getRootNode();
        mctsAlgorithm.runSimulation(rootNode);
        List<MCTSNode> selectedPath = mctsAlgorithm.getSelectedPath();
        assertTrue(selectedPath.isEmpty());
    }

    @Test
    void testSelectLeafNode() {
        MCTSNode rootNode = mctsAlgorithm.getRootNode();
        MCTSNode leafNode = mctsAlgorithm.selectLeafNode(rootNode);
        assertNotNull(leafNode);
        assertEquals(rootNode, leafNode);
    }

    @Test
    void testExpandGameTree() {
        MCTSNode rootNode = mctsAlgorithm.getRootNode();
        mctsAlgorithm.expandGameTree(rootNode);
        assertFalse(rootNode.isLeaf());
        assertFalse(rootNode.getChildNodes().isEmpty());
    }

    @Test
    void testRandomPlayout() {
        MCTSNode rootNode = mctsAlgorithm.getRootNode();
        mctsAlgorithm.expandGameTree(rootNode);
        mctsAlgorithm.randomPlayout(rootNode);
        List<MCTSNode> selectedPath = mctsAlgorithm.getSelectedPath();
        assertFalse(selectedPath.isEmpty());
    }

    @Test
    void testBackpropagation() {
        MCTSNode rootNode = mctsAlgorithm.getRootNode();
        mctsAlgorithm.expandGameTree(rootNode);
        mctsAlgorithm.randomPlayout(rootNode);
        mctsAlgorithm.backpropagation();
        List<MCTSNode> selectedPath = mctsAlgorithm.getSelectedPath();
        assertTrue(selectedPath.isEmpty());
    }
}