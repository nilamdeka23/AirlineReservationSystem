package edu.sjsu.cmpe275.repository;


import edu.sjsu.cmpe275.model.Reservation;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;

import javax.transaction.Transactional;
import java.util.List;

/**
 * The interface Reservation repository.
 */
@Transactional(Transactional.TxType.REQUIRED)
public interface ReservationRepository extends CrudRepository<Reservation, String> {

    /**
     * Search for reservations list.
     *
     * @param passengerId  the passenger id
     * @param dest_from    the dest from
     * @param dest_to      the dest to
     * @param flightNumber the flight number
     * @return the list
     */
    @Query(value = "SELECT DISTINCT * FROM lab2.passenger p, lab2.reservation r, lab2.flight_reservation fr, lab2.flight f " +
            "WHERE p.id = r.id AND r.order_no = fr.res_order_id AND fr.flyt_no = f.flight_no " +
            "AND r.id = COALESCE(:passengerId,r.id) AND f.flight_no = COALESCE(:flightNumber,f.flight_no) " +
            "AND f.dest_from = COALESCE(:dest_from,f.dest_from) and f.dest_to = COALESCE(:dest_to,f.dest_to)",
            nativeQuery = true)
    List<Reservation> searchForReservations(@Param("passengerId") String passengerId, @Param("dest_from") String dest_from,
                                            @Param("dest_to") String dest_to, @Param("flightNumber") String flightNumber);


}

