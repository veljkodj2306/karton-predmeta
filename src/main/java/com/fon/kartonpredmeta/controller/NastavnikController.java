package com.fon.kartonpredmeta.controller;


import com.fon.kartonpredmeta.dto.NastavnikDTO;
import com.fon.kartonpredmeta.entity.Nastavnik;
import com.fon.kartonpredmeta.exception.ApiError;
import com.fon.kartonpredmeta.mapper.NastavnikMapper;
import com.fon.kartonpredmeta.service.NastavnikService;
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
@RequestMapping("/api/nastavnici")
@Tag(name = "Nastavnici", description = "CRUD nad nastavnicima")
public class NastavnikController {

    private final NastavnikService nastavnikService;
    private final NastavnikMapper nastavnikMapper;

    public NastavnikController(NastavnikService nastavnikService, NastavnikMapper nastavnikMapper) {
        this.nastavnikService = nastavnikService;
        this.nastavnikMapper = nastavnikMapper;
    }

    @Operation(summary = "Lista svih nastavnika")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Uspesno vracena lista")})
    @GetMapping
    public List<NastavnikDTO> getNastavnici() {
        return nastavnikService.findAll();

    }


    @Operation(summary = "Vraca nastavnika po ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Nastavnik pronadjen"),
            @ApiResponse(responseCode = "404", description = "Nastavnik ne postoji",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @GetMapping("/{id}")
    public NastavnikDTO getNastavnik(@PathVariable Long id) {
        return nastavnikService.findById(id);
    }


    @Operation(summary = "Kreiranje nastavnika")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Nastavnik kreiran"),
            @ApiResponse(responseCode = "400", description = "Validacija nije prosla",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public NastavnikDTO createNastavnik(@Valid @RequestBody NastavnikDTO nastavnikDTO) {
        Nastavnik nastavnik = nastavnikMapper.toEntity(nastavnikDTO);
        return nastavnikService.save(nastavnik);
    }


    @Operation(summary = "Brisanje nastavnika")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Nastavnik obrisan"),
            @ApiResponse(responseCode = "404", description = "Nastavnik ne postoji",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteNastavnik(@PathVariable Long id) {
        nastavnikService.deleteById(id);
    }


    @Operation(summary = "Azuriranje nastavnika")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Nastavnik azuriran"),
            @ApiResponse(responseCode = "404", description = "Nastavnik ne postoji",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @PutMapping("/{id}")
    public NastavnikDTO updateNastavnik(@PathVariable Long id, @Valid @RequestBody NastavnikDTO nastavnikDTO) {
        return nastavnikService.updateById(nastavnikDTO, id);
    }
}
