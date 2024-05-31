package com.lostrucos.jicig.algorithms.mcts;

import com.lostrucos.jicig.core.Action;
import com.lostrucos.jicig.core.GameState;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class MCTSNodeTest {

    private GameState mockState;
    private MCTSNode mockNode;
    private final double explorationConstant = Math.sqrt(2);

    @BeforeEach
    public void setUp() {
        mockState = mock(GameState.class);
        MCTSNode mockParentNode = mock(MCTSNode.class);
        mockNode = new MCTSNode(mockState, mockParentNode);
    }

    @Test
    public void testIsLeaf() {
        assertTrue(mockNode.getChildNodes().isEmpty());
        assertTrue(mockNode.isLeaf());

        Action mockAction1 = mock(Action.class);
        mockNode.getOrCreateChild(mockAction1);

        assertFalse(mockNode.isLeaf());
        assertFalse(mockNode.getChildNodes().isEmpty());
    }

    @Test
    public void testSetLeaf() {
        mockNode.setLeaf(false);
        assertFalse(mockNode.isLeaf());

        mockNode.setLeaf(true);
        assertTrue(mockNode.isLeaf());
    }

    @Test
    public void testIsTerminal() {
        when(mockState.isTerminalNode()).thenReturn(true);
        mockNode.setLeaf(true);
        assertTrue(mockNode.isTerminal());

        mockNode.setLeaf(false);
        assertFalse(mockNode.isTerminal());
    }

    @Test
    public void testGetOrCreateChild() {
        Action mockAction1 = mock(Action.class);
        MCTSNode mockChildNode1 = mockNode.getOrCreateChild(mockAction1);
        assertNotNull(mockChildNode1);
        assertSame(mockChildNode1, mockNode.getOrCreateChild(mockAction1));

        Action mockAction2 = mock(Action.class);
        MCTSNode childNode2 = mockNode.getOrCreateChild(mockAction2);
        assertNotNull(childNode2);
        assertNotSame(mockChildNode1, mockNode.getOrCreateChild(mockAction2));
    }

    @Test
    public void testSelectChild() {
        Action mockAction1 = mock(Action.class);
        Action mockAction2 = mock(Action.class);
        MCTSNode mockChildNode1 = mock(MCTSNode.class);
        MCTSNode mockChildNode2 = mock(MCTSNode.class);

        mockNode.getChildNodes().put(mockAction1, mockChildNode1);
        mockNode.getChildNodes().put(mockAction2, mockChildNode2);

        when(mockChildNode1.getTotalReward()).thenReturn(10.0);
        when(mockChildNode1.getVisitCount()).thenReturn(5);
        when(mockChildNode2.getTotalReward()).thenReturn(20.0);
        when(mockChildNode2.getVisitCount()).thenReturn(10);

        MCTSNode selectedChild = mockNode.selectChild(explorationConstant);
        assertEquals(mockChildNode1, selectedChild);
    }

    @Test
    public void testSelectBestAction() {
        Action mockAction1 = mock(Action.class);
        Action mockAction2 = mock(Action.class);
        MCTSNode mockChildNode1 = mock(MCTSNode.class);
        MCTSNode mockChildNode2 = mock(MCTSNode.class);

        mockNode.getChildNodes().put(mockAction1, mockChildNode1);
        mockNode.getChildNodes().put(mockAction2, mockChildNode2);

        when(mockChildNode1.getTotalReward()).thenReturn(10.0);
        when(mockChildNode1.getVisitCount()).thenReturn(5);
        when(mockChildNode2.getTotalReward()).thenReturn(20.0);
        when(mockChildNode2.getVisitCount()).thenReturn(10);

        Action bestAction = mockNode.selectBestAction();
        assertEquals(mockAction1, bestAction);
    }

    @Test
    public void testUpdateNodeStats() {
        double reward = 10.0;
        mockNode.updateNodeStats(reward);
        assertEquals(1, mockNode.getVisitCount());
        assertEquals(reward, mockNode.getTotalReward());

        mockNode.updateNodeStats(reward);
        assertEquals(2, mockNode.getVisitCount());
        assertEquals(20.0, mockNode.getTotalReward());
    }

    @Test
    public void testGetChildren() {
        assertTrue(mockNode.getChildNodes().isEmpty());
        Action mockAction1 = mock(Action.class);
        mockNode.getOrCreateChild(mockAction1);
        assertFalse(mockNode.getChildNodes().isEmpty());
    }
}