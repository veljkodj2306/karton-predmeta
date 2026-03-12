package com.fon.kartonpredmeta.service;


import com.fon.kartonpredmeta.dto.LiteraturaDTO;
import com.fon.kartonpredmeta.dto.PredmetCreateRequest;
import com.fon.kartonpredmeta.dto.PredmetResponse;
import com.fon.kartonpredmeta.dto.PredmetUpdateRequest;
import com.fon.kartonpredmeta.entity.Literatura;
import com.fon.kartonpredmeta.entity.Predmet;
import com.fon.kartonpredmeta.exception.ConflictException;
import com.fon.kartonpredmeta.exception.NotFoundException;
import com.fon.kartonpredmeta.mapper.PredmetMapper;
import com.fon.kartonpredmeta.repository.LiteraturaRepository;
import com.fon.kartonpredmeta.repository.PredmetRepository;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PredmetService {

    private final PredmetRepository predmetRepository;
    private final PredmetMapper predmetMapper;
    private final LiteraturaRepository literaturaRepository;


    public PredmetService(PredmetRepository predmetRepository, PredmetMapper predmetMapper, LiteraturaRepository literaturaRepository) {

        this.predmetRepository = predmetRepository;
        this.predmetMapper = predmetMapper;
        this.literaturaRepository = literaturaRepository;
    }

    public PredmetResponse findBySifra(String sifra) {
        Predmet predmet = predmetRepository.findBySifra(sifra)
                .orElseThrow(() -> new NotFoundException("Predmet sa sifrom=" + sifra + " ne postoji"));

        return predmetMapper.toResponse(predmet);

    }


    public PredmetResponse create(PredmetCreateRequest request) {

        if (predmetRepository.existsBySifra(request.getSifra())) {
            throw new ConflictException("Predmet sa ovom siform postoji");
        }

        Predmet predmet = predmetMapper.toEntity(request);
        if (request.getLiteratura() != null) {
            predmet.setLiteratura(request.getLiteratura().stream().
                    map(this::kreirajIliPronadjiLiteraturu).toList());
        }
        Predmet saved = predmetRepository.save(predmet);
        return predmetMapper.toResponse(saved);
    }


    public PredmetResponse findById(Long id) {

        Predmet predmet = predmetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Predmet sa id=" + id + " ne postoji"));

        return predmetMapper.toResponse(predmet);
    }


    public List<PredmetResponse> findAll() {

        return predmetRepository.findAll().stream().map(predmetMapper::toResponse).toList();
    }


    public PredmetResponse update(Long id, PredmetUpdateRequest request) {
        Predmet predmet = predmetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Predmet sa id=" + id + " ne postoji"));

        if (request.getSifra() != null && !request.getSifra().equals(predmet.getSifra())) {
            if (predmetRepository.existsBySifra(request.getSifra())) {
                throw new ConflictException("Predmet sa ovom siform postoji");
            }


        }

        predmetMapper.update(request, predmet);
        if (request.getLiteratura() != null) {
            List<Literatura> novaLiteratura = new ArrayList<>(request.getLiteratura().stream()
                    .map(this::kreirajIliPronadjiLiteraturu).toList());
            predmet.setLiteratura(novaLiteratura);
        }

        predmetRepository.save(predmet);

        return predmetMapper.toResponse(predmet);
    }

    public void delete(Long id) {
        Predmet predmet = predmetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Predmet sa id=" + id + " ne postoji"));

        predmetRepository.delete(predmet);
    }


    public Literatura kreirajIliPronadjiLiteraturu(LiteraturaDTO literaturaDTO) {

        Literatura postojeca = literaturaRepository.findByNaslovAndAutorAndGodina(literaturaDTO.getNaslov(),
                literaturaDTO.getAutor(), literaturaDTO.getGodina());

        if (postojeca != null) {
            return postojeca;
        }

        Literatura novaLiteratura = predmetMapper.toLiteratura(literaturaDTO);


        return literaturaRepository.save(novaLiteratura);

    }

    public List<PredmetResponse> traziPoLiteraturi(String naslov) {
        List<Predmet> sviPredmeti = predmetRepository.findAll();
        List<PredmetResponse> rezultat = new ArrayList<>();

        for (Predmet predmet : sviPredmeti) {
            for (Literatura literatura : predmet.getLiteratura()) {
                if (literatura.getNaslov().equalsIgnoreCase(naslov)) {
                    rezultat.add(predmetMapper.toResponse(predmet));
                    break;
                }
            }
        }
        return rezultat;
    }

}




