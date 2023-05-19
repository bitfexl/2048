package com.github.bitfexl._2048;

public interface GameBoard {

    /**
     * Make a move. Do not generate a new tile afterwards.
     * @param dir The direction in which to move.
     * @return true: move successful (board changed), false: board not changed;
     */
    boolean move(Direction dir);

    /**
     * Generate a tile.
     * @param x The x cord of the new tile.
     * @param y The y cord of the new tile.
     * @param value The value of the tile (2 or 4).
     * @return true: generated, false: field not empty;
     */
    boolean generateTile(int x, int y, int value);

    /**
     * Generate a new random tile.
     * @return true: generated, false: not generated (game over);
     */
    boolean generateNewTile();

    /**
     * Make a full move. Move and then generate a new tile.
     * @param dir The direction in which to move.
     * @return true: move successful (board changed), false: board not changed;
     */
    default boolean moveFull(Direction dir) {
        if (move(dir)) {
            return generateNewTile();
        }
        return false;
    }

    /**
     * Check if it is game over.
     * @return true: game over, false: not game over;
     */
    boolean isGameOver();

    /**
     * Save the board. The save object should implement
     * equals and hashCode. The score should also be saved.
     * @return A state object, load must accept the same object.
     */
    Object save();

    /**
     * Load a saved board. The state object should remain
     * untouched.
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

    /**
     * Get the boards score.
     * The score gets incremented every time two
     * tiles are merged by the value of the resulting tile.
     * @return The current score.
     */
    int getScore();

    /**
     * Set the boards score.
     * @param score The new score.
     */
    void setScore(int score);
}
