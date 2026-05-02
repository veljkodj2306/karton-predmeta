package com.fon.kartonpredmeta.controller;


import com.fon.kartonpredmeta.dto.IshodDTO;
import com.fon.kartonpredmeta.exception.ApiError;
import com.fon.kartonpredmeta.service.IshodService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ishodi")
@Tag(name = "Ishodi", description = "CRUD nad ishodima")
public class IshodController {

    private final IshodService ishodService;

    public IshodController(IshodService ishodService) {
        this.ishodService = ishodService;
    }


    @Operation(summary = "Lista svih ishoda")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Uspesno vracena lista")})
    @GetMapping
    public List<IshodDTO> getAllIshod() {
        return ishodService.getAllIshod();
    }


    @Operation(summary = "Vraca ishod po ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Ishod pronadjen"),
            @ApiResponse(responseCode = "404", description = "Ishod ne postoji",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @GetMapping("/{id}")
    public IshodDTO getIshodById(@PathVariable Long id) {
        return ishodService.getIshodById(id);
    }


    @Operation(summary = "Kreiranje ishoda")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Ishod kreiran"),
            @ApiResponse(responseCode = "400", description = "Validacija nije prosla",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public IshodDTO saveIshod(@RequestBody IshodDTO ishodDTO) {
        return ishodService.saveIshod(ishodDTO);
    }


    @Operation(summary = "Brisanje ishoda")
    @ApiResponses(value = {@ApiResponse(responseCode = "204", description = "Ishod obrisan"),
            @ApiResponse(responseCode = "404", description = "Ishod ne postoji",
                    content = @Content(schema = @Schema(implementation = ApiError.class)))})
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteIshodById(@PathVariable Long id) {
        ishodService.deleteIshodById(id);
    }
}
