package com.example.justatest.post;

import com.example.justatest.dto.PostRequestDto;
import com.example.justatest.dto.PostResponseDto;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;

import java.util.List;
import java.util.UUID;

import static org.mockito.Mockito.*;

@WebMvcTest(PostController.class)
class PostControllerTest {
    @MockBean
    private PostService postService;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    @DisplayName("findAll() -> should return 200 and print list of posts")
    void findAll_shouldReturn200AndPrint() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        List<PostResponseDto> postResponseDto = List.of(
                new PostResponseDto(id, "Title", "Description")
        );

        when(postService.findAll()).thenReturn(postResponseDto);

        // Act
        mockMvc.perform(get("/posts")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].id").value(id.toString()))
                .andExpect(jsonPath("$[0].title").value("Title"))
                .andExpect(jsonPath("$[0].description").value("Description"));

        // Assert
        verify(postService, times(1)).findAll();
    }

    @Test
    @DisplayName("findById() -> should return 200 and print post")
    void findById_shouldReturn200AndPrint() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        PostResponseDto postResponseDto = new PostResponseDto(id, "Title", "Description");

        when(postService.findById(id)).thenReturn(postResponseDto);

        // Act
        mockMvc.perform(get("/posts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.description").value("Description"));

        // Assert
        verify(postService, times(1)).findById(id);
    }

    @Test
    @DisplayName("save() -> should return 201 and print post")
    void save_shouldReturn201AndPrint() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        PostRequestDto postRequestDto = new PostRequestDto("Title", "Description");
        PostResponseDto postResponseDto = new PostResponseDto(id, "Title", "Description");

        when(postService.save(any())).thenReturn(postResponseDto);

        // Act
        mockMvc.perform(post("/posts")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.description").value("Description"));

        // Assert
        verify(postService, times(1)).save(any());
    }

    @Test
    @DisplayName("update() -> should return 200 and print post")
    void update_shouldReturn200AndPrint() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();
        PostRequestDto postRequestDto = new PostRequestDto("Title", "Description");
        PostResponseDto postResponseDto = new PostResponseDto(id, "Title", "Description");

        when(postService.update(any(), any())).thenReturn(postResponseDto);

        // Act
        mockMvc.perform(put("/posts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(postRequestDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(id.toString()))
                .andExpect(jsonPath("$.title").value("Title"))
                .andExpect(jsonPath("$.description").value("Description"));

        // Assert
        verify(postService, times(1)).update(any(), any());
    }

    @Test
    @DisplayName("delete() -> should return 204")
    void delete_shouldReturn204() throws Exception {
        // Arrange
        UUID id = UUID.randomUUID();

        // Act
        mockMvc.perform(delete("/posts/{id}", id)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());

        // Assert
        verify(postService, times(1)).deleteById(id);
    }

}