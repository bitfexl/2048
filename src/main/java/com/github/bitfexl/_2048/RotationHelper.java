package com.github.bitfexl._2048;

public class RotationHelper {
    private RotationHelper() { }

    public static byte[][] rotateCW(byte[][] mat) {
        final int M = mat.length;
        final int N = mat[0].length;

        byte[][] ret = new byte[N][M];

        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
                ret[c][M-1-r] = mat[r][c];
            }
        }

        return ret;
    }

    public static byte[][] rotateCCW(byte[][] mat) {
        final int M = mat.length;
        final int N = mat[0].length;

        byte[][] ret = new byte[N][M];

        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
                ret[N-1-c][r] = mat[r][c];
            }
        }

        return ret;
    }

    public static byte[][] rotate180(byte[][] mat) {
        final int M = mat.length;
        final int N = mat[0].length;

        byte[][] ret = new byte[M][N];

        for (int r = 0; r < M; r++) {
            for (int c = 0; c < N; c++) {
                ret[M-1-r][N-1-c] = mat[r][c];
            }
        }

        return ret;
    }
}
