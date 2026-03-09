package com.fon.kartonpredmeta.dto;

import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class LiteraturaDTO {

    @NotBlank(message = "Naslov jeobavezan")
    private String naslov;

    @NotBlank(message = "Autor je obavezan")
    private String autor;

    @Max(value = 2026, message = "Godina mora biti manja od 2026")
    @Min(value = 1950, message = "Godina mora biti veca od 1950")
    private int godina;
}
