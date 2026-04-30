package com.project.aes.service;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.nio.file.Paths;

@Service   // ✅ THIS IS THE FIX
public class QrCodeService {

    public Path generateQrCode(String key) throws Exception {
        BitMatrix matrix = new MultiFormatWriter()
                .encode(key, BarcodeFormat.QR_CODE, 300, 300);

        Path path = Paths.get(
                System.getProperty("java.io.tmpdir"),
                "aes_key_qr.png"
        );

        MatrixToImageWriter.writeToPath(matrix, "PNG", path);
        return path;
    }
}
