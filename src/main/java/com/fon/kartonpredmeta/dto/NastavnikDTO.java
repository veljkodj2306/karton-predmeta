package com.fon.kartonpredmeta.dto;


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
    private String ime;
    private String prezime;
    private String zvanje;

}
