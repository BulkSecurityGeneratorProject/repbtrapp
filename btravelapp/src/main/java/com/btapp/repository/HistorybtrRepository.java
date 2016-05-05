package com.btapp.repository;

import com.btapp.domain.Historybtr;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Historybtr entity.
 */
public interface HistorybtrRepository extends JpaRepository<Historybtr,Long> {

}
