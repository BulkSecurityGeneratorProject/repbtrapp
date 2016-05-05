package com.btapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.btapp.domain.Expense;
import com.btapp.service.ExpenseService;
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
 * REST controller for managing Expense.
 */
@RestController
@RequestMapping("/api")
public class ExpenseResource {

    private final Logger log = LoggerFactory.getLogger(ExpenseResource.class);
        
    @Inject
    private ExpenseService expenseService;
    
    /**
     * POST  /expenses -> Create a new expense.
     */
    @RequestMapping(value = "/expenses",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Expense> createExpense(@Valid @RequestBody Expense expense) throws URISyntaxException {
        log.debug("REST request to save Expense : {}", expense);
        if (expense.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("expense", "idexists", "A new expense cannot already have an ID")).body(null);
        }
        Expense result = expenseService.save(expense);
        return ResponseEntity.created(new URI("/api/expenses/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("expense", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /expenses -> Updates an existing expense.
     */
    @RequestMapping(value = "/expenses",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Expense> updateExpense(@Valid @RequestBody Expense expense) throws URISyntaxException {
        log.debug("REST request to update Expense : {}", expense);
        if (expense.getId() == null) {
            return createExpense(expense);
        }
        Expense result = expenseService.save(expense);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("expense", expense.getId().toString()))
            .body(result);
    }

    /**
     * GET  /expenses -> get all the expenses.
     */
    @RequestMapping(value = "/expenses",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Expense>> getAllExpenses(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Expenses");
        Page<Expense> page = expenseService.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/expenses");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /expenses/:id -> get the "id" expense.
     * @throws URISyntaxException 
     */
    @RequestMapping(value = "/expenses/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Expense>> getExpense(@PathVariable Long id) throws URISyntaxException {
        log.debug("REST request to get Expense : {}", id);
        List<Expense> expense = expenseService.findOne(id);
        return Optional.ofNullable(expense)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /expenses/:id -> delete the "id" expense.
     */
    @RequestMapping(value = "/expenses/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteExpense(@PathVariable Long id) {
        log.debug("REST request to delete Expense : {}", id);
        expenseService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("expense", id.toString())).build();
    }

    /**
     * SEARCH  /_search/expenses/:query -> search for the expense corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/expenses/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Expense> searchExpenses(@PathVariable String query) {
        log.debug("Request to search Expenses for query {}", query);
        return expenseService.search(query);
    }
}
