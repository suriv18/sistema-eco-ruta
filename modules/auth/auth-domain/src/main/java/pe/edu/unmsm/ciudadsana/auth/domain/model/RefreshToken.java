package pe.edu.unmsm.ciudadsana.auth.domain.model;

import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.RefreshTokenId;
import pe.edu.unmsm.ciudadsana.auth.domain.valueobject.UsuarioId;

import java.time.Instant;

public class RefreshToken {

    private final RefreshTokenId id;
    private final UsuarioId usuarioId;
    private final String tokenHash;
    private final Instant expiraEn;
    private boolean revocado;
    private final Instant creadoEn;
    private Instant revocadoEn;

    private RefreshToken(
            RefreshTokenId id,
            UsuarioId usuarioId,
            String tokenHash,
            Instant expiraEn,
            boolean revocado,
            Instant creadoEn,
            Instant revocadoEn
    ) {
        this.id = id;
        this.usuarioId = usuarioId;
        this.tokenHash = tokenHash;
        this.expiraEn = expiraEn;
        this.revocado = revocado;
        this.creadoEn = creadoEn;
        this.revocadoEn = revocadoEn;
    }

    public static RefreshToken create(
            RefreshTokenId id,
            UsuarioId usuarioId,
            String tokenHash,
            Instant expiraEn,
            Instant creadoEn
    ) {
        if (id == null) throw new IllegalArgumentException("RefreshTokenId no puede ser nulo");
        if (usuarioId == null) throw new IllegalArgumentException("UsuarioId no puede ser nulo");
        if (tokenHash == null || tokenHash.isBlank()) throw new IllegalArgumentException("El tokenHash no puede ser nulo o vacío");
        if (expiraEn == null) throw new IllegalArgumentException("La fecha de expiración no puede ser nula");
        if (creadoEn == null) throw new IllegalArgumentException("La fecha de creación no puede ser nula");
        return new RefreshToken(id, usuarioId, tokenHash, expiraEn, false, creadoEn, null);
    }

    public static RefreshToken reconstitute(
            RefreshTokenId id,
            UsuarioId usuarioId,
            String tokenHash,
            Instant expiraEn,
            boolean revocado,
            Instant creadoEn,
            Instant revocadoEn
    ) {
        return new RefreshToken(id, usuarioId, tokenHash, expiraEn, revocado, creadoEn, revocadoEn);
    }

    public void revocar(Instant ahora) {
        this.revocado = true;
        this.revocadoEn = ahora;
    }

    public boolean estaVigente(Instant ahora) {
        return !revocado && ahora.isBefore(expiraEn);
    }

    public RefreshTokenId getId() {
        return id;
    }

    public UsuarioId getUsuarioId() {
        return usuarioId;
    }

    public String getTokenHash() {
        return tokenHash;
    }

    public Instant getExpiraEn() {
        return expiraEn;
    }

    public boolean isRevocado() {
        return revocado;
    }

    public Instant getCreadoEn() {
        return creadoEn;
    }

    public Instant getRevocadoEn() {
        return revocadoEn;
    }
}
