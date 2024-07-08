package com.lostrucos.jabtbg.tris;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BoardTest {
    private Board board;
    @BeforeEach
    public void setUp(){
        board = new Board();
    }

    @Test
    public void testBoardIsInitialized(){
        for(int i=0; i<3; i++){
            for(int j=0; j<3; j++){
                assertEquals(board.getBoard()[i][j], Symbol.FREE);
            }
        }
    }

    @Test
    public void display(){
        board.display();
    }

    @Test
    public void applyModification(){
        board.setSymbol(1,2, Symbol.CIRCLE);
        board.display();
        assertEquals(board.getBoard()[1][2], Symbol.CIRCLE);
    }

}
