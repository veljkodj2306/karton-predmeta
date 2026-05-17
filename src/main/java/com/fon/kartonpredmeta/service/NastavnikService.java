package com.fon.kartonpredmeta.service;


import com.fon.kartonpredmeta.dto.NastavnikDTO;
import com.fon.kartonpredmeta.entity.Nastavnik;
import com.fon.kartonpredmeta.exception.NotFoundException;
import com.fon.kartonpredmeta.mapper.NastavnikMapper;
import com.fon.kartonpredmeta.repository.IzvodjenjeRepository;
import com.fon.kartonpredmeta.repository.NastavnikRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NastavnikService {

    private final NastavnikRepository nastavnikRepository;
    private final NastavnikMapper nastavnikMapper;
    private final IzvodjenjeRepository izvodjenjeRepository;

    public NastavnikService(NastavnikRepository nastavnikRepository, NastavnikMapper nastavnikMapper, IzvodjenjeRepository izvodjenjeRepository) {

        this.nastavnikRepository = nastavnikRepository;
        this.nastavnikMapper = nastavnikMapper;
        this.izvodjenjeRepository = izvodjenjeRepository;
    }


    public List<NastavnikDTO> findAll() {

        return nastavnikRepository.findAll().stream().map(nastavnikMapper::toDTO).toList();
    }


    public NastavnikDTO findById(Long id) {

        Nastavnik nastavnik = nastavnikRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nastavnik sa id " + id + " ne postoji"));

        return nastavnikMapper.toDTO(nastavnik);
    }


    public NastavnikDTO save(Nastavnik nastavnik) {

        Nastavnik saved = nastavnikRepository.save(nastavnik);
        return nastavnikMapper.toDTO(saved);
    }


    public void deleteById(Long id) {

        Nastavnik nastavnik = nastavnikRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nastavnik sa id " + id + " ne postoji"));


        izvodjenjeRepository.deleteAll(nastavnik.getIzvodjenja());
        nastavnikRepository.delete(nastavnik);
    }


    public NastavnikDTO updateById(NastavnikDTO dto, Long id) {
        Nastavnik trenutni = nastavnikRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Nastavnik sa id " + id + " ne postoji"));

        nastavnikMapper.update(dto, trenutni);

        Nastavnik saved = nastavnikRepository.save(trenutni);
        return nastavnikMapper.toDTO(saved);
    }
}
