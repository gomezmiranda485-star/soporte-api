package com.idat.soporte.service.impl;

import com.idat.soporte.dto.ClienteRequest;
import com.idat.soporte.exception.RecursoNoEncontradoException;
import com.idat.soporte.exception.ReglaNegocioException;
import com.idat.soporte.model.Cliente;
import com.idat.soporte.service.ClienteService;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;
import org.springframework.stereotype.Service;

/**
 * Simula la persistencia de clientes en memoria usando un Map.
 * El generador de ids (AtomicLong) imita el autoincremento de una base de datos.
 */
@Service
public class ClienteServiceImpl implements ClienteService {

    private final Map<Long, Cliente> almacen = new ConcurrentHashMap<>();
    private final AtomicLong secuencia = new AtomicLong(0);

    public ClienteServiceImpl() {
        // Datos de ejemplo para poder probar la API de inmediato
        guardar(new ClienteRequest("María Torres", "maria.torres@acme.com", "ACME S.A.", "987654321"));
        guardar(new ClienteRequest("Luis Ramírez", "luis.ramirez@globex.com", "Globex Perú", "912345678"));
    }

    @Override
    public List<Cliente> listar() {
        List<Cliente> lista = new ArrayList<>(almacen.values());
        lista.sort((a, b) -> a.getId().compareTo(b.getId()));
        return lista;
    }

    @Override
    public Cliente obtenerPorId(Long id) {
        Cliente cliente = almacen.get(id);
        if (cliente == null) {
            throw new RecursoNoEncontradoException("Cliente", id);
        }
        return cliente;
    }

    @Override
    public Cliente crear(ClienteRequest request) {
        validarEmailUnico(request.email(), null);
        return guardar(request);
    }

    @Override
    public Cliente actualizar(Long id, ClienteRequest request) {
        Cliente existente = obtenerPorId(id);
        validarEmailUnico(request.email(), id);
        existente.setNombre(request.nombre());
        existente.setEmail(request.email());
        existente.setEmpresa(request.empresa());
        existente.setTelefono(request.telefono());
        return existente;
    }

    @Override
    public void eliminar(Long id) {
        obtenerPorId(id);
        almacen.remove(id);
    }

    private Cliente guardar(ClienteRequest request) {
        Long id = secuencia.incrementAndGet();
        Cliente cliente = new Cliente(id, request.nombre(), request.email(),
                request.empresa(), request.telefono());
        almacen.put(id, cliente);
        return cliente;
    }

    private void validarEmailUnico(String email, Long idActual) {
        boolean duplicado = almacen.values().stream()
                .anyMatch(c -> c.getEmail().equalsIgnoreCase(email)
                        && (idActual == null || !c.getId().equals(idActual)));
        if (duplicado) {
            throw new ReglaNegocioException("Ya existe un cliente con el email " + email);
        }
    }
}
