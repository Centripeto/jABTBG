package com.lostrucos.jabtbg.algorithms.mcts;

import com.lostrucos.jabtbg.core.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ISMCTSNodeTest {
    private InformationSet informationSet;
    private ISMCTSNode parentNode;
    private ISMCTSNode node;
    private Action action;

    @BeforeEach
    public void setUp() {
        informationSet = mock(InformationSet.class);
        parentNode = mock(ISMCTSNode.class);
        node = new ISMCTSNode(informationSet, parentNode);
        action = mock(Action.class);

        when(informationSet.getPlayerActions()).thenReturn(Collections.singletonList(action));
        when(informationSet.getNextInformationSet(any())).thenReturn(informationSet);
    }

    @Test
    public void testGetOrCreateChild() {
        ISMCTSNode childNode = node.getOrCreateChild(action);
        assertNotNull(childNode);
        assertEquals(childNode, node.getOrCreateChild(action));
    }

    @Test
    public void testSelectChild() {
        ISMCTSNode childNode = mock(ISMCTSNode.class);
        when(childNode.getUCBValue(anyDouble())).thenReturn(1.0);
        node.getChildNodes().put(action, childNode);

        ISMCTSNode selectedNode = node.selectChild(1.4);
        assertEquals(childNode, selectedNode);
    }

    @Test
    public void testSelectBestAction() {
        ISMCTSNode childNode = mock(ISMCTSNode.class);
        when(childNode.getTotalReward()).thenReturn(10.0);
        when(childNode.getVisitCount()).thenReturn(2);
        node.getChildNodes().put(action, childNode);

        Action bestAction = node.selectBestAction();
        assertEquals(action, bestAction);
    }

    @Test
    public void testUpdateNodeStats() {
        node.updateNodeStats(5.0);
        assertEquals(1, node.getVisitCount());
        assertEquals(5.0, node.getTotalReward());
    }

    @Test
    public void testGetInformationSet() {
        assertEquals(informationSet, node.getInformationSet());
    }

    @Test
    public void testGetParentNode() {
        assertEquals(parentNode, node.getParentNode());
    }

    @Test
    public void testSetParentNode() {
        ISMCTSNode newParentNode = mock(ISMCTSNode.class);
        node.setParentNode(newParentNode);
        assertEquals(newParentNode, node.getParentNode());
    }

    @Test
    public void testGetChildNodes() {
        assertNotNull(node.getChildNodes());
        assertTrue(node.getChildNodes().isEmpty());
    }
}