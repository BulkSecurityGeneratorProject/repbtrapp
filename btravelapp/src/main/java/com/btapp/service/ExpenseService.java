package com.btapp.service;

import com.btapp.domain.Expense;
import com.btapp.repository.ExpenseRepository;
import com.btapp.repository.search.ExpenseSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;

import javax.inject.Inject;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * Service Implementation for managing Expense.
 */
@Service
@Transactional
public class ExpenseService {

    private final Logger log = LoggerFactory.getLogger(ExpenseService.class);
    
    @Inject
    private ExpenseRepository expenseRepository;
    
    @Inject
    private ExpenseSearchRepository expenseSearchRepository;
    
    /**
     * Save a expense.
     * @return the persisted entity
     */
    public Expense save(Expense expense) {
        log.debug("Request to save Expense : {}", expense);
        //if(expense.getBtr().getId() != null){ // am incercat sa fac update daca am id de btr deja
        if(expense.getBtr().getStatus() == "Initiated"){ // am incercat sa fac update daca gasesc statusul Initiated la momentul salvarii costului
        expense.getBtr().setStatus("Waiting for approval"); // nu merge! unde este update-ul
        Expense result = expenseRepository.save(expense);
        expenseSearchRepository.save(result);
        return result;
        }  
        else{
        Expense result = expenseRepository.save(expense);
        expenseSearchRepository.save(result);
        return result;}
    }

    /**
     *  get all the expenses.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Expense> findAll(Pageable pageable) {
        log.debug("Request to get all Expenses");
        Page<Expense> result = expenseRepository.findAll(pageable); 
        return result;
    }
    
    /** OLD
     *  get one expense by id.
     *  @return the entity
     
    @Transactional(readOnly = true) 
    public Expense findOne(Long id) {
        log.debug("Request to get Expense : {}", id);
       Expense expense = expenseRepository.findOne(id);
        
        return expense;
    }
*/
    /** NEW
     *  get one expense by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public List<Expense> findOne(Long id) {
        log.debug("Request to get Expense : {}", id);
       // Expense expense = expenseRepository.findOne(id);
        List<Expense> expense = expenseRepository.findOneById(id);
        return  expense;
    }

    /**
     *  delete the  expense by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Expense : {}", id);
        expenseRepository.delete(id);
        expenseSearchRepository.delete(id);
    }

    /**
     * search for the expense corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Expense> search(String query) {
        
        log.debug("REST request to search Expenses for query {}", query);
        return StreamSupport
            .stream(expenseSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
    
    /** INCERCARE
     *  get expenses by btr id.
     *  @return the entity
      
    public Expense findOneById(Long id) {
        //log.debug("Request to get Expense for a btr : {}", id);
        Expense expense = (Expense) expenseRepository.findOneById();
        return expense;
    }
  */

}
