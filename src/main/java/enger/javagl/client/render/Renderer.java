package enger.javagl.client.render;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.*;
import enger.javagl.Main;
import enger.javagl.client.Client;
import enger.javagl.client.gameplay.Camera;
import enger.javagl.client.gameplay.Chunk;
import enger.javagl.util.ResourceUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

/**
 * Class performing rendering.
 */

@SuppressWarnings("FieldCanBeLocal")
public class Renderer implements GLEventListener {

    public boolean updateCamera = false;
    public boolean updateWireframe = false;
    public boolean updateChunk = true;

    private boolean wireframe = false;
    private int chunkVertexCount;

    private int VAO;
    private int VBO;

    private int shaderProgram;

    @Override
    public void init(GLAutoDrawable drawable) {
        final GL4 gl = drawable.getGL().getGL4();

        gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
        gl.glEnable(gl.GL_DEPTH_TEST);

        //wireframe
//        gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_LINE);

        gl.glEnable(gl.GL_CULL_FACE);
        gl.glFrontFace(gl.GL_CW);

        //Buffers
        {
            IntBuffer VAOBuffer = Buffers.newDirectIntBuffer(1);
            gl.glGenVertexArrays(1, VAOBuffer);
            VAO = VAOBuffer.get(0);
            gl.glBindVertexArray(VAO);

            /*
            float[] vertices = { // cube
                    // Back face
                    -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, // Bottom-left
                    0.5f, -0.5f, -0.5f,  1.0f, 0.0f, // bottom-right
                    0.5f,  0.5f, -0.5f,  1.0f, 1.0f, // top-right
                    0.5f,  0.5f, -0.5f,  1.0f, 1.0f, // top-right
                    -0.5f,  0.5f, -0.5f,  0.0f, 1.0f, // top-left
                    -0.5f, -0.5f, -0.5f,  0.0f, 0.0f, // bottom-left
                    // Front face
                    -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, // bottom-left
                    0.5f,  0.5f,  0.5f,  1.0f, 1.0f, // top-right
                    0.5f, -0.5f,  0.5f,  1.0f, 0.0f, // bottom-right
                    0.5f,  0.5f,  0.5f,  1.0f, 1.0f, // top-right
                    -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, // bottom-left
                    -0.5f,  0.5f,  0.5f,  0.0f, 1.0f, // top-left
                    // Left face
                    -0.5f,  0.5f,  0.5f,  1.0f, 0.0f, // top-right
                    -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, // bottom-left
                    -0.5f,  0.5f, -0.5f,  1.0f, 1.0f, // top-left
                    -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, // bottom-left
                    -0.5f,  0.5f,  0.5f,  1.0f, 0.0f, // top-right
                    -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, // bottom-right
                    // Right face
                    0.5f,  0.5f,  0.5f,  1.0f, 0.0f, // top-left
                    0.5f,  0.5f, -0.5f,  1.0f, 1.0f, // top-right
                    0.5f, -0.5f, -0.5f,  0.0f, 1.0f, // bottom-right
                    0.5f, -0.5f, -0.5f,  0.0f, 1.0f, // bottom-right
                    0.5f, -0.5f,  0.5f,  0.0f, 0.0f, // bottom-left
                    0.5f,  0.5f,  0.5f,  1.0f, 0.0f, // top-left
                    // Bottom face
                    -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, // top-right
                    0.5f, -0.5f,  0.5f,  1.0f, 0.0f, // bottom-left
                    0.5f, -0.5f, -0.5f,  1.0f, 1.0f, // top-left
                    0.5f, -0.5f,  0.5f,  1.0f, 0.0f, // bottom-left
                    -0.5f, -0.5f, -0.5f,  0.0f, 1.0f, // top-right
                    -0.5f, -0.5f,  0.5f,  0.0f, 0.0f, // bottom-right
                    // Top face
                    -0.5f,  0.5f, -0.5f,  0.0f, 1.0f, // top-left
                    0.5f,  0.5f, -0.5f,  1.0f, 1.0f, // top-right
                    0.5f,  0.5f,  0.5f,  1.0f, 0.0f, // bottom-right
                    0.5f,  0.5f,  0.5f,  1.0f, 0.0f, // bottom-right
                    -0.5f,  0.5f,  0.5f,  0.0f, 0.0f, // bottom-left
                    -0.5f,  0.5f, -0.5f,  0.0f, 1.0f  // top-left
            };
            */

            IntBuffer VBOBuffer = Buffers.newDirectIntBuffer(1);
            gl.glGenBuffers(1, VBOBuffer);
            VBO = VBOBuffer.get(0);
            gl.glBindBuffer(gl.GL_ARRAY_BUFFER, VBO);

//            FloatBuffer buffer = Buffers.newDirectFloatBuffer(vertices);
//            gl.glBufferData(gl.GL_ARRAY_BUFFER, buffer.limit() * Buffers.SIZEOF_FLOAT, buffer, GL.GL_STATIC_DRAW);
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
            gl.glVertexAttribPointer(posAttrib, 3, gl.GL_FLOAT, false, 5 * Buffers.SIZEOF_FLOAT, 0);
            gl.glEnableVertexAttribArray(posAttrib);

            int textureAttrib = gl.glGetAttribLocation(shaderProgram, "texPosition");
            gl.glVertexAttribPointer(textureAttrib, 2, gl.GL_FLOAT, false, 5 * Buffers.SIZEOF_FLOAT, 3 * Buffers.SIZEOF_FLOAT);
            gl.glEnableVertexAttribArray(textureAttrib);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        //Textures
        {
            int texture;
            IntBuffer buffer = Buffers.newDirectIntBuffer(1);
            gl.glGenTextures(1, buffer);
            texture = buffer.get(0);
            gl.glBindTexture(gl.GL_TEXTURE_2D, texture);

            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_S, gl.GL_REPEAT);
            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_WRAP_T, gl.GL_REPEAT);

            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MIN_FILTER, gl.GL_NEAREST);
            gl.glTexParameteri(gl.GL_TEXTURE_2D, gl.GL_TEXTURE_MAG_FILTER, gl.GL_NEAREST);

