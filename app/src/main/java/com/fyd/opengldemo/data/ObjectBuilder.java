package com.fyd.opengldemo.data;

/**
 * Created by dong on 2017/9/12.
 */

public class ObjectBuilder {
    private static final int FLOATS_PER_VERTEX = 3;

    private final float[] vertexData;
    private int offset = 6;

    private ObjectBuilder(int sizeInVertices) {
        vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }

    private static int sizeOfCircleInVertices(int numPoints) {
        return 1 + (1 + numPoints);
    }
}
