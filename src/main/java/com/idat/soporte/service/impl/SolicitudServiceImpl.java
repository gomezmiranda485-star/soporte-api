package com.idat.soporte.service.impl;

import com.idat.soporte.dto.SolicitudRequest;
import com.idat.soporte.dto.SolicitudResponse;
import com.idat.soporte.exception.RecursoNoEncontradoException;
import com.idat.soporte.exception.ReglaNegocioException;
import com.idat.soporte.model.Cliente;
import com.idat.soporte.model.Estado;
import com.idat.soporte.model.Solicitud;
import com.idat.soporte.model.Tecnico;
import com.idat.soporte.service.ClienteService;
import com.idat.soporte.service.SolicitudService;
import com.idat.soporte.service.TecnicoService;
import java.time.LocalDateTime;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

/**
 * Lógica de negocio de las solicitudes. Simula la base de datos con un Map
 * en memoria y valida las relaciones con Cliente y Técnico.
 */
@Service
public class SolicitudServiceImpl implements SolicitudService {

    private final Map<Long, Solicitud> almacen = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    private final ClienteService clienteService;
    private final TecnicoService tecnicoService;

    public SolicitudServiceImpl(ClienteService clienteService, TecnicoService tecnicoService) {
        this.clienteService = clienteService;
        this.tecnicoService = tecnicoService;
    }

    @Override
    public List<SolicitudResponse> listar(Estado estado) {
        return almacen.values().stream()
                .filter(s -> estado == null || s.getEstado() == estado)
                .sorted(Comparator.comparing(Solicitud::getId))
                .map(this::aRespuesta)
                .toList();
    }

    @Override
    public SolicitudResponse obtenerPorId(Long id) {
        return aRespuesta(buscar(id));
    }

    @Override
    public SolicitudResponse crear(SolicitudRequest request) {
        validarRelaciones(request);
        Long id = secuencia.incrementAndGet();
        Estado estado = request.estado() != null ? request.estado() : Estado.PENDIENTE;
        Solicitud solicitud = new Solicitud(id, request.titulo(), request.descripcion(),
                request.prioridad(), estado, request.clienteId(), request.tecnicoId());
        almacen.put(id, solicitud);
        return aRespuesta(solicitud);
    }

    @Override
    public SolicitudResponse actualizar(Long id, SolicitudRequest request) {
        Solicitud existente = buscar(id);
        if (existente.getEstado() == Estado.CANCELADA) {
            throw new ReglaNegocioException("No se puede modificar una solicitud CANCELADA");
        }
        validarRelaciones(request);
        existente.setTitulo(request.titulo());
        existente.setDescripcion(request.descripcion());
        existente.setPrioridad(request.prioridad());
        if (request.estado() != null) {
            existente.setEstado(request.estado());
        }
        existente.setClienteId(request.clienteId());
        existente.setTecnicoId(request.tecnicoId());
        existente.setFechaActualizacion(LocalDateTime.now());
        return aRespuesta(existente);
    }

    @Override
    public void eliminar(Long id) {
        buscar(id);
        almacen.remove(id);
    }

    private Solicitud buscar(Long id) {
        Solicitud solicitud = almacen.get(id);
        if (solicitud == null) {
            throw new RecursoNoEncontradoException("Solicitud", id);
        }
        return solicitud;
    }

    private void validarRelaciones(SolicitudRequest request) {
        clienteService.obtenerPorId(request.clienteId());
        if (request.tecnicoId() != null) {
            Tecnico tecnico = tecnicoService.obtenerPorId(request.tecnicoId());
            if (!tecnico.isDisponible()) {
                throw new ReglaNegocioException(
                        "El técnico " + tecnico.getNombre() + " no está disponible");
            }
        }
    }

    private SolicitudResponse aRespuesta(Solicitud s) {
        Cliente cliente = clienteService.obtenerPorId(s.getClienteId());
        String tecnicoNombre = null;
        if (s.getTecnicoId() != null) {
            tecnicoNombre = tecnicoService.obtenerPorId(s.getTecnicoId()).getNombre();
        }
        return new SolicitudResponse(s.getId(), s.getTitulo(), s.getDescripcion(),
                s.getPrioridad(), s.getEstado(), s.getClienteId(), cliente.getNombre(),
                s.getTecnicoId(), tecnicoNombre, s.getFechaCreacion(), s.getFechaActualizacion());
    }
}
