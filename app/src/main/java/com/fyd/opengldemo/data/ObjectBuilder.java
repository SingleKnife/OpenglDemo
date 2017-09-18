package com.fyd.opengldemo.data;

import android.opengl.GLES20;

import com.fyd.opengldemo.util.Circle;
import com.fyd.opengldemo.util.Cylinder;
import com.fyd.opengldemo.util.Point;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dong on 2017/9/12.
 */

public class ObjectBuilder {
    private static final int FLOATS_PER_VERTEX = 3;

    private final float[] vertexData;
    private int offset = 0;

    private List<DrawCommand> drawList = new ArrayList<>();

    private ObjectBuilder(int sizeInVertices) {
        vertexData = new float[sizeInVertices * FLOATS_PER_VERTEX];
    }

    private static int sizeOfCircleInVertices(int numPoints) {
        return 1 + (1 + numPoints);
    }

    private static int sizeOfOpenCylinderInVertices(int numPoints) {
        return (numPoints + 1) * 2;
    }

    private void appendCircle(Circle circle, int numPoints) {
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfCircleInVertices(numPoints);

        vertexData[offset++] = circle.center.x;
        vertexData[offset++] = circle.center.y;
        vertexData[offset++] = circle.center.z;

        for(int i = 0; i <= numPoints; ++i) {
            float angleInRadians = ((float)i / (float)numPoints) * 2f * (float)Math.PI;
            vertexData[offset++] = circle.center.x + circle.radius * (float)Math.cos(angleInRadians);
            vertexData[offset++] = circle.center.y;
            vertexData[offset++] = circle.center.z + circle.radius * (float)Math.sin(angleInRadians);
        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_FAN, startVertex, numVertices);
            }
        });
    }

    private void appendOpenCylinder(Cylinder cylinder, final int numPoints) {
        final int startVertex = offset / FLOATS_PER_VERTEX;
        final int numVertices = sizeOfOpenCylinderInVertices(numPoints);

        final float yStart = cylinder.center.y - cylinder.height / 2;
        final float yEnd = cylinder.center.y + cylinder.height / 2;

        for(int i = 0; i <= numPoints; ++i) {
            float angleInRadians = ((float)i / (float)numPoints) * 2f * (float)Math.PI;
            float xPosition = cylinder.center.x + cylinder.radius * (float)Math.cos(angleInRadians);
            float zPosition = cylinder.center.z + cylinder.radius * (float)Math.sin(angleInRadians);

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yStart;
            vertexData[offset++] = zPosition;

            vertexData[offset++] = xPosition;
            vertexData[offset++] = yEnd;
            vertexData[offset++] = zPosition;
        }

        drawList.add(new DrawCommand() {
            @Override
            public void draw() {
                GLES20.glDrawArrays(GLES20.GL_TRIANGLE_STRIP, startVertex, numVertices);
            }
        });
    }

    public static GeneratedData createPuck(Cylinder puck, int numPointers) {
        int size = sizeOfCircleInVertices(numPointers) + sizeOfOpenCylinderInVertices(numPointers);
        ObjectBuilder objectBuilder = new ObjectBuilder(size);

        objectBuilder.appendCircle(new Circle(puck.center.translateY(puck.height / 2f), puck.radius)
                , numPointers);
        objectBuilder.appendOpenCylinder(puck, numPointers);

        return objectBuilder.build();
    }

    public static GeneratedData createMallet(Point center, float radius, float height, int numPoints) {
        int size = sizeOfCircleInVertices(numPoints) * 2
                + sizeOfOpenCylinderInVertices(numPoints);
        ObjectBuilder builder = new ObjectBuilder(size);

        float baseHeight = height * 0.25f;
        Circle baseCircle = new Circle(center.translateY(-baseHeight), radius);
        Cylinder baseCylinder = new Cylinder(baseCircle.center.translateY(-baseHeight),
                radius, baseHeight);
        builder.appendCircle(baseCircle, numPoints);
        builder.appendOpenCylinder(baseCylinder, numPoints);

        float handleHeight = height * height * 0.75f;
        float handleRadius = radius / 3f;

        Circle handleCircle = new Circle(center.translateY(height * 0.5f), handleRadius);
        Cylinder handleCylinder = new Cylinder(handleCircle.center.translateY(-handleHeight / 2),
                handleRadius, handleHeight);

        builder.appendCircle(handleCircle, numPoints);
        builder.appendOpenCylinder(handleCylinder, numPoints);

        return builder.build();
    }

    private GeneratedData build() {
        return new GeneratedData(vertexData, drawList);
    }

    public interface DrawCommand {
        void draw();
    }

    public static class GeneratedData {
        public final float[] vertexData;
        public final List<DrawCommand> drawList;

        GeneratedData(float[] vertexData, List<DrawCommand> drawList) {
            this.vertexData = vertexData;
            this.drawList = drawList;
        }
    }

}
