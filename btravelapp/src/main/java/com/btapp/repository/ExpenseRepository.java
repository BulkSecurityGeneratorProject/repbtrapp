package com.btapp.repository;

import com.btapp.domain.Btr;
import com.btapp.domain.Expense;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
	
	//on (expense.btr.id = btr.id) hibernate leaga automat
	
	@Query("select expense from Expense expense join expense.btr btr where"
    		+ " expense.btr.status != 'Closed'"
    		+ " and "
    		+ "("
    		+ " expense.btr.assigned_to.login = ?#{principal.username}"
    		+ " or expense.btr.user.login = ?#{principal.username}"
    		+ " or expense.btr.assigned_from.login = ?#{principal.username}"
    		+ " or expense.btr.manager.login = ?#{principal.username}"
    		+ " or expense.btr.supplier.login = ?#{principal.username} "
    		+ ")")
    Page<Expense> finByBtrExpenssesIsCurrentUser(Pageable pageable);
}
