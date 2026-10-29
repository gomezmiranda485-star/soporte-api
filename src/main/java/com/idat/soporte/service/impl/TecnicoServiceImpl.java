package com.idat.soporte.service.impl;

import com.idat.soporte.dto.TecnicoRequest;
import com.idat.soporte.exception.RecursoNoEncontradoException;
import com.idat.soporte.exception.ReglaNegocioException;
import com.idat.soporte.model.Tecnico;
import com.idat.soporte.service.TecnicoService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

/**
 * Simula la persistencia de técnicos en memoria usando un Map.
 */
@Service
public class TecnicoServiceImpl implements TecnicoService {

    private final Map<Long, Tecnico> almacen = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    public TecnicoServiceImpl() {
        guardar(new TecnicoRequest("Carlos Mendoza", "carlos.mendoza@soporte.com", "Redes", true));
        guardar(new TecnicoRequest("Ana Quispe", "ana.quispe@soporte.com", "Software", true));
    }

    @Override
    public List<Tecnico> listar() {
        List<Tecnico> lista = new ArrayList<>(almacen.values());
        lista.sort((a, b) -> a.getId().compareTo(b.getId()));
        return lista;
    }

    @Override
    public Tecnico obtenerPorId(Long id) {
        Tecnico tecnico = almacen.get(id);
        if (tecnico == null) {
            throw new RecursoNoEncontradoException("Técnico", id);
        }
        return tecnico;
    }

    @Override
    public Tecnico crear(TecnicoRequest request) {
        validarEmailUnico(request.email(), null);
        return guardar(request);
    }

    @Override
    public Tecnico actualizar(Long id, TecnicoRequest request) {
        Tecnico existente = obtenerPorId(id);
        validarEmailUnico(request.email(), id);
        existente.setNombre(request.nombre());
        existente.setEmail(request.email());
        existente.setEspecialidad(request.especialidad());
        existente.setDisponible(request.disponible());
        return existente;
    }

    @Override
    public void eliminar(Long id) {
        obtenerPorId(id);
        almacen.remove(id);
    }

    private Tecnico guardar(TecnicoRequest request) {
        Long id = secuencia.incrementAndGet();
        Tecnico tecnico = new Tecnico(id, request.nombre(), request.email(),
                request.especialidad(), request.disponible());
        almacen.put(id, tecnico);
        return tecnico;
    }

    private void validarEmailUnico(String email, Long idActual) {
        boolean duplicado = almacen.values().stream()
                .anyMatch(t -> t.getEmail().equalsIgnoreCase(email)
                        && (idActual == null || !t.getId().equals(idActual)));
        if (duplicado) {
            throw new ReglaNegocioException("Ya existe un técnico con el email " + email);
        }
    }
}
