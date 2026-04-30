package com.project.aes.controller;

import com.project.aes.service.QrCodeService;
import com.project.aes.service.SteganographyService;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;

@Controller
public class Stage2Controller {

    private final QrCodeService qrService;
    private final SteganographyService stegService;

    // ✅ Proper constructor-based injection
    public Stage2Controller(QrCodeService qrService,
                            SteganographyService stegService) {
        this.qrService = qrService;
        this.stegService = stegService;
    }

    // -------------------- QR CODE GENERATION --------------------
    @PostMapping("/stage2/generateQr")
    @ResponseBody
    public String generateQr(@RequestParam String key) throws Exception {
        Path qrPath = qrService.generateQrCode(key);
        return "/download/" + qrPath.getFileName().toString();
    }

    // -------------------- HIDE KEY USING STEGANOGRAPHY --------------------
    @PostMapping("/stage2/hideKey")
    @ResponseBody
    public String hideKey(@RequestParam String key,
                          @RequestParam MultipartFile image) throws Exception {

        File coverImage = Files.createTempFile("cover_", ".png").toFile();
        image.transferTo(coverImage);

        File stegoImage = stegService.hideKeyInImage(key, coverImage);

        return "/download/" + stegoImage.getName();
    }

    // -------------------- EXTRACT KEY FROM STEGO IMAGE --------------------
    @PostMapping("/stage2/extractKey")
    @ResponseBody
    public String extractKey(@RequestParam MultipartFile image) throws Exception {

        File stegoImage = Files.createTempFile("stego_", ".png").toFile();
        image.transferTo(stegoImage);

        return stegService.extractKey(stegoImage);
    }
}
