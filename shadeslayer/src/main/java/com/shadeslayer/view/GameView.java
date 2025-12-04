package com.shadeslayer.view;

import com.shadeslayer.model.GameState;

public interface GameView {

    void updateGameState(GameState gameState);

    String getNextInput();
}
