package com.iafenvoy.elb.gui;

import java.util.Random;

public class Shape {
    private Tetrominoe pieceShape;
    private int[][] coords;
    private int[][][] coordsTable;
    public Shape() {
        this.initShape();
    }

    private void initShape() {
        this.coords = new int[4][2];
        this.coordsTable = new int[][][]{
                {{0, 0}, {0, 0}, {0, 0}, {0, 0}},
                {{0, -1}, {0, 0}, {-1, 0}, {-1, 1}},
                {{0, -1}, {0, 0}, {1, 0}, {1, 1}},
                {{0, -1}, {0, 0}, {0, 1}, {0, 2}},
                {{-1, 0}, {0, 0}, {1, 0}, {0, 1}},
                {{0, 0}, {1, 0}, {0, 1}, {1, 1}},
                {{-1, -1}, {0, -1}, {0, 0}, {0, 1}},
                {{1, -1}, {0, -1}, {0, 0}, {0, 1}}
        };
        this.setShape(Tetrominoe.NoShape);
    }

    private void setX(int index, int x) {
        this.coords[index][0] = x;
    }

    private void setY(int index, int y) {
        this.coords[index][1] = y;
    }

    public int x(int index) {
        return this.coords[index][0];
    }

    public int y(int index) {
        return this.coords[index][1];
    }

    protected Tetrominoe getShape() {
        return this.pieceShape;
    }

    protected void setShape(Tetrominoe shape) {
        for (int i = 0; i < 4; i++)
            System.arraycopy(this.coordsTable[shape.ordinal()][i], 0, this.coords[i], 0, 2);
        this.pieceShape = shape;
    }

    public void setRandomShape() {
        Random r = new Random();
        int x = Math.abs(r.nextInt()) % 7 + 1;
        Tetrominoe[] values = Tetrominoe.values();
        this.setShape(values[x]);
    }

    public int minX() {
        int m = this.coords[0][0];
        for (int i = 0; i < 4; i++)
            m = Math.min(m, this.coords[i][0]);
        return m;
    }

    public int minY() {
        int m = this.coords[0][1];
        for (int i = 0; i < 4; i++)
            m = Math.min(m, this.coords[i][1]);
        return m;
    }

    public Shape rotateLeft() {
        if (this.pieceShape == Tetrominoe.SquareShape)
            return this;
        Shape result = new Shape();
        result.pieceShape = this.pieceShape;
        for (int i = 0; i < 4; ++i) {
            result.setX(i, this.y(i));
            result.setY(i, -this.x(i));
        }
        return result;
    }

    public Shape rotateRight() {
        if (this.pieceShape == Tetrominoe.SquareShape)
            return this;
        Shape result = new Shape();
        result.pieceShape = this.pieceShape;
        for (int i = 0; i < 4; ++i) {
            result.setX(i, -this.y(i));
            result.setY(i, this.x(i));
        }
        return result;
    }

    protected enum Tetrominoe {
        NoShape, ZShape, SShape, LineShape,
        TShape, SquareShape, LShape, MirroredLShape
    }
}
