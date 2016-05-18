package com.btapp.service;

import com.btapp.domain.Btr;
import com.btapp.domain.Historybtr;
import com.btapp.domain.User;
import com.btapp.repository.BtrRepository;
import com.btapp.repository.UserRepository;
import com.btapp.repository.search.BtrSearchRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.inject.Inject;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static org.elasticsearch.index.query.QueryBuilders.queryStringQuery;

/**
 * Service Implementation for managing Btr.
 */
@Service
@Transactional
public class BtrService {

    private final Logger log = LoggerFactory.getLogger(BtrService.class);
    
    @Inject
    private UserRepository userRepository;
    
    @Inject
    private BtrRepository btrRepository;
    
    @Inject
    private BtrSearchRepository btrSearchRepository;
    
    //@Inject
   // private Historybtr historybtr;
    
    /**
     * Save a btr.
     * @return the persisted entity
     */
    public Btr save(Btr btr) {
        log.debug("Request to save Btr : {}", btr);
        
        btr.getUser();
        Optional<User> user = userRepository.findOneByLogin(User.getCurrentUser());
        
        if(btr.getStatus() == null){
        	btr.setStatus("Initiated"); 
        	// history line added;
        	//historybtr.setBtrstatusbefore("N/A");
        	//historybtr.setBtrstatusafter("Initiated");
        }
        else
	        if(btr.getStatus() == "Initiated"){
	        	btr.setStatus("Waiting for approval");
        		//historybtr.setBtrstatusbefore("Initiated");
        		//historybtr.setBtrstatusafter("Waiting for approval");
	        }
        if(btr.getId() == null)
        {
	        btr.setAssigned_from((User)user.get());
	        
	        btr.setSupplier(btr.getAssigned_to()); // ma intereseaza supplier-ul curent care se ocupa de btr 
	        btr.setManager((User)user.get());
	        btr.setRequest_date(ZonedDateTime.now());
	        btr.setLast_modified_date(ZonedDateTime.now()); // modificat 25.03.2016
	        btr.setSuma_totala(null);
	        
	        /*history line added
	        historybtr.setAssigned_from(btr.getAssigned_from().getLogin());
	        historybtr.setAssigned_to(btr.getAssigned_to().getLogin());
	        historybtr.setBtr(btr);
	        historybtr.setBtrstatusafter("N/A");
	        historybtr.setBtrstatusbefore("Initiated");
	        historybtr.setCenter_cost(btr.getCenter_cost());
	        historybtr.setChange_date(btr.getLast_modified_date());
	        historybtr.setChanged_by(btr.getAssigned_from().getLogin());
	        historybtr.setEnd_date(btr.getEnd_date());
	        historybtr.setLast_modified_date(btr.getLast_modified_date());
	        historybtr.setLocation(btr.getLocation());
	        historybtr.setRequest_date(btr.getRequest_date());
	        historybtr.setStart_date(btr.getStart_date());*/
        }
        else
        {
        	//if(btr.getStatus() == "Waiting for approval"){
        	//	btr.setAssigned_from(btr.getSupplier());
        	//}
        	btr.setAssigned_from((User)user.get());
	        btr.setLast_modified_date(ZonedDateTime.now()); // modificat 25.03.2016
        }
        
        
        
        Btr result = btrRepository.save(btr);
        btrSearchRepository.save(result);
        return result;
       
    }


	/** OLD
     *  get all the btrs.
     *  @return the list of entities
    
    @Transactional(readOnly = true) 
    public Page<Btr> findAll(Pageable pageable) {
        log.debug("Request to get all Btrs");
        Page<Btr> result = btrRepository.findAll(pageable); 
        return result;
    }
     */
    
    /** NEW
     *  get all the btrs.
     *  @return the list of entities
    */
    @Transactional(readOnly = true) 
    public Page<Btr> findAll(Pageable pageable) {
        log.debug("Request to get all Btrs");
        Page<Btr> result = btrRepository.finByAssigned_toOrEmployeeIsCurrentUser(pageable); 
        return result;
    }

    /**
     *  get one btr by id.
     *  @return the entity
     */
    @Transactional(readOnly = true) 
    public Btr findOne(Long id) {
        log.debug("Request to get Btr : {}", id);
        Btr btr = btrRepository.findOne(id);
        return btr;
    }

    /**
     *  delete the  btr by id.
     */
    public void delete(Long id) {
        log.debug("Request to delete Btr : {}", id);
        btrRepository.delete(id);
        btrSearchRepository.delete(id);
    }

    /**
     * search for the btr corresponding
     * to the query.
     */
    @Transactional(readOnly = true) 
    public List<Btr> search(String query) {
        
        log.debug("REST request to search Btrs for query {}", query);    
        return (List<Btr>) StreamSupport
            .stream(btrSearchRepository.search(queryStringQuery(query)).spliterator(), false)
            .collect(Collectors.toList());
        
    }
    
    
}
