package enger.voxelgame.common.world;

import java.io.Serializable;

public class Chunk implements Serializable {

    //temporary blocks
    public static final short AIR = 0;
    public static final short TESTBLOCK = 1;

    public short[][][] blocks = new short[5][5][5];

    public Chunk() {
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                blocks[i][0][j] = TESTBLOCK;
                if (i == j) {
                    blocks[i][2][j] = TESTBLOCK;
                }
            }
        }

        blocks[2][4][2] = TESTBLOCK;
    }
}
