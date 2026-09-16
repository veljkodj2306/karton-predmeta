package com.fon.kartonpredmeta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fon.kartonpredmeta.dto.IshodDTO;
import com.fon.kartonpredmeta.service.IshodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(IshodController.class)
class IshodControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private IshodService ishodService;

    @Test
    void saveIshod_kadaJeNazivIspravan_vracaStatus201() throws Exception {
        IshodDTO request = new IshodDTO(null, "Student razume automatizaciju razvoja softvera");
        IshodDTO response = new IshodDTO(1L, "Student razume automatizaciju razvoja softvera");
        when(ishodService.saveIshod(any(IshodDTO.class))).thenReturn(response);

        mockMvc.perform(post("/api/ishodi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.naziv")
                        .value("Student razume automatizaciju razvoja softvera"));

        verify(ishodService).saveIshod(any(IshodDTO.class));
    }

    @Test
    void saveIshod_kadaNazivNijeUnet_vracaStatus400() throws Exception {
        IshodDTO request = new IshodDTO(null, "");

        mockMvc.perform(post("/api/ishodi")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Greska u validaciji"))
                .andExpect(jsonPath("$.fieldErrors.naziv").value("Naziv ishoda je obavezan"));

        verify(ishodService, never()).saveIshod(any(IshodDTO.class));
    }

    @Test
    void deleteIshodById_kadaIshodPostoji_vracaStatus204() throws Exception {
        mockMvc.perform(delete("/api/ishodi/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(ishodService).deleteIshodById(1L);
    }
}
