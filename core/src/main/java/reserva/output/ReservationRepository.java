package reserva.output;

import reserva.modelo.Reserva;

import java.util.Optional;
import java.util.UUID;

public interface ReservationRepository {

    Optional<Reserva> findById(UUID id);

    boolean save(Reserva reserva);
}
