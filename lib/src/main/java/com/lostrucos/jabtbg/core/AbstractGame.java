package com.lostrucos.jabtbg.core;

public abstract class AbstractGame<T extends GameState<E>, E extends Action> implements Game<T, E> {
    private final int playerCount;

    protected AbstractGame(int playerCount) {
        this.playerCount = playerCount;
    }
}
