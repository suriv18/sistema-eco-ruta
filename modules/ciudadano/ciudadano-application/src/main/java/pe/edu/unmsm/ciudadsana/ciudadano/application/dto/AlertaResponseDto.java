package pe.edu.unmsm.ciudadsana.ciudadano.application.dto;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public record AlertaResponseDto(
    UUID id,
    UUID tenantId,
    UUID ciudadanoId,
    UUID distritoExternoId,
    UUID zonaExternoId,
    String titulo,
    String descripcion,
    double latitud,
    double longitud,
    String volumenEstimado,
    String nivelCriticidad,
    String fuente,
    String estado,
    List<AlertaFotoDto> fotos,
    List<AlertaHistorialDto> historial,
    ValidacionAlertaDto validacion,
    Instant registradaEn,
    Instant actualizadaEn
) {}
