package com.fon.kartonpredmeta.dto;


import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NastavnikDTO {

    private Long id;

    @NotBlank(message = "Ime je obavezno")
    @Size(max = 50, message = "Ime ne moze imati vise od 50 karaktera")
    private String ime;

    @NotBlank(message = "Prezime je obavezno")
    @Size(max = 50, message = "Prezime ne moze imati vise od 50 karaktera")
    private String prezime;

    @NotBlank(message = "Zvanje je obavezno")
    @Size(max = 50, message = "Zvanje ne moze imati vise od 50 karaktera")
    private String zvanje;

}
