package reserva.usecase;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import reserva.exception.PersistenceException;
import reserva.exception.ReservationAlreadyCancelledException;
import reserva.exception.ReservationNotFoundException;
import reserva.modelo.Reserva;
import reserva.output.ReservationRepository;

import java.time.LocalDate;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CancelReservationUseCaseTest {

    @Mock
    private ReservationRepository reservationRepository;

    private CancelReservationUseCase useCase;
    private UUID reservationId;
    private Reserva reserva;

    @BeforeEach
    void setUp() {
        useCase = new CancelReservationUseCase(reservationRepository);
        reservationId = UUID.randomUUID();
        reserva = Reserva.instance(
                reservationId,
                "contacto@ejemplo.com",
                LocalDate.of(2026, 10, 25),
                2
        );
    }

    @Test
    void cancelaYGuardaUnaReservaExistente() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reserva));

        Reserva resultado = useCase.execute(reservationId);

        assertEquals(reserva, resultado);
        assertEquals(Reserva.Estado.CANCELADA, resultado.getEstado());
        verify(reservationRepository).findById(reservationId);
        verify(reservationRepository).save(reserva);
    }

    @Test
    void informaCuandoLaReservaNoExiste() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.empty());

        assertThrows(
                ReservationNotFoundException.class,
                () -> useCase.execute(reservationId)
        );

        verify(reservationRepository, never()).save(reserva);
    }

    @Test
    void informaCuandoLaReservaYaEstaCancelada() {
        reserva.cancelar();
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reserva));

        assertThrows(
                ReservationAlreadyCancelledException.class,
                () -> useCase.execute(reservationId)
        );

        verify(reservationRepository, never()).save(reserva);
    }

    @Test
    void informaCuandoNoPuedeGuardarLaReservaCancelada() {
        when(reservationRepository.findById(reservationId)).thenReturn(Optional.of(reserva));
        doThrow(new PersistenceException("No se pudo guardar la reserva"))
                .when(reservationRepository)
                .save(reserva);

        assertThrows(
                PersistenceException.class,
                () -> useCase.execute(reservationId)
        );

        assertEquals(Reserva.Estado.CANCELADA, reserva.getEstado());
        verify(reservationRepository).save(reserva);
    }
}
