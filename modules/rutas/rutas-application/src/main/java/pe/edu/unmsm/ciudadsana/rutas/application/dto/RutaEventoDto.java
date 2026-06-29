package pe.edu.unmsm.ciudadsana.rutas.application.dto;

import java.time.Instant;
import java.util.UUID;

public record RutaEventoDto(
        UUID id,
        UUID rutaId,
        String tipoEvento,
        String descripcion,
        String datosJson,
        Instant creadoEn
) {}
