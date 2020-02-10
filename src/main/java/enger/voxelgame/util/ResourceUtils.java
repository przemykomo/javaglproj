package enger.voxelgame.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SuppressWarnings("ConstantConditions")
public final class ResourceUtils {
    private ResourceUtils() {}

    private static ClassLoader CLASS_LOADER;

    static {
        try {
            CLASS_LOADER = Class.forName(ResourceUtils.class.getName()).getClassLoader();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    public static String loadStringResource(String fileName) throws ClassNotFoundException, IOException {
        return new String(Files.readAllBytes(new File(CLASS_LOADER.getResource(fileName).getFile()).toPath()));
    }

    public static byte[] loadImageResource(String fileName) throws IOException {
        BufferedImage bufferedImage = ImageIO.read(new File(CLASS_LOADER.getResource(fileName).getFile()));
        ImageIO.write(bufferedImage, "png", new ByteArrayOutputStream());

        return ((DataBufferByte) bufferedImage.getRaster().getDataBuffer()).getData();
    }
}
