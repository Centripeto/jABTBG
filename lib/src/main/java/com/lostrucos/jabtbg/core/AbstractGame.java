package com.lostrucos.jabtbg.core;

import java.util.List;

public abstract class AbstractGame<T extends GameState<E>, E extends Action> implements Game<T,E> {
    private final int playerCount;

    protected AbstractGame(int playerCount) {
        this.playerCount = playerCount;
    } //perchè protected?devo capire

    @Override
    public int getNumberOfPlayers() {
        return playerCount;
    }

    @Override
    public abstract T getInitialState();

    @Override
    public abstract List<E> getPlayerActions(int player, T state);

    @Override
    public abstract T getNextState(T state, List<E> actions);

    @Override
    public abstract boolean isTerminal(T state);

    @Override
    public abstract double getUtility(T state, int player);
}
