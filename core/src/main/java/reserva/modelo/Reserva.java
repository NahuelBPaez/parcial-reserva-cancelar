package reserva.modelo;

import reserva.exception.InvalidReservationException;
import reserva.exception.ReservationAlreadyCancelledException;

import java.time.LocalDate;
import java.util.UUID;

public final class Reserva {

    public enum Estado {
        ACTIVA,
        CANCELADA
    }

    private final UUID id;
    private final String emailContacto;
    private final LocalDate fecha;
    private final int cantidadPersonas;
    private Estado estado;

    private Reserva(
            UUID id,
            String emailContacto,
            LocalDate fecha,
            int cantidadPersonas
    ) {
        this.id = id;
        this.emailContacto = emailContacto;
        this.fecha = fecha;
        this.cantidadPersonas = cantidadPersonas;
        this.estado = Estado.ACTIVA;
    }

    public static Reserva instance(
            UUID id,
            String emailContacto,
            LocalDate fecha,
            int cantidadPersonas
    ) {
        if (id == null) {
            throw new InvalidReservationException("El identificador es obligatorio");
        }
        if (fecha == null) {
            throw new InvalidReservationException("La fecha es obligatoria");
        }
        if (emailContacto == null || emailContacto.isBlank()) {
            throw new InvalidReservationException("El correo de contacto es obligatorio");
        }
        if (cantidadPersonas <= 0) {
            throw new InvalidReservationException("La cantidad de personas debe ser mayor que cero");
        }
        return new Reserva(id, emailContacto, fecha, cantidadPersonas);
    }

    public void cancelar() {
        if (estado == Estado.CANCELADA) {
            throw new ReservationAlreadyCancelledException("La reserva ya está cancelada");
        }
        estado = Estado.CANCELADA;
    }

    public UUID getId() {
        return id;
    }

    public String getEmailContacto() {
        return emailContacto;
    }

    public LocalDate getFecha() {
        return fecha;
    }

    public int getCantidadPersonas() {
        return cantidadPersonas;
    }

    public Estado getEstado() {
        return estado;
    }
}
