package com.example.justatest.post;

import com.example.justatest.dto.PostRequestDto;
import com.example.justatest.dto.PostResponseDto;
import jakarta.persistence.EntityNotFoundException;
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

    @Test
    @DisplayName("findAll() -> should return all posts")
    void findAll_shouldReturnAllPosts() {
        // Arrange
        PostResponseDto mockPostResponseDto = new PostResponseDto(post.getId(), post.getTitle(), post.getDescription());

        when(postRepository.findAll()).thenReturn(List.of(post));
        when(modelMapper.map(post, PostResponseDto.class)).thenReturn(mockPostResponseDto);

        // Act
        List<PostResponseDto> postResponseDtoList = postService.findAll();
        PostResponseDto postResponseDto = postResponseDtoList.get(0);

        // Assert
        assertEquals(1, postResponseDtoList.size());
        assertEquals(post.getId(), postResponseDto.getId());
        assertEquals(post.getTitle(), postResponseDto.getTitle());
        assertEquals(post.getDescription(), postResponseDto.getDescription());

        verify(postRepository, times(1)).findAll();
        verify(modelMapper, times(1)).map(post, PostResponseDto.class);
    }

    @Test
    @DisplayName("findById() -> should return post by id")
    void findById_shouldReturnPostById() {
        // Arrange
        PostResponseDto mockPostResponseDto = new PostResponseDto(post.getId(), post.getTitle(), post.getDescription());

        when(postRepository.findById(post.getId())).thenReturn(Optional.of(post));
        when(modelMapper.map(post, PostResponseDto.class)).thenReturn(mockPostResponseDto);

        // Act
        PostResponseDto postResponseDto = postService.findById(post.getId());

        // Assert
        assertEquals(post.getId(), postResponseDto.getId());
        assertEquals(post.getTitle(), postResponseDto.getTitle());
        assertEquals(post.getDescription(), postResponseDto.getDescription());

        verify(postRepository, times(1)).findById(post.getId());
        verify(modelMapper, times(1)).map(post, PostResponseDto.class);
    }

    @Test
    @DisplayName("findById() -> when post not found by id -> should throw EntityNotFoundException")
    void findById_shouldThrowEntityNotFoundWhenPostNotFound() {
        // Arrange
        when(postRepository.findById(any())).thenReturn(Optional.empty());

        // Act
        Runnable runnable = () -> postService.findById(post.getId());

        // Assert
        assertThrows(EntityNotFoundException.class, runnable::run);

        verify(postRepository, times(1)).findById(post.getId());
        verify(modelMapper, times(0)).map(post, PostResponseDto.class);
    }

    @Test
    @DisplayName("save() -> should save post")
    void save_shouldSavePost() {
        // Arrange
        PostRequestDto mockPostRequestDto = new PostRequestDto(post.getTitle(), post.getDescription());
        PostResponseDto mockPostResponseDto = new PostResponseDto(post.getId(), post.getTitle(), post.getDescription());

        doReturn(post).when(postRepository).save(post);
        doReturn(post).when(modelMapper).map(mockPostRequestDto, Post.class);
        doReturn(mockPostResponseDto).when(modelMapper).map(post, PostResponseDto.class);

        // Act
        PostResponseDto savedPostResponseDto = postService.save(mockPostRequestDto);

        // Assert
        assertEquals(mockPostResponseDto.getId(), savedPostResponseDto.getId());
        assertEquals(mockPostResponseDto.getTitle(), savedPostResponseDto.getTitle());
        assertEquals(mockPostResponseDto.getDescription(), savedPostResponseDto.getDescription());

        verify(postRepository, times(1)).save(post);
        verify(modelMapper, times(1)).map(mockPostRequestDto, Post.class);
        verify(modelMapper, times(1)).map(post, PostResponseDto.class);
    }

    @Test
    @DisplayName("update() -> should update post")
    void update_shouldUpdatePost() {
        PostRequestDto mockPostRequestDto = new PostRequestDto("Updated Title", "Updated Description");
        Post mockUpdatedPost = new Post(post.getId(), mockPostRequestDto.getTitle(), mockPostRequestDto.getDescription());
        PostResponseDto mockPostResponseDto = new PostResponseDto(mockUpdatedPost.getId(), mockUpdatedPost.getTitle(), mockUpdatedPost.getDescription());

        when(postRepository.findById(any())).thenReturn(Optional.of(post));
        doNothing().when(modelMapper).map(mockPostRequestDto, post);
        when(postRepository.save(post)).thenReturn(mockUpdatedPost);
        when(modelMapper.map(mockUpdatedPost, PostResponseDto.class)).thenReturn(mockPostResponseDto);

        PostResponseDto updatedPost = postService.update(post.getId(), mockPostRequestDto);

        assertEquals(mockPostResponseDto.getId(), updatedPost.getId());
        assertEquals(mockPostResponseDto.getTitle(), updatedPost.getTitle());
        assertEquals(mockPostResponseDto.getDescription(), updatedPost.getDescription());

        verify(postRepository, times(1)).findById(post.getId());
        verify(postRepository, times(1)).save(post);
        verify(modelMapper, times(1)).map(mockPostRequestDto, post);
        verify(modelMapper, times(1)).map(mockUpdatedPost, PostResponseDto.class);
    }

    @Test
    @DisplayName("update() -> when post not found -> should throw EntityNotFoundException")
    void update_shouldThrowEntityNotFoundWhenPostNotFound() {
        // Arrange
        PostRequestDto mockPostRequestDto = new PostRequestDto("Updated Title", "Updated Description");

        when(postRepository.findById(any())).thenReturn(Optional.empty());

        // Act
        Runnable runnable = () -> postService.update(post.getId(), mockPostRequestDto);

        // Assert
        assertThrows(EntityNotFoundException.class, runnable::run);

        verify(postRepository, times(1)).findById(post.getId());
        verify(postRepository, times(0)).save(post);
        verify(modelMapper, times(0)).map(mockPostRequestDto, post);
        verify(modelMapper, times(0)).map(post, PostResponseDto.class);
    }

    @Test
    @DisplayName("deleteById() -> should delete post by id")
    void delete_shouldDeletePostById() {
        // Arrange
        when(postRepository.existsById(any())).thenReturn(true);
        doNothing().when(postRepository).deleteById(any());

        // Act
        postService.deleteById(post.getId());

        // Assert
        verify(postRepository, times(1)).existsById(post.getId());
        verify(postRepository, times(1)).deleteById(post.getId());
    }

    @Test
    @DisplayName("deleteById() -> when post not found -> should throw EntityNotFoundException")
    void delete_shouldThrowEntityNotFoundWhenPostNotFound() {
        // Arrange
        when(postRepository.existsById(any())).thenReturn(false);

        // Act
        Runnable runnable = () -> postService.deleteById(post.getId());

        // Assert
        assertThrows(EntityNotFoundException.class, runnable::run);

        verify(postRepository, times(1)).existsById(post.getId());
        verify(postRepository, times(0)).deleteById(post.getId());
    }
}