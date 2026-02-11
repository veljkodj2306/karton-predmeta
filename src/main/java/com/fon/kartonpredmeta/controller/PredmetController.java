package com.fon.kartonpredmeta.controller;


import com.fon.kartonpredmeta.dto.PredmetCreateRequest;
import com.fon.kartonpredmeta.dto.PredmetResponse;
import com.fon.kartonpredmeta.dto.PredmetUpdateRequest;
import com.fon.kartonpredmeta.service.PredmetService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/predmeti")
public class PredmetController {

    private final PredmetService predmetService;

    public PredmetController(PredmetService predmetService) {
        this.predmetService = predmetService;
    }


    @GetMapping
    public List<PredmetResponse> getPredmeti() {
        return predmetService.findAll();
    }

    @GetMapping("/{id}")
    public PredmetResponse getPredmet(@PathVariable Long id) {
        return predmetService.findById(id);
    }

    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public PredmetResponse createPredmet(@Valid @RequestBody PredmetCreateRequest request) {
        return predmetService.create(request);

    }

    @PutMapping("/{id}")
    public PredmetResponse updatePredmet(@Valid @RequestBody PredmetUpdateRequest request, @PathVariable Long id) {
        return predmetService.update(id, request);

    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePredmet(@PathVariable Long id) {
        predmetService.delete(id);
    }


}
