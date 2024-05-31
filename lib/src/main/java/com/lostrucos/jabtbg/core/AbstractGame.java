package com.lostrucos.jabtbg.core;

import java.util.List;

public abstract class AbstractGame implements Game {
    private final int playerCount;

    protected AbstractGame(int playerCount) {
        this.playerCount = playerCount;
    }

    @Override
    public int getNumberOfPlayers() {
        return playerCount;
    }

    @Override
    public abstract GameState getInitialState();

    @Override
    public abstract List<Action> getPlayerActions(int player, GameState state);

    @Override
    public abstract GameState getNextState(GameState state, List<Action> actions);

    @Override
    public abstract boolean isTerminal(GameState state);

    @Override
    public abstract double getUtility(GameState state, int player);
}
