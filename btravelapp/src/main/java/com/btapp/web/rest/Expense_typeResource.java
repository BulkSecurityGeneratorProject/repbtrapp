package com.btapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.btapp.domain.Expense_type;
import com.btapp.service.Expense_typeService;
import com.btapp.web.rest.util.HeaderUtil;
import com.btapp.web.rest.util.PaginationUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.inject.Inject;
import javax.validation.Valid;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * REST controller for managing Expense_type.
 */
@RestController
@RequestMapping("/api")
public class Expense_typeResource {

    private final Logger log = LoggerFactory.getLogger(Expense_typeResource.class);
        
    @Inject
    private Expense_typeService expense_typeService;
    
    /**
     * POST  /expense_types -> Create a new expense_type.
     */
    @RequestMapping(value = "/expense_types",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Expense_type> createExpense_type(@Valid @RequestBody Expense_type expense_type) throws URISyntaxException {
        log.debug("REST request to save Expense_type : {}", expense_type);
        if (expense_type.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("expense_type", "idexists", "A new expense_type cannot already have an ID")).body(null);
        }
        Expense_type result = expense_typeService.save(expense_type);
        return ResponseEntity.created(new URI("/api/expense_types/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("expense_type", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /expense_types -> Updates an existing expense_type.
     */
    @RequestMapping(value = "/expense_types",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Expense_type> updateExpense_type(@Valid @RequestBody Expense_type expense_type) throws URISyntaxException {
        log.debug("REST request to update Expense_type : {}", expense_type);
        if (expense_type.getId() == null) {
            return createExpense_type(expense_type);
        }
        Expense_type result = expense_typeService.save(expense_type);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("expense_type", expense_type.getId().toString()))
            .body(result);
    }

    /**
     * GET  /expense_types -> get all the expense_types.
     */
    @RequestMapping(value = "/expense_types",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Expense_type>> getAllExpense_types(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Expense_types");
        Page<Expense_type> page = expense_typeService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/expense_types");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /expense_types/:id -> get the "id" expense_type.
     */
    @RequestMapping(value = "/expense_types/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Expense_type> getExpense_type(@PathVariable Long id) {
        log.debug("REST request to get Expense_type : {}", id);
        Expense_type expense_type = expense_typeService.findOne(id);
        return Optional.ofNullable(expense_type)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /expense_types/:id -> delete the "id" expense_type.
     */
    @RequestMapping(value = "/expense_types/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteExpense_type(@PathVariable Long id) {
        log.debug("REST request to delete Expense_type : {}", id);
        expense_typeService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("expense_type", id.toString())).build();
    }

    /**
     * SEARCH  /_search/expense_types/:query -> search for the expense_type corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/expense_types/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Expense_type> searchExpense_types(@PathVariable String query) {
        log.debug("Request to search Expense_types for query {}", query);
        return expense_typeService.search(query);
    }
}
