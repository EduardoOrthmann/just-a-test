package com.example.justatest.post;

import com.example.justatest.dto.PostRequestDto;
import com.example.justatest.dto.PostResponseDto;
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

    public Optional<PostResponseDto> findById(UUID uuid) {
        return postRepository.findById(uuid)
                .map(post -> modelMapper.map(post, PostResponseDto.class));
    }

    public PostResponseDto save(PostRequestDto postRequestDto) {
        Post post = modelMapper.map(postRequestDto, Post.class);
        return modelMapper.map(postRepository.save(post), PostResponseDto.class);
    }

    public Optional<PostResponseDto> update(UUID uuid, PostRequestDto postRequestDto) {
        Optional<Post> postOptional = postRepository.findById(uuid);

        if (postOptional.isEmpty()) {
            return Optional.empty();
        }

        Post post = postOptional.get();
        modelMapper.map(postRequestDto, post);

        return Optional.of(modelMapper.map(postRepository.save(post), PostResponseDto.class));
    }

    public void deleteById(UUID uuid) {
        postRepository.deleteById(uuid);
    }
}
