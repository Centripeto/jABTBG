package com.lostrucos.jabtbg.tris;

import com.lostrucos.jabtbg.core.Player;
import com.lostrucos.jabtbg.core.Algorithm;
import com.lostrucos.jabtbg.core.Strategy;

public class MCTSPlayer implements Player<TrisGameState, TrisAction> {
    private final int id;
    private final Algorithm<TrisGameState, TrisAction> algorithm;

    public MCTSPlayer(int id, Algorithm<TrisGameState, TrisAction> algorithm, Strategy<TrisGameState, TrisAction> strategy) {
        this.id = id;
        this.algorithm = algorithm;
        this.algorithm.setStrategy(strategy);
    }

    @Override
    public int getPlayerIndex() {
        return id;
    }

    @Override
    public TrisAction getAction(TrisGameState state) {
        algorithm.initialize(state);
        return algorithm.chooseAction(state);
    }
}
