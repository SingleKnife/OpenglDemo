package com.fyd.opengldemo.object;

import com.fyd.opengldemo.data.ObjectBuilder;
import com.fyd.opengldemo.data.VertexArray;
import com.fyd.opengldemo.program.ColorShaderProgram;
import com.fyd.opengldemo.util.Cylinder;
import com.fyd.opengldemo.util.Point;

import java.util.List;

/**
 * Created by Dong on 2017/9/18.
 */

public class Puck {
    private static final int POSITION_COMPONENT_COUNT = 3;

    public final float radius, height;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Puck(float radius, float height, int numPointsAroundPuck) {
        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createPuck(
                new Cylinder(new Point(0, 0, 0), radius, height), numPointsAroundPuck);
        this.radius = radius;
        this.height = height;

        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public void bindDate(ColorShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(0, colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT, 0);
    }

    public void draw() {
        for(ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}
