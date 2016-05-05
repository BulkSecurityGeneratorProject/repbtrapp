package com.btapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.btapp.domain.Historybtr;
import com.btapp.repository.HistorybtrRepository;
import com.btapp.repository.search.HistorybtrSearchRepository;
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
 * REST controller for managing Historybtr.
 */
@RestController
@RequestMapping("/api")
public class HistorybtrResource {

    private final Logger log = LoggerFactory.getLogger(HistorybtrResource.class);
        
    @Inject
    private HistorybtrRepository historybtrRepository;
    
    @Inject
    private HistorybtrSearchRepository historybtrSearchRepository;
    
    /**
     * POST  /historybtrs -> Create a new historybtr.
     */
    @RequestMapping(value = "/historybtrs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Historybtr> createHistorybtr(@Valid @RequestBody Historybtr historybtr) throws URISyntaxException {
        log.debug("REST request to save Historybtr : {}", historybtr);
        if (historybtr.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("historybtr", "idexists", "A new historybtr cannot already have an ID")).body(null);
        }
        Historybtr result = historybtrRepository.save(historybtr);
        historybtrSearchRepository.save(result);
        return ResponseEntity.created(new URI("/api/historybtrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("historybtr", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /historybtrs -> Updates an existing historybtr.
     */
    @RequestMapping(value = "/historybtrs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Historybtr> updateHistorybtr(@Valid @RequestBody Historybtr historybtr) throws URISyntaxException {
        log.debug("REST request to update Historybtr : {}", historybtr);
        if (historybtr.getId() == null) {
            return createHistorybtr(historybtr);
        }
        Historybtr result = historybtrRepository.save(historybtr);
        historybtrSearchRepository.save(result);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("historybtr", historybtr.getId().toString()))
            .body(result);
    }

    /**
     * GET  /historybtrs -> get all the historybtrs.
     */
    @RequestMapping(value = "/historybtrs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Historybtr>> getAllHistorybtrs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Historybtrs");
        Page<Historybtr> page = historybtrRepository.findAll(pageable); 
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/historybtrs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /historybtrs/:id -> get the "id" historybtr.
     */
    @RequestMapping(value = "/historybtrs/{id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Historybtr> getHistorybtr(@PathVariable Long id) {
        log.debug("REST request to get Historybtr : {}", id);
        Historybtr historybtr = historybtrRepository.findOne(id);
        return Optional.ofNullable(historybtr)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /historybtrs/:id -> delete the "id" historybtr.
     */
    @RequestMapping(value = "/historybtrs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteHistorybtr(@PathVariable Long id) {
        log.debug("REST request to delete Historybtr : {}", id);
        historybtrRepository.delete(id);
        historybtrSearchRepository.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("historybtr", id.toString())).build();
    }

    /**
     * SEARCH  /_search/historybtrs/:query -> search for the historybtr corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/historybtrs/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Historybtr> searchHistorybtrs(@PathVariable String query) {
        log.debug("REST request to search Historybtrs for query {}", query);
        return StreamSupport
            .stream(historybtrSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
    }
}
