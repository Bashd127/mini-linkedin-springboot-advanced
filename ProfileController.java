package com.example.minilinkedadvanced.controller;

import com.example.minilinkedadvanced.entity.UserAccount;
import com.example.minilinkedadvanced.repository.UserRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Optional;

@RestController
@RequestMapping("/api/profiles")
public class ProfileController {

    private final UserRepository userRepository;

    @Value("${file.upload-dir}")
    private String uploadDir;

    public ProfileController(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getProfile(@PathVariable Long id) {
        Optional<UserAccount> u = userRepository.findById(id);
        return u.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/avatar")
    public ResponseEntity<?> uploadAvatar(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        Optional<UserAccount> opt = userRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        UserAccount user = opt.get();
        String filename = System.currentTimeMillis() + "-" + StringUtils.cleanPath(file.getOriginalFilename());
        Path dir = Paths.get(uploadDir);
        if (!Files.exists(dir)) Files.createDirectories(dir);
        Path target = dir.resolve(filename);
        file.transferTo(target.toFile());
        user.setAvatarFilename(filename);
        userRepository.save(user);
        return ResponseEntity.ok().body("Uploaded");
    }

    @GetMapping("/{id}/avatar")
    public ResponseEntity<?> getAvatar(@PathVariable Long id) throws IOException {
        Optional<UserAccount> opt = userRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        UserAccount user = opt.get();
        if (user.getAvatarFilename() == null) return ResponseEntity.notFound().build();
        Path file = Paths.get(uploadDir).resolve(user.getAvatarFilename());
        if (!Files.exists(file)) return ResponseEntity.notFound().build();
        byte[] bytes = Files.readAllBytes(file);
        return ResponseEntity.ok().body(bytes);
    }
}
