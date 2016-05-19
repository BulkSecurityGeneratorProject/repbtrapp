package com.btapp.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.btapp.domain.Expense;


/**
 * Spring Data JPA repository for the Expense entity.
 */
public interface ExpenseRepository extends JpaRepository<Expense,Long> {
	
	@Query("select e from Expense e WHERE e.btr.id= ?1")
 	List<Expense> findOneById(Long id);
	
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
