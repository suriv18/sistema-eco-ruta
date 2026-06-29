package pe.edu.unmsm.ciudadsana.telemetria.interfaces.rest.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RegistrarDispositivoRequest(
        @NotNull UUID unidadExternoId,
        @Size(max = 50) String imei,
        @Size(max = 80) String proveedor
) {}
