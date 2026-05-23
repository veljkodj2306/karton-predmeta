package com.fon.kartonpredmeta.controller;


import com.fon.kartonpredmeta.dto.PredmetCreateRequest;
import com.fon.kartonpredmeta.dto.PredmetResponse;
import com.fon.kartonpredmeta.dto.PredmetUpdateRequest;
import com.fon.kartonpredmeta.exception.ApiError;
import com.fon.kartonpredmeta.service.PredmetService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import org.springframework.http.HttpStatus;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/predmeti")
@Tag(name = "Predmeti", description = "CRUD nad predmetima")
@Validated
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


    @Operation(summary = "Trazenje predmeta na osnovu naslova literature")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Predmeti pronadjeni"),
            @ApiResponse(responseCode = "400", description = "Naslov nije pravilno unet",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @GetMapping("/trazi-po-literaturi")
    public List<PredmetResponse> traziPoLiteraturi(@Parameter(description = "Naslov literature po kom se pretrazuju predmeti ")
                                                   @RequestParam @NotBlank(message = "Naslov literature je obavezan") String naslov) {

        return predmetService.traziPoLiteraturi(naslov);
    }


    @Operation(summary = "Brisanje izvodjenja sa predmeta")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Izvodjenje obrisano"),
            @ApiResponse(responseCode = "404", description = "Izvodjenje ne postoji",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @DeleteMapping("/izvodjenja/{izvodjenjeId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIzvodjenje(@PathVariable Long izvodjenjeId) {
        predmetService.deleteIzvodjenje(izvodjenjeId);
    }


    @Operation(summary = "Brisanje literature sa predmeta")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Literatura obrisana"),
            @ApiResponse(responseCode = "404", description = "Literatura nije pronadjena",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @DeleteMapping("/literatura/{literaturaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteLiteratura(@PathVariable Long literaturaId) {
        predmetService.deleteLiteraturaOdPredmeta(literaturaId);
    }

}
