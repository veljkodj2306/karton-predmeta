package com.fon.kartonpredmeta.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class IshodDTO {

    private Long id;

    @NotBlank(message = "Naziv ishoda je obavezan")
    @Size(max = 200, message = "Naziv ne moze imati vise od 200 karaktera")
    private String naziv;
}
