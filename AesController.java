package com.project.aes.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

import com.project.aes.service.AesService;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Controller
public class AesController {

    private final AesService aesService;
    private final Path tempDir = Paths.get(System.getProperty("java.io.tmpdir"));

    public AesController(AesService aesService) {
        this.aesService = aesService;
    }

    @GetMapping("/")
    public String index() {
        return "index";
    }

    // -------------------- ENCRYPT --------------------
    @PostMapping("/encrypt")
    public String encrypt(@RequestParam("file") MultipartFile file,
                          @RequestParam("key") String key,
                          Model model) {
        try {
            byte[] encrypted = aesService.encrypt(file.getBytes(), key);

            Path encryptedPath = tempDir.resolve(
                    "encrypted_" + file.getOriginalFilename() + ".aes"
            );
            Files.write(encryptedPath, encrypted);

            model.addAttribute("message", "File encrypted successfully!");
            model.addAttribute("downloadFile", encryptedPath.getFileName().toString());

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "index";
    }

    // -------------------- DECRYPT --------------------
    @PostMapping("/decrypt")
    public String decrypt(@RequestParam("file") MultipartFile file,
                          @RequestParam("key") String key,
                          Model model) {
        try {
            byte[] decrypted = aesService.decrypt(file.getBytes(), key);

            String originalName = file.getOriginalFilename().replace(".aes", "");
            Path decryptedPath = tempDir.resolve("decrypted_" + originalName);
            Files.write(decryptedPath, decrypted);

            model.addAttribute("message", "File decrypted successfully!");
            model.addAttribute("downloadFile", decryptedPath.getFileName().toString());

        } catch (Exception e) {
            model.addAttribute("error", e.getMessage());
        }
        return "index";
    }

    // -------------------- DOWNLOAD --------------------
    @GetMapping("/download/{filename}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String filename)
            throws Exception {

        Path file = tempDir.resolve(filename);

        if (!Files.exists(file)) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new UrlResource(file.toUri());

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=\"" + file.getFileName() + "\"")
                .body(resource);
    }

    // -------------------- DYNAMIC KEY GENERATOR --------------------
    @GetMapping("/generateKey")
    @ResponseBody
    public String generateKey() throws Exception {
        return aesService.generateKeyString();
    }
}
