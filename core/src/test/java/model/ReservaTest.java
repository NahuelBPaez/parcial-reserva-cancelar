package model;

import org.junit.jupiter.api.Test;
import reserva.exception.InvalidReservationException;
import reserva.exception.ReservationAlreadyCancelledException;
import reserva.modelo.Reserva;

import java.time.LocalDate;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservaTest {

    private final UUID id = UUID.randomUUID();
    private final String emailContacto = "contacto@ejemplo.com";
    private final LocalDate fecha = LocalDate.of(2026, 10, 25);

    @Test
    void creaUnaReservaActivaConLosDatosRecibidos() {
        Reserva reserva = Reserva.instance(id, emailContacto, fecha, 2);

        assertAll(
                () -> assertEquals(id, reserva.getId()),
                () -> assertEquals(emailContacto, reserva.getEmailContacto()),
                () -> assertEquals(fecha, reserva.getFecha()),
                () -> assertEquals(2, reserva.getCantidadPersonas()),
                () -> assertEquals(Reserva.Estado.ACTIVA, reserva.getEstado())
        );
    }

    @Test
    void rechazaUnIdentificadorNulo() {
        assertThrows(
                InvalidReservationException.class,
                () -> Reserva.instance(null, emailContacto, fecha, 2)
        );
    }

    @Test
    void rechazaUnaFechaNula() {
        assertThrows(
                InvalidReservationException.class,
                () -> Reserva.instance(id, emailContacto, null, 2)
        );
    }

    @Test
    void rechazaUnCorreoNulo() {
        assertThrows(
                InvalidReservationException.class,
                () -> Reserva.instance(id, null, fecha, 2)
        );
    }

    @Test
    void rechazaUnCorreoVacio() {
        assertThrows(
                InvalidReservationException.class,
                () -> Reserva.instance(id, "", fecha, 2)
        );
    }
    @Test
    void rechazaUnCorreoSoloConEspacios() {
        assertThrows(InvalidReservationException.class,
                () -> Reserva.instance(id, "   ", fecha, 2));
    }

    @Test
    void rechazaUnaCantidadDePersonasCero() {
        assertThrows(
                InvalidReservationException.class,
                () -> Reserva.instance(id, emailContacto, fecha, 0)
        );
    }

    @Test
    void rechazaUnaCantidadDePersonasNegativa() {
        assertThrows(
                InvalidReservationException.class,
                () -> Reserva.instance(id, emailContacto, fecha, -1)
        );
    }

    @Test
    void cancelaUnaReservaActiva() {
        Reserva reserva = Reserva.instance(id, emailContacto, fecha, 2);

        reserva.cancelar();

        assertEquals(Reserva.Estado.CANCELADA, reserva.getEstado());
    }

    @Test
    void noPermiteCancelarUnaReservaDosVeces() {
        Reserva reserva = Reserva.instance(id, emailContacto, fecha, 2);
        reserva.cancelar();

        assertThrows(ReservationAlreadyCancelledException.class, reserva::cancelar);
    }
}
