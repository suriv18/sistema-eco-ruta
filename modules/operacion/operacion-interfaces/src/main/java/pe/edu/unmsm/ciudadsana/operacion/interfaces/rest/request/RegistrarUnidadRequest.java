package pe.edu.unmsm.ciudadsana.operacion.interfaces.rest.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotBlank;

import java.math.BigDecimal;

public record RegistrarUnidadRequest(
        @NotBlank String placa,
        String codigoInterno,
        @NotBlank String tipoUnidad,
        @DecimalMin("0.01") BigDecimal capacidadM3,
        @DecimalMin("0.01") BigDecimal capacidadKg
) {}
