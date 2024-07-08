package com.lostrucos.jabtbg.simulations;

import com.lostrucos.jabtbg.core.Agent;
import com.lostrucos.jabtbg.core.Game;

import java.util.List;

public interface Simulator {
    void initialize(Game game, List<Agent> agents);
    SimulationResult simulate(int numIterations);
}