package enger.javagl.render;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import util.ResourceUtils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Renderer implements GLEventListener {

    private int VAO;
    private int VBO;

    private int shaderProgram;

    @Override
    public void init(GLAutoDrawable drawable) {
        final GL4 gl = drawable.getGL().getGL4();

        gl.glClearColor(0.3f, 0.3f, 0.3f, 1);

        //Buffers
        {
            IntBuffer VAOBuffer = Buffers.newDirectIntBuffer(1);
            gl.glGenVertexArrays(1, VAOBuffer);
            VAO = VAOBuffer.get(0);
            gl.glBindVertexArray(VAO);

            float[] vertices = {
                    +0.0f, +0.5f,
                    +0.5f, -0.5f,
                    -0.5f, -0.5f
            };

            IntBuffer VBOBuffer = Buffers.newDirectIntBuffer(1);
            gl.glGenBuffers(1, VBOBuffer);
            VBO = VBOBuffer.get(0);
            gl.glBindBuffer(gl.GL_ARRAY_BUFFER, VBO);

            FloatBuffer buffer = Buffers.newDirectFloatBuffer(vertices);
            gl.glBufferData(gl.GL_ARRAY_BUFFER, buffer.limit() * Buffers.SIZEOF_FLOAT, buffer, GL.GL_STATIC_DRAW);
        }

        //Shaders
        try {
            String vertexSource = ResourceUtils.loadStringResource("./shaders/vertex.glsl");
            String fragmentSource = ResourceUtils.loadStringResource("./shaders/fragment.glsl");
            int vertexShader = createShader(gl, gl.GL_VERTEX_SHADER, vertexSource);
            int fragmentShader = createShader(gl, gl.GL_FRAGMENT_SHADER, fragmentSource);

            shaderProgram = gl.glCreateProgram();
            gl.glAttachShader(shaderProgram, vertexShader);
            gl.glAttachShader(shaderProgram, fragmentShader);

            gl.glBindFragDataLocation(shaderProgram, 0, "outColor");

            gl.glLinkProgram(shaderProgram);

            gl.glUseProgram(shaderProgram);

            int posAttrib = gl.glGetAttribLocation(shaderProgram, "position");
            gl.glVertexAttribPointer(posAttrib, 2, gl.GL_FLOAT, false, 0, 0);
            gl.glEnableVertexAttribArray(posAttrib);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private int createShader(GL4 gl, int type, String src) {
        int shader = gl.glCreateShader(type);
        gl.glShaderSource(shader, 1, new String[]{src}, null);
        gl.glCompileShader(shader);

        // DEBUG
        {
            ByteBuffer buffer = Buffers.newDirectByteBuffer(512);
            buffer.order(ByteOrder.nativeOrder());

            gl.glGetShaderInfoLog(shader, 512, null, buffer);
            byte[] bytes = new byte[512];
            buffer.get(bytes);
            System.out.println(new String(bytes));
        }
        return shader;
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        System.out.println("dispose");
    }

    @Override
    public void display(GLAutoDrawable drawable) {
        final GL4 gl = drawable.getGL().getGL4();

        gl.glClear(GL.GL_COLOR_BUFFER_BIT);
        gl.glDrawArrays(gl.GL_POINTS, 0, 3);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL4 gl = drawable.getGL().getGL4();
        gl.glViewport(0, 0, width, height);
    }
}
