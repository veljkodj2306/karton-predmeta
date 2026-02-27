package com.fon.kartonpredmeta.controller;


import com.fon.kartonpredmeta.dto.PredmetCreateRequest;
import com.fon.kartonpredmeta.dto.PredmetResponse;
import com.fon.kartonpredmeta.dto.PredmetUpdateRequest;
import com.fon.kartonpredmeta.exception.ApiError;
import com.fon.kartonpredmeta.service.PredmetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/predmeti")
@Tag(name = "Predmeti", description = "CRUD nad predmetima")
public class PredmetController {


    private final PredmetService predmetService;


    public PredmetController(PredmetService predmetService) {
        this.predmetService = predmetService;
    }


    @Operation(summary = "Lista predmeta")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Uspesno vracena lista")})
    @GetMapping
    public List<PredmetResponse> getPredmeti() {
        return predmetService.findAll();
    }


    @Operation(summary = "Vraca predmet po ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Predmet pronadjen"),
            @ApiResponse(responseCode = "404", description = "Predmet ne postoji",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @GetMapping("/{id}")
    public PredmetResponse getPredmet(@PathVariable Long id) {
        return predmetService.findById(id);
    }


    @Operation(summary = "Kreiramo predmet")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Predmet kreiran"),
            @ApiResponse(responseCode = "400",
                    description = "Validacija nije prosla", content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Sifra vec postoji",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @PostMapping()
    @ResponseStatus(HttpStatus.CREATED)
    public PredmetResponse createPredmet(@Valid @RequestBody PredmetCreateRequest request) {
        return predmetService.create(request);

    }


    @Operation(summary = "Azuriramo postojeci predmet")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Predmet azuriran"),
            @ApiResponse(responseCode = "400", description = "Validacija nije prosla",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "404", description = "Predmet ne postoji",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),
            @ApiResponse(responseCode = "409", description = "Sifra vec postoji",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @PutMapping("/{id}")
    public PredmetResponse updatePredmet(@Valid @RequestBody PredmetUpdateRequest request, @PathVariable Long id) {
        return predmetService.update(id, request);

    }


    @Operation(summary = "Brisanje predmeta")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Predmet obrisan"),
            @ApiResponse(responseCode = "404", description = "Predmet ne postoji",
                    content = @Content(schema = @Schema(implementation = ApiError.class))),})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePredmet(@PathVariable Long id) {
        predmetService.delete(id);
    }


}
