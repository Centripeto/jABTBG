/*
package com.lostrucos.jabtbg.algorithms.mcts;

import com.lostrucos.jabtbg.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ISMCTSAlgorithmTest {

    private Game mockGame;
    private Agent mockAgent;
    private GameState mockInitialState;
    private GameState mockNextState;
    private Action mockAction;
    private InformationSet mockInfoSet;
    private ISMCTSAlgorithm ismctsAlgorithm;

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
        when(mockInfoSet.getPossibleStates()).thenReturn(Collections.singletonList(mockInitialState));
        when(mockNextState.getInformationSet(anyInt())).thenReturn(mockInfoSet);
        when(mockAction.applyAction(any(GameState.class))).thenReturn(mockNextState);
        when(mockGame.getUtility(any(GameState.class), anyInt())).thenReturn(1.0);
        when(mockInitialState.isTerminalNode()).thenReturn(false);
        when(mockNextState.isTerminalNode()).thenReturn(true);

        ismctsAlgorithm = new ISMCTSAlgorithm(100, Math.sqrt(2));
        ismctsAlgorithm.initialize(mockGame, mockAgent);
    }

    @Test
    void testInitialize() {
        assertNotNull(ismctsAlgorithm.getRootNode());
        assertEquals(mockInitialState, ismctsAlgorithm.getRootNode().getState());
    }

    @Test
    void testChooseAction() {
        when(mockAgent.getPlayerIndex()).thenReturn(0);
        Action selectedAction = ismctsAlgorithm.chooseAction(mockInitialState);

        assertNotNull(selectedAction);

        verify(mockInitialState, atLeastOnce()).getInformationSet(0);
        verify(mockInfoSet, atLeastOnce()).getPlayerActions();
    }

    @Test
    void testUpdateAfterAction() {
        InformationSet mockInfoSet = mock(InformationSet.class);
        when(mockNextState.getInformationSet(anyInt())).thenReturn(mockInfoSet);

        ismctsAlgorithm.updateAfterAction(mockNextState, mockAction);
        ArgumentCaptor<GameState> stateCaptor = ArgumentCaptor.forClass(GameState.class);
        verify(mockNextState, times(1)).getInformationSet(anyInt());
    }

    @Test
    void testRunIteration() {
        MCTSNode rootNode = ismctsAlgorithm.getRootNode();
        ismctsAlgorithm.runIteration(rootNode);
        List<MCTSNode> selectedPath = ismctsAlgorithm.getSelectedPath();
        assertTrue(selectedPath.isEmpty());
    }

    @Test
    void testSelectLeafNode() {
        MCTSNode rootNode = ismctsAlgorithm.getRootNode();
        MCTSNode leafNode = ismctsAlgorithm.selectLeafNode(rootNode);
        assertNotNull(leafNode);
        assertEquals(rootNode, leafNode);
    }

    @Test
    void testExpandGameTree() {
        MCTSNode rootNode = ismctsAlgorithm.getRootNode();
        ismctsAlgorithm.expandGameTree(rootNode);
        assertFalse(rootNode.isLeaf());
        assertFalse(rootNode.getChildNodes().isEmpty());
    }

    @Test
    void testRandomPlayout() {
        MCTSNode rootNode = ismctsAlgorithm.getRootNode();
        ismctsAlgorithm.expandGameTree(rootNode);
        ismctsAlgorithm.randomPlayout(rootNode);
        List<MCTSNode> selectedPath = ismctsAlgorithm.getSelectedPath();
        assertFalse(selectedPath.isEmpty());
    }

    @Test
    void testBackpropagation() {
        MCTSNode rootNode = ismctsAlgorithm.getRootNode();
        ismctsAlgorithm.expandGameTree(rootNode);
        ismctsAlgorithm.randomPlayout(rootNode);
        ismctsAlgorithm.backpropagation();
        List<MCTSNode> selectedPath = ismctsAlgorithm.getSelectedPath();
        assertTrue(selectedPath.isEmpty());
    }
}

 */