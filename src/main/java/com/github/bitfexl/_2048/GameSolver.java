package com.github.bitfexl._2048;

public interface GameSolver {
    /**
     * Calculate the best move for a given board.
     * Can throw an exception or return null if no move could be found (e.g. game over).
     * @param board The board to calculate the best move for, must be left unchanged,
     *              or at least be the same after calculation.
     * @return The best move in the current position or null.
     */
    Direction bestMove(GameBoard board);
}
