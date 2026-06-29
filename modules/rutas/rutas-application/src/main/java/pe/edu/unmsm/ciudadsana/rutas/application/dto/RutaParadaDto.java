package pe.edu.unmsm.ciudadsana.rutas.application.dto;

import java.time.Instant;
import java.util.UUID;

public record RutaParadaDto(
        UUID id,
        UUID rutaVersionId,
        UUID zonaId,
        UUID contenedorId,
        int orden,
        Instant eta,
        Instant horaLlegadaReal,
        Instant horaSalidaReal,
        double demandaEstimadaKg,
        double cargaAcumuladaKg,
        String estado,
        Instant creadoEn
) {}
