package com.example.minilinkedadvanced.repository;

import com.example.minilinkedadvanced.entity.Post;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PostRepository extends JpaRepository<Post, Long> {}
