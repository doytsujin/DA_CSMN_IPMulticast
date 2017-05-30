/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package library;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import javax.imageio.ImageIO;

/**
 *
 * @author Admin
 */
public class LibImage {
    public static int COLOUR_OUTPUT = BufferedImage.TYPE_INT_RGB;
    /**
     * Converts BufferedImage to byte array
     *
     * @param image Image to convert
     * @param format Image format (JPEG, PNG or GIF)
     * @return Byte Array
     * @throws IOException
     */
    public static byte[] bufferedImageToByteArray(BufferedImage image, String format) throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(image, format, baos);
        return baos.toByteArray();
    }

    /**
     * Scales a bufferd image
     *
     * @param source Image to scale
     * @param w Image widht
     * @param h Image height
     * @return Scaled image
     */
    public static BufferedImage scale(BufferedImage source, int w, int h) {
        Image image = source
                .getScaledInstance(w, h, Image.SCALE_SMOOTH);
        BufferedImage result = new BufferedImage(w, h, COLOUR_OUTPUT);
        Graphics2D g = result.createGraphics();
        g.drawImage(image, 0, 0, null);
        g.dispose();
        return result;
    }

    /**
     * Shrinks a BufferedImage
     *
     * @param source Image to shrink
     * @param factor Scaling factor
     * @return Scaled image
     */
    public static BufferedImage shrink(BufferedImage source, double factor) {
        int w = (int) (source.getWidth() * factor);
        int h = (int) (source.getHeight() * factor);
        return scale(source, w, h);
    }

    /**
     * Copies a BufferedImage
     *
     * @param image Image to copy
     * @return Copied image
     */
    public static BufferedImage copyBufferedImage(BufferedImage image) {
        BufferedImage copyOfIm = new BufferedImage(image.getWidth(), image.getHeight(), image.getType());
        Graphics2D g = copyOfIm.createGraphics();
        g.drawRenderedImage(image, null);
        g.dispose();
        return copyOfIm;
    }

    
    
}
