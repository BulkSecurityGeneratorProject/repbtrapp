package com.btapp.repository;

import com.btapp.domain.Expense_type;

import org.springframework.data.jpa.repository.*;

import java.util.List;

/**
 * Spring Data JPA repository for the Expense_type entity.
 */
public interface Expense_typeRepository extends JpaRepository<Expense_type,Long> {

}
