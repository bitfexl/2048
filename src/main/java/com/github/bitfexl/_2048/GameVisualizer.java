package com.github.bitfexl._2048;

public interface GameVisualizer {
    /**
     * Update the visualization.
     * @param gameBoard The new game board/state to display.
     */
    void update(GameBoard gameBoard);
}
