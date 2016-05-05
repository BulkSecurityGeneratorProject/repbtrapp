package com.btapp.service;

import com.btapp.domain.Expense_type;
import com.btapp.repository.Expense_typeRepository;
import com.btapp.repository.search.Expense_typeSearchRepository;
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
 * Service Implementation for managing Expense_type.
 */
@Service
@Transactional
public class Expense_typeService {

    private final Logger log = LoggerFactory.getLogger(Expense_typeService.class);
    
    @Inject
    private Expense_typeRepository expense_typeRepository;
    
    @Inject
    private Expense_typeSearchRepository expense_typeSearchRepository;
    
    /**
     * Save a expense_type.
     * @return the persisted entity
     */
    public Expense_type save(Expense_type expense_type) {
        log.debug("Request to save Expense_type : {}", expense_type);
        Expense_type result = expense_typeRepository.save(expense_type);
        expense_typeSearchRepository.save(result);
        return result;
    }

    /**
     *  get all the expense_types.
     *  @return the list of entities
     */
    @Transactional(readOnly = true) 
    public Page<Expense_type> findAll(Pageable pageable) {
        log.debug("Request to get all Expense_types");
        Page<Expense_type> result = expense_typeRepository.findAll(pageable); 
        return result;
    }

    /**
     *  get one expense_type by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Expense_type findOne(Long id) {
        log.debug("Request to get Expense_type : {}", id);
        Expense_type expense_type = expense_typeRepository.findOne(id);
        return expense_type;
    }

    /**
     *  delete the  expense_type by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Expense_type : {}", id);
        expense_typeRepository.delete(id);
        expense_typeSearchRepository.delete(id);
    }

    /**
     * search for the expense_type corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Expense_type> search(String query) {
        
        log.debug("REST request to search Expense_types for query {}", query);
        return StreamSupport
            .stream(expense_typeSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
