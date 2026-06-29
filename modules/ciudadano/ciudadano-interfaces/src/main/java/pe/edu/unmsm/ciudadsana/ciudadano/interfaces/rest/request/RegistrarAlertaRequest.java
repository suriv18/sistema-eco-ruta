package pe.edu.unmsm.ciudadsana.ciudadano.interfaces.rest.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;

import java.util.UUID;

public record RegistrarAlertaRequest(
        UUID ciudadanoId,
        @NotNull UUID distritoExternoId,
        UUID zonaExternoId,
        @NotBlank @Size(max = 150) String titulo,
        String descripcion,
        @NotNull Double latitud,
        @NotNull Double longitud,
        @NotBlank String volumenEstimado,
        @NotBlank String nivelCriticidad,
        @NotBlank String fuente
) {}
