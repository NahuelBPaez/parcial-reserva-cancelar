package reserva.usecase;

import reserva.exception.PersistenceException;
import reserva.exception.ReservationNotFoundException;
import reserva.modelo.Reserva;
import reserva.output.ReservationRepository;

import java.util.UUID;

public final class CancelReservationUseCase {

    private final ReservationRepository reservationRepository;

    public CancelReservationUseCase(ReservationRepository reservationRepository) {
        this.reservationRepository = reservationRepository;
    }

    public Reserva execute(UUID reservationId) {
        Reserva reserva = reservationRepository.findById(reservationId)
                .orElseThrow(() -> new ReservationNotFoundException(
                        "No se encontró la reserva: " + reservationId
                ));

        reserva.cancelar();
        if (!reservationRepository.save(reserva)) {
            throw new PersistenceException("No se pudo guardar la reserva");
        }

        return reserva;
    }
}
