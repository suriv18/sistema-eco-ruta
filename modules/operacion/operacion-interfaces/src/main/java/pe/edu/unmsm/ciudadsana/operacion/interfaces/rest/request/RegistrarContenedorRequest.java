package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.math.BigDecimal;
import java.util.UUID;

public record RegistrarContenedorRequest(
        @NotNull UUID zonaId,
        @NotBlank @Size(max = 50) String codigo,
        @DecimalMin("0.01") BigDecimal capacidadM3
) {}
