package com.idat.soporte;

import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD) // datos limpios en cada prueba
class SolicitudControllerTest {

    @Autowired
    private MockMvc mockMvc;

    private static final String VALIDA = """
            {"titulo":"Fallo en el servidor de correo",
             "descripcion":"No se pueden enviar correos desde la mañana",
             "prioridad":"ALTA","clienteId":1,"tecnicoId":1}""";

    @Test
    void crearSolicitudDevuelve201YLocation() throws Exception {
        mockMvc.perform(post("/api/v1/solicitudes").contentType(MediaType.APPLICATION_JSON).content(VALIDA))
                .andExpect(status().isCreated())
                .andExpect(header().exists("Location"))
                .andExpect(jsonPath("$.estado").value("PENDIENTE"))
                .andExpect(jsonPath("$.clienteNombre").value("María Torres"))
                .andExpect(jsonPath("$.tecnicoNombre").value("Carlos Mendoza"));
    }

    @Test
    void crudCompleto() throws Exception {
        mockMvc.perform(post("/api/v1/solicitudes").contentType(MediaType.APPLICATION_JSON).content(VALIDA))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/v1/solicitudes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[*].titulo").value(hasItem("Fallo en el servidor de correo")));

        String actualizado = """
                {"titulo":"Fallo en el servidor de correo",
                 "descripcion":"Reparado el relay SMTP",
                 "prioridad":"ALTA","estado":"RESUELTA","clienteId":1,"tecnicoId":2}""";
        mockMvc.perform(put("/api/v1/solicitudes/1").contentType(MediaType.APPLICATION_JSON).content(actualizado))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.estado").value("RESUELTA"));

        mockMvc.perform(delete("/api/v1/solicitudes/1")).andExpect(status().isNoContent());
        mockMvc.perform(get("/api/v1/solicitudes/1")).andExpect(status().isNotFound());
    }

    @Test
    void validacionDevuelve400ConDetalles() throws Exception {
        String invalida = """
                {"titulo":"","descripcion":"","clienteId":null}""";
        mockMvc.perform(post("/api/v1/solicitudes").contentType(MediaType.APPLICATION_JSON).content(invalida))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.mensaje").value("Los datos enviados no son válidos"))
                .andExpect(jsonPath("$.detalles").isArray());
    }

    @Test
    void solicitudInexistenteDevuelve404() throws Exception {
        mockMvc.perform(get("/api/v1/solicitudes/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.mensaje").value("Solicitud con id 999 no fue encontrado"));
    }

    @Test
    void clienteInexistenteDevuelve404() throws Exception {
        String conClienteFalso = VALIDA.replace("\"clienteId\":1", "\"clienteId\":99");
        mockMvc.perform(post("/api/v1/solicitudes").contentType(MediaType.APPLICATION_JSON).content(conClienteFalso))
                .andExpect(status().isNotFound());
    }

    @Test
    void estadoInvalidoEnFiltroDevuelve400() throws Exception {
        mockMvc.perform(get("/api/v1/solicitudes?estado=XYZ"))
                .andExpect(status().isBadRequest());
    }
}
