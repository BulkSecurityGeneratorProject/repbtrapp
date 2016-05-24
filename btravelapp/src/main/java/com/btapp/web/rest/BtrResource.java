package com.btapp.web.rest;

import com.codahale.metrics.annotation.Timed;
import com.btapp.domain.Btr;
import com.btapp.service.BtrService;
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
 * REST controller for managing Btr.
 */
@RestController
@RequestMapping("/api")
public class BtrResource {

    private final Logger log = LoggerFactory.getLogger(BtrResource.class);
        
    @Inject
    private BtrService btrService;
    
    /**
     * POST  /btrs -> Create a new btr.
     */
    @RequestMapping(value = "/btrs",
        method = RequestMethod.POST,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Btr> createBtr(@Valid @RequestBody Btr btr) throws URISyntaxException {
        log.debug("REST request to save Btr : {}", btr);
        if (btr.getId() != null) {
            return ResponseEntity.badRequest().headers(HeaderUtil.createFailureAlert("btr", "idexists", "A new btr cannot already have an ID")).body(null);
        }
        Btr result = btrService.save(btr);
        return ResponseEntity.created(new URI("/api/btrs/" + result.getId()))
            .headers(HeaderUtil.createEntityCreationAlert("btr", result.getId().toString()))
            .body(result);
    }

    /**
     * PUT  /btrs -> Updates an existing btr.
     */
    @RequestMapping(value = "/btrs",
        method = RequestMethod.PUT,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Btr> updateBtr(@Valid @RequestBody Btr btr) throws URISyntaxException {
        log.debug("REST request to update Btr : {}", btr);
        if (btr.getId() == null) {
            return createBtr(btr);
        }
        Btr result = btrService.save(btr);
        return ResponseEntity.ok()
            .headers(HeaderUtil.createEntityUpdateAlert("btr", btr.getId().toString()))
            .body(result);
    }

    /**
     * GET  /btrs -> get all the btrs.
     */
    @RequestMapping(value = "/btrs",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<List<Btr>> getAllBtrs(Pageable pageable)
        throws URISyntaxException {
        log.debug("REST request to get a page of Btrs");
        Page<Btr> page = btrService.findAll(pageable); // findAll
        HttpHeaders headers = PaginationUtil.generatePaginationHttpHeaders(page, "/api/btrs");
        return new ResponseEntity<>(page.getContent(), headers, HttpStatus.OK);
    }

    /**
     * GET  /btr/:id -> get the "id" btr.
     */
    @RequestMapping(value = "/btrs/{id}",///expenses/{btr.id}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Btr> getBtr(@PathVariable Long id) {
        log.debug("REST request to get Btr : {}", id);
        Btr btr = btrService.findOne(id);
        return Optional.ofNullable(btr)
            .map(result -> new ResponseEntity<>(
                result,
                HttpStatus.OK))
            .orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    /**
     * DELETE  /btrs/:id -> delete the "id" btr.
     */
    @RequestMapping(value = "/btrs/{id}",
        method = RequestMethod.DELETE,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public ResponseEntity<Void> deleteBtr(@PathVariable Long id) {
        log.debug("REST request to delete Btr : {}", id);
        btrService.delete(id);
        return ResponseEntity.ok().headers(HeaderUtil.createEntityDeletionAlert("btr", id.toString())).build();
    }

    /**
     * SEARCH  /_search/btrs/:query -> search for the btr corresponding
     * to the query.
     */
    @RequestMapping(value = "/_search/btrs/{query:.+}",
        method = RequestMethod.GET,
        produces = MediaType.APPLICATION_JSON_VALUE)
    @Timed
    public List<Btr> searchBtrs(@PathVariable String query) {
        log.debug("Request to search Btrs for query {}", query);
        return btrService.search(query);
    }
}
