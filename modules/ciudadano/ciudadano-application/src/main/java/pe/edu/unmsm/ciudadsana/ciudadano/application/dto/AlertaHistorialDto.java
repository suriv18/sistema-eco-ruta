package pe.edu.unmsm.ciudadsana.ciudadano.application.dto;

import java.time.Instant;
import java.util.UUID;

public record AlertaHistorialDto(
    UUID historialId,
    String estadoAnterior,
    String estadoNuevo,
    String comentario,
    UUID cambiadoPorUsuarioId,
    Instant cambiadoEn
) {}
