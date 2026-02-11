package com.fon.kartonpredmeta.service;


import com.fon.kartonpredmeta.dto.PredmetCreateRequest;
import com.fon.kartonpredmeta.dto.PredmetResponse;
import com.fon.kartonpredmeta.dto.PredmetUpdateRequest;
import com.fon.kartonpredmeta.entity.Predmet;
import com.fon.kartonpredmeta.mapper.PredmetMapper;
import com.fon.kartonpredmeta.repository.PredmetRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PredmetService {

    private final PredmetRepository predmetRepository;
    private final PredmetMapper predmetMapper;


    public PredmetService(PredmetRepository predmetRepository, PredmetMapper predmetMapper) {

        this.predmetRepository = predmetRepository;
        this.predmetMapper = predmetMapper;
    }

    public PredmetResponse findBySifra(String sifra) {
        Predmet predmet = predmetRepository.findBySifra(sifra)
                .orElseThrow(() -> new RuntimeException("Predmet sa sifrom=" + sifra + " ne postoji"));

        return predmetMapper.toResponse(predmet);

    }


    public PredmetResponse create(PredmetCreateRequest request) {
        Predmet predmet = predmetMapper.toEntity(request);
        Predmet saved = predmetRepository.save(predmet);
        return predmetMapper.toResponse(saved);
    }


    public PredmetResponse findById(Long id) {

        Predmet predmet = predmetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Predmet sa id=" + id + " ne postoji"));

        return predmetMapper.toResponse(predmet);
    }


    public List<PredmetResponse> findAll() {

        return predmetRepository.findAll().stream().map(predmetMapper::toResponse).toList();
    }


    public PredmetResponse update(Long id, PredmetUpdateRequest request) {
        Predmet predmet = predmetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Predmet sa id=" + id + " ne postoji"));

        predmetMapper.update(request, predmet);

        return predmetMapper.toResponse(predmet);
    }

    public void delete(Long id) {
        Predmet predmet = predmetRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Predmet sa id=" + id + " ne postoji"));

        predmetRepository.delete(predmet);
    }


}




