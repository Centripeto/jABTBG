package com.lostrucos.jabtbg.tris;

import com.lostrucos.jabtbg.core.Strategy;

import java.util.List;

// Strategia di base: considera solo la vittoria/sconfitta
public class BasicStrategy implements Strategy<TrisGameState, TrisAction> {
    @Override
    public double calculateUtility(TrisGameState state, int playerIndex) {
        int winner = state.checkForWinner();
        if (state.isTie()) return 0.5;
        if(state.getCurrentPlayer() == winner) {
            if (playerIndex == winner) {
                return 1.0;
            } else {
                return -1.0;
            }
        } else {
            if (playerIndex == winner) {
                return -1.0;
            } else {
                return 1.0;
            }
        }
    }

    @Override
    public List<TrisAction> suggestStrategicMoves(TrisGameState state, int currentPlayer) {
        return List.of();
    }
}
