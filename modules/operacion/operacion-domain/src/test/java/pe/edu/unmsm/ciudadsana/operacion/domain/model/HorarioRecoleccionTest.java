package pe.edu.unmsm.ciudadsana.operacion.domain.model;

import org.junit.jupiter.api.Test;
import pe.edu.unmsm.ciudadsana.operacion.domain.enums.EstadoHorario;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.HorarioRecoleccionId;
import pe.edu.unmsm.ciudadsana.operacion.domain.valueobject.ZonaId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.time.Instant;
import java.time.LocalTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class HorarioRecoleccionTest {

    private HorarioRecoleccion horarioActivo() {
        return HorarioRecoleccion.reconstitute(
                HorarioRecoleccionId.of(UUID.randomUUID()),
                TenantId.of(UUID.randomUUID()),
                ZonaId.of(UUID.randomUUID()),
                1, LocalTime.of(8, 0), LocalTime.of(12, 0),
                "observacion", EstadoHorario.ACTIVO, Instant.now(), null);
    }

    @Test
    void create_conDatosValidos_creaHorarioActivo() {
        HorarioRecoleccion h = HorarioRecoleccion.create(
                HorarioRecoleccionId.of(UUID.randomUUID()),
                TenantId.of(UUID.randomUUID()),
                ZonaId.of(UUID.randomUUID()),
                3, LocalTime.of(7, 0), LocalTime.of(10, 0), "lunes", Instant.now());

        assertThat(h.getEstado()).isEqualTo(EstadoHorario.ACTIVO);
        assertThat(h.getDiaSemana()).isEqualTo(3);
    }

    @Test
    void create_diaSemanaFueraDeRango_lanzaExcepcion() {
        assertThatThrownBy(() -> HorarioRecoleccion.create(
                HorarioRecoleccionId.of(UUID.randomUUID()),
                TenantId.of(UUID.randomUUID()),
                ZonaId.of(UUID.randomUUID()),
                8, LocalTime.of(8, 0), LocalTime.of(12, 0), null, Instant.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("diaSemana");
    }

    @Test
    void create_horaFinAnteriorAInicio_lanzaExcepcion() {
        assertThatThrownBy(() -> HorarioRecoleccion.create(
                HorarioRecoleccionId.of(UUID.randomUUID()),
                TenantId.of(UUID.randomUUID()),
                ZonaId.of(UUID.randomUUID()),
                1, LocalTime.of(12, 0), LocalTime.of(8, 0), null, Instant.now()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("horaFin");
    }

    @Test
    void actualizar_conRangoValido_actualizaCampos() {
        HorarioRecoleccion h = horarioActivo();

        h.actualizar(LocalTime.of(9, 0), LocalTime.of(13, 0), "nueva observacion");

        assertThat(h.getHoraInicio()).isEqualTo(LocalTime.of(9, 0));
        assertThat(h.getHoraFin()).isEqualTo(LocalTime.of(13, 0));
        assertThat(h.getObservacion()).isEqualTo("nueva observacion");
        assertThat(h.getActualizadoEn()).isNotNull();
    }

    @Test
    void actualizar_horaFinIgualAInicio_lanzaExcepcion() {
        HorarioRecoleccion h = horarioActivo();

        assertThatThrownBy(() -> h.actualizar(LocalTime.of(8, 0), LocalTime.of(8, 0), null))
                .isInstanceOf(IllegalArgumentException.class);
    }

    @Test
    void desactivar_horarioActivo_cambiaEstado() {
        HorarioRecoleccion h = horarioActivo();

        h.desactivar();

        assertThat(h.getEstado()).isEqualTo(EstadoHorario.INACTIVO);
        assertThat(h.getActualizadoEn()).isNotNull();
    }

    @Test
    void desactivar_horarioYaInactivo_lanzaExcepcion() {
        HorarioRecoleccion h = HorarioRecoleccion.reconstitute(
                HorarioRecoleccionId.of(UUID.randomUUID()),
                TenantId.of(UUID.randomUUID()),
                ZonaId.of(UUID.randomUUID()),
                1, LocalTime.of(8, 0), LocalTime.of(12, 0),
                null, EstadoHorario.INACTIVO, Instant.now(), null);

        assertThatThrownBy(h::desactivar)
                .isInstanceOf(IllegalStateException.class)
                .hasMessageContaining("INACTIVO");
    }
}
