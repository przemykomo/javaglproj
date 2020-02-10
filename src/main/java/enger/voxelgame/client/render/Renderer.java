package enger.voxelgame.client.render;

import com.jogamp.common.nio.Buffers;
import com.jogamp.opengl.GL4;
import com.jogamp.opengl.GLAutoDrawable;
import com.jogamp.opengl.GLEventListener;
import enger.voxelgame.client.ChunkHelper;
import enger.voxelgame.client.Client;
import enger.voxelgame.common.world.Camera;
import enger.voxelgame.util.ResourceUtils;
import org.joml.Matrix4f;
import org.joml.Vector3f;

import java.awt.*;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.FloatBuffer;
import java.nio.IntBuffer;

public class Renderer implements GLEventListener {

    private volatile boolean updateCamera;
    private volatile boolean updateWireframe;
    private volatile boolean updateChunk;

    private boolean wireframe;
    //TODO: multiple chunks
    private int chunkVertexCount = 0;

    private int VAO;
    private int VBO;

    private int shaderProgram;

    public void updateCamera() {
        updateCamera = true;
    }

    public void updateWireframe() {
        updateWireframe = true;
    }

    public void updateChunk() {
        updateChunk = true;
    }

    @Override
    public void init(GLAutoDrawable drawable) {
        final GL4 gl = drawable.getGL().getGL4();

        gl.glClearColor(0.3f, 0.3f, 0.3f, 1);
        gl.glEnable(GL4.GL_DEPTH_TEST);

        gl.glEnable(GL4.GL_CULL_FACE);
        gl.glFrontFace(GL4.GL_CW);

        // init buffers
        {
            IntBuffer VAOBuffer = Buffers.newDirectIntBuffer(1);
            gl.glGenVertexArrays(1, VAOBuffer);
            VAO = VAOBuffer.get(0);
            gl.glBindVertexArray(VAO);

            IntBuffer VBOBuffer = Buffers.newDirectIntBuffer(1);
            gl.glGenBuffers(1, VBOBuffer);
            VBO = VBOBuffer.get(0);
            gl.glBindBuffer(GL4.GL_ARRAY_BUFFER, VBO);
        }

        // init shaders
        try {
            String vertexSource = ResourceUtils.loadStringResource("./shaders/vertex.glsl");
            String fragmentSource = ResourceUtils.loadStringResource("./shaders/fragment.glsl");
            int vertexShader = createShader(gl, GL4.GL_VERTEX_SHADER, vertexSource);
            int fragmentShader = createShader(gl, GL4.GL_FRAGMENT_SHADER, fragmentSource);

            shaderProgram = gl.glCreateProgram();
            gl.glAttachShader(shaderProgram, vertexShader);
            gl.glAttachShader(shaderProgram, fragmentShader);

            gl.glBindFragDataLocation(shaderProgram, 0, "pixelColor");

            gl.glLinkProgram(shaderProgram);
            gl.glUseProgram(shaderProgram);

            int posAttrib = gl.glGetAttribLocation(shaderProgram, "position");
            gl.glVertexAttribPointer(posAttrib, 3, GL4.GL_FLOAT, false, 5 * Buffers.SIZEOF_FLOAT, 0);
            gl.glEnableVertexAttribArray(posAttrib);

            int textureAttrib = gl.glGetAttribLocation(shaderProgram, "texPosition");
            gl.glVertexAttribPointer(textureAttrib, 2, GL4.GL_FLOAT, false, 5 * Buffers.SIZEOF_FLOAT, 3 * Buffers.SIZEOF_FLOAT);
            gl.glEnableVertexAttribArray(textureAttrib);

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }

        // init textures
        {
            int texture;
            IntBuffer textureBuffer = Buffers.newDirectIntBuffer(1);
            gl.glGenTextures(1, textureBuffer);
            texture = textureBuffer.get(0);
            gl.glBindTexture(GL4.GL_TEXTURE_2D, texture);

            gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_S, GL4.GL_REPEAT);
            gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_WRAP_T, GL4.GL_REPEAT);

            gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MIN_FILTER, GL4.GL_NEAREST);
            gl.glTexParameteri(GL4.GL_TEXTURE_2D, GL4.GL_TEXTURE_MAG_FILTER, GL4.GL_NEAREST);

            try {
                ByteBuffer bytesBuffer = Buffers.newDirectByteBuffer(ResourceUtils.loadImageResource("./images/block.png"));
                gl.glPixelStorei(GL4.GL_UNPACK_ALIGNMENT, 1);
                gl.glTexImage2D(GL4.GL_TEXTURE_2D, 0, GL4.GL_RGB,16, 16, 0, GL4.GL_RGB, GL4.GL_UNSIGNED_BYTE, bytesBuffer);
            } catch (IOException e) {
                e.printStackTrace();
            }

            // init matrices
            {
                FloatBuffer modelMatrix = Buffers.newDirectFloatBuffer(16);
                new Matrix4f().get(modelMatrix);

                FloatBuffer viewMatrix = Buffers.newDirectFloatBuffer(16);
                Camera camera = Client.getInstance().WORLD.playerCamera;
                new Matrix4f().lookAt(camera.position, new Vector3f(camera.position).add(camera.front), camera.up).get(viewMatrix);

                FloatBuffer projectionMatrix = Buffers.newDirectFloatBuffer(16);
                Point windowSize = Client.getInstance().WINDOW.getWindowSize();
                new Matrix4f().perspective((float) Math.toRadians(45.0f), (float) windowSize.x / (float) windowSize.y, 0.1f, 100.0f).get(projectionMatrix);

                int modelLocation = gl.glGetUniformLocation(shaderProgram, "model");
                int viewLocation = gl.glGetUniformLocation(shaderProgram, "view");
                int projectionLocation = gl.glGetUniformLocation(shaderProgram, "projection");

                gl.glUniformMatrix4fv(modelLocation, 1, false, modelMatrix);
                gl.glUniformMatrix4fv(viewLocation, 1, false, viewMatrix);
                gl.glUniformMatrix4fv(projectionLocation, 1, false, projectionMatrix);
            }
        }
    }

    private int createShader(GL4 gl, int type, String src) {
        int shader = gl.glCreateShader(type);
        gl.glShaderSource(shader, 1, new String[]{src}, null);
        gl.glCompileShader(shader);

        // Print debug info
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

        gl.glClear(GL4.GL_COLOR_BUFFER_BIT);
        gl.glClear(GL4.GL_DEPTH_BUFFER_BIT);

        if (updateWireframe) {
            if (wireframe) {
                gl.glPolygonMode(GL4.GL_FRONT_AND_BACK, GL4.GL_FILL);
                wireframe = false;
            } else {
                gl.glPolygonMode(GL4.GL_FRONT_AND_BACK, GL4.GL_LINE);
                wireframe = true;
            }

            updateWireframe = false;
        }

        if (updateCamera) {
            FloatBuffer viewMatrix = Buffers.newDirectFloatBuffer(16);
            Camera camera = Client.getInstance().WORLD.playerCamera;
            new Matrix4f().lookAt(camera.position, new Vector3f(camera.position).add(camera.front), camera.up).get(viewMatrix);

            int viewLocation = gl.glGetUniformLocation(shaderProgram, "view");
            gl.glUniformMatrix4fv(viewLocation, 1, false, viewMatrix);

            updateCamera = false;
        }

        if (updateChunk) {
            float[] chunkVertices = ChunkHelper.getVertices(Client.getInstance().WORLD.getChunk(0, 0).blocks);

            FloatBuffer buffer = Buffers.newDirectFloatBuffer(chunkVertices);
            gl.glBufferData(GL4.GL_ARRAY_BUFFER, buffer.limit() * Buffers.SIZEOF_FLOAT, buffer, GL4.GL_DYNAMIC_DRAW);

            chunkVertexCount = chunkVertices.length / 5;

            updateChunk = false;
        }

        gl.glDrawArrays(GL4.GL_TRIANGLES, 0, chunkVertexCount);
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

    }
}
