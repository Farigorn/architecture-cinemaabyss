package com.cineaabyss.proxy.dto;

import jakarta.validation.constraints.*;

import java.util.List;

public record CreateMovieDto(
        @NotBlank String title,
        @Size(max = 5000) String description,
        @NotEmpty List<@NotBlank String> genres,
        @DecimalMin(value = "0.1", inclusive = true)
        @DecimalMax(value = "5.0", inclusive = true)
        Float rating
) {}
