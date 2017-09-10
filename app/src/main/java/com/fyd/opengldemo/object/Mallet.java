package com.fyd.opengldemo.object;

import com.fyd.opengldemo.Constants;
import com.fyd.opengldemo.data.VertexArray;
import com.fyd.opengldemo.program.ColorShaderProgram;

import static android.opengl.GLES20.*;

/**
 * Created by dong on 2017/9/10.
 */

public class Mallet {
    private static final int POSITION_COMPONENT_COUNT = 2;
    private static final int COLOR_COMPONENT_COUNT = 3;
    private static final int STRIDE = (POSITION_COMPONENT_COUNT + COLOR_COMPONENT_COUNT) * Constants.BYTE_PER_FLOAT;

    private final float[] VERTEX_DATA = {
            0f, -0.4f, 0f, 0f, 1f,
            0f,  0.4f, 1f, 0f, 0f };

    private final VertexArray vertexArray;

    public Mallet() {
        vertexArray = new VertexArray(VERTEX_DATA);
    }

    public void bindData(ColorShaderProgram colorProgram) {
        vertexArray.setVertexAttribPointer(0,
                colorProgram.getPositionAttributeLocation(),
                POSITION_COMPONENT_COUNT,
                STRIDE);
        vertexArray.setVertexAttribPointer(
                POSITION_COMPONENT_COUNT,
                colorProgram.getColorAttributeLocation(),
                COLOR_COMPONENT_COUNT,
                STRIDE);
    }

    public void draw() {
        glDrawArrays(GL_POINTS, 0, 2);
    }
}
