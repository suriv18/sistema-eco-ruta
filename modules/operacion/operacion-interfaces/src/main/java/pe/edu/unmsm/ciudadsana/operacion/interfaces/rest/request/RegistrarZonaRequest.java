package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RegistrarZonaRequest(
        @NotNull UUID distritoId,
        @NotBlank @Size(max = 50) String codigo,
        @NotBlank @Size(max = 150) String nombre,
        @NotBlank String tipoZona,
        @Min(1) int prioridad
) {}
