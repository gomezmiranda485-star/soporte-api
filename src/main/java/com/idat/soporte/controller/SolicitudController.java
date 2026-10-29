package com.idat.soporte.controller;

import com.idat.soporte.dto.SolicitudRequest;
import com.idat.soporte.dto.SolicitudResponse;
import com.idat.soporte.model.Estado;
import com.idat.soporte.service.SolicitudService;
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
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/solicitudes")
@Tag(name = "Solicitudes", description = "Gestión de solicitudes de soporte técnico")
public class SolicitudController {

    private final SolicitudService solicitudService;

    public SolicitudController(SolicitudService solicitudService) {
        this.solicitudService = solicitudService;
    }

    @Operation(summary = "Listar solicitudes", description = "Permite filtrar por estado con ?estado=PENDIENTE")
    @GetMapping
    public ResponseEntity<List<SolicitudResponse>> listar(@RequestParam(required = false) Estado estado) {
        return ResponseEntity.ok(solicitudService.listar(estado));
    }

    @Operation(summary = "Obtener una solicitud por id")
    @GetMapping("/{id}")
    public ResponseEntity<SolicitudResponse> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(solicitudService.obtenerPorId(id));
    }

    @Operation(summary = "Registrar una nueva solicitud")
    @PostMapping
    public ResponseEntity<SolicitudResponse> crear(@Valid @RequestBody SolicitudRequest request) {
        SolicitudResponse creada = solicitudService.crear(request);
        return ResponseEntity.created(URI.create("/api/v1/solicitudes/" + creada.id())).body(creada);
    }

    @Operation(summary = "Actualizar una solicitud existente")
    @PutMapping("/{id}")
    public ResponseEntity<SolicitudResponse> actualizar(@PathVariable Long id,
                                                        @Valid @RequestBody SolicitudRequest request) {
        return ResponseEntity.ok(solicitudService.actualizar(id, request));
    }

    @Operation(summary = "Eliminar una solicitud")
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> eliminar(@PathVariable Long id) {
        solicitudService.eliminar(id);
        return ResponseEntity.noContent().build();
    }
}
