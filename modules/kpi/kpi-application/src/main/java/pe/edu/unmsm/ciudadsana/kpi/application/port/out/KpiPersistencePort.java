package pe.edu.unmsm.ciudadsana.kpi.application.port.out;

import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiAlertaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiRutaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiUnidadDto;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.KpiZonaDto;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.ResumenOperativoDto;
import pe.edu.unmsm.ciudadsana.shared.result.PageResult;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public interface KpiPersistencePort {

    Optional<ResumenOperativoDto> findResumenByDistritoAndFecha(UUID tenantId, UUID distritoId, LocalDate fecha);

    ResumenOperativoDto saveResumen(ResumenOperativoDto resumen);

    PageResult<KpiRutaDto> findKpisRuta(UUID tenantId, LocalDate fechaDesde, LocalDate fechaHasta, int page, int size);

    PageResult<KpiUnidadDto> findKpisUnidad(UUID tenantId, UUID unidadId, LocalDate fechaDesde, LocalDate fechaHasta, int page, int size);

    PageResult<KpiZonaDto> findKpisZona(UUID tenantId, UUID zonaId, LocalDate fechaDesde, LocalDate fechaHasta, int page, int size);

    PageResult<KpiAlertaDto> findKpisAlerta(UUID tenantId, UUID zonaId, LocalDate fechaDesde, LocalDate fechaHasta, int page, int size);
}