            try {
                byte[] bytes = ResourceUtils.loadImageResource("./images/block.png");
                ByteBuffer buffer1 = Buffers.newDirectByteBuffer(bytes);
                gl.glPixelStorei(gl.GL_UNPACK_ALIGNMENT, 1);
                gl.glTexImage2D(gl.GL_TEXTURE_2D, 0, gl.GL_RGB, 16, 16, 0, gl.GL_RGB, gl.GL_UNSIGNED_BYTE, buffer1);
            } catch (ClassNotFoundException | IOException e) {
                e.printStackTrace();
            }

        }

        //Matrices
        {
            FloatBuffer modelMatrix = Buffers.newDirectFloatBuffer(16);
            new Matrix4f().translate(0f, -2f ,0f).get(modelMatrix);

            FloatBuffer viewMatrix = Buffers.newDirectFloatBuffer(16);
            new Matrix4f().lookAt(Camera.position, new Vector3f(Camera.position).add(Camera.front), Camera.up).get(viewMatrix);

            FloatBuffer projectionMatrix = Buffers.newDirectFloatBuffer(16);
            new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) Client.WIDTH / (float) Client.HEIGHT, 0.1f, 100.0f).get(projectionMatrix);

            int modelLocation = gl.glGetUniformLocation(shaderProgram, "model");
            int viewLocation = gl.glGetUniformLocation(shaderProgram, "view");
            int projectionLocation = gl.glGetUniformLocation(shaderProgram, "projection");

            gl.glUniformMatrix4fv(modelLocation, 1, false, modelMatrix);
            gl.glUniformMatrix4fv(viewLocation, 1, false, viewMatrix);
            gl.glUniformMatrix4fv(projectionLocation, 1, false, projectionMatrix);
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
    public void display(GLAutoDrawable drawable) {
        final GL4 gl = drawable.getGL().getGL4();
        gl.glClear(gl.GL_COLOR_BUFFER_BIT);
        gl.glClear(gl.GL_DEPTH_BUFFER_BIT);

        if (updateWireframe) {
                if (wireframe) {
                    gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_FILL);
                    wireframe = false;
                } else {
                    gl.glPolygonMode(gl.GL_FRONT_AND_BACK, gl.GL_LINE);
                    wireframe = true;
                }

            updateWireframe = false;
        }

        if (updateCamera) {
            FloatBuffer viewMatrix = Buffers.newDirectFloatBuffer(16);
            new Matrix4f().lookAt(Camera.position, new Vector3f(Camera.position).add(Camera.front), Camera.up).get(viewMatrix);
            int viewLocation = gl.glGetUniformLocation(shaderProgram, "view");
            gl.glUniformMatrix4fv(viewLocation, 1, false, viewMatrix);

            updateCamera = false;
        }

        if (updateChunk) {
            float[] chunkVertices = Chunk.getVertices();

            FloatBuffer buffer = Buffers.newDirectFloatBuffer(chunkVertices);
            gl.glBufferData(gl.GL_ARRAY_BUFFER, buffer.limit() * Buffers.SIZEOF_FLOAT, buffer, GL.GL_DYNAMIC_DRAW);

            chunkVertexCount = chunkVertices.length / 5;

            updateChunk = false;
        }

//        //render chunk
//        {
//            FloatBuffer modelMatrix = Buffers.newDirectFloatBuffer(16);
//            int modelLocation = gl.glGetUniformLocation(shaderProgram, "model");
//            for (int i = 0; i < Chunk.blocks.length; i++) {
//                for (int j = 0; j < Chunk.blocks.length; j++) {
//                    for (int k = 0; k < Chunk.blocks.length; k++) {
//                        if (Chunk.blocks[i][j][k] == 1) {
//                            new Matrix4f().translate(i, j, k).get(modelMatrix);
//
//                            gl.glUniformMatrix4fv(modelLocation, 1, false, modelMatrix);
//
//                            gl.glDrawArrays(gl.GL_TRIANGLES, 0, 36);
//                        }
//                    }
//                }
//            }
//        }
        gl.glDrawArrays(gl.GL_TRIANGLES, 0, chunkVertexCount);
    }

    @Override
    public void reshape(GLAutoDrawable drawable, int x, int y, int width, int height) {
        final GL4 gl = drawable.getGL().getGL4();

        FloatBuffer projectionMatrix = Buffers.newDirectFloatBuffer(16);
        new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) width / (float) height, 0.1f, 100.0f).get(projectionMatrix);
        int projectionLocation = gl.glGetUniformLocation(shaderProgram, "projection");
        gl.glUniformMatrix4fv(projectionLocation, 1, false, projectionMatrix);

        gl.glViewport(0, 0, width, height);
    }

    @Override
    public void dispose(GLAutoDrawable drawable) {
        Client.TICK.terminate();
        try {
            Client.threadTick.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
