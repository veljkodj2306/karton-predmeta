package com.fon.kartonpredmeta.repository;

import com.fon.kartonpredmeta.entity.Literatura;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LiteraturaRepository extends JpaRepository<Literatura, Long> {

    Literatura findByNaslovAndAutorAndGodina(String naslov, String autor, int godina);

}