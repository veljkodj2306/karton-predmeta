package com.fon.kartonpredmeta.service;

import com.fon.kartonpredmeta.dto.IshodDTO;
import com.fon.kartonpredmeta.entity.Ishod;
import com.fon.kartonpredmeta.entity.Predmet;
import com.fon.kartonpredmeta.exception.NotFoundException;
import com.fon.kartonpredmeta.mapper.IshodMapper;
import com.fon.kartonpredmeta.repository.IshodRepository;
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
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class IshodServiceTest {

    @Mock
    private IshodRepository ishodRepository;

    @Mock
    private IshodMapper ishodMapper;

    @Mock
    private PredmetRepository predmetRepository;

    @InjectMocks
    private IshodService ishodService;

    @Test
    void getIshodById_kadaIshodPostoji_vracaIshod() {
        Ishod ishod = new Ishod(1L, "Student razume automatizaciju razvoja softvera");
        IshodDTO dto = new IshodDTO(1L, "Student razume automatizaciju razvoja softvera");

        when(ishodRepository.findById(1L)).thenReturn(Optional.of(ishod));
        when(ishodMapper.toIshodDTO(ishod)).thenReturn(dto);

        IshodDTO rezultat = ishodService.getIshodById(1L);

        assertSame(dto, rezultat);
    }

    @Test
    void getIshodById_kadaIshodNePostoji_bacaNotFoundException() {
        when(ishodRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> ishodService.getIshodById(99L));
    }

    @Test
    void deleteIshodById_kadaIshodPostoji_uklanjaVezeIBriseIshod() {
        Ishod ishodZaBrisanje = new Ishod(1L, "Ishod za brisanje");
        Ishod drugiIshod = new Ishod(2L, "Ishod koji ostaje");

        Predmet prviPredmet = napraviPredmet(10L, List.of(ishodZaBrisanje, drugiIshod));
        Predmet drugiPredmet = napraviPredmet(11L, List.of(ishodZaBrisanje));
        List<Predmet> povezaniPredmeti = List.of(prviPredmet, drugiPredmet);

        when(ishodRepository.findById(1L)).thenReturn(Optional.of(ishodZaBrisanje));
        when(predmetRepository.findByIshodi_Id(1L)).thenReturn(povezaniPredmeti);

        ishodService.deleteIshodById(1L);

        assertEquals(List.of(drugiIshod), prviPredmet.getIshodi());
        assertTrue(drugiPredmet.getIshodi().isEmpty());

        verify(predmetRepository).saveAll(povezaniPredmeti);
        verify(ishodRepository).delete(ishodZaBrisanje);
    }

    @Test
    void deleteIshodById_kadaIshodNePostoji_bacaNotFoundException() {
        when(ishodRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(NotFoundException.class, () -> ishodService.deleteIshodById(99L));

        verify(predmetRepository, never()).saveAll(any());
        verify(ishodRepository, never()).delete(any(Ishod.class));
    }

    private Predmet napraviPredmet(Long id, List<Ishod> ishodi) {
        Predmet predmet = new Predmet();
        predmet.setId(id);
        predmet.getIshodi().addAll(ishodi);
        return predmet;
    }
}
