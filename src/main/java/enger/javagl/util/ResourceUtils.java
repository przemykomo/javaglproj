package enger.javagl.util;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

@SuppressWarnings("ConstantConditions")
public class ResourceUtils {

    public static String loadStringResource(String fileName) throws IOException, ClassNotFoundException, NullPointerException {
        ClassLoader classLoader = Class.forName(ResourceUtils.class.getName()).getClassLoader();

        File file = new File(classLoader.getResource(fileName).getFile());

        return new String(Files.readAllBytes(file.toPath()));
    }

    public static byte[] loadImageResource(String fileName) throws ClassNotFoundException, IOException {
        ClassLoader classLoader = Class.forName(ResourceUtils.class.getName()).getClassLoader();

        File file = new File(classLoader.getResource(fileName).getFile());

        BufferedImage bufferedImage = ImageIO.read(file);
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        ImageIO.write(bufferedImage, "png", byteArrayOutputStream);

        DataBufferByte dataBufferByte = (DataBufferByte) bufferedImage.getRaster().getDataBuffer();

        byte[] pixels = dataBufferByte.getData();

        return pixels;
    }
}
