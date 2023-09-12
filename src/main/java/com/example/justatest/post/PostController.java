package com.example.justatest.post;

import com.example.justatest.dto.PostRequestDto;
import com.example.justatest.dto.PostResponseDto;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
public class PostController {
    private final PostService postService;

    @GetMapping
    public ResponseEntity<List<PostResponseDto>> findAll() {
        return ResponseEntity.ok(postService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<PostResponseDto> findById(@PathVariable(value = "id") UUID id) {
        return ResponseEntity.ok(postService.findById(id));
    }

    @PostMapping
    public ResponseEntity<PostResponseDto> save(@RequestBody @Valid PostRequestDto postRequestDto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(postService.save(postRequestDto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<PostResponseDto> update(@PathVariable(value = "id") UUID id, @RequestBody @Valid PostRequestDto postRequestDto) {
        return ResponseEntity.ok(postService.update(id, postRequestDto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable(value = "id") UUID id) {
        postService.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
