package com.github.bitfexl._2048;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class GameSolverImpl implements GameSolver {
    @Override
    public Direction bestMove(GameBoard board) {
        final Object originalState = board.save();

        Direction bestMove = null;
        double bestScore = -1;

        for (Direction move : Direction.values()) {
            if (board.move(move)) {
                double score = getScore(board, 3, null);

                if (score > bestScore) {
                    bestScore = score;
                    bestMove = move;
                }

                board.load(originalState);
            }
        }

        return bestMove;
    }

    private double getScore(GameBoard board, int depth, Object save) {
        if (save == null) {
            save = board.save();
        }

        if (depth == 0) {
            return calculateScore(board);
        }

        double totalScore = 0;

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                if (board.get(x, y) != 0) {
                    continue;
                }

                double bestMoveScore = 0;

                for (Direction move : Direction.values()) {
                    board.move(move);

                    double moveScore = 0;

                    // generate 2 with 90% probability
                    if (board instanceof GameBoardImpl gbi) {
                        gbi.generateTileRaw(x, y, (byte)1, false);
                    } else {
                        board.generateTile(x, y, 2);
                    }
                    moveScore += 0.9 * getScore(board, depth - 1, null);

                    // generate 4 with 10% probability
                    if (board instanceof GameBoardImpl gbi) {
                        gbi.generateTileRaw(x, y, (byte)2, true);
                    } else {
                        board.load(save);
                        board.generateTile(x, y, 4);
                    }
                    moveScore += 0.1 * getScore(board, depth - 1, null);

                    bestMoveScore = Math.max(bestMoveScore, moveScore);

                    board.load(save);
                }

                totalScore += bestMoveScore;
            }
        }

        return totalScore;
    }

    private double getScore(GameBoard board, Object save) {
        // todo: integrate
        if (save == null) {
            save = board.save();
        }

        if (save instanceof byte[] bytes) {
            save = new ByteArrayWrapper(bytes);
        }

        Double score = scoresCache.get(save);
        if (score != null) {
            return score;
        }

        score = calculateScore(board);
        scoresCache.put(save, score);

        return score;
    }

    private double calculateScore(GameBoard board) {
        return board.getHighest();
    }

    private final Map<Object, Double> scoresCache = new HashMap<>();

    private record ByteArrayWrapper(byte[] value) {
        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            ByteArrayWrapper that = (ByteArrayWrapper) o;
            return Arrays.equals(value, that.value);
        }

        @Override
        public int hashCode() {
            return Arrays.hashCode(value);
        }
    }
}
