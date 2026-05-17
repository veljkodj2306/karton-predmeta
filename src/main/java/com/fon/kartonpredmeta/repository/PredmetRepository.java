package com.fon.kartonpredmeta.repository;

import com.fon.kartonpredmeta.entity.Predmet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PredmetRepository extends JpaRepository<Predmet, Long> {

    Optional<Predmet> findBySifra(String sifra);

    boolean existsBySifra(String sifra);

    List<Predmet> findByIshodi_Id(Long ishodId);
}
