package com.github.bitfexl._2048;

public interface GameBoard {

    /**
     * Make a move.
     * @param dir The direction in which to move.
     * @return true: move successful (board changed), false: board not changed;
     */
    boolean move(Direction dir);

    /**
     * Check if it is game over.
     * @return true: game over, false: not game over;
     */
    boolean isGameOver();

    /**
     * Save the board.
     * @return A state object, load must accept the same object.
     */
    Object save();

    /**
     * Load a saved board.
     * @param state The state object returned by save.
     */
    void load(Object state);

    /**
     * Get the number at a given cell.
     * 0, 0 is top left.
     * @param x The x cord (horizontal).
     * @param y The y cord (vertical).
     * @return The number or 0 if empty.
     */
    int get(int x, int y);

    /**
     * Get the highest tile.
     * @return The value of the highest tile.
     */
    int getHighest();

    /**
     * The width (x).
     * @return The game board with in tiles.
     */
    int getWidth();

    /**
     * The height (y).
     * @return The game board height in tiles.
     */
    int getHeight();
}
