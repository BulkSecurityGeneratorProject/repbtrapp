package com.btapp.service;

import com.btapp.domain.Btr;
import com.btapp.domain.Expense;
import com.btapp.domain.User;
import com.btapp.repository.BtrRepository;
import com.btapp.repository.ExpenseRepository;
import com.btapp.repository.search.BtrSearchRepository;
import com.btapp.repository.search.ExpenseSearchRepository;

import akka.util.Collections;

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
    
    @Inject
    private BtrRepository btrRepository;
    

    @Inject
    private BtrSearchRepository btrSearchRepository;
    
    /**
     * Save a expense.
     * @return the persisted entity
     */
    public Expense save(Expense expense) {
    	log.debug("Request to save Expense : {}", expense);

    	//expense.getBtr();
		// old way to sum the expenses
    	/*if(expense.getBtr().getSuma_totala() == null){
    		expense.getBtr().setSuma_totala(expense.getExpense_cost());
    		btrRepository.save(expense.getBtr());
    	}
    	else{
    		expense.getBtr().setSuma_totala(expense.getBtr().getSuma_totala() + expense.getExpense_cost());
    		btrRepository.save(expense.getBtr());
    	}*/
        
        Expense result = expenseRepository.saveAndFlush(expense);
        expenseSearchRepository.save(result);
        Btr btr = btrRepository.findOne(expense.getBtr().getId());
		btr.setSuma_totala(btr.calcTotalExpenses());
		btrRepository.save(btr);
		btrSearchRepository.save(btr);
        return result;
    }

    /**
     *  get all the expenses.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Expense> findAll(Pageable pageable) {
        log.debug("Request to get all Expenses");
        Page<Expense> result = expenseRepository.finByBtrExpenssesIsCurrentUser(pageable); //findAll
        return result;
    }
    
    /**
     *  get all the expenses by btr id.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public List<Expense> findAllByBtrId(Long id) {
        log.debug("Request to get all Expenses by Btr");
        List<Expense> result = (List<Expense>) expenseRepository.findAll().stream().filter(expense -> id.equals(expense.getBtr().getId())).collect(Collectors.toList()); //findAll
        return result;
    }
    
    /** 
     *  get one expense by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Expense findOne(Long id) {
        log.debug("Request to get Expense : {}", id);
       Expense expense = expenseRepository.findOne(id);
        
        return expense;
    }

    /**
     *  delete the  expense by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Expense : {}", id);
  
        
        Expense expense = findOne(id);
        Btr btr = btrRepository.findOne(expense.getBtr().getId());
        
        btr.setSuma_totala(btr.getSuma_totala() - expense.getExpense_cost());
		btr.getExpenses().remove(expense);
        
        expenseRepository.delete(id);      
        expenseSearchRepository.delete(id);

		btrRepository.save(expense.getBtr());
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
    

}
