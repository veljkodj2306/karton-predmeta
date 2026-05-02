package com.fon.kartonpredmeta.dto;

import com.fon.kartonpredmeta.entity.OblikNastave;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class IzvodjenjeRequest {


    @NotNull(message = "Morate uneti nastavnika")
    private Long nastavnikId;

    @NotNull(message = "Morate uneti oblik nastave")
    private OblikNastave oblikNastave;
}
