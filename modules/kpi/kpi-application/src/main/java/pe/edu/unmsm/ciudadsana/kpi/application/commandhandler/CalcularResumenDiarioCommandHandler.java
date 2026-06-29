package pe.edu.unmsm.ciudadsana.kpi.application.commandhandler;

import org.springframework.stereotype.Component;
import pe.edu.unmsm.ciudadsana.kpi.application.command.CalcularResumenDiarioCommand;
import pe.edu.unmsm.ciudadsana.kpi.application.dto.ResumenOperativoDto;
import pe.edu.unmsm.ciudadsana.kpi.application.port.in.CalcularResumenDiarioUseCase;
import pe.edu.unmsm.ciudadsana.kpi.application.port.out.KpiPersistencePort;
import pe.edu.unmsm.ciudadsana.kpi.domain.model.ResumenOperativoDiario;
import pe.edu.unmsm.ciudadsana.kpi.domain.valueobject.ResumenId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;
import pe.edu.unmsm.ciudadsana.shared.result.Result;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Optional;

@Component
public class CalcularResumenDiarioCommandHandler implements CalcularResumenDiarioUseCase {

    private final KpiPersistencePort kpiPersistencePort;

    public CalcularResumenDiarioCommandHandler(KpiPersistencePort kpiPersistencePort) {
        this.kpiPersistencePort = kpiPersistencePort;
    }

    @Override
    public Result<ResumenOperativoDto> calcular(CalcularResumenDiarioCommand cmd) {
        Optional<ResumenOperativoDto> existente = kpiPersistencePort
                .findResumenByDistritoAndFecha(cmd.tenantId(), cmd.distritoId(), cmd.fecha());

        ResumenOperativoDto dto;
        if (existente.isPresent()) {
            ResumenOperativoDto e = existente.get();
            dto = new ResumenOperativoDto(
                    e.resumenId(), e.tenantId(), e.distritoIdExterno(), e.fecha(),
                    e.kmProgramados(), e.kmRecorridos(), e.toneladasRecolectadas(),
                    e.coberturaPorcentaje(), e.alertasRegistradas(), e.alertasAtendidas(),
                    e.tiempoRespuestaPromedioMin(), e.creadoEn()
            );
        } else {
            dto = new ResumenOperativoDto(
                    ResumenId.generate().value(), cmd.tenantId(), cmd.distritoId(), cmd.fecha(),
                    BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                    0, 0, null, Instant.now()
            );
        }

        ResumenOperativoDto saved = kpiPersistencePort.saveResumen(dto);
        return Result.success(saved);
    }
}
