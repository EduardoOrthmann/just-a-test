package com.example.justatest.post;

import com.example.justatest.dto.PostRequestDto;
import com.example.justatest.dto.PostResponseDto;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class PostService {
    private final PostRepository postRepository;
    private final ModelMapper modelMapper;

    public List<PostResponseDto> findAll() {
        return postRepository.findAll().stream()
                .map(post -> modelMapper.map(post, PostResponseDto.class))
                .toList();
    }

    public PostResponseDto findById(UUID uuid) {
        return postRepository.findById(uuid)
                .map(post -> modelMapper.map(post, PostResponseDto.class))
                .orElseThrow(EntityNotFoundException::new);
    }

    public PostResponseDto save(PostRequestDto postRequestDto) {
        Post post = modelMapper.map(postRequestDto, Post.class);
        return modelMapper.map(postRepository.save(post), PostResponseDto.class);
    }

    public PostResponseDto update(UUID uuid, PostRequestDto postRequestDto) {
        Optional<Post> postOptional = postRepository.findById(uuid);

        if (postOptional.isEmpty()) {
            throw new EntityNotFoundException();
        }

        Post post = postOptional.get();
        modelMapper.map(postRequestDto, post);

        return modelMapper.map(postRepository.save(post), PostResponseDto.class);
    }

    public void deleteById(UUID uuid) {
        if (!postRepository.existsById(uuid)) {
            throw new EntityNotFoundException();
        }

        postRepository.deleteById(uuid);
    }
}
