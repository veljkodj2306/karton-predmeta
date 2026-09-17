package com.fon.kartonpredmeta.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fon.kartonpredmeta.dto.IzvodjenjeRequest;
import com.fon.kartonpredmeta.dto.PredmetCreateRequest;
import com.fon.kartonpredmeta.dto.PredmetResponse;
import com.fon.kartonpredmeta.dto.PredmetUpdateRequest;
import com.fon.kartonpredmeta.entity.OblikNastave;
import com.fon.kartonpredmeta.exception.ConflictException;
import com.fon.kartonpredmeta.exception.NotFoundException;
import com.fon.kartonpredmeta.service.PredmetService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(PredmetController.class)
class PredmetControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PredmetService predmetService;

    @Test
    void getPredmetBySifra_kadaPredmetPostoji_vracaStatus200() throws Exception {
        PredmetResponse response = napraviResponse();
        when(predmetService.findBySifra("ARS")).thenReturn(response);

        mockMvc.perform(get("/api/predmeti/sifra/{sifra}", "ARS"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.naziv").value("Automatizacija razvoja softvera"))
                .andExpect(jsonPath("$.sifra").value("ARS"));
    }

    @Test
    void getPredmetBySifra_kadaPredmetNePostoji_vracaStatus404() throws Exception {
        when(predmetService.findBySifra("NEPOSTOJECA"))
                .thenThrow(new NotFoundException("Predmet sa sifrom=NEPOSTOJECA ne postoji"));

        mockMvc.perform(get("/api/predmeti/sifra/{sifra}", "NEPOSTOJECA"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Predmet sa sifrom=NEPOSTOJECA ne postoji"))
                .andExpect(jsonPath("$.path").value("/api/predmeti/sifra/NEPOSTOJECA"));
    }

    @Test
    void getPredmet_kadaPredmetPostoji_vracaStatus200() throws Exception {
        PredmetResponse response = napraviResponse();
        when(predmetService.findById(1L)).thenReturn(response);

        mockMvc.perform(get("/api/predmeti/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.naziv").value("Automatizacija razvoja softvera"))
                .andExpect(jsonPath("$.sifra").value("ARS"));
    }

    @Test
    void getPredmet_kadaPredmetNePostoji_vracaStatus404() throws Exception {
        when(predmetService.findById(99L))
                .thenThrow(new NotFoundException("Predmet sa id=99 ne postoji"));

        mockMvc.perform(get("/api/predmeti/{id}", 99L))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404))
                .andExpect(jsonPath("$.error").value("Not Found"))
                .andExpect(jsonPath("$.message").value("Predmet sa id=99 ne postoji"))
                .andExpect(jsonPath("$.path").value("/api/predmeti/99"));
    }

    @Test
    void createPredmet_kadaJeZahtevIspravan_vracaStatus201() throws Exception {
        PredmetCreateRequest request = napraviIspravanCreateRequest();
        PredmetResponse response = napraviResponse();
        when(predmetService.create(any(PredmetCreateRequest.class))).thenReturn(response);

        mockMvc.perform(post("/api/predmeti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.naziv").value("Automatizacija razvoja softvera"))
                .andExpect(jsonPath("$.sifra").value("ARS"));

        verify(predmetService).create(any(PredmetCreateRequest.class));
    }

    @Test
    void createPredmet_kadaNazivNijeUnet_vracaStatus400() throws Exception {
        PredmetCreateRequest request = napraviIspravanCreateRequest();
        request.setNaziv("   ");

        mockMvc.perform(post("/api/predmeti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Greska u validaciji"))
                .andExpect(jsonPath("$.fieldErrors.naziv").value("Naziv je obavezan"));

        verify(predmetService, never()).create(any(PredmetCreateRequest.class));
    }

    @Test
    void createPredmet_kadaNastavnikNijeUnet_vracaStatus400() throws Exception {
        PredmetCreateRequest request = napraviIspravanCreateRequest();
        request.setIzvodjenja(List.of(new IzvodjenjeRequest(null, OblikNastave.PREDAVANJA)));

        mockMvc.perform(post("/api/predmeti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$['fieldErrors']['izvodjenja[0].nastavnikId']")
                        .value("Morate uneti nastavnika"));

        verify(predmetService, never()).create(any(PredmetCreateRequest.class));
    }

    @Test
    void createPredmet_kadaJsonNijeIspravan_vracaStatus400() throws Exception {
        String neispravanJson = "{\"naziv\":\"Automatizacija razvoja softvera\"";

        mockMvc.perform(post("/api/predmeti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(neispravanJson))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Neispravan JSON format."));

        verify(predmetService, never()).create(any(PredmetCreateRequest.class));
    }

    @Test
    void createPredmet_kadaSifraVecPostoji_vracaStatus409() throws Exception {
        PredmetCreateRequest request = napraviIspravanCreateRequest();
        when(predmetService.create(any(PredmetCreateRequest.class)))
                .thenThrow(new ConflictException("Predmet sa ovom sifrom postoji"));

        mockMvc.perform(post("/api/predmeti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isConflict())
                .andExpect(jsonPath("$.status").value(409))
                .andExpect(jsonPath("$.error").value("Conflict"))
                .andExpect(jsonPath("$.message").value("Predmet sa ovom sifrom postoji"))
                .andExpect(jsonPath("$.path").value("/api/predmeti"));
    }

    @Test
    void updatePredmet_kadaJeZahtevIspravan_vracaStatus200() throws Exception {
        PredmetUpdateRequest request = new PredmetUpdateRequest();
        request.setNaziv("Novi naziv predmeta");

        PredmetResponse response = napraviResponse();
        response.setNaziv("Novi naziv predmeta");
        when(predmetService.update(eq(1L), any(PredmetUpdateRequest.class))).thenReturn(response);

        mockMvc.perform(put("/api/predmeti/{id}", 1L)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.naziv").value("Novi naziv predmeta"));

        verify(predmetService).update(eq(1L), any(PredmetUpdateRequest.class));
    }

    @Test
    void deletePredmet_kadaPredmetPostoji_vracaStatus204() throws Exception {
        mockMvc.perform(delete("/api/predmeti/{id}", 1L))
                .andExpect(status().isNoContent());

        verify(predmetService).delete(1L);
    }

    @Test
    void traziPoLiteraturi_kadaJeNaslovUnet_vracaPronadjenePredmete() throws Exception {
        PredmetResponse response = napraviResponse();
        when(predmetService.traziPoLiteraturi("Clean Code")).thenReturn(List.of(response));

        mockMvc.perform(get("/api/predmeti/trazi-po-literaturi")
                        .param("naslov", "Clean Code"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].sifra").value("ARS"));
    }

    @Test
    void traziPoLiteraturi_kadaNaslovNijeUnet_vracaStatus400() throws Exception {
        mockMvc.perform(get("/api/predmeti/trazi-po-literaturi")
                        .param("naslov", "   "))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.status").value(400))
                .andExpect(jsonPath("$.message").value("Naslov literature je obavezan"));
    }

    @Test
    void deleteLiteratura_kadaJePovezanaSaPredmetom_vracaStatus204() throws Exception {
        mockMvc.perform(delete("/api/predmeti/{predmetId}/literatura/{literaturaId}", 1L, 7L))
                .andExpect(status().isNoContent());

        verify(predmetService).deleteLiteraturaOdPredmeta(1L, 7L);
    }

    private PredmetCreateRequest napraviIspravanCreateRequest() {
        PredmetCreateRequest request = new PredmetCreateRequest();
        request.setNaziv("Automatizacija razvoja softvera");
        request.setSifra("ARS");
        request.setEspb(6);
        request.setBrojCasovaPredavanja(2);
        request.setBrojCasovaVezbi(2);
        request.setBrojCasovaLab(1);
        request.setCiljPredmeta("Razumevanje automatizacije razvoja softvera");
        request.setSadrzajPredmeta("Testiranje, CI/CD i staticka analiza koda");
        request.setIshodIds(List.of(1L));
        request.setIzvodjenja(List.of(new IzvodjenjeRequest(10L, OblikNastave.PREDAVANJA)));
        return request;
    }

    private PredmetResponse napraviResponse() {
        PredmetResponse response = new PredmetResponse();
        response.setId(1L);
        response.setNaziv("Automatizacija razvoja softvera");
        response.setSifra("ARS");
        response.setEspb(6);
        return response;
    }
}
