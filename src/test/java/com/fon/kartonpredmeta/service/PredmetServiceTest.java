package com.fon.kartonpredmeta.service;

import com.fon.kartonpredmeta.dto.IzvodjenjeRequest;
import com.fon.kartonpredmeta.dto.LiteraturaDTO;
import com.fon.kartonpredmeta.dto.PredmetCreateRequest;
import com.fon.kartonpredmeta.dto.PredmetResponse;
import com.fon.kartonpredmeta.dto.PredmetUpdateRequest;
import com.fon.kartonpredmeta.entity.Ishod;
import com.fon.kartonpredmeta.entity.Izvodjenje;
import com.fon.kartonpredmeta.entity.Literatura;
import com.fon.kartonpredmeta.entity.Nastavnik;
import com.fon.kartonpredmeta.entity.OblikNastave;
import com.fon.kartonpredmeta.entity.Predmet;
import com.fon.kartonpredmeta.exception.BadRequestException;
import com.fon.kartonpredmeta.exception.ConflictException;
import com.fon.kartonpredmeta.exception.NotFoundException;
import com.fon.kartonpredmeta.mapper.PredmetMapper;
import com.fon.kartonpredmeta.repository.IshodRepository;
import com.fon.kartonpredmeta.repository.IzvodjenjeRepository;
import com.fon.kartonpredmeta.repository.LiteraturaRepository;
import com.fon.kartonpredmeta.repository.NastavnikRepository;
import com.fon.kartonpredmeta.repository.PredmetRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PredmetServiceTest {

    @Mock
    private PredmetRepository predmetRepository;

    @Mock
    private PredmetMapper predmetMapper;

    @Mock
    private LiteraturaRepository literaturaRepository;

    @Mock
    private IshodRepository ishodRepository;

    @Mock
    private NastavnikRepository nastavnikRepository;

    @Mock
    private IzvodjenjeRepository izvodjenjeRepository;

    @InjectMocks
    private PredmetService predmetService;

    @Test
    void findBySifra_kadaPredmetPostoji_vracaPredmet() {
        Predmet predmet = napraviPredmet(1L, "Automatizacija razvoja softvera", "ARS");
        PredmetResponse response = new PredmetResponse();

        when(predmetRepository.findBySifra("ARS")).thenReturn(Optional.of(predmet));
        when(predmetMapper.toResponse(predmet)).thenReturn(response);

        PredmetResponse rezultat = predmetService.findBySifra("ARS");

        assertSame(response, rezultat);
    }

    @Test
    void findBySifra_kadaPredmetNePostoji_bacaNotFoundException() {
        when(predmetRepository.findBySifra("NEPOSTOJECA")).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> predmetService.findBySifra("NEPOSTOJECA"));
    }

    @Test
    void create_kadaSuPodaciIspravni_cuvaPredmetIIzvodjenje() {
        PredmetCreateRequest request = new PredmetCreateRequest();
        request.setSifra("ARS");
        request.setIshodIds(List.of(1L));
        request.setIzvodjenja(List.of(new IzvodjenjeRequest(10L, OblikNastave.PREDAVANJA)));

        Predmet predmet = napraviPredmet(1L, "Automatizacija razvoja softvera", "ARS");
        Ishod ishod = new Ishod(1L, "Razumevanje automatizacije razvoja softvera");
        Nastavnik nastavnik = napraviNastavnika(10L);
        PredmetResponse response = new PredmetResponse();

        when(predmetRepository.existsBySifra("ARS")).thenReturn(false);
        when(predmetMapper.toEntity(request)).thenReturn(predmet);
        when(ishodRepository.findById(1L)).thenReturn(Optional.of(ishod));
        when(predmetRepository.save(predmet)).thenReturn(predmet);
        when(nastavnikRepository.findById(10L)).thenReturn(Optional.of(nastavnik));
        when(predmetMapper.toResponse(predmet)).thenReturn(response);

        PredmetResponse rezultat = predmetService.create(request);

        assertSame(response, rezultat);
        assertEquals(List.of(ishod), predmet.getIshodi());
        assertEquals(1, predmet.getIzvodjenja().size());

        Izvodjenje izvodjenje = predmet.getIzvodjenja().get(0);
        assertSame(predmet, izvodjenje.getPredmet());
        assertSame(nastavnik, izvodjenje.getNastavnik());
        assertEquals(OblikNastave.PREDAVANJA, izvodjenje.getOblikNastave());

        verify(predmetRepository).save(predmet);
        verify(izvodjenjeRepository).saveAll(predmet.getIzvodjenja());
    }

    @Test
    void create_kadaSifraVecPostoji_bacaConflictException() {
        PredmetCreateRequest request = new PredmetCreateRequest();
        request.setSifra("ARS");

        when(predmetRepository.existsBySifra("ARS")).thenReturn(true);

        assertThrows(ConflictException.class, () -> predmetService.create(request));

        verify(predmetRepository, never()).save(any(Predmet.class));
    }

    @Test
    void findById_kadaPredmetPostoji_vracaPredmet() {
        Predmet predmet = napraviPredmet(1L, "Automatizacija razvoja softvera", "ARS");
        PredmetResponse response = new PredmetResponse();

        when(predmetRepository.findById(1L)).thenReturn(Optional.of(predmet));
        when(predmetMapper.toResponse(predmet)).thenReturn(response);

        PredmetResponse rezultat = predmetService.findById(1L);

        assertSame(response, rezultat);
    }

    @Test
    void findById_kadaPredmetNePostoji_bacaNotFoundException() {
        when(predmetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> predmetService.findById(99L));
    }

    @Test
    void update_kadaSuPodaciIspravni_cuvaIzmene() {
        Predmet predmet = napraviPredmet(1L, "Stari naziv", "ARS");
        PredmetUpdateRequest request = new PredmetUpdateRequest();
        request.setNaziv("Novi naziv");
        PredmetResponse response = new PredmetResponse();

        when(predmetRepository.findById(1L)).thenReturn(Optional.of(predmet));
        when(predmetMapper.toResponse(predmet)).thenReturn(response);

        PredmetResponse rezultat = predmetService.update(1L, request);

        assertSame(response, rezultat);
        verify(predmetMapper).update(request, predmet);
        verify(predmetRepository).save(predmet);
    }

    @Test
    void update_kadaJeNazivPrazan_bacaBadRequestException() {
        Predmet predmet = napraviPredmet(1L, "Automatizacija razvoja softvera", "ARS");
        PredmetUpdateRequest request = new PredmetUpdateRequest();
        request.setNaziv("   ");

        when(predmetRepository.findById(1L)).thenReturn(Optional.of(predmet));

        assertThrows(BadRequestException.class, () -> predmetService.update(1L, request));

        verify(predmetRepository, never()).save(any(Predmet.class));
    }

    @Test
    void update_kadaNovaSifraVecPostoji_bacaConflictException() {
        Predmet predmet = napraviPredmet(1L, "Automatizacija razvoja softvera", "ARS");
        PredmetUpdateRequest request = new PredmetUpdateRequest();
        request.setSifra("NST");

        when(predmetRepository.findById(1L)).thenReturn(Optional.of(predmet));
        when(predmetRepository.existsBySifra("NST")).thenReturn(true);

        assertThrows(ConflictException.class, () -> predmetService.update(1L, request));

        verify(predmetRepository, never()).save(any(Predmet.class));
    }

    @Test
    void delete_kadaPredmetPostoji_briseIzvodjenjaIPredmet() {
        Predmet predmet = napraviPredmet(1L, "Automatizacija razvoja softvera", "ARS");
        Izvodjenje predavanja = new Izvodjenje(1L, OblikNastave.PREDAVANJA, predmet, napraviNastavnika(10L));
        Izvodjenje vezbe = new Izvodjenje(2L, OblikNastave.VEZBE, predmet, napraviNastavnika(11L));
        predmet.setIzvodjenja(List.of(predavanja, vezbe));

        when(predmetRepository.findById(1L)).thenReturn(Optional.of(predmet));

        predmetService.delete(1L);

        verify(izvodjenjeRepository).deleteAll(predmet.getIzvodjenja());
        verify(predmetRepository).delete(predmet);
    }

    @Test
    void delete_kadaPredmetNePostoji_bacaNotFoundException() {
        when(predmetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> predmetService.delete(99L));

        verify(izvodjenjeRepository, never()).deleteAll(any());
        verify(predmetRepository, never()).delete(any(Predmet.class));
    }

    @Test
    void traziPoLiteraturi_kadaSeRazlikujuVelikaIMalaSlova_pronalaziPredmet() {
        Predmet pronadjeniPredmet = napraviPredmet(1L, "Napredne softverske tehnologije", "NST");
        pronadjeniPredmet.setLiteratura(List.of(new Literatura(1L, "Clean Code", "Robert Martin", 2008)));

        Predmet drugiPredmet = napraviPredmet(2L, "Automatizacija razvoja softvera", "ARS");
        drugiPredmet.setLiteratura(List.of(new Literatura(2L, "Design Patterns", "Erich Gamma", 1994)));

        PredmetResponse response = new PredmetResponse();
        when(predmetRepository.findAll()).thenReturn(List.of(pronadjeniPredmet, drugiPredmet));
        when(predmetMapper.toResponse(pronadjeniPredmet)).thenReturn(response);

        List<PredmetResponse> rezultat = predmetService.traziPoLiteraturi("clean code");

        assertEquals(1, rezultat.size());
        assertSame(response, rezultat.get(0));
    }

    @Test
    void create_kadaIshodNePostoji_bacaNotFoundException() {
        PredmetCreateRequest request = new PredmetCreateRequest();
        request.setSifra("ARS");
        request.setIshodIds(List.of(99L));

        Predmet predmet = napraviPredmet(null, "Automatizacija razvoja softvera", "ARS");
        when(predmetRepository.existsBySifra("ARS")).thenReturn(false);
        when(predmetMapper.toEntity(request)).thenReturn(predmet);
        when(ishodRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> predmetService.create(request));

        verify(predmetRepository, never()).save(any(Predmet.class));
        verify(izvodjenjeRepository, never()).saveAll(any());
    }

    @Test
    void update_kadaNastavnikVecDrziIstiOblikNastave_bacaConflictException() {
        Predmet predmet = napraviPredmet(1L, "Automatizacija razvoja softvera", "ARS");
        Nastavnik nastavnik = napraviNastavnika(10L);
        predmet.getIzvodjenja().add(
                new Izvodjenje(1L, OblikNastave.PREDAVANJA, predmet, nastavnik));

        PredmetUpdateRequest request = new PredmetUpdateRequest();
        request.setIzvodjenja(List.of(
                new IzvodjenjeRequest(10L, OblikNastave.PREDAVANJA)));

        when(predmetRepository.findById(1L)).thenReturn(Optional.of(predmet));

        assertThrows(ConflictException.class, () -> predmetService.update(1L, request));

        verify(izvodjenjeRepository, never()).saveAll(any());
        verify(predmetRepository, never()).save(any(Predmet.class));
    }

    @Test
    void kreirajIliPronadjiLiteraturu_kadaLiteraturaPostoji_vracaPostojecu() {
        LiteraturaDTO dto = new LiteraturaDTO(null, "Clean Code", "Robert Martin", 2008);
        Literatura postojeca = new Literatura(1L, "Clean Code", "Robert Martin", 2008);
        when(literaturaRepository.findByNaslovAndAutorAndGodina(
                "Clean Code", "Robert Martin", 2008)).thenReturn(postojeca);

        Literatura rezultat = predmetService.kreirajIliPronadjiLiteraturu(dto);

        assertSame(postojeca, rezultat);
        verify(literaturaRepository, never()).save(any(Literatura.class));
    }

    @Test
    void kreirajIliPronadjiLiteraturu_kadaLiteraturaNePostoji_cuvaNovu() {
        LiteraturaDTO dto = new LiteraturaDTO(null, "Clean Architecture", "Robert Martin", 2017);
        Literatura nova = new Literatura(null, "Clean Architecture", "Robert Martin", 2017);
        Literatura sacuvana = new Literatura(2L, "Clean Architecture", "Robert Martin", 2017);

        when(literaturaRepository.findByNaslovAndAutorAndGodina(
                "Clean Architecture", "Robert Martin", 2017)).thenReturn(null);
        when(predmetMapper.toLiteratura(dto)).thenReturn(nova);
        when(literaturaRepository.save(nova)).thenReturn(sacuvana);

        Literatura rezultat = predmetService.kreirajIliPronadjiLiteraturu(dto);

        assertSame(sacuvana, rezultat);
        verify(literaturaRepository).save(nova);
    }

    @Test
    void deleteLiteraturaOdPredmeta_kadaJeLiteraturaZajednicka_uklanjaJeSamoSaIzabranogPredmeta() {
        Literatura zaUklanjanje = new Literatura(10L, "Clean Code", "Robert Martin", 2008);
        Literatura drugaLiteratura = new Literatura(20L, "Design Patterns", "Erich Gamma", 1994);

        Predmet prviPredmet = napraviPredmet(1L, "Napredne softverske tehnologije", "NST");
        prviPredmet.getLiteratura().addAll(List.of(zaUklanjanje, drugaLiteratura));
        Predmet drugiPredmet = napraviPredmet(2L, "Automatizacija razvoja softvera", "ARS");
        drugiPredmet.getLiteratura().add(zaUklanjanje);

        when(predmetRepository.findById(1L)).thenReturn(Optional.of(prviPredmet));

        predmetService.deleteLiteraturaOdPredmeta(1L, 10L);

        assertEquals(List.of(drugaLiteratura), prviPredmet.getLiteratura());
        assertEquals(List.of(zaUklanjanje), drugiPredmet.getLiteratura());
        verify(predmetRepository).save(prviPredmet);
        verify(predmetRepository, never()).save(drugiPredmet);
        verify(literaturaRepository, never()).delete(any(Literatura.class));
    }

    @Test
    void deleteLiteraturaOdPredmeta_kadaPredmetNePostoji_bacaNotFoundException() {
        when(predmetRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class,
                () -> predmetService.deleteLiteraturaOdPredmeta(99L, 10L));

        verify(predmetRepository, never()).save(any(Predmet.class));
    }

    @Test
    void deleteLiteraturaOdPredmeta_kadaLiteraturaNijeNaPredmetu_bacaNotFoundException() {
        Predmet predmet = napraviPredmet(1L, "Automatizacija razvoja softvera", "ARS");
        Literatura drugaLiteratura = new Literatura(20L, "Design Patterns", "Erich Gamma", 1994);
        predmet.getLiteratura().add(drugaLiteratura);
        when(predmetRepository.findById(1L)).thenReturn(Optional.of(predmet));

        assertThrows(NotFoundException.class,
                () -> predmetService.deleteLiteraturaOdPredmeta(1L, 10L));

        assertEquals(List.of(drugaLiteratura), predmet.getLiteratura());
        verify(predmetRepository, never()).save(any(Predmet.class));
    }

    private Predmet napraviPredmet(Long id, String naziv, String sifra) {
        Predmet predmet = new Predmet();
        predmet.setId(id);
        predmet.setNaziv(naziv);
        predmet.setSifra(sifra);
        return predmet;
    }

    private Nastavnik napraviNastavnika(Long id) {
        Nastavnik nastavnik = new Nastavnik();
        nastavnik.setId(id);
        nastavnik.setIme("Petar");
        nastavnik.setPrezime("Petrovic");
        nastavnik.setZvanje("profesor");
        return nastavnik;
    }
}
