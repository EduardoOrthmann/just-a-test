package com.example.justatest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostRequestDto {
    @NotNull(message = "O campo title é obrigatório.")
    @NotBlank(message = "O título não pode ser vazio.")
    @Size(max = 100, message = "O título deve ter no máximo 100 caracteres.")
    private String title;

    @NotBlank(message = "A descrição não pode ser vazia.")
    @Size(max = 500, message = "A descrição deve ter no máximo 500 caracteres.")
    private String description;
}
