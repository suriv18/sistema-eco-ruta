package pe.edu.unmsm.ciudadsana.rutas.application.command;

import java.util.UUID;

public record RegistrarEventoRutaCommand(
        UUID rutaId,
        UUID tenantId,
        String tipoEvento,
        String descripcion,
        String datosJson
) {}
