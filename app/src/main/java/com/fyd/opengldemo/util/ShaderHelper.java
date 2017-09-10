package com.fyd.opengldemo.util;

import android.opengl.GLES20;
import android.util.Log;

/**
 * Created by fyd225 on 2017/5/17.
 */

public class ShaderHelper {
    public static final String TAG = "ShaderHelper";

    public static int linkProgram(int vertexShaderId, int fragmentShaderId) {
        final int programObjectId = GLES20.glCreateProgram();
        if (programObjectId == 0) {
            // failed
            return 0;
        }
        GLES20.glAttachShader(programObjectId, vertexShaderId);
        checkGlError("glAttachShader");
        GLES20.glAttachShader(programObjectId, fragmentShaderId);
        checkGlError("glAttachShader");

        GLES20.glLinkProgram(programObjectId);

        final int[] linkStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_LINK_STATUS, linkStatus, 0);
        if (linkStatus[0] == GLES20.GL_FALSE) {
            // link failed
            GLES20.glDeleteProgram(programObjectId);
            Log.e(TAG, "Could not link program: ");
            Log.e(TAG, GLES20.glGetProgramInfoLog(programObjectId));
            return 0;
        }
        return programObjectId;
    }

    public static boolean validateProgram(int programObjectId) {
        GLES20.glValidateProgram(programObjectId);

        final int[] validateStatus = new int[1];
        GLES20.glGetProgramiv(programObjectId, GLES20.GL_VALIDATE_STATUS, validateStatus, 0);
        Log.i(TAG, "results of validating program: " + validateStatus[0]
                + "\nLog: " + GLES20.glGetProgramInfoLog(programObjectId));

        return validateStatus[0] != 0;
    }

    public static int compileVertexShader(String shaderCode) {
        return compileShader(GLES20.GL_VERTEX_SHADER, shaderCode);
    }

    public static int compileFragmentShader(String shaderCode) {
        return compileShader(GLES20.GL_FRAGMENT_SHADER, shaderCode);
    }

    private static int compileShader(int type, String shaderCode) {
        final int shaderObjectId = GLES20.glCreateShader(type);

        if (shaderObjectId == 0) {
            return 0;
        }

        GLES20.glShaderSource(shaderObjectId, shaderCode);
        GLES20.glCompileShader(shaderObjectId);
        checkGlError("glCompileShader");
        //
        final int[] compileStatus = new int[1];
        GLES20.glGetShaderiv(shaderObjectId, GLES20.GL_COMPILE_STATUS, compileStatus, 0);

        if (compileStatus[0] == GLES20.GL_FALSE) {
            //compile failed
            Log.e(TAG, "Could not compile shader " + type + ":");
            Log.e(TAG, GLES20.glGetShaderInfoLog(shaderObjectId));
            GLES20.glDeleteShader(shaderObjectId);
            return 0;
        }

        return shaderObjectId;
    }

    private static void checkGlError(String op) {
        int error;
        while ((error = GLES20.glGetError()) != GLES20.GL_NO_ERROR) {
            Log.e(TAG, op + ": glError " + error);
            throw new RuntimeException(op + ": glError " + error);
        }
    }

    public static int buildProgram(String vertexShaderSource, String fragmentShaderSource) {
        int vertexShader = compileVertexShader(vertexShaderSource);
        int fragmentShader = compileFragmentShader(fragmentShaderSource);

        int program = linkProgram(vertexShader, fragmentShader);
        validateProgram(program);
        return program;
    }
}
