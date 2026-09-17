package com.fon.kartonpredmeta.service;

import com.fon.kartonpredmeta.dto.NastavnikDTO;
import com.fon.kartonpredmeta.entity.Izvodjenje;
import com.fon.kartonpredmeta.entity.Nastavnik;
import com.fon.kartonpredmeta.entity.OblikNastave;
import com.fon.kartonpredmeta.entity.Predmet;
import com.fon.kartonpredmeta.exception.NotFoundException;
import com.fon.kartonpredmeta.mapper.NastavnikMapper;
import com.fon.kartonpredmeta.repository.IzvodjenjeRepository;
import com.fon.kartonpredmeta.repository.NastavnikRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class NastavnikServiceTest {

    @Mock
    private NastavnikRepository nastavnikRepository;

    @Mock
    private NastavnikMapper nastavnikMapper;

    @Mock
    private IzvodjenjeRepository izvodjenjeRepository;

    @InjectMocks
    private NastavnikService nastavnikService;

    @Test
    void findById_kadaNastavnikPostoji_vracaNastavnika() {
        Nastavnik nastavnik = napraviNastavnika(1L);
        NastavnikDTO dto = new NastavnikDTO(1L, "Ana", "Anic", "profesor");

        when(nastavnikRepository.findById(1L)).thenReturn(Optional.of(nastavnik));
        when(nastavnikMapper.toDTO(nastavnik)).thenReturn(dto);

        NastavnikDTO rezultat = nastavnikService.findById(1L);

        assertSame(dto, rezultat);
    }

    @Test
    void findById_kadaNastavnikNePostoji_bacaNotFoundException() {
        when(nastavnikRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> nastavnikService.findById(99L));
    }

    @Test
    void updateById_kadaNastavnikPostoji_cuvaIzmene() {
        Nastavnik trenutni = napraviNastavnika(1L);
        NastavnikDTO izmene = new NastavnikDTO(null, "Ana", "Jovic", "redovni profesor");
        NastavnikDTO response = new NastavnikDTO(1L, "Ana", "Jovic", "redovni profesor");

        when(nastavnikRepository.findById(1L)).thenReturn(Optional.of(trenutni));
        when(nastavnikRepository.save(trenutni)).thenReturn(trenutni);
        when(nastavnikMapper.toDTO(trenutni)).thenReturn(response);

        NastavnikDTO rezultat = nastavnikService.updateById(izmene, 1L);

        assertSame(response, rezultat);
        verify(nastavnikMapper).update(izmene, trenutni);
        verify(nastavnikRepository).save(trenutni);
    }

    @Test
    void updateById_kadaNastavnikNePostoji_bacaNotFoundException() {
        NastavnikDTO izmene = new NastavnikDTO(null, "Ana", "Jovic", "redovni profesor");
        when(nastavnikRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> nastavnikService.updateById(izmene, 99L));

        verify(nastavnikRepository, never()).save(any(Nastavnik.class));
    }

    @Test
    void deleteById_kadaNastavnikPostoji_briseIzvodjenjaINastavnika() {
        Nastavnik nastavnik = napraviNastavnika(1L);
        Predmet predmet = new Predmet();
        predmet.setId(10L);

        Izvodjenje predavanja = new Izvodjenje(1L, OblikNastave.PREDAVANJA, predmet, nastavnik);
        Izvodjenje vezbe = new Izvodjenje(2L, OblikNastave.VEZBE, predmet, nastavnik);
        nastavnik.setIzvodjenja(List.of(predavanja, vezbe));

        when(nastavnikRepository.findById(1L)).thenReturn(Optional.of(nastavnik));

        nastavnikService.deleteById(1L);

        verify(izvodjenjeRepository).deleteAll(nastavnik.getIzvodjenja());
        verify(nastavnikRepository).delete(nastavnik);
    }

    @Test
    void deleteById_kadaNastavnikNePostoji_bacaNotFoundException() {
        when(nastavnikRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> nastavnikService.deleteById(99L));

        verify(izvodjenjeRepository, never()).deleteAll(any());
        verify(nastavnikRepository, never()).delete(any(Nastavnik.class));
    }

    private Nastavnik napraviNastavnika(Long id) {
        Nastavnik nastavnik = new Nastavnik();
        nastavnik.setId(id);
        nastavnik.setIme("Ana");
        nastavnik.setPrezime("Anic");
        nastavnik.setZvanje("profesor");
        return nastavnik;
    }
}
