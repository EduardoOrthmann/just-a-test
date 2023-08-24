package com.example.justatest.post;

import com.example.justatest.dto.PostRequestDto;
import com.example.justatest.dto.PostResponseDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
class PostServiceTest {
    @Mock
    private PostRepository postRepository;

    @Mock
    private ModelMapper modelMapper;

    @InjectMocks
    private PostService postService;
    private Post post;

    @BeforeEach
    void setUp() {
        this.post = new Post(UUID.randomUUID(), "Title", "Description");
    }

    @DisplayName("Test for findAll method in PostService")
    @Test
    void shouldReturnAllPosts() {
        PostResponseDto mockPostResponseDto = new PostResponseDto(post.getId(), post.getTitle(), post.getDescription());

        doReturn(List.of(post)).when(postRepository).findAll();
        doReturn(mockPostResponseDto).when(modelMapper).map(post, PostResponseDto.class);

        List<PostResponseDto> postResponseDtoList = postService.findAll();
        PostResponseDto postResponseDto = postResponseDtoList.get(0);

        assertEquals(1, postResponseDtoList.size());
        assertEquals(post.getId(), postResponseDto.getId());
        assertEquals(post.getTitle(), postResponseDto.getTitle());
        assertEquals(post.getDescription(), postResponseDto.getDescription());

        verify(postRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(post, PostResponseDto.class);
    }

    @DisplayName("Test for findById method in PostService")
    @Test
    void shouldReturnPostById() {
        PostResponseDto mockPostResponseDto = new PostResponseDto(post.getId(), post.getTitle(), post.getDescription());

        doReturn(Optional.of(post)).when(postRepository).findById(post.getId());
        doReturn(mockPostResponseDto).when(modelMapper).map(post, PostResponseDto.class);

        Optional<PostResponseDto> postResponseDtoOptional = postService.findById(post.getId());
        PostResponseDto postResponseDto = postResponseDtoOptional.orElseGet(PostResponseDto::new);

        assertEquals(post.getId(), postResponseDto.getId());
        assertEquals(post.getTitle(), postResponseDto.getTitle());
        assertEquals(post.getDescription(), postResponseDto.getDescription());

        verify(postRepository, times(1)).findById(post.getId());
        verify(modelMapper, times(1)).map(post, PostResponseDto.class);
    }

    @DisplayName("Test for findById method in PostService when post not found")
    @Test
    void whenPostNotFoundById_shouldReturnEmptyOptional() {
        UUID randomUUID = UUID.randomUUID();

        doReturn(Optional.empty()).when(postRepository).findById(randomUUID);

        Optional<PostResponseDto> postResponseDtoOptional = postService.findById(randomUUID);

        assertTrue(postResponseDtoOptional.isEmpty());

        verify(postRepository, times(1)).findById(randomUUID);
        verify(modelMapper, times(0)).map(post, PostResponseDto.class);
    }

    @DisplayName("Test for save method in PostService")
    @Test
    void shouldSavePost() {
        PostRequestDto mockPostRequestDto = new PostRequestDto(post.getTitle(), post.getDescription());
        PostResponseDto mockPostResponseDto = new PostResponseDto(post.getId(), post.getTitle(), post.getDescription());

        doReturn(post).when(postRepository).save(post);
        doReturn(post).when(modelMapper).map(mockPostRequestDto, Post.class);
        doReturn(mockPostResponseDto).when(modelMapper).map(post, PostResponseDto.class);

        PostResponseDto savedPostResponseDto = postService.save(mockPostRequestDto);

        assertEquals(mockPostResponseDto.getId(), savedPostResponseDto.getId());
        assertEquals(mockPostResponseDto.getTitle(), savedPostResponseDto.getTitle());
        assertEquals(mockPostResponseDto.getDescription(), savedPostResponseDto.getDescription());

        verify(postRepository, times(1)).save(post);
        verify(modelMapper, times(1)).map(mockPostRequestDto, Post.class);
        verify(modelMapper, times(1)).map(post, PostResponseDto.class);
    }

    @DisplayName("Test for update method in PostService")
    @Test
    void shouldUpdatePost() {
        PostRequestDto mockPostRequestDto = new PostRequestDto("Updated Title", "Updated Description");
        Post mockUpdatedPost = new Post(post.getId(), mockPostRequestDto.getTitle(), mockPostRequestDto.getDescription());
        PostResponseDto mockPostResponseDto = new PostResponseDto(mockUpdatedPost.getId(), mockUpdatedPost.getTitle(), mockUpdatedPost.getDescription());

        doReturn(Optional.of(post)).when(postRepository).findById(post.getId());
        doNothing().when(modelMapper).map(mockPostRequestDto, post);
        doReturn(mockUpdatedPost).when(postRepository).save(post);
        doReturn(mockPostResponseDto).when(modelMapper).map(mockUpdatedPost, PostResponseDto.class);

        Optional<PostResponseDto> updatedPostOptional = postService.update(post.getId(), mockPostRequestDto);

        assertTrue(updatedPostOptional.isPresent());
        PostResponseDto updatedPost = updatedPostOptional.get();

        assertEquals(mockPostResponseDto.getId(), updatedPost.getId());
        assertEquals(mockPostResponseDto.getTitle(), updatedPost.getTitle());
        assertEquals(mockPostResponseDto.getDescription(), updatedPost.getDescription());

        verify(postRepository, times(1)).findById(post.getId());
        verify(postRepository, times(1)).save(post);
        verify(modelMapper, times(1)).map(mockPostRequestDto, post);
        verify(modelMapper, times(1)).map(mockUpdatedPost, PostResponseDto.class);
    }

    @DisplayName("Test for update method in PostService when post not found")
    @Test
    void whenPostNotFound_shouldReturnEmptyOptional() {
        UUID randomUUID = UUID.randomUUID();
        PostRequestDto mockPostRequestDto = new PostRequestDto("Updated Title", "Updated Description");

        doReturn(Optional.empty()).when(postRepository).findById(randomUUID);

        Optional<PostResponseDto> updatedPostOptional = postService.update(randomUUID, mockPostRequestDto);

        assertTrue(updatedPostOptional.isEmpty());

        verify(postRepository, times(1)).findById(randomUUID);
        verify(postRepository, times(0)).save(post);
        verify(modelMapper, times(0)).map(mockPostRequestDto, post);
        verify(modelMapper, times(0)).map(post, PostResponseDto.class);
    }

    @DisplayName("Test for deleteById method in PostService")
    @Test
    void shouldDeletePostById() {
        doNothing().when(postRepository).deleteById(post.getId());

        postService.deleteById(post.getId());

        verify(postRepository, times(1)).deleteById(post.getId());
    }

    @DisplayName("Test for deleteById method in PostService when post not found")
    @Test
    void whenPostNotFound_shouldDoNothing() {
        UUID randomUUID = UUID.randomUUID();
        doNothing().when(postRepository).deleteById(randomUUID);

        postService.deleteById(randomUUID);

        verify(postRepository, times(1)).deleteById(randomUUID);
    }
}