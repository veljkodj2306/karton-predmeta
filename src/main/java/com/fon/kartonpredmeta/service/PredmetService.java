package com.fon.kartonpredmeta.service;


import com.fon.kartonpredmeta.entity.Predmet;
import com.fon.kartonpredmeta.repository.PredmetRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class PredmetService {

    private final PredmetRepository predmetRepository;

    public PredmetService(PredmetRepository predmetRepository) {
        this.predmetRepository = predmetRepository;
    }

    public Optional<Predmet> findBySifra(String sifra) {
        return predmetRepository.findBySifra(sifra);
    }

    public boolean existsBySifra(String sifra) {
        return predmetRepository.existsBySifra(sifra);
    }

    
}
