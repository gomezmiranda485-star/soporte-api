package com.idat.soporte.controller;

import com.idat.soporte.dto.TecnicoRequest;
import com.idat.soporte.model.Tecnico;
import com.idat.soporte.service.TecnicoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.net.URI;
import java.util.List;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/tecnicos")
@Tag(name = "Técnicos", description = "Gestión de técnicos que atienden las solicitudes")
public class TecnicoController {

    private final TecnicoService tecnicoService;

    public TecnicoController(TecnicoService tecnicoService) {
        this.tecnicoService = tecnicoService;
    }

    @Operation(summary = "Listar técnicos")
    @GetMapping
    public ResponseEntity<List<Tecnico>> listar() {
        return ResponseEntity.ok(tecnicoService.listar());
    }

    @Operation(summary = "Obtener un técnico por id")
    @GetMapping("/{id}")
    public ResponseEntity<Tecnico> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(tecnicoService.obtenerPorId(id));
    }

    @Operation(summary = "Registrar un técnico")
    @PostMapping
    public ResponseEntity<Tecnico> crear(@Valid @RequestBody TecnicoRequest request) {
        Tecnico creado = tecnicoService.crear(request);
        return ResponseEntity.created(URI.create("/api/v1/tecnicos/" + creado.getId())).body(creado);
    }

    @Operation(summary = "Actualizar un técnico")
    @PutMapping("/{id}")
    public ResponseEntity<Tecnico> actualizar(@PathVariable Long id,
                                              @Valid @RequestBody TecnicoRequest request) {
        return ResponseEntity.ok(tecnicoService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar un técnico")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        tecnicoService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
