package com.fon.kartonpredmeta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fon.kartonpredmeta.dto.NastavnikDTO;
import com.fon.kartonpredmeta.entity.Nastavnik;
import com.fon.kartonpredmeta.mapper.NastavnikMapper;
import com.fon.kartonpredmeta.service.NastavnikService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(NastavnikController.class)
class NastavnikControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private NastavnikService nastavnikService;

    @MockitoBean
    private NastavnikMapper nastavnikMapper;

    @Test
    void getNastavnici_kadaPostojeNastavnici_vracaListu() throws Exception {
        NastavnikDTO prvi = new NastavnikDTO(1L, "Ana", "Anic", "profesor");
        NastavnikDTO drugi = new NastavnikDTO(2L, "Marko", "Markovic", "asistent");
        when(nastavnikService.findAll()).thenReturn(List.of(prvi, drugi));

        mockMvc.perform(get("/api/nastavnici"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].ime").value("Ana"))
                .andExpect(jsonPath("$[1].zvanje").value("asistent"));
    }

    @Test
    void createNastavnik_kadaSuPodaciIspravni_vracaStatus201() throws Exception {
        NastavnikDTO request = new NastavnikDTO(null, "Ana", "Anic", "profesor");
        Nastavnik nastavnik = new Nastavnik();
        nastavnik.setIme("Ana");
        nastavnik.setPrezime("Anic");
        nastavnik.setZvanje("profesor");
        NastavnikDTO response = new NastavnikDTO(1L, "Ana", "Anic", "profesor");

        when(nastavnikMapper.toEntity(any(NastavnikDTO.class))).thenReturn(nastavnik);
        when(nastavnikService.save(nastavnik)).thenReturn(response);

        mockMvc.perform(post("/api/nastavnici")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.ime").value("Ana"))
                .andExpect(jsonPath("$.prezime").value("Anic"));

        verify(nastavnikService).save(nastavnik);
    }

    @Test
    void createNastavnik_kadaImeNijeUneto_vracaStatus400() throws Exception {
        NastavnikDTO request = new NastavnikDTO(null, "   ", "Anic", "profesor");

        mockMvc.perform(post("/api/nastavnici")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Greska u validaciji"))
                .andExpect(jsonPath("$.fieldErrors.ime").value("Ime je obavezno"));

        verify(nastavnikService, never()).save(any(Nastavnik.class));
    }
}
