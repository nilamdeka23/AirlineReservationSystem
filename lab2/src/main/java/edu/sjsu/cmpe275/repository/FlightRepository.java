package edu.sjsu.cmpe275.repository;

import edu.sjsu.cmpe275.model.Flight;
import org.springframework.data.repository.CrudRepository;

import javax.transaction.Transactional;


/**
 * The interface Flight repository.
 */
@Transactional(Transactional.TxType.REQUIRED)
public interface FlightRepository extends CrudRepository<Flight, String> {
}
