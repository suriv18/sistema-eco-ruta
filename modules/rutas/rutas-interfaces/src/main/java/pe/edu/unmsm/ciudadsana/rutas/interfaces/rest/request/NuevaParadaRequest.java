package pe.edu.unmsm.ciudadsana.rutas.interfaces.rest.request;

import jakarta.validation.constraints.NotNull;

import java.time.Instant;
import java.util.UUID;

public record NuevaParadaRequest(
        @NotNull UUID zonaId,
        UUID contenedorId,
        @NotNull Integer orden,
        Instant eta,
        double demandaEstimadaKg
) {}
