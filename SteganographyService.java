package com.project.aes.service;

import org.springframework.stereotype.Service;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;

@Service   // ✅ REQUIRED
public class SteganographyService {

    public File hideKeyInImage(String key, File coverImage) throws Exception {
        BufferedImage image = ImageIO.read(coverImage);
        byte[] data = (key + "#").getBytes();

        int index = 0;
        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                if (index >= data.length) break;

                int pixel = image.getRGB(x, y);
                pixel = (pixel & 0xFFFFFF00) | data[index];
                image.setRGB(x, y, pixel);
                index++;
            }
        }

        File stego = new File(
                System.getProperty("java.io.tmpdir"),
                "stego_image.png"
        );

        ImageIO.write(image, "png", stego);
        return stego;
    }

    public String extractKey(File stegoImage) throws Exception {
        BufferedImage image = ImageIO.read(stegoImage);
        StringBuilder key = new StringBuilder();

        for (int y = 0; y < image.getHeight(); y++) {
            for (int x = 0; x < image.getWidth(); x++) {
                char c = (char) (image.getRGB(x, y) & 0xFF);
                if (c == '#') return key.toString();
                key.append(c);
            }
        }
        return key.toString();
    }
}
