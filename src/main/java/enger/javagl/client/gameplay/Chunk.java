package enger.javagl.client.gameplay;

//temporary class
public class Chunk {
    public static short[][][] blocks = new short[5][5][5];

    static {
        for (int i = 0; i < blocks.length; i++) {
            for (int j = 0; j < blocks.length; j++) {
                blocks[i][0][j] = 1;
                if (i == j) {
                    blocks[i][2][j] = 1;
                }
            }
        }

        blocks[2][4][2] = 1;
    }
}
