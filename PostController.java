package com.example.minilinkedadvanced.controller;

import com.example.minilinkedadvanced.entity.Post;
import com.example.minilinkedadvanced.entity.UserAccount;
import com.example.minilinkedadvanced.repository.PostRepository;
import com.example.minilinkedadvanced.repository.UserRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/posts")
public class PostController {

    private final PostRepository postRepository;
    private final UserRepository userRepository;

    public PostController(PostRepository postRepository, UserRepository userRepository) {
        this.postRepository = postRepository;
        this.userRepository = userRepository;
    }

    @GetMapping
    public List<Post> feed() {
        return postRepository.findAll();
    }

    public static class CreatePost {
        public Long authorId;
        public String content;
    }

    @PostMapping
    public ResponseEntity<?> create(@RequestBody CreatePost req) {
        Optional<UserAccount> opt = userRepository.findById(req.authorId);
        if (opt.isEmpty()) return ResponseEntity.badRequest().body("Invalid author");
        Post p = new Post();
        p.setAuthor(opt.get());
        p.setContent(req.content);
        postRepository.save(p);
        return ResponseEntity.ok(p);
    }

    @PostMapping("/{id}/like")
    public ResponseEntity<?> like(@PathVariable Long id) {
        Optional<Post> opt = postRepository.findById(id);
        if (opt.isEmpty()) return ResponseEntity.notFound().build();
        Post p = opt.get();
        p.setLikeCount(p.getLikeCount() + 1);
        postRepository.save(p);
        return ResponseEntity.ok(p);
    }
}
