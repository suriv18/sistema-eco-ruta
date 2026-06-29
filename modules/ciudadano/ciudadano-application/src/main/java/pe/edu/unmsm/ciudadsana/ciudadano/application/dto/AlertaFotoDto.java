package pe.edu.unmsm.ciudadsana.ciudadano.application.dto;

import java.util.UUID;

public record AlertaFotoDto(
    UUID id,
    String urlArchivo,
    String tipoMime,
    Long tamanioBytes
) {}
