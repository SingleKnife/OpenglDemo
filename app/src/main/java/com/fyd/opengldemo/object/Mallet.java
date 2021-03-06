package com.fyd.opengldemo.object;

import com.fyd.opengldemo.Constants;
import com.fyd.opengldemo.data.ObjectBuilder;
import com.fyd.opengldemo.data.VertexArray;
import com.fyd.opengldemo.program.ColorShaderProgram;
import com.fyd.opengldemo.util.Point;

import java.util.List;

import static android.opengl.GLES20.*;

/**
 * Created by dong on 2017/9/10.
 */

public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 3;

    private final float radius;
    private final float height;

    private final VertexArray vertexArray;
    private final List<ObjectBuilder.DrawCommand> drawList;

    public Mallet(float radius, float height, int numPointsAroundMallet) {
        this.radius = radius;
        this.height = height;

        ObjectBuilder.GeneratedData generatedData = ObjectBuilder.createMallet(new Point(0, 0, 0),
                radius, height, numPointsAroundMallet);
        vertexArray = new VertexArray(generatedData.vertexData);
        drawList = generatedData.drawList;
    }

    public void bindData(ColorShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(
                0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                0);
    }

    public void draw() {
        for(ObjectBuilder.DrawCommand drawCommand : drawList) {
            drawCommand.draw();
        }
    }
}
