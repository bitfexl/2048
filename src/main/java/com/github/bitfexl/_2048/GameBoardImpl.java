package com.github.bitfexl._2048;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class GameBoardImpl implements GameBoard {
    /**
     * The board [y][x], saves the exponent with base 2,
     * except for 0 (empty), which is 0.
     */
    protected byte[][] board;

    private int score;

    public GameBoardImpl(int size) {
        if (size > Byte.MAX_VALUE) {
            throw new IllegalArgumentException("Size cannot be bigger than " + Byte.MAX_VALUE + ".");
        }

        board = new byte[size][size];

        // generate first two tiles
        generateNewTile();
        generateNewTile();
    }

    @Override
    public boolean move(Direction dir) {
        boolean changed;

        changed = compressTiles(dir);
        changed = mergeTiles(dir) || changed;
        changed = compressTiles(dir) || changed;

        return changed;
    }

    @Override
    public boolean generateTile(int x, int y, int value) {
        return generateTileRaw(x, y, (byte) (Math.log(value) / Math.log(2)), false);
    }

    /**
     * Generate a tile.
     * @param x The x cord of the new tile.
     * @param y The y cord of the new tile.
     * @param exponent The exponent with base 2 of the value.
     * @param override Override the field even if not empty. If set, will always return true.
     * @return true: generated, false: field not empty;
     */
    public boolean generateTileRaw(int x, int y, byte exponent, boolean override) {
        if (board[y][x] == 0) {
            board[y][x] = exponent;
            return true;
        }
        return false;
    }

    @Override
    public boolean generateNewTile() {
        final List<XY> available = new ArrayList<>();

        for (byte x = 0; x < board[0].length; x++) {
            for (byte y = 0; y < board.length; y++) {
                if (board[y][x] == 0) {
                    available.add(new XY(x, y));
                }
            }
        }

        if (available.isEmpty()) {
            return false;
        }

        final XY xy = available.get((int)(Math.random() * available.size()));
        // 10% chance of being a 4 else a 2
        board[xy.y][xy.x] = Math.random() <= 0.1 ? (byte) 2 : (byte) 1;

        return true;
    }

    @Override
    public boolean isGameOver() {
        for (byte[] row : board) {
            for (byte tile : row) {
                if (tile == 0) {
                    return false;
                }
            }
        }

        // try left first as it is the cheapest to compute
        for (Direction dir : new Direction[] { Direction.LEFT, Direction.UP, Direction.RIGHT, Direction.DOWN }) {
            final Object copy = save();

            if (move(dir)) {
                load(copy);
                return false;
            }
        }

        return true;
    }

    public record SaveObject(int score, ByteArrayWrapper board) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            SaveObject that = (SaveObject) o;
            return score == that.score && board.equals(that.board);
        }

        @Override
        public int hashCode() {
            return Objects.hash(score, board);
        }
    }

    @Override
    public Object save() {
        final byte[] copy = new byte[board.length * board[0].length];

        int i = 0;
        for (byte[] row : board) {
            for (byte field : row) {
                copy[i++] = field;
            }
        }

        return new SaveObject(score, new ByteArrayWrapper(copy));
    }

    @Override
    public void load(Object state) {
        SaveObject saveObject = (SaveObject) state;

        score = saveObject.score();

        final byte[] rawBoard = saveObject.board().value();
        final int w = board[0].length;

        for (int i = 0; i < rawBoard.length; i++) {
            board[i / w][i % w] = rawBoard[i];
        }
    }

    @Override
    public int get(int x, int y) {
        byte value = getRaw(x, y);
        if (value == 0) {
            return 0;
        }
        return (int) Math.pow(2, value);
    }

    /**
     * Get a tile.
     * @param x The x cord of the tile.
     * @param y The y cord of the tile.
     * @return The exponent with base 2 of the value or 0 if empty.
     */
    public byte getRaw(int x, int y) {
        return board[y][x];
    }

    @Override
    public int getHighest() {
        byte highest = 0;

        for (byte[] row : board) {
            for (byte tile : row) {
                highest = (byte) Math.max(highest, tile);
            }
        }

        // highest should never be 0 as there always has to be at least one 2 or 4 tile
        return (int) Math.pow(2, highest);
    }

    @Override
    public int getWidth() {
        return board[0].length;
    }

    @Override
    public int getHeight() {
        return board.length;
    }

    @Override
    public int getScore() {
        return score;
    }

    @Override
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * Move tiles.
     * @param dir The dir to move to.
     * @return true: changed, false: board unchanged;
     */
    protected boolean compressTiles(Direction dir) {
        boolean changed;

        switch (dir) {
            case LEFT -> {
                changed = compressLeft(board);
            }
            case RIGHT ->  {
                byte[][] rotated = RotationHelper.rotate180(board);
                changed = compressLeft(rotated);
                if (changed) {
                    board = RotationHelper.rotate180(rotated);
                }
            }
            case DOWN -> {
                byte[][] rotated = RotationHelper.rotateCW(board);
                changed = compressLeft(rotated);
                if (changed) {
                    board = RotationHelper.rotateCCW(rotated);
                }
            }
            case UP -> {
                byte[][] rotated = RotationHelper.rotateCCW(board);
                changed = compressLeft(rotated);
                if (changed) {
                    board = RotationHelper.rotateCW(rotated);
                }
            }
            default -> {
                throw new UnsupportedOperationException("Unknown direction " + dir + ".");
            }
        }

        return changed;
    }

    protected boolean compressLeft(byte[][] board) {
        boolean changed = false;

        for (byte[] row : board) {
            int destI = 0;

            for (int i = 0; i < row.length; i++) {
                changed = changed || row[destI] != row[i];
                row[destI] = row[i];

                if (destI != i) {
                    row[i] = 0;
                }

                if (row[destI] != 0) {
                    destI++;
                }
            }
        }

        return changed;
    }

    /**
     * Merge tiles.
     * @param dir The dir to move (merge) to.
     * @return true: changed, false: board unchanged;
     */
    protected boolean mergeTiles(Direction dir) {
        boolean changed;

        switch (dir) {
            case LEFT -> {
                changed = mergeLeft(board);
            }
            case RIGHT ->  {
                byte[][] rotated = RotationHelper.rotate180(board);
                changed = mergeLeft(rotated);
                if (changed) {
                    board = RotationHelper.rotate180(rotated);
                }
            }
            case DOWN -> {
                byte[][] rotated = RotationHelper.rotateCW(board);
                changed = mergeLeft(rotated);
                if (changed) {
                    board = RotationHelper.rotateCCW(rotated);
                }
            }
            case UP -> {
                byte[][] rotated = RotationHelper.rotateCCW(board);
                changed = mergeLeft(rotated);
                if (changed) {
                    board = RotationHelper.rotateCW(rotated);
                }
            }
            default -> {
                throw new UnsupportedOperationException("Unknown direction " + dir + ".");
            }
        }

        return changed;
    }

    protected boolean mergeLeft(byte[][] board) {
        boolean changed = false;

        for (byte[] row : board) {
            for (int i = 0; i < row.length - 1; i++) {
                if (row[i] != 0 && row[i] == row[i + 1]) {
                    changed = true;
                    row[i]++;
                    score += Math.pow(2, row[i]);
                    row[i + 1] = 0;
                    i++;
                }
            }
        }

        return changed;
    }

    private record XY(byte x, byte y) { }
}
