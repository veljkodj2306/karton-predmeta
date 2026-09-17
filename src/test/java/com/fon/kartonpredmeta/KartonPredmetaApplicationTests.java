package com.fon.kartonpredmeta;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fon.kartonpredmeta.dto.IzvodjenjeRequest;
import com.fon.kartonpredmeta.dto.PredmetCreateRequest;
import com.fon.kartonpredmeta.entity.Ishod;
import com.fon.kartonpredmeta.entity.Nastavnik;
import com.fon.kartonpredmeta.entity.OblikNastave;
import com.fon.kartonpredmeta.entity.Predmet;
import com.fon.kartonpredmeta.repository.IshodRepository;
import com.fon.kartonpredmeta.repository.NastavnikRepository;
import com.fon.kartonpredmeta.repository.PredmetRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.ResultActions;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
class KartonPredmetaApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private NastavnikRepository nastavnikRepository;

    @Autowired
    private IshodRepository ishodRepository;

    @Autowired
    private PredmetRepository predmetRepository;

    @Test
    void contextLoads() {
    }

    @Test
    void kreiranjeCitanjeIBrisanjePredmeta_prolaziKrozSveSlojeve() throws Exception {
        Nastavnik nastavnik = new Nastavnik();
        nastavnik.setIme("Jelena");
        nastavnik.setPrezime("Jelic");
        nastavnik.setZvanje("docent");
        nastavnik = nastavnikRepository.save(nastavnik);

        Ishod ishod = ishodRepository.save(
                new Ishod(null, "Student razume automatizaciju razvoja softvera"));

        PredmetCreateRequest request = napraviRequest(nastavnik.getId(), ishod.getId());

        ResultActions rezultatKreiranja = mockMvc.perform(post("/api/predmeti")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.naziv").value("Integracioni predmet"))
                .andExpect(jsonPath("$.sifra").value(request.getSifra()))
                .andExpect(jsonPath("$.ishodi[0].id").value(ishod.getId().intValue()))
                .andExpect(jsonPath("$.izvodjenja[0].nastavnikId")
                        .value(nastavnik.getId().intValue()));

        Predmet predmet = predmetRepository.findBySifra(request.getSifra()).orElseThrow();
        Long predmetId = predmet.getId();

        rezultatKreiranja.andExpect(jsonPath("$.id").value(predmetId));

        mockMvc.perform(get("/api/predmeti/{id}", predmetId))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(predmetId))
                .andExpect(jsonPath("$.naziv").value("Integracioni predmet"))
                .andExpect(jsonPath("$.izvodjenja[0].oblikNastave")
                        .value("PREDAVANJA"));

        mockMvc.perform(delete("/api/predmeti/{id}", predmetId))
                .andExpect(status().isNoContent());

        assertFalse(predmetRepository.existsById(predmetId));
    }

    @Test
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void kreiranjePredmeta_kadaNastavnikNePostoji_neOstavljaPredmetUBazi() throws Exception {
        Ishod ishod = ishodRepository.save(
                new Ishod(null, "Ishod za proveru neuspesnog kreiranja predmeta"));
        PredmetCreateRequest request = napraviRequest(Long.MAX_VALUE, ishod.getId());

        try {
            mockMvc.perform(post("/api/predmeti")
                            .contentType(MediaType.APPLICATION_JSON)
                            .content(objectMapper.writeValueAsString(request)))
                    .andExpect(status().isNotFound())
                    .andExpect(jsonPath("$.message").value("Nastavnik ne postoji"));

            assertFalse(predmetRepository.existsBySifra(request.getSifra()));
        } finally {
            Predmet predmet = predmetRepository.findBySifra(request.getSifra()).orElse(null);
            if (predmet != null) {
                predmetRepository.delete(predmet);
            }
            ishodRepository.deleteById(ishod.getId());
        }
    }

    private PredmetCreateRequest napraviRequest(Long nastavnikId, Long ishodId) {
        PredmetCreateRequest request = new PredmetCreateRequest();
        request.setNaziv("Integracioni predmet");
        request.setSifra("INT-" + ishodId);
        request.setEspb(6);
        request.setBrojCasovaPredavanja(2);
        request.setBrojCasovaVezbi(2);
        request.setBrojCasovaLab(1);
        request.setCiljPredmeta("Provera rada svih slojeva aplikacije");
        request.setSadrzajPredmeta("Kontroler, servis, repository i baza");
        request.setIshodIds(List.of(ishodId));
        request.setIzvodjenja(List.of(
                new IzvodjenjeRequest(nastavnikId, OblikNastave.PREDAVANJA)));
        return request;
    }

}
