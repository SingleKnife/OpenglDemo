package com.fyd.opengldemo.program;

import android.content.Context;
import android.opengl.GLES20;

import com.fyd.opengldemo.Constants;
import com.fyd.opengldemo.util.ShaderHelper;
import com.fyd.opengldemo.util.TextResourceReader;

/**
 * Created by dong on 2017/9/10.
 */

public class ShaderProgram {
    static final String U_MATRIX = "u_Matrix";
    static final String U_TEXTURE_UNIT = "u_TextureUnit";

    static final String A_POSITION = "a_Position";
    static final String A_TEXTURE_COORDINATES = "a_TextureCoordinates";

    static final String U_COLOR = "u_Color";

    final int program;

    ShaderProgram(Context context, int vertexShaderResourceId, int fragmentShaderResourceId) {
        program = ShaderHelper.buildProgram(
                TextResourceReader.readTextFileFromResource(context, vertexShaderResourceId),
                TextResourceReader.readTextFileFromResource(context, fragmentShaderResourceId));
    }

    public void useProgram() {
        GLES20.glUseProgram(program);
    }
}
