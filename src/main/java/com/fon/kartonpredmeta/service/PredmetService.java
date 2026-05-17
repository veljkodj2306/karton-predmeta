package com.fon.kartonpredmeta.service;


import com.fon.kartonpredmeta.dto.*;
import com.fon.kartonpredmeta.entity.*;
import com.fon.kartonpredmeta.exception.BadRequestException;
import com.fon.kartonpredmeta.exception.ConflictException;
import com.fon.kartonpredmeta.exception.NotFoundException;
import com.fon.kartonpredmeta.mapper.PredmetMapper;
import com.fon.kartonpredmeta.repository.*;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class PredmetService {

    private final PredmetRepository predmetRepository;
    private final PredmetMapper predmetMapper;
    private final LiteraturaRepository literaturaRepository;
    private final IshodRepository ishodRepository;
    private final NastavnikRepository nastavnikRepository;
    private final IzvodjenjeRepository izvodjenjeRepository;


    public PredmetService(PredmetRepository predmetRepository, PredmetMapper predmetMapper, LiteraturaRepository literaturaRepository,
                          IshodRepository ishodRepository, NastavnikRepository nastavnikRepository, IzvodjenjeRepository izvodjenjeRepository) {

        this.predmetRepository = predmetRepository;
        this.predmetMapper = predmetMapper;
        this.literaturaRepository = literaturaRepository;
        this.ishodRepository = ishodRepository;
        this.nastavnikRepository = nastavnikRepository;
        this.izvodjenjeRepository = izvodjenjeRepository;
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
        predmet.setIshodi(this.nadjiIshodePoId(request.getIshodIds()));

        if (request.getLiteratura() != null) {
            predmet.setLiteratura(request.getLiteratura().stream().
                    map(this::kreirajIliPronadjiLiteraturu).toList());
        }
        Predmet saved = predmetRepository.save(predmet);
        List<Izvodjenje> izvodjenja = this.napraviIzvodjenje(request.getIzvodjenja(), saved);
        izvodjenjeRepository.saveAll(izvodjenja);
        saved.setIzvodjenja(izvodjenja);
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

        if (request.getNaziv() != null && request.getNaziv().isBlank()) {
            throw new BadRequestException("Naziv ne moze da bude prazan");
        }

        if (request.getSifra() != null && request.getSifra().isBlank()) {
            throw new BadRequestException("Sifra ne moze da bude prazna");
        }


        predmetMapper.update(request, predmet);

        if (request.getIshodIds() != null) {
            predmet.setIshodi(this.nadjiIshodePoId(request.getIshodIds()));
        }

        if (request.getLiteratura() != null) {
            List<Literatura> novaLiteratura = new ArrayList<>(request.getLiteratura().stream()
                    .map(this::kreirajIliPronadjiLiteraturu).toList());
            predmet.setLiteratura(novaLiteratura);
        }

        if (request.getIzvodjenja() != null) {
            for (IzvodjenjeRequest izvReq : request.getIzvodjenja()) {

                boolean duplikat = predmet.getIzvodjenja().stream().anyMatch(izvodjenje -> izvodjenje.getNastavnik().getId().equals(izvReq.getNastavnikId()) && izvodjenje.getOblikNastave().equals(izvReq.getOblikNastave()));

                if (duplikat) {
                    throw new ConflictException("Nastavnik sa id " + izvReq.getNastavnikId() + " vec drzi " + izvReq.getOblikNastave() + " na ovom predmetu");
                }

            }
            List<Izvodjenje> novaIzvodjenja = this.napraviIzvodjenje(request.getIzvodjenja(), predmet);
            izvodjenjeRepository.saveAll(novaIzvodjenja);
            predmet.getIzvodjenja().addAll(novaIzvodjenja);
        }
        predmetRepository.save(predmet);


        return predmetMapper.toResponse(predmet);
    }


    public void delete(Long id) {
        Predmet predmet = predmetRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("Predmet sa id=" + id + " ne postoji"));

        izvodjenjeRepository.deleteAll(predmet.getIzvodjenja());
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


    public List<Ishod> nadjiIshodePoId(List<Long> ids) {
        List<Ishod> rezultat = new ArrayList<>();

        for (Long id : ids) {
            Ishod ishod = ishodRepository.findById(id)
                    .orElseThrow(() -> new NotFoundException("Ishod sa id=" + id + " ne postoji"));

            rezultat.add(ishod);
        }

        return rezultat;
    }

    public List<Izvodjenje> napraviIzvodjenje(List<IzvodjenjeRequest> izvodjenjaRequest, Predmet predmet) {
        List<Izvodjenje> rezultat = new ArrayList<>();

        for (IzvodjenjeRequest izvodjenjeRequest : izvodjenjaRequest) {
            Nastavnik nastavnik = nastavnikRepository.findById(izvodjenjeRequest.getNastavnikId())
                    .orElseThrow(() -> new NotFoundException("Nastavnik ne postoji"));
            Izvodjenje izvodjenje = new Izvodjenje(null, izvodjenjeRequest.getOblikNastave(), predmet, nastavnik);
            rezultat.add(izvodjenje);

        }
        return rezultat;
    }

}




