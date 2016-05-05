package com.btapp.repository;

import com.btapp.domain.Btr;
import com.btapp.domain.Expense;


import org.springframework.data.jpa.repository.*;

import java.util.List;


/**
 * Spring Data JPA repository for the Expense entity.
 */
public interface ExpenseRepository extends JpaRepository<Expense,Long> {

/*
	@Query("select btr_id from Expense btr where btr.id = ?#{principal.btr_id}")
	List<Btr> findOneById(); // adaugat 31.03.2016 :id
	*/

	@Query("select e from Expense e WHERE e.btr.id= ?1")
	List<Expense> findOneById(Long id);
}
