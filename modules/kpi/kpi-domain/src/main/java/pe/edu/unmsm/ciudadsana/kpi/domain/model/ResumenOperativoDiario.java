package pe.edu.unmsm.ciudadsana.kpi.domain.model;

import pe.edu.unmsm.ciudadsana.kpi.domain.valueobject.ResumenId;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.model.AggregateRoot;
import pe.edu.unmsm.ciudadsana.shared.kernel.domain.valueobject.TenantId;

import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

public class ResumenOperativoDiario extends AggregateRoot<ResumenId> {

    private final ResumenId id;
    private final TenantId tenantId;
    private final UUID distritoIdExterno;
    private final LocalDate fecha;
    private BigDecimal kmProgramados;
    private BigDecimal kmRecorridos;
    private BigDecimal toneladasRecolectadas;
    private BigDecimal coberturaPorcentaje;
    private int alertasRegistradas;
    private int alertasAtendidas;
    private Optional<BigDecimal> tiempoRespuestaPromedioMin;
    private final Instant creadoEn;

    private ResumenOperativoDiario(
            ResumenId id,
            TenantId tenantId,
            UUID distritoIdExterno,
            LocalDate fecha,
            BigDecimal kmProgramados,
            BigDecimal kmRecorridos,
            BigDecimal toneladasRecolectadas,
            BigDecimal coberturaPorcentaje,
            int alertasRegistradas,
            int alertasAtendidas,
            Optional<BigDecimal> tiempoRespuestaPromedioMin,
            Instant creadoEn
    ) {
        this.id = id;
        this.tenantId = tenantId;
        this.distritoIdExterno = distritoIdExterno;
        this.fecha = fecha;
        this.kmProgramados = kmProgramados;
        this.kmRecorridos = kmRecorridos;
        this.toneladasRecolectadas = toneladasRecolectadas;
        this.coberturaPorcentaje = coberturaPorcentaje;
        this.alertasRegistradas = alertasRegistradas;
        this.alertasAtendidas = alertasAtendidas;
        this.tiempoRespuestaPromedioMin = tiempoRespuestaPromedioMin;
        this.creadoEn = creadoEn;
    }

    public static ResumenOperativoDiario create(
            ResumenId id,
            TenantId tenantId,
            UUID distritoIdExterno,
            LocalDate fecha
    ) {
        return new ResumenOperativoDiario(
                id, tenantId, distritoIdExterno, fecha,
                BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO, BigDecimal.ZERO,
                0, 0, Optional.empty(), Instant.now()
        );
    }

    public static ResumenOperativoDiario reconstitute(
            ResumenId id,
            TenantId tenantId,
            UUID distritoIdExterno,
            LocalDate fecha,
            BigDecimal kmProgramados,
            BigDecimal kmRecorridos,
            BigDecimal toneladasRecolectadas,
            BigDecimal coberturaPorcentaje,
            int alertasRegistradas,
            int alertasAtendidas,
            BigDecimal tiempoRespuestaPromedioMin,
            Instant creadoEn
    ) {
        return new ResumenOperativoDiario(
                id, tenantId, distritoIdExterno, fecha,
                kmProgramados, kmRecorridos, toneladasRecolectadas, coberturaPorcentaje,
                alertasRegistradas, alertasAtendidas,
                Optional.ofNullable(tiempoRespuestaPromedioMin), creadoEn
        );
    }

    public void actualizar(
            BigDecimal kmProgramados,
            BigDecimal kmRecorridos,
            BigDecimal toneladasRecolectadas,
            BigDecimal coberturaPorcentaje,
            int alertasRegistradas,
            int alertasAtendidas,
            BigDecimal tiempoRespuestaPromedioMin
    ) {
        this.kmProgramados = kmProgramados;
        this.kmRecorridos = kmRecorridos;
        this.toneladasRecolectadas = toneladasRecolectadas;
        this.coberturaPorcentaje = coberturaPorcentaje;
        this.alertasRegistradas = alertasRegistradas;
        this.alertasAtendidas = alertasAtendidas;
        this.tiempoRespuestaPromedioMin = Optional.ofNullable(tiempoRespuestaPromedioMin);
    }

    @Override
    public ResumenId getId() { return id; }
    public TenantId getTenantId() { return tenantId; }
    public UUID getDistritoIdExterno() { return distritoIdExterno; }
    public LocalDate getFecha() { return fecha; }
    public BigDecimal getKmProgramados() { return kmProgramados; }
    public BigDecimal getKmRecorridos() { return kmRecorridos; }
    public BigDecimal getToneladasRecolectadas() { return toneladasRecolectadas; }
    public BigDecimal getCoberturaPorcentaje() { return coberturaPorcentaje; }
    public int getAlertasRegistradas() { return alertasRegistradas; }
    public int getAlertasAtendidas() { return alertasAtendidas; }
    public Optional<BigDecimal> getTiempoRespuestaPromedioMin() { return tiempoRespuestaPromedioMin; }
    public Instant getCreadoEn() { return creadoEn; }
}
