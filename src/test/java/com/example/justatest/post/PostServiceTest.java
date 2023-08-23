package com.example.justatest.post;

import com.example.justatest.dto.PostResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PostService postService;
    private Post post;
    private PostResponseDto postResponseDto;

    @BeforeEach
    void setUp() {
        this.post = new Post(UUID.randomUUID(), "title", "description");
        this.postResponseDto = new PostResponseDto(post.getId(), post.getTitle(), post.getDescription());
    }

    @Test
    void shouldReturnAllPosts() {
        Mockito.when(postRepository.findAll()).thenReturn(List.of(post));

        Mockito.when(modelMapper.map(Mockito.any(), Mockito.eq(PostResponseDto.class))).thenReturn(this.postResponseDto);

        List<PostResponseDto> postResponseDtoList = postService.findAll();
        PostResponseDto postResponseDto = postResponseDtoList.get(0);

        assertEquals(1, postResponseDtoList.size());
        assertEquals(post.getId(), postResponseDto.getId());
        assertEquals(post.getTitle(), postResponseDto.getTitle());
        assertEquals(post.getDescription(), postResponseDto.getDescription());

        Mockito.verify(postRepository, Mockito.times(1)).findAll();
        Mockito.verify(modelMapper, Mockito.times(1)).map(post, PostResponseDto.class);
    }

    @Test
    void shouldReturnPostById() {
        Mockito.when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));

        Mockito.when(modelMapper.map(Mockito.any(), Mockito.eq(PostResponseDto.class)))
                .thenReturn(this.postResponseDto);

        Optional<PostResponseDto> postResponseDtoOptional = postService.findById(post.getId());
        PostResponseDto postResponseDto = postResponseDtoOptional.orElseGet(PostResponseDto::new);

        assertEquals(post.getId(), postResponseDto.getId());
        assertEquals(post.getTitle(), postResponseDto.getTitle());
        assertEquals(post.getDescription(), postResponseDto.getDescription());

        Mockito.verify(postRepository, Mockito.times(1)).findById(post.getId());
        Mockito.verify(modelMapper, Mockito.times(1)).map(post, PostResponseDto.class);
    }

    @Test
    void whenPostNotFoundById_shouldReturnEmptyOptional() {
        UUID randomUUID = UUID.randomUUID();
        Mockito.when(postRepository.findById(randomUUID)).thenReturn(Optional.empty());

        Optional<PostResponseDto> postResponseDtoOptional = postService.findById(randomUUID);

        assertTrue(postResponseDtoOptional.isEmpty());

        Mockito.verify(postRepository, Mockito.times(1)).findById(randomUUID);
        Mockito.verify(modelMapper, Mockito.times(0)).map(post, PostResponseDto.class);
    }
}