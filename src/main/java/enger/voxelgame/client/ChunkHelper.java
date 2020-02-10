package enger.voxelgame.client;

import java.util.ArrayList;

public class ChunkHelper {
    private ChunkHelper() {}

    //temporary blocks
    public static final short AIR = 0;
    public static final short TESTBLOCK = 1;

    public static float[] getVertices(short[][][] blocks) {
        ArrayList<Float> vertices = new ArrayList<>();

        for (int x = 0; x < blocks.length; x++) {
            for (int y = 0; y < blocks[0].length; y++) {
                for (int z = 0; z < blocks[0][0].length; z++) {

                    short currentBlock = blocks[x][y][z];

                    if (currentBlock != AIR) {
                        //cube
                        //30 floats per face
                        //180 floats per full cube

                        //left face
                        if (x == 0 || blocks[x-1][y][z] == AIR) {
                            //top-right
                            vertices.add(-0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(0.0f);

                            //bottom-left
                            vertices.add(-0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(1.0f);

                            //top-left
                            vertices.add(-0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(1.0f);

                            //bottom-left
                            vertices.add(-0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(1.0f);

                            //top-right
                            vertices.add(-0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(0.0f);

                            //bottom-right
                            vertices.add(-0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(0.0f);
                        }

                        //right face
                        if (x == blocks.length-1 || blocks[x+1][y][z] == AIR) {
                            //top-left
                            vertices.add(0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(0.0f);

                            //top-right
                            vertices.add(0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(1.0f);

                            //bottom-right
                            vertices.add(0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(1.0f);

                            //bottom-right
                            vertices.add(0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(1.0f);

                            //bottom-left
                            vertices.add(0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(0.0f);

                            //top-left
                            vertices.add(0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(0.0f);
                        }

                        //bottom face
                        if (y == 0 || blocks[x][y-1][z] == AIR) {
                            //top-right
                            vertices.add(-0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(1.0f);

                            //bottom-left
                            vertices.add(0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(0.0f);

                            //top-left
                            vertices.add(0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(1.0f);

                            //bottom-left
                            vertices.add(0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(0.0f);

                            //top-right
                            vertices.add(-0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(1.0f);

                            //bottom-right
                            vertices.add(-0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(0.0f);
                        }

                        //top face
                        if (y == blocks[0].length-1 || blocks[x][y+1][z] == AIR) {
                            //top-left
                            vertices.add(-0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(1.0f);

                            //top-right
                            vertices.add(0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(1.0f);

                            //bottom-right
                            vertices.add(0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(0.0f);

                            //bottom-right
                            vertices.add(0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(0.0f);

                            //bottom-left
                            vertices.add(-0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(0.0f);

                            //top-left
                            vertices.add(-0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(1.0f);
                        }

                        //back face
                        if (z == 0 || blocks[x][y][z-1] == AIR) {
                            //bottom-left
                            vertices.add(-0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(0.0f);

                            //bottom-right
                            vertices.add(0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(0.0f);

                            //top-right
                            vertices.add(0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(1.0f);

                            //top-right
                            vertices.add(0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(1.0f);

                            //top-left
                            vertices.add(-0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(1.0f);

                            //bottom-left
                            vertices.add(-0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(-0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(0.0f);
                        }

                        //front face
                        if (z == blocks[0][0].length-1 || blocks[x][y][z+1] == AIR) {
                            //bottom-left
                            vertices.add(-0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(0.0f);

                            //top-right
                            vertices.add(0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(1.0f);

                            //bottom-right
                            vertices.add(0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(0.0f);

                            //top-right
                            vertices.add(0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(1.0f);
                            vertices.add(1.0f);

                            //bottom-left
                            vertices.add(-0.5f + x);
                            vertices.add(-0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(0.0f);

                            //top-left
                            vertices.add(-0.5f + x);
                            vertices.add(0.5f + y);
                            vertices.add(0.5f + z);
                            vertices.add(0.0f);
                            vertices.add(1.0f);
                        }
                    }
                }
            }
        }

        float[] verticesArray = new float[vertices.size()];
        int i = 0;

        for (Float f : vertices) {
            verticesArray[i++] = (f != null ? f : Float.NaN);
        }

        return verticesArray;
    }
}
