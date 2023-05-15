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
                double score = getScoreWithCache(board, 4);

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
                    moveScore += 0.9 * getScoreWithCache(board, depth - 1);

                    // generate 4 with 10% probability
                    if (board instanceof GameBoardImpl gbi) {
                        gbi.generateTileRaw(x, y, (byte)2, true);
                    } else {
                        board.load(save);
                        board.generateTile(x, y, 4);
                    }
                    moveScore += 0.1 * getScoreWithCache(board, depth - 1);

                    bestMoveScore = Math.max(bestMoveScore, moveScore);

                    board.load(save);
                }

                totalScore += bestMoveScore;
            }
        }

        return totalScore;
    }

    private double getScoreWithCache(GameBoard board, int depth) {
        final Object save = board.save();
        final Object cacheKey;

        if (save instanceof byte[] bytes) {
            cacheKey = new ByteArrayWrapper(bytes);
        } else {
            cacheKey = save;
        }

        Map<Integer, Double> scores = scoresCache.get(cacheKey);
        if (scores != null) {
            // depth needs to be the same for each move (left, up, right, down) because the score will increase with depth
            final Double score = scores.get(depth);
            if (score != null) {
                return score;
            }
        } else {
            scores = new HashMap<>();
            scoresCache.put(cacheKey, scores);
        }

        final double score = getScore(board, depth, save);

        scores.put(depth, score);

        return score;
    }

    private double calculateScore(GameBoard board) {
        return 2 * board.getHighest() + emptyFields(board);
    }

    /* ***** SCORE EVALUATORS ***** */
    private int emptyFields(GameBoard board) {
        int emptyFields = 0;

        for (int x = 0; x < board.getWidth(); x++) {
            for (int y = 0; y < board.getHeight(); y++) {
                if (board.get(x, y) == 0) {
                    emptyFields++;
                }
            }
        }

        return emptyFields;
    }

    // save object: depth: score
    private final Map<Object, Map<Integer, Double>> scoresCache = new HashMap<>();

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
